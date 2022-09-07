import java.sql.*;

public class Main {
    public static void main(String [] args){
        String jdbURL = "jdbc:postgresql://localhost:5432/ovchip";
        String username = "postgres";
        String password = "1234";

        try {
            Connection connection = DriverManager.getConnection(jdbURL,username,password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from reiziger");

            while (result.next()){
                String tussenvoegsel = result.getString("tussenvoegsel");
                if(tussenvoegsel == null){
                    tussenvoegsel="";
                }
                System.out.println(result.getString("reiziger_id")+": "+result.getString("voorletters")+". "+tussenvoegsel+result.getString("achternaam")+" ("+result.getString("geboortedatum")+")");
            }


        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }
}
