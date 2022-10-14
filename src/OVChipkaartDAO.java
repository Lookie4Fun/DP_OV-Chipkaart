import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    List<OVChipkaart> findAll() throws SQLException;

    boolean save(OVChipkaart kaart) throws SQLException;

    boolean update(OVChipkaart kaart);

    boolean delete(OVChipkaart kaart);

    List<OVChipkaart> findByReiziger(Reiziger reiziger);

}
