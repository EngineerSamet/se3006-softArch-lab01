package tr.edu.mu.se3006.catalog;

// PUBLIC Factory: Assembles the internal components and exposes ONLY the interface.
public class CatalogFactory {
    public static CatalogService create() {
        ProductRepository repository = new ProductRepository();
        return new CatalogServiceImpl(repository);
    }
}
