package tr.edu.mu.se3006.domain;
public class Product {
    private Long id;
    private String name;
    private int stock;

    public Product(Long id, String name, int stock) {
        this.id = id; this.name = name; this.stock = stock;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
