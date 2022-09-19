import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOsql implements OVChipkaartDAO{

    Connection conn;


    public OVChipkaartDAOsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> kaarten = new ArrayList<>();
        try {
            String query ="select * from ov_chipkaart;";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                OVChipkaart kaart = new OVChipkaart(result.getInt("kaart_nummer"), result.getDate("geldig_tot"), result.getInt("klasse"), result.getInt("saldo"), result.getInt("reiziger_id"));
                kaarten.add(kaart);
            }
            return kaarten;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean save(OVChipkaart kaart) throws SQLException {
        try {
            String query =
                    "INSERT INTO ov_chipkaart"
                    +"(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id)"
                    +"values (?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, kaart.getKaart_nummer());
            pst.setDate(2, kaart.getGeldig_tot());
            pst.setInt(3, kaart.getKlasse());
            pst.setInt(4, kaart.getSaldo());
            pst.setInt(5, kaart.getReiziger_id());

            pst.executeQuery();
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart kaart, int saldo) {
        try {
            String query ="update ov_chipkaart set saldo =? where kaart_nummer =?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, saldo);
            pst.setInt(2, kaart.getKaart_nummer());
            pst.executeQuery();
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart kaart) {
        try {
            String query ="DELETE FROM ov_chipkaart WHERE kaart_nummer =?;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, kaart.getKaart_nummer());
            pst.executeQuery();
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return null;
    }
}
