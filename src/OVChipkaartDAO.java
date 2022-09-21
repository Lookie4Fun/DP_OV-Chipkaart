import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    List<OVChipkaart> findAll() throws SQLException;

    boolean save(List<OVChipkaart> kaart) throws SQLException;

    boolean update(OVChipkaart kaart, int saldo);

    boolean delete(OVChipkaart kaart);

    List<OVChipkaart> findByReiziger(Reiziger reiziger);

}
