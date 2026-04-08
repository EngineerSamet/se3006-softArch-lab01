# SE 3006 Software Architectures - Lab 01 Report
## Topic: Layered Architecture Built with Pure Java

---

## 1. Objective
This lab demonstrates a **Strict Layered Architecture** using pure Java without any framework (e.g., Spring Boot). The system is divided into three horizontal layers based on technical responsibilities, and objects are connected using **Manual Dependency Injection**.

---

## 2. Architecture Overview

```
┌──────────────────────────────────────┐
│    Presentation (OrderController)    │  ← Receives user requests
│         Knows: Business Layer        │
├──────────────────────────────────────┤
│      Business (OrderService)         │  ← Business logic (brain)
│       Knows: Persistence Layer       │
├──────────────────────────────────────┤
│   Persistence (ProductRepository)    │  ← Data access (save/read)
│       Knows: Nothing above it        │
└──────────────────────────────────────┘
          ↕ Domain (Product, Order) — Shared data models
```

**Key Rule:** Data flows **only from top to bottom**. Lower layers never reference upper layers.

---

## 3. Task Implementations

### TASK 1: Persistence Layer — `ProductRepository`
The `ProductRepository` class uses a `HashMap<Long, Product>` as an in-memory database. Two methods were implemented:

- **`findById(Long id)`** — Retrieves a product from the map by its ID.
- **`save(Product product)`** — Stores/updates a product in the map using the product's ID as the key.

```java
public Product findById(Long id) {
    return database.get(id);
}

public void save(Product product) {
    database.put(product.getId(), product);
}
```

---

### TASK 2: Business Layer — `OrderService`
The `OrderService` receives `ProductRepository` via **Constructor Injection**. The `placeOrder` method contains the core business logic:

1. Find the product via repository (throw exception if not found)
2. Check stock availability (throw `IllegalArgumentException` if insufficient)
3. Reduce the stock by the requested quantity
4. Save the updated product back to the repository
5. Create and confirm the order

```java
private final ProductRepository productRepository;

public OrderService(ProductRepository productRepository) {
    this.productRepository = productRepository;
}

public void placeOrder(Long productId, int quantity) {
    Product product = productRepository.findById(productId);
    if (product == null) {
        throw new IllegalArgumentException("Product not found with ID: " + productId);
    }
    if (product.getStock() < quantity) {
        throw new IllegalArgumentException("Insufficient stock! Requested: " + quantity + ", Available: " + product.getStock());
    }
    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
}
```

---

### TASK 3: Presentation Layer — `OrderController`
The `OrderController` receives `OrderService` through **Constructor Injection**. The `handleUserRequest` method wraps the service call in a `try-catch` block:

```java
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
```

---

### TASK 4: Main Class — Bootstrapping (The Wiring)
Objects are created **from bottom to top** following the dependency chain:

```java
ProductRepository productRepository = new ProductRepository();
OrderService orderService = new OrderService(productRepository);
OrderController orderController = new OrderController(orderService);
```

This is **Manual Dependency Injection** — each layer's dependency is explicitly passed through the constructor.

---

## 4. Test Results

The system was tested with 4 scenarios:

| # | Scenario | Product ID | Quantity | Expected Result | Actual Result |
|---|----------|-----------|----------|-----------------|---------------|
| 1 | Successful order | 1 (MacBook Pro) | 2 | ✅ Order Confirmed | ✅ Order Confirmed! Product ID: 1, Quantity: 2 |
| 2 | Successful order | 2 (Logitech Mouse) | 5 | ✅ Order Confirmed | ✅ Order Confirmed! Product ID: 2, Quantity: 5 |
| 3 | Insufficient stock | 1 (MacBook Pro, 3 left) | 10 | ❌ Error | ❌ ERROR: Insufficient stock! Requested: 10, Available: 3 |
| 4 | Non-existent product | 99 | 1 | ❌ Error | ❌ ERROR: Product not found with ID: 99 |

**Console Output:**
```
🚀 System Starting...

--- Test Scenarios ---
>>> New Request: Product ID=1, Quantity=2
✅ Order Confirmed! Product ID: 1, Quantity: 2
>>> New Request: Product ID=2, Quantity=5
✅ Order Confirmed! Product ID: 2, Quantity: 5
>>> New Request: Product ID=1, Quantity=10
❌ ERROR: Insufficient stock! Requested: 10, Available: 3
>>> New Request: Product ID=99, Quantity=1
❌ ERROR: Product not found with ID: 99
```

---

## 5. Observations & Key Takeaways

1. **Strict Layering**: Each layer only knows about the layer directly below it. `OrderController` has no reference to `ProductRepository`, and `ProductRepository` has no reference to any upper layer.

2. **Manual Dependency Injection**: Without frameworks like Spring, we manually create and wire dependencies in the `Main` class from bottom to top. This makes the dependency flow explicit and easy to understand.

3. **Separation of Concerns**: Each layer has a clear technical responsibility:
   - **Presentation**: User interaction and error handling
   - **Business**: Core logic (validation, calculations)
   - **Persistence**: Data storage and retrieval

4. **Testability**: The constructor injection pattern makes it easy to test each layer independently by providing mock dependencies.
