import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection = null;

    public static void main(String [] args) throws SQLException {
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection());
        AdresDAOPsql adresDAOPsql = new AdresDAOPsql(getConnection());
        testReizigerDAO(reizigerDAOPsql);
        testAdresDAO(adresDAOPsql);
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
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        //Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe adres aan en persisteer deze in de database
        Adres nieuwadres = new Adres(77,"1234XZ", "12", "huislaan", "Amsterdam", 77);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na adresDAO.save() ");
        adao.save(nieuwadres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Update een adres in de database
        adao.update(nieuwadres);
        System.out.println("[Test] resultaat voor en na updaten van postcode op adres met id 77");
        System.out.println("VOOR UPDATE");
        for (Adres a : adressen) {
            System.out.println(a);
        }
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
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(getConnection());

        System.out.println(adao.findByReiziger(reizigerDAO.findById(1)));


    }
}
