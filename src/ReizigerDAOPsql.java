import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    private Connection conn;
    private AdresDAOPsql adresDAOPsql;
    private OVChipkaartDAOsql ovChipkaartDAOsql;

    public ReizigerDAOPsql(Connection conn, AdresDAOPsql adresDAOPsql, OVChipkaartDAOsql ovChipkaartDAOsql) {
        this.conn = conn;
        this.adresDAOPsql = adresDAOPsql;
        this.ovChipkaartDAOsql = ovChipkaartDAOsql;

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
            adresDAOPsql.save(reiziger.getAdres());
            ovChipkaartDAOsql.save(reiziger.getOVChipkaarten());
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger, String nieuwachternaam) {
        try {
            Statement statement = conn.createStatement();
            String query ="update reiziger set achternaam ='"+nieuwachternaam+"' where reiziger_id ='"+reiziger.getId()+"'";
            statement.executeQuery(query);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            Statement statement = conn.createStatement();
            String query ="DELETE FROM reiziger WHERE reiziger_id="+reiziger.getId()+";";

            statement.executeQuery(query);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from reiziger");
            Reiziger reiziger = null;
            while (result.next()) {
                if(id == result.getInt("reiziger_id")){
                    reiziger = new Reiziger(result.getInt("reiziger_id"), result.getString("voorletters"), result.getString("tussenvoegsel"), result.getString("achternaam"), result.getDate("geboortedatum"));
                }
            }
            return reiziger;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        try {
            Statement statement = conn.createStatement();
            String query ="select * from reiziger where geboortedatum ='"+datum+"'";
            ResultSet result = statement.executeQuery(query);
            List<Reiziger> reizigers = new ArrayList<>();
            while (result.next()) {

                Reiziger reiziger = new Reiziger(result.getInt("reiziger_id"), result.getString("voorletters"), result.getString("tussenvoegsel"), result.getString("achternaam"), result.getDate("geboortedatum"));
                reizigers.add(reiziger);
            }
            return reizigers;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        try {
            String query = "select * from reiziger;";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            List<Reiziger> reizigers = new ArrayList<>();
            while (result.next()) {
                String tussenvoegsel = result.getString("tussenvoegsel");
                if (tussenvoegsel == null) {
                    tussenvoegsel = "";
                }
                Reiziger reiziger = new Reiziger(result.getInt("reiziger_id"), result.getString("voorletters"), tussenvoegsel, result.getString("achternaam"), result.getDate("geboortedatum"));
                try {
                    String adresQuery ="select * from adres where reiziger_id = ?;";
                    PreparedStatement adresPst = conn.prepareStatement(adresQuery);
                    adresPst.setInt(1, reiziger.getId());
                    ResultSet adresResult = adresPst.executeQuery();
                    while (adresResult.next()) {
                        try {
                            if (reiziger.getId() == adresResult.getInt("reiziger_id")){
                                reiziger.setAdres(new Adres(adresResult.getInt("adres_id"), adresResult.getString("postcode"), adresResult.getString("huisnummer"), adresResult.getString("straat"), adresResult.getString("woonplaats"), adresResult.getInt("reiziger_id")));
                            }
                        }
                        catch (Exception e){
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    String ovQuery ="select * from ov_chipkaart where reiziger_id = ?;";
                    PreparedStatement ovPst = conn.prepareStatement(ovQuery);
                    ovPst.setInt(1, reiziger.getId());
                    ResultSet ovResult = ovPst.executeQuery();
                    while (ovResult.next()) {
                        try {
                            if (reiziger.getId() == ovResult.getInt("reiziger_id")){
                                OVChipkaart kaart = new OVChipkaart(ovResult.getInt("kaart_nummer"), ovResult.getDate("geldig_tot"), ovResult.getInt("klasse"), ovResult.getInt("saldo"), ovResult.getInt("reiziger_id"));
                                reiziger.addOVChipkaart(kaart);

                            }
                        }
                        catch (Exception e){
                        }
                    }
                } catch (Exception e) {
                }
                reizigers.add(reiziger);
            }

            return reizigers;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
