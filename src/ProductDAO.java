import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    List<Product> findAll() throws SQLException;

    boolean save(Product product) throws SQLException;

    boolean update(Product product);

    boolean delete(Product product);

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
}
