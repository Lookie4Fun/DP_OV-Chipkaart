import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static Connection connection = null;

    public static void main(String [] args) throws SQLException {
        OVChipkaartDAOsql ovChipkaartDAOsql = new OVChipkaartDAOsql(getConnection());
        AdresDAOPsql adresDAOPsql = new AdresDAOPsql(getConnection());
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection(),adresDAOPsql,ovChipkaartDAOsql);
        ProductDAOsql productDAOsql = new ProductDAOsql(getConnection(),ovChipkaartDAOsql);

        testReizigerDAO(reizigerDAOPsql);
        testAdresDAO(adresDAOPsql);
        testOVChipkaartDAO(ovChipkaartDAOsql);
        testProductDAO(productDAOsql);
        closeConnection(getConnection());

    }

    private static Connection getConnection(){
        if(connection == null){
            String jdbURL = "jdbc:postgresql://localhost:5432/ovchip";
            String username = "postgres";
            String password = "1234";

            try {
                connection = DriverManager.getConnection(jdbURL, username, password);
            } catch (SQLException e) {
                System.out.println("Error");
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }

    private static void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }


        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("\n[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        // Update een reiziger in de database
        System.out.println("[Test] resultaat voor en na updaten van achternaam op reiziger met id 77");
        Reiziger updateSietske = new Reiziger(77, "S", "van", "Steen", java.sql.Date.valueOf(gbdatum));
        System.out.println("VOOR UPDATE");
        for (Reiziger r: reizigers) {
            System.out.println(r);
        }
        rdao.update(updateSietske);
        System.out.println("\nNA UPDATE");
        reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        //zoeken op geboorte datum
        System.out.println("\n[Test] resultaat zoeken op geboortedatum:2002-12-03");
        List<Reiziger> gbReizigers = rdao.findByGbdatum("2002-12-03");
        for(Reiziger r : gbReizigers){
            System.out.println(r);
        }


//        //Delete een reiziger uit de database
//        rdao.delete(sietske);
//        System.out.println("\n[Test] resultaat na deleten van reiziger op adres met id 77");
//        reizigers = rdao.findAll();
//        for (Reiziger r : reizigers) {
//            System.out.println(r);
//        }

    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        //Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }

        // Maak een nieuwe adres aan en persisteer deze in de database
        Adres nieuwadres = new Adres(77,"1234XZ", "12", "huislaan", "Amsterdam", 77);
        System.out.print("\n[Test] Eerst " + adressen.size() + " adressen, na adresDAO.save() ");
        adao.save(nieuwadres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Update een adres in de database
        System.out.println("[Test] resultaat voor en na updaten van postcode op adres met id 77");
        Adres updateAdres = new Adres(77,"1234FL", "21", "Brugweg", "Rotterdam", 77);
        System.out.println("VOOR UPDATE");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        adao.update(updateAdres);
        System.out.println("\nNA UPDATE");
        adressen = adao.findAll();
        for (Adres a : adressen) {
            System.out.println(a);
        }

        // Delete een adres uit de database
        adao.delete(nieuwadres);
        System.out.println("\n[Test] resultaat na deleten van postcode op adres met id 77");
        adressen = adao.findAll();
        for (Adres a : adressen) {
            System.out.println(a);
        }

        //FindByReiziger
        System.out.println("\n[Test] resultaat na FindByReiziger op reiziger met id 1");
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(getConnection(),new AdresDAOPsql(getConnection()),new OVChipkaartDAOsql(getConnection()));

        System.out.println(adao.findByReiziger(reizigerDAO.findById(1)));
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO ovdao)throws SQLException{
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle ovkaarten op uit de database
        List<OVChipkaart> kaarten = ovdao.findAll();
        System.out.println("[Test] OVChipkaart.findAll() geeft de volgende kaarten:");
        for (OVChipkaart k : kaarten) {
            System.out.println(k);
        }

        // Maak een OVChipkaart aan en persisteer deze in de database
        String kaartdatum = "2028-09-08";
        OVChipkaart ovChipkaart = new OVChipkaart(2403711,java.sql.Date.valueOf(kaartdatum),2,40,1);
        System.out.print("\n [Test] Eerst " + kaarten.size() + " kaarten, na OVChipkaartDAO.save() ");
        ovdao.save(ovChipkaart);
        kaarten = ovdao.findAll();
        System.out.println(kaarten.size() + " kaarten\n");

        // Update een ovkaart in de database
        System.out.println("[Test] resultaat voor en na updaten van ovkaart met kaartnummer 2403711");
        String kaartdatum2 = "2029-09-08";
        OVChipkaart updateOvChipkaart = new OVChipkaart(2403711,java.sql.Date.valueOf(kaartdatum2),2,150,1);
        System.out.println("VOOR UPDATE");
        for (OVChipkaart k : kaarten) {
            System.out.println(k);
        }
        ovdao.update(updateOvChipkaart);
        System.out.println("\nNA UPDATE");
        kaarten = ovdao.findAll();
        for (OVChipkaart k : kaarten) {
            System.out.println(k);
        }

        // Delete een ovkaart uit de database
        ovdao.delete(ovChipkaart);
        System.out.println("\n[Test] resultaat na deleten van OVChipkaart met kaartnummer 2403711");
        kaarten = ovdao.findAll();
        for (OVChipkaart k : kaarten) {
            System.out.println(k);
        }

        //vind een ovkaart via de reiziger
        System.out.println("\n[Test] resultaat na zoeken via reiziger met reiziger_id 2");
        Reiziger sietske = new Reiziger(2, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"));
        kaarten = ovdao.findByReiziger(sietske);
        for (OVChipkaart k : kaarten) {
            System.out.println(k);
        }
    }

    private static void testProductDAO(ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Haal alle producten op uit de database
        List<Product> producten = pdao.findAll();
        System.out.println("[Test] Product.findAll() geeft de volgende producten:");
        for (Product p : producten) {
            System.out.println(p);
        }

        // Maak een product aan en persisteer deze in de database
        Product nieuwProduct = new Product(7,"Vertragings garantie", "5 dagen per week vertraging.", 20);
        System.out.print("\n [Test] Eerst " + producten.size() + " producten, na ProductDAO.save() ");
        pdao.save(nieuwProduct);
        producten = pdao.findAll();
        System.out.println(producten.size() + " producten\n");

        // Update een product in de database
        System.out.println("[Test] resultaat voor en na updaten van product met productnummer 7");
        OVChipkaart chipkaart = new OVChipkaart(18326, java.sql.Date.valueOf("2022-12-23"), 2, 10, 6);
        OVChipkaart chipkaart2 = new OVChipkaart(90537, java.sql.Date.valueOf("2022-12-23"), 2, 30, 6);
        nieuwProduct.setOvChipkaarten(List.of(chipkaart, chipkaart2));
        nieuwProduct.setPrijs(100);
        nieuwProduct.setNaam("7 dagen Vertragings garantie");
        System.out.println("VOOR UPDATE");
        for (Product p : producten) {
            System.out.println(p);
        }
        pdao.update(nieuwProduct);
        System.out.println("\nNA UPDATE");
        producten = pdao.findAll();
        for (Product p : producten) {
            System.out.println(p);
        }

        // Delete een product uit de database
        pdao.delete(nieuwProduct);
        System.out.println("\n[Test] resultaat na deleten van product met productnummer 7");
        producten = pdao.findAll();
        for (Product p : producten) {
            System.out.println(p);
        }

        //Find by ovchipkaart
        System.out.println("\n[Test] resultaat  findByOVChipkaart op ovChipkaart met kaartnummer 35283");
        OVChipkaart test = new OVChipkaart(35283,java.sql.Date.valueOf("2018-05-31"),2, 25,2);
        for(Product p :  pdao.findByOVChipkaart(test)){
            System.out.println(p);
        }

    }
}
