import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAO rdao;




    public AdresDAOPsql(Connection conn){
        this.conn = conn;
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from adres");
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(conn);
            List<Reiziger> reizigers = reizigerDAOPsql.findAll();
            while (result.next()) {
                    try {

                        for(Reiziger r : reizigers){
                            if (r.getId() == result.getInt("reiziger_id")){
                                r.setAdres(new Adres(result.getInt("adres_id"), result.getString("postcode"), result.getString("huisnummer"), result.getString("straat"), result.getString("woonplaats"), result.getInt("reiziger_id")));
                            }
                        }
                    }
                    catch (Exception e){

                    }
                }
            } catch (Exception e) {
        }
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
            Statement statement = conn.createStatement();
            String query =
                    "update adres set postcode='2403JD' where adres_id='"+adres.getId()+"'";


            statement.executeQuery(query);
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
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from adres");
            Adres adres = null;
            while (result.next()) {
                if(reiziger.getId() == result.getInt("reiziger_id")){
                     adres = new Adres(result.getInt("adres_id"), result.getString("postcode"), result.getString("huisnummer"), result.getString("straat"), result.getString("woonplaats"), result.getInt("reiziger_id"));
                }
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
