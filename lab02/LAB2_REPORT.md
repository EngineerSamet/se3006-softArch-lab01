# SE 3006 Software Architectures - Lab 02 Report
## Topic: Modular Monolith Built with Pure Java

---

## 1. Objective
This lab demonstrates a **Modular Monolith Architecture** using pure Java. Unlike Lab 01's layered approach where `OrderService` directly manipulated `ProductRepository` (creating tight coupling), this lab introduces **vertical business domains (Modules)** — `catalog` and `orders` — that communicate only through public interfaces.

Key goals:
- Apply **Information Hiding** using Java's `package-private` access modifier
- Enforce module boundaries through **Interfaces** (public APIs)
- Use **Factories** to assemble internal dependencies while hiding them from other modules

---

## 2. Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                     Main.java                       │
│            (Bootstrapping / Wiring)                 │
└────────┬───────────────────────────────┬────────────┘
         │                               │
         ▼                               ▼
┌─────────────────────┐     ┌─────────────────────────┐
│   Catalog Module    │     │     Orders Module        │
│  ╔═══════════════╗  │     │  ╔═════════════════════╗ │
│  ║ CatalogService║◄─┼─────┼──║  OrderController    ║ │
│  ║  (Interface)  ║  │     │  ║     (public)        ║ │
│  ╠═══════════════╣  │     │  ╠═════════════════════╣ │
│  ║CatalogFactory ║  │     │  ║  OrdersFactory      ║ │
│  ║   (public)    ║  │     │  ║    (public)         ║ │
│  ╚═══════════════╝  │     │  ╚═════════════════════╝ │
│  ┌───────────────┐  │     │  ┌─────────────────────┐ │
│  │CatalogSvcImpl │  │     │  │   OrderService      │ │
│  │(pkg-private)  │  │     │  │  (pkg-private)      │ │
│  ├───────────────┤  │     │  ├─────────────────────┤ │
│  │ProductRepo    │  │     │  │   OrderRepository   │ │
│  │(pkg-private)  │  │     │  │  (pkg-private)      │ │
│  ├───────────────┤  │     │  ├─────────────────────┤ │
│  │   Product     │  │     │  │      Order          │ │
│  │(pkg-private)  │  │     │  │  (pkg-private)      │ │
│  └───────────────┘  │     │  └─────────────────────┘ │
└─────────────────────┘     └─────────────────────────┘
```

**Key Rule:** Modules communicate ONLY through public interfaces. Internal classes are `package-private` and invisible to other modules.

---

## 3. Task Implementations

### TASK 1: Catalog Module Internal Logic — `ProductRepository` & `CatalogServiceImpl`

**ProductRepository** uses a `HashMap<Long, Product>` as an in-memory database. Two methods were implemented:

- **`findById(Long id)`** — Retrieves a product from the map by its ID.
- **`save(Product product)`** — Stores/updates a product in the map.

```java
Product findById(Long id) {
    return database.get(id);
}

void save(Product product) {
    database.put(product.getId(), product);
}
```

**CatalogServiceImpl** receives `ProductRepository` via **Constructor Injection** and implements the `checkAndReduceStock` method:

1. Find product via repository (throw exception if not found)
2. Check stock availability (throw `IllegalArgumentException` if insufficient)
3. Reduce stock and save the updated product

```java
private final ProductRepository productRepository;

CatalogServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
}

