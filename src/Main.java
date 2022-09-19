import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        try {
            getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from reiziger");

            while (result.next()) {
                String tussenvoegsel = result.getString("tussenvoegsel");
                if (tussenvoegsel == null) {
                    tussenvoegsel = "";
                }
                System.out.println(result.getString("reiziger_id") + ": " + result.getString("voorletters") + ". " + tussenvoegsel + result.getString("achternaam") + " (" + result.getString("geboortedatum") + ")");
            }

        closeConnection();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
            closeConnection();
        }


    }

    private static Connection getConnection() {
        if (connection == null) {
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

    private static void closeConnection() throws SQLException {
        try {
            connection.close();
            connection=null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
