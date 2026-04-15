package tr.edu.mu.se3006.catalog;

// PUBLIC API: This is the ONLY contract exposed to other modules.
public interface CatalogService {
    void checkAndReduceStock(Long productId, int quantity);
}
