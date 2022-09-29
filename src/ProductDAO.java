import java.sql.SQLException;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;

    boolean update(Product product);

    boolean delete(Product product);
}
