# 🏗️ SE 3006: Software Architecture - Lab 01
## Topic: Layered Architecture Built with Pure Java

### 🎯 Objective of the Laboratory
The main objective of this lab is to practically observe how a software system is divided into horizontal layers based on its "Technical Responsibilities".
In this practice, frameworks that automate tasks like Spring Boot will **not be used.** Our goals are to:
1. Understand how objects are connected to each other (Manual Dependency Injection).
2. See how strict access rules between layers (Strict Layering) are implemented at the code level.

---

### 🏛️ Architectural Rules (Strict Layering)
Our system consists of 3 main layers from top to bottom. Data flow can **only go from top to bottom**. Lower layers must never know about upper layers.

1. **Presentation (Controller):** Only receives requests. It only knows the Business layer.
2. **Business (Service):** The brain of the application. It only knows the Persistence layer.
3. **Persistence (Repository):** Saves and reads data. It knows nothing about any layer above it.

---

## 🛠️ STEP-BY-STEP TASKS

Fill in the areas marked with `// TODO` in the following order:

### **TASK 1: Data Access (Persistence Layer)**
1. Open the `persistence.ProductRepository` class.
2. Define a `HashMap` that holds a `Long` type ID and a `Product` object.
3. Write the `findById(Long id)` and `save(Product product)` methods.

### **TASK 2: Business Logic (Business Layer)**
1. Open the `business.OrderService` class.
2. Inject `ProductRepository` via the **Constructor**.
3. Fill in the `placeOrder(Long productId, int quantity)` method: Find product, check stock (throw Exception if insufficient), reduce stock, and save.

### **TASK 3: Presentation Layer**
1. Open the `presentation.OrderController` class.
2. Inject the `OrderService` dependency via the Constructor.
3. Write the `handleUserRequest(Long productId, int quantity)` method using a `try-catch` block.

### **TASK 4: Bootstrapping the System (The Wiring - Main Class)**
1. Open the `Main` class.
2. Create the objects in order **from bottom to top**: `ProductRepository` -> `OrderService` -> `OrderController`.
3. Test the system by calling `handleUserRequest`.

---

### 📦 Submission Rules
* Perform **Commit** and **Push** operations to your own GitLab branch.
* Create your lab report named `LAB_REPORT.md` in the project root directory.
