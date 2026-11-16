# StockFlow

Java desktop application for **stock and inventory management**: articles, internal and external orders, stock movements, inventories, statistics and full traceability. Built with a **PostgreSQL** database and a clean **MVC architecture**.

> Academic project developed at ISIMM (Institut Supérieur d'Informatique et de Mathématiques de Monastir).

![Java](https://img.shields.io/badge/Java-17+-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-JDBC-blue)
![Architecture](https://img.shields.io/badge/Architecture-MVC-green)

## Features

- **Authentication**: secure login screen with user accounts
- **Article management**: create, edit, search and delete articles
- **Stock management**: real-time stock levels per article and storage location
- **Internal orders**: requests between services, with order lines
- **External orders**: purchase orders to suppliers, with order lines
- **Stock movements**: entries, exits and transfers recorded automatically
- **Inventories**: periodic inventory sessions with count lines and gap detection
- **Quality controls**: control records linked to deliveries and articles
- **Statistics**: dashboards and indicators on stock and orders
- **Traceability**: history of actions performed in the application
- **Organisation**: management of services, storage locations (locaux) and suppliers (fournisseurs)

## Architecture

The project follows the **MVC** pattern, with a dedicated utility layer for database access.

```
src/
├── model/        # Entities and enums (Article, Stock, CommandeInterne, Inventaire, ...)
├── controller/   # Business logic and SQL access (ArticleController, StockController, ...)
├── view/         # User interface forms (LoginForm, ArticleForm, Main, ...)
└── util/         # DBConnection (JDBC connection read from config.properties)
```

Dependencies only go one way: `view` -> `controller` -> `model` / `util`. No lower layer depends on `view`.

## Tech stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Language       | Java                                |
| UI             | Java Swing                          |
| Database       | PostgreSQL                          |
| Data access    | JDBC (PostgreSQL driver)            |
| IDE            | IntelliJ IDEA                       |

## Getting started

### Prerequisites

- JDK 17 or later
- PostgreSQL 13 or later
- The [PostgreSQL JDBC driver](https://jdbc.postgresql.org/) added to the project libraries

### 1. Clone the repository

```bash
git clone https://github.com/FatmaMoussaa/StockFlow.git
cd StockFlow
```

### 2. Create the database

Create an empty database named `GestionStock`, then run the SQL schema included in the repository:

```bash
psql -U postgres -c "CREATE DATABASE \"GestionStock\";"
psql -U postgres -d GestionStock -f path/to/schema.sql
```

### 3. Configure the connection

Copy the example configuration file and fill in your own credentials:

```bash
copy config.example.properties config.properties      # Windows
# cp config.example.properties config.properties      # Linux / macOS
```

Then edit `config.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/GestionStock
db.user=postgres
db.password=your_password
```

`config.properties` is listed in `.gitignore`, so your credentials are never committed.

### 4. Run the application

Open the project in IntelliJ IDEA, make sure the PostgreSQL driver is on the classpath, and run:

```
src/view/Main.java
```

To check the database connection on its own, run `src/util/TestConnection.java`.

## Screenshots

<!-- Add your screenshots to docs/screenshots/ then uncomment the lines below.

![Login](docs/screenshots/login.png)
![Articles](docs/screenshots/articles.png)
![Orders](docs/screenshots/orders.png)
-->

## Security notes

- Database credentials are loaded from an external `config.properties` file that is **not** tracked by Git.
- Only `config.example.properties`, which contains placeholder values, is published.

## Author

**Fatma Moussaa**
GitHub: [@FatmaMoussaa](https://github.com/FatmaMoussaa)
