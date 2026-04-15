package tr.edu.mu.se3006.orders;
import tr.edu.mu.se3006.catalog.CatalogService;

class OrderService {
    private final CatalogService catalogService;
    private final OrderRepository orderRepository;
    
    OrderService(CatalogService catalogService, OrderRepository orderRepository) {
        this.catalogService = catalogService;
        this.orderRepository = orderRepository;
    }
    
    void placeOrder(Long productId, int quantity) {
        catalogService.checkAndReduceStock(productId, quantity);
        Order order = new Order(productId, quantity);
        orderRepository.save(order);
    }
}
