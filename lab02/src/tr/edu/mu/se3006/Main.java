package tr.edu.mu.se3006;
import tr.edu.mu.se3006.catalog.CatalogService;
import tr.edu.mu.se3006.catalog.CatalogFactory;
import tr.edu.mu.se3006.orders.OrderController;
import tr.edu.mu.se3006.orders.OrdersFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 System Starting in Modular Monolith Mode...");
        System.out.println("----------------------------------------------\n");
        
        CatalogService catalog = CatalogFactory.create();
        
        OrderController controller = OrdersFactory.create(catalog);
        
        System.out.println("--- Test Scenarios ---");
        controller.handleUserRequest(1L, 2);
        controller.handleUserRequest(2L, 5);
        controller.handleUserRequest(1L, 10);
        controller.handleUserRequest(99L, 1);
    }
}
