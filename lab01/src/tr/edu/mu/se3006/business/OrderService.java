package tr.edu.mu.se3006.business;
import tr.edu.mu.se3006.persistence.ProductRepository;
import tr.edu.mu.se3006.domain.Product;
import tr.edu.mu.se3006.domain.Order;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void placeOrder(Long productId, int quantity) {
        // 1: Find product via repository
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        // 2: Check stock (throw IllegalArgumentException if insufficient)
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock! Requested: " + quantity + ", Available: " + product.getStock());
        }

        // 3: Reduce stock
        product.setStock(product.getStock() - quantity);

        // 4: Save updated product
        productRepository.save(product);

        Order order = new Order(productId, quantity);
        System.out.println("✅ Order Confirmed! Order ID: " + order.getId() + ", Product: " + product.getName() + ", Quantity: " + quantity);
    }
}
