package tr.edu.mu.se3006.catalog;

// Notice the lack of 'public'. This is package-private.
class Product {
    private Long id;
    private String name;
    private int stock;

    Product(Long id, String name, int stock) {
        this.id = id; this.name = name; this.stock = stock;
    }
    Long getId() { return id; }
    int getStock() { return stock; }
    void setStock(int stock) { this.stock = stock; }
}
