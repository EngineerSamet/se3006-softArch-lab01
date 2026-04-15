package tr.edu.mu.se3006.orders;
import tr.edu.mu.se3006.catalog.CatalogService;

// PUBLIC Factory: Wires the orders module. Needs Catalog API to function.
public class OrdersFactory {
    public static OrderController create(CatalogService catalogService) {
        OrderRepository repository = new OrderRepository();
        OrderService service = new OrderService(catalogService, repository);
        return new OrderController(service);
    }
}
