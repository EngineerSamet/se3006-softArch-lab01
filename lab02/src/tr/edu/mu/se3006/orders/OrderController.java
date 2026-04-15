package tr.edu.mu.se3006.orders;

// PUBLIC: Entry point for the Orders context.
public class OrderController {
    
    private final OrderService orderService;
    
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void handleUserRequest(Long productId, int quantity) {
        System.out.println(">>> New Request: Product ID=" + productId + ", Quantity=" + quantity);
        try {
            orderService.placeOrder(productId, quantity);
            System.out.println("✅ Order Confirmed");
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}
