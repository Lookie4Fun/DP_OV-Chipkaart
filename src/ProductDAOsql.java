import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOsql implements ProductDAO{
    private Connection conn;
    private OVChipkaartDAOsql ovChipkaartDAOsql;
    public ProductDAOsql(Connection conn, OVChipkaartDAOsql ovChipkaartDAOsql) {
        this.conn = conn;
        this.ovChipkaartDAOsql = ovChipkaartDAOsql;

    }

    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        try {
            String query ="select * from product;";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                Product product = new Product(result.getInt("product_nummer"), result.getString("naam"), result.getString("beschrijving"), result.getInt("prijs"));

                try {
                    String chipkaart_productQuery ="select * from ov_chipkaart_product where product_nummer = ?;";
                    PreparedStatement chipkaart_productPst = conn.prepareStatement(chipkaart_productQuery);
                    chipkaart_productPst.setInt(1, product.getProduct_nummer());
                    ResultSet chipkaart_productResult = chipkaart_productPst.executeQuery();
                    while (chipkaart_productResult.next()) {
                        try {
                            String kaartQuery ="select * from ov_chipkaart where kaart_nummer = ?;";
                            PreparedStatement kaartPst = conn.prepareStatement(kaartQuery);
                            kaartPst.setInt(1, chipkaart_productResult.getInt("kaart_nummer"));
                            ResultSet KaartResult = kaartPst.executeQuery();
                            while (KaartResult.next()) {
                                try {
                                    product.addOVChipkaart(new OVChipkaart(KaartResult.getInt("kaart_nummer"),KaartResult.getDate("geldig_tot"),KaartResult.getInt("klasse"),KaartResult.getInt("saldo"),KaartResult.getInt("reiziger_id")));
                                }catch (Exception e){
                                    System.out.println(e);
                                }
                            }
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                producten.add(product);
            }
            return producten;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean save(Product product) throws SQLException {
        try {
            String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.executeUpdate();
            pst.close();

            for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                String status = "";
                if(ovChipkaart.getGeldig_tot().after(Date.valueOf(LocalDate.now()))){
                    status = "actief";
                }else {
                    status = "verlopen";
                }
                ovChipkaartDAOsql.save(ovChipkaart);
                String query2 = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer,status , last_update) VALUES (?, ?,?, ?);";
                PreparedStatement pst2 = conn.prepareStatement(query2);
                pst2.setInt(1, ovChipkaart.getKaart_nummer());
                pst2.setInt(2, product.getProduct_nummer());
                pst2.setString(3, status);
                pst2.setDate(4, Date.valueOf(LocalDate.now()));
                pst2.executeUpdate();
                pst.close();
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            String productQuery ="update product set naam=?, beschrijving=?, prijs=? where product_nummer =?;";
            PreparedStatement pst = conn.prepareStatement(productQuery);
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setInt(3, product.getPrijs());
            pst.setInt(4, product.getProduct_nummer());
            pst.executeUpdate();
            pst.close();

            for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                String status = "";
                if(ovChipkaart.getGeldig_tot().after(Date.valueOf(LocalDate.now()))){
                    status = "actief";
                }else {
                    status = "verlopen";
                }
                ovChipkaartDAOsql.save(ovChipkaart);
                String query2 = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer,status , last_update) VALUES (?, ?,?, ?);";
                PreparedStatement pst2 = conn.prepareStatement(query2);
                pst2.setInt(1, ovChipkaart.getKaart_nummer());
                pst2.setInt(2, product.getProduct_nummer());
                pst2.setString(3, status);
                pst2.setDate(4, Date.valueOf(LocalDate.now()));
                pst2.executeUpdate();
                pst.close();
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            try {
                String query2 ="delete from ov_chipkaart_product where product_nummer=?;";
                PreparedStatement pst2 = conn.prepareStatement(query2);
                pst2.setInt(1, product.getProduct_nummer());
                pst2.executeQuery();
            }catch (Exception e){
                System.out.println(e);
            }
            try {
                String query = "delete from product where product_nummer=?;";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, product.getProduct_nummer());
                pst.executeQuery();
            }catch (Exception e){
                System.out.println(e);
            }
            return true;

        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart){
        List<Product> producten = new ArrayList<>();
        try {

            String query =
                    "select p.product_nummer, naam, beschrijving, prijs from product p join ov_chipkaart_product a on(p.product_nummer = a.product_nummer and a.kaart_nummer = ? );";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet result =pst.executeQuery();
            while (result.next()) {
                Product product = new Product(result.getInt("product_nummer"), result.getString("naam"), result.getString("beschrijving"), result.getInt("prijs"));
                producten.add(product);
            }
            }catch (Exception e){
            System.out.println(e);
        }
        return producten;
    }
}
