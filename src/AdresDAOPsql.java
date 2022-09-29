import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(Adres adres) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            String query = "" +
                    "INSERT INTO adres"
                    +"(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)"
                    +"values ('"+adres.getId()+"','"+adres.getPostcode()+"','"+adres.getHuisnummer()+"','"+adres.getStraat()+"','"+adres.getWoonplaats()+"','"+adres.getReiziger_id()+"')";
            statement.executeQuery(query);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query ="update adres set postcode =?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=? where adres_id =?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger_id());
            pst.setInt(6, adres.getId());
            pst.executeQuery();
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            Statement statement = conn.createStatement();
            String query =
                    "DELETE FROM adres WHERE adres_id="+adres.getId()+";";

            statement.executeQuery(query);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            String query = "select * from adres where reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet result = pst.executeQuery();
            Adres adres = null;
            while (result.next()) {
                adres = new Adres(result.getInt("adres_id"), result.getString("postcode"), result.getString("huisnummer"), result.getString("straat"), result.getString("woonplaats"), result.getInt("reiziger_id"));
            }
            return adres;
        }catch (Exception e){
            System.out.println(e.getMessage());

            return null;
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from adres");
            List<Adres> adressen = new ArrayList<>();
            while (result.next()) {

                Adres adres = new Adres(result.getInt("adres_id"), result.getString("postcode"), result.getString("huisnummer"), result.getString("straat"), result.getString("woonplaats"), result.getInt("reiziger_id"));
                adressen.add(adres);
            }
            return adressen;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
