import javax.management.Query;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            String query = "" +
                    "INSERT INTO reiziger"
                    +"(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)"
                    +"values ('"+reiziger.getId()+"','"+reiziger.getVoorletters()+"','"+reiziger.getTussenvoegsel()+"','"+reiziger.getAchternaam()+"','"+reiziger.getGeboortedatum()+"')";
            statement.executeQuery(query);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        return null;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from reiziger");
            List<Reiziger> reizigers = new ArrayList<>();
            while (result.next()) {
                String tussenvoegsel = result.getString("tussenvoegsel");
                if (tussenvoegsel == null) {
                    tussenvoegsel = "";
                }
                Reiziger reiziger = new Reiziger(result.getInt("reiziger_id"), result.getString("voorletters"), tussenvoegsel, result.getString("achternaam"), result.getDate("geboortedatum"));
                reizigers.add(reiziger);
            }
            return reizigers;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