@Override
public void checkAndReduceStock(Long productId, int quantity) {
    Product product = productRepository.findById(productId);
    if (product == null) {
        throw new IllegalArgumentException("Product not found with ID: " + productId);
    }
    if (product.getStock() < quantity) {
        throw new IllegalArgumentException("Insufficient stock for product ID: " + productId
            + ". Available: " + product.getStock() + ", Requested: " + quantity);
    }
    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
}
```

> **Note:** Both `ProductRepository` and `CatalogServiceImpl` are `package-private`. The Orders module cannot access them directly.

---

### TASK 2: Catalog Module Factory — `CatalogFactory`

The factory wires internal components and returns **only the interface**, hiding the implementation details:

```java
public class CatalogFactory {
    public static CatalogService create() {
        ProductRepository repository = new ProductRepository();
        return new CatalogServiceImpl(repository);
    }
}
```

The outside world receives a `CatalogService` reference and has no knowledge of `ProductRepository` or `CatalogServiceImpl`.

---

### TASK 3: Orders Module Logic — `OrderService` & `OrderController`

**OrderService** takes `CatalogService` (not `ProductRepository`!) and `OrderRepository` as dependencies:

```java
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
```

**OrderController** wraps the service call in a `try-catch` block:

```java
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
```

---

### TASK 4: Orders Module Factory — `OrdersFactory`

The factory takes `CatalogService` as an **external dependency** and wires everything internally:

```java
public class OrdersFactory {
    public static OrderController create(CatalogService catalogService) {
        OrderRepository repository = new OrderRepository();
        OrderService service = new OrderService(catalogService, repository);
        return new OrderController(service);
    }
}
```

---

### TASK 5: Main Bootstrapping

The `Main` class uses the factories to create the modular system:

```java
CatalogService catalog = CatalogFactory.create();
OrderController controller = OrdersFactory.create(catalog);
```

This is a **two-step wiring**: first create the Catalog module, then pass its public API to the Orders module.

---

## 4. Test Results

The system was tested with 4 scenarios:

| # | Scenario | Product ID | Quantity | Expected Result | Actual Result |
|---|----------|-----------|----------|-----------------|---------------|
| 1 | Successful order | 1 (MacBook Pro) | 2 | ✅ Order Confirmed | ✅ Order Confirmed |
| 2 | Successful order | 2 (Logitech Mouse) | 5 | ✅ Order Confirmed | ✅ Order Confirmed |
| 3 | Insufficient stock | 1 (MacBook Pro, 3 left) | 10 | ❌ Error | ❌ ERROR: Insufficient stock for product ID: 1. Available: 3, Requested: 10 |
| 4 | Non-existent product | 99 | 1 | ❌ Error | ❌ ERROR: Product not found with ID: 99 |

**Console Output:**
```
🚀 System Starting in Modular Monolith Mode...
----------------------------------------------

--- Test Scenarios ---
>>> New Request: Product ID=1, Quantity=2
✅ Order Confirmed
>>> New Request: Product ID=2, Quantity=5
✅ Order Confirmed
>>> New Request: Product ID=1, Quantity=10
❌ ERROR: Insufficient stock for product ID: 1. Available: 3, Requested: 10
>>> New Request: Product ID=99, Quantity=1
❌ ERROR: Product not found with ID: 99
```

---

## 5. Lab 01 vs Lab 02 Comparison

| Aspect | Lab 01 (Layered) | Lab 02 (Modular Monolith) |
|--------|------------------|--------------------------|
| **Division** | Horizontal (by technical concern) | Vertical (by business domain) |
| **Coupling** | `OrderService` directly uses `ProductRepository` | Orders module only knows `CatalogService` interface |
| **Visibility** | All classes are `public` | Internal classes are `package-private` |
| **Wiring** | Manual in `Main` (bottom-up) | Via public Factories |
| **Information Hiding** | Not enforced | Enforced by compiler (access modifiers) |

---

## 6. Observations & Key Takeaways

1. **Modular Boundaries**: By making internal classes `package-private`, we get **compile-time enforcement** of module boundaries. If the Orders module tries to import `ProductRepository`, the compiler will refuse.

2. **Interface-Based Communication**: The `CatalogService` interface is the only public contract. This means we can swap the entire Catalog implementation without affecting the Orders module.

3. **Factory Pattern**: Factories serve as the **assembly point** for each module. They know about internal details, create the dependency graph, and expose only the public API.

4. **Evolution from Lab 01**: In Lab 01, all classes were `public` and any layer could theoretically access any other. In Lab 02, information hiding is enforced at the language level, which is a much stronger architectural guarantee.

5. **Scalability**: This modular approach makes it easier to extract modules into separate services (microservices) in the future since they already communicate through well-defined interfaces.
