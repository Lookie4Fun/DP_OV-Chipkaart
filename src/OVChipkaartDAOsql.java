import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOsql implements OVChipkaartDAO{

    Connection conn;

    public OVChipkaartDAOsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> kaarten = new ArrayList<>();
        try {
            String query ="select * from ov_chipkaart;";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                OVChipkaart kaart = new OVChipkaart(result.getInt("kaart_nummer"), result.getDate("geldig_tot"), result.getInt("klasse"), result.getInt("saldo"), result.getInt("reiziger_id"));

                try {
                    String chipkaart_productQuery ="select * from ov_chipkaart_product where kaart_nummer = ?;";
                    PreparedStatement chipkaart_productPst = conn.prepareStatement(chipkaart_productQuery);
                    chipkaart_productPst.setInt(1, kaart.getKaart_nummer());
                    ResultSet chipkaart_productResult = chipkaart_productPst.executeQuery();
                    while (chipkaart_productResult.next()) {
                        try {
                            String productQuery ="select * from product where product_nummer = ?;";
                            PreparedStatement productPst = conn.prepareStatement(productQuery);
                            productPst.setInt(1, chipkaart_productResult.getInt("product_nummer"));
                            ResultSet productResult = productPst.executeQuery();
                            while (productResult.next()) {
                                try {
                                    kaart.addProduct(new Product(productResult.getInt("product_nummer"),productResult.getString("naam"),productResult.getString("beschrijving"),productResult.getInt("prijs")));
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
                    System.out.println();
                }
                kaarten.add(kaart);
            }

            return kaarten;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean save(OVChipkaart kaart) throws SQLException {
            try {
                String query =
                        "INSERT INTO ov_chipkaart"
                                + "(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id)"
                                + "values (?,?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, kaart.getKaart_nummer());
                pst.setDate(2, kaart.getGeldig_tot());
                pst.setInt(3, kaart.getKlasse());
                pst.setInt(4, kaart.getSaldo());
                pst.setInt(5, kaart.getReiziger_id());

                pst.executeQuery();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
    }

    @Override
    public boolean update(OVChipkaart kaart) {
        try {
            String query ="update ov_chipkaart set geldig_tot =?, klasse=?, saldo=?, reiziger_id=? where kaart_nummer =?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, kaart.getGeldig_tot());
            pst.setInt(2, kaart.getKlasse());
            pst.setInt(3, kaart.getSaldo());
            pst.setInt(4, kaart.getReiziger_id());
            pst.setInt(5, kaart.getKaart_nummer());
            pst.executeQuery();
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart kaart) {
        try {
            try {
                String query2 ="delete from ov_chipkaart_product where kaart_nummer=?;";
                PreparedStatement pst2 = conn.prepareStatement(query2);
                pst2.setInt(1, kaart.getKaart_nummer());
                pst2.executeQuery();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer =?;";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, kaart.getKaart_nummer());
                pst.executeQuery();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> kaarten = new ArrayList<>();
        try {
            String query ="select * from ov_chipkaart where reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                OVChipkaart kaart = new OVChipkaart(result.getInt("kaart_nummer"), result.getDate("geldig_tot"), result.getInt("klasse"), result.getInt("saldo"), result.getInt("reiziger_id"));
                kaarten.add(kaart);
            }
            reiziger.setOVChipkaarten(kaarten);
            return kaarten;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
