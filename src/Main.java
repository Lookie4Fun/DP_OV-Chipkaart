import java.sql.*;

public class Main {
    private static Connection connection = null;

    public static void main(String [] args) {
        if (connection == null) {
            String jdbURL = "jdbc:postgresql://localhost:5432/ovchip";
            String username = "postgres";
            String password = "1234";

            try {
                connection = DriverManager.getConnection(jdbURL, username, password);
            } catch (SQLException e) {
                System.out.println("Error");
                e.printStackTrace();
            }


            try {

                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery("select * from reiziger");

                while (result.next()) {
                    String tussenvoegsel = result.getString("tussenvoegsel");
                    if (tussenvoegsel == null) {
                        tussenvoegsel = "";
                    }
                    System.out.println(result.getString("reiziger_id") + ": " + result.getString("voorletters") + ". " + tussenvoegsel + result.getString("achternaam") + " (" + result.getString("geboortedatum") + ")");
                }


            } catch (SQLException e) {
                System.out.println("Error");
                e.printStackTrace();
            }

        }
    }
}
