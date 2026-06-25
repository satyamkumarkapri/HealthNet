<div align="center">
  <h1>🏥 HEALTHNET</h1>
  <h3>Intelligent Hospital Resource Management System</h3>
  <p>An enterprise-grade, console-based Java application demonstrating advanced Data Structures, Algorithms, and Software Engineering Design Patterns.</p>
</div>

---

## 📖 Overview

HealthNet is an "Ultra Pro Level" hospital management simulator. Unlike typical applications that rely on external relational databases, HealthNet operates entirely **in-memory**, orchestrating complex, real-world hospital scenarios using raw Data Structures (Trees, Graphs, Heaps) and classic Algorithms (Dynamic Programming, Greedy Algorithms, Graph Traversal).

It is built as an educational and technical showcase of writing highly optimal, mathematically rigorous Java code.

---

## 🚀 Key Architectural Enhancements

### Design Patterns Implemented
To ensure the codebase scales cleanly and prevents memory leaks or data desynchronization, HealthNet strictly adheres to several GoF Design Patterns:

1. **Singleton Pattern**: Core controllers (`PatientService`, `DoctorService`, `ResourceService`, `AppointmentService`) are thread-safe Singletons. This establishes a singular, global point of truth for all in-memory data structures.
2. **Observer Pattern**: The system implements an event-driven `Subject/Observer` model. If critical physical resources (e.g., Ventilators, ICU Beds) dip below 20% capacity during allocation, real-time alerts are pushed to the UI.
3. **Strategy Pattern**: The Analytics and Reporting module dynamically swaps sorting behaviors at runtime via a `SortingContext`. Algorithms like `QuickSort`, `HeapSort`, and `RadixSort` are wrapped in interchangeable `SortingStrategy` classes.

### Advanced Triage Engine
Patients are modeled with a dynamically assigned `severityScore` (1-100). This dictates processing priority, simulating a real-world Emergency Room triage where severe trauma cases bypass standard queues via a Max-Heap (`PriorityQueue`).

---

## 🛠️ Tech Stack & Build Instructions

- **Language**: Java 17+
- **Build System**: Native `javac` (No external dependencies like Maven or Gradle required)
- **UI**: Interactive Console/Terminal

### Compilation and Execution
To compile the project from the root directory:
```bash
# 1. Discover all Java source files
find src -name "*.java" > sources.txt

# 2. Compile into the bin/ directory
javac -d bin -sourcepath src @sources.txt
```

To run the application:
```bash
java -cp bin healthnet.Main
```

---

## 🧩 Detailed Module Breakdown

### 1. Patient Management
* **Data Structures**: Binary Search Tree (BST) and AVL Tree.
* **Functionality**: Patients are inserted into both a standard BST and an actively self-balancing AVL tree. This demonstrates the performance differences in `O(log N)` lookup times vs `O(N)` worst-case scenarios when managing massive hospital rosters.

### 2. Appointment Scheduling
* **Algorithm**: Greedy Approach (Activity Selection).
* **Functionality**: Doctors have limited shifts. When multiple patients request overlapping appointments, the Greedy algorithm calculates the mathematical maximum number of patients a doctor can see by sorting requests by their end times (`HH:mm` format).

### 3. Medical Records Indexing
* **Data Structures**: B-Tree and B+ Tree.
* **Functionality**: Simulates a database index. Medical records are stored in B-Trees to allow for rapid range queries (e.g., "Find all patient records between ID 1000 and 5000").

### 4. Hospital Resource Allocation
* **Algorithm**: Dynamic Programming (Fractional and 0/1 Knapsack).
* **Functionality**: Given limited hospital capacity (e.g., limited oxygen cylinders), the system calculates the optimal allocation of resources to patients to maximize overall survival/health benefit. Wrapped in the Observer pattern to prevent resource depletion.

### 5. Emergency Routing System
* **Data Structures**: Weighted Directed Graphs.
* **Algorithms**: Dijkstra's, Bellman-Ford, Floyd-Warshall.
* **Functionality**: Models ambulance routing between city hospitals and clinics. Capable of calculating the absolute shortest path to an emergency, detecting negative weight cycles (e.g., impassable traffic), and generating a complete distance matrix for the city grid.

### 6. Department Network Analysis
* **Data Structures**: Unweighted Graphs.
* **Algorithms**: Breadth-First Search (BFS), Depth-First Search (DFS), Minimum Spanning Tree (Kruskal/Prim).
* **Functionality**: Maps the internal structure of the hospital. Used to find the minimum cable/networking required to connect all hospital departments (MST) or to traverse departments in search of specific specialists.

### 7. Advanced Analytics & Reporting
* **Algorithms**: Merge Sort, Quick Sort, Heap Sort, Counting Sort, Radix Sort.
* **Functionality**: Benchmarks massive datasets of Patients and Medical Records to demonstrate the efficiency of different sorting algorithms. Exports these datasets into beautifully formatted `.txt` reports using the `ReportGenerator` utility.

### 8. Patient Name Correction
* **Algorithm**: Dynamic Programming (Edit Distance & Longest Common Subsequence).
* **Functionality**: Acts as an intelligent search bar. If a receptionist misspells a patient's name, the system calculates the Levenshtein distance against the database to suggest the closest correct patient profile.

---

## 📁 Directory Structure
```text
HealthNet/
├── src/
│   └── healthnet/
│       ├── Main.java               # Application Entry Point
│       ├── model/                  # Domain Entities (Patient, Doctor, Resource, etc.)
│       ├── services/               # Singleton Controllers
│       ├── patterns/               # GoF Pattern Implementations (Observer, Strategy)
│       ├── sorting/                # Sorting Algorithm implementations
│       ├── trees/                  # BST, AVL, B-Tree implementations
│       ├── graphs/                 # Graph Traversals, Shortest Path Algorithms
│       ├── dp/                     # Knapsack, LCS, Edit Distance algorithms
│       └── utils/                  # ConsoleUI, ReportGenerator, DataLoader
├── bin/                            # Compiled .class files
└── README.md
```

---
*Developed as a comprehensive deep-dive into Data Structures and algorithmic efficiency.*
