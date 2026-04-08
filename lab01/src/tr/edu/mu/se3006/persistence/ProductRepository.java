package tr.edu.mu.se3006.persistence;
import tr.edu.mu.se3006.domain.Product;
import java.util.*;

public class ProductRepository {
    private Map<Long, Product> database = new HashMap<>();

    public ProductRepository() {
        database.put(1L, new Product(1L, "MacBook Pro", 5));
        database.put(2L, new Product(2L, "Logitech Mouse", 20));
    }
    
    public Product findById(Long id) {
        return database.get(id);
    }

    public void save(Product product) {
        database.put(product.getId(), product);
    }
}
