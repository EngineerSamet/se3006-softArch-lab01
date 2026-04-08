package tr.edu.mu.se3006.presentation;
import tr.edu.mu.se3006.business.OrderService;

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void handleUserRequest(Long productId, int quantity) {
        System.out.println(">>> New Request: Product ID=" + productId + ", Quantity=" + quantity);
        try {
            orderService.placeOrder(productId, quantity);
            System.out.println("✅ Order Confirmed! Product ID: " + productId + ", Quantity: " + quantity);
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}
