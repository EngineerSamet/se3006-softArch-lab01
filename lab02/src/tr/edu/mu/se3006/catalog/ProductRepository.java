package tr.edu.mu.se3006.catalog;
import java.util.*;

// Package-private: Orders module cannot see this!
class ProductRepository {
    private Map<Long, Product> database = new HashMap<>();

    ProductRepository() {
        database.put(1L, new Product(1L, "MacBook Pro", 5));
        database.put(2L, new Product(2L, "Logitech Mouse", 20));
    }
    
    Product findById(Long id) {
        return database.get(id);
    }
    
    void save(Product product) {
        database.put(product.getId(), product);
    }
}
