package tr.edu.mu.se3006;
import tr.edu.mu.se3006.presentation.OrderController;
import tr.edu.mu.se3006.business.OrderService;
import tr.edu.mu.se3006.persistence.ProductRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 System Starting...\n");
        
        // 1: Create the lowest layer (ProductRepository)
        ProductRepository productRepository = new ProductRepository();

        // 2: Create the middle layer (OrderService) and inject the repository
        OrderService orderService = new OrderService(productRepository);

        // 3: Create the top layer (OrderController) and inject the service
        OrderController orderController = new OrderController(orderService);

        System.out.println("--- Test Scenarios ---");
        // Test 1: Successful order (MacBook Pro, stock=5, ordering 2)
        orderController.handleUserRequest(1L, 2);

        // Test 2: Another successful order (Logitech Mouse, stock=20, ordering 5)
        orderController.handleUserRequest(2L, 5);

        // Test 3: Insufficient stock (MacBook Pro now has 3 left, ordering 10)
        orderController.handleUserRequest(1L, 10);

        // Test 4: Non-existent product
        orderController.handleUserRequest(99L, 1);
    }
}
