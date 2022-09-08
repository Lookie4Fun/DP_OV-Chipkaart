import java.sql.*;
import java.util.List;

public class Main {
    private Connection connection;
    public static void main(String [] args) throws SQLException {
        getConnection();
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection());
        testReizigerDAO(reizigerDAOPsql);
        closeConnection();


    }

    private static Connection getConnection(){
        String jdbURL = "jdbc:postgresql://localhost:5432/ovchip";
        String username = "postgres";
        String password = "1234";

        try {
            return DriverManager.getConnection(jdbURL, username, password);

        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
            return null;
        }
    }

    private static void closeConnection(){

    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


    }
}
