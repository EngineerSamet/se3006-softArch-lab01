# 📦 SE 3006: Software Architecture - Lab 02
## Topic: Modular Monolith Built with Pure Java

### 🎯 Objective of the Laboratory
In Lab 01, we built a Layered Architecture where the `OrderService` directly manipulated the `ProductRepository`. This created **Tight Coupling**. 
In this lab, we will refactor the system into a **Modular Monolith**. The system will be divided into vertical business domains (Modules): `catalog` and `orders`.

Our goals are to:
1. Apply **Information Hiding** by using Java's `package-private` (default) access modifier.
2. Ensure modules communicate ONLY through public **Interfaces** (APIs), never by sharing internal databases.
3. Use **Factories** to wire internal dependencies while keeping them hidden from the outside world.

---

### 🏛️ Architectural Rules (Modular Boundaries)
1. **Catalog Module:** Manages products and stock. Only `CatalogService` (Interface) and `CatalogFactory` are `public`. Everything else (`Product`, `ProductRepository`, `CatalogServiceImpl`) must be `package-private`.
2. **Orders Module:** Manages orders. It does NOT know what a `Product` is. It only knows that it can call `catalogService.checkAndReduceStock()`.

---

## 🛠️ STEP-BY-STEP TASKS

Fill in the areas marked with `// TODO` in the following order:

### **TASK 1: Catalog Module Internal Logic**
1. Open `catalog.ProductRepository` and write the `findById` and `save` methods. Notice it is NOT public.
2. Open `catalog.CatalogServiceImpl`. Inject `ProductRepository` via the constructor.
3. Implement the `checkAndReduceStock(Long id, int quantity)` method here.

### **TASK 2: Catalog Module Factory (The Exporter)**
1. Open `catalog.CatalogFactory`.
2. Implement the `create()` method to instantiate the repository, pass it to the service implementation, and return the `CatalogService` interface.

### **TASK 3: Orders Module Logic**
1. Open `orders.OrderService`. It must take `CatalogService` as a dependency (Constructor Injection).
2. Write the `placeOrder(Long productId, int quantity)` method. Note that you CANNOT access `ProductRepository` here even if you tried. Call the interface method instead.
3. Open `orders.OrderController` and inject `OrderService`. Handle the request with a `try-catch` block.

### **TASK 4: Orders Module Factory**
1. Open `orders.OrdersFactory`. 
2. It requires `CatalogService` as an external dependency. Wire the `OrderRepository`, `OrderService`, and `OrderController` together and return the controller.

### **TASK 5: Main Bootstrapping**
1. Open `Main.java`.
2. Use the factories to create the Catalog module, pass it to the Orders module, and test the system.

---

### 📦 Submission Rules
* Perform **Commit** and **Push** operations to your own GitLab/Github branch.
* Create your lab report named `LAB_REPORT.md` in the project root directory.
