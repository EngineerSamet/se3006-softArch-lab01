package tr.edu.mu.se3006.domain;
public class Order {
    private static Long idCounter = 1L;
    private Long id;
    private Long productId;
    private int quantity;

    public Order(Long productId, int quantity) {
        this.id = idCounter++;
        this.productId = productId;
        this.quantity = quantity;
    }
    public Long getId() { return id; }
}
