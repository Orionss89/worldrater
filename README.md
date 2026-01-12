# 🌍 WorldRater

**WorldRater** is a professional full-stack application serving as a **Tourist Attraction Rating API**. It allows users to discover, rate, and review attractions worldwide, providing a reliable platform for community-driven travel insights.

Built with security and scalability in mind, it features a **Spring Boot 3** backend and a clean, responsive web interface.

---

## 🚀 Key Features

### 🏛 Attraction Management

* **Browse & Search**: Explore all attractions or search by name, city, and category.
* **Detailed View**: Access full information, including average ratings.
* **Admin Control**: Full CRUD operations for administrators to manage the attraction database.

### ⭐ Community Reviews

* **Interactive Ratings**: Users can leave ratings (1–5 stars) and detailed comments.
* **Review Tracking**: View all reviews for a specific attraction to make informed travel decisions.

### 🔐 Security & User Management

* **Role-Based Access Control (RBAC)**: Separate permissions for `USER` and `ADMIN` roles.
* **Secure Authentication**: Custom login and registration flow with **BCrypt** password encryption.
* **Form Login**: Integrated security for both web UI and REST endpoints.

---

## 🛠 Tech Stack

| Component         | Technology                     |
| ----------------- | ------------------------------ |
| **Backend**       | Java 21, Spring Boot 3.2.0     |
| **Security**      | Spring Security (Form + Basic) |
| **Database**      | H2 In-Memory Database          |
| **Tools**         | Maven, Lombok, Spring Data JPA |
| **Documentation** | Springdoc OpenAPI (Swagger UI) |
| **Frontend**      | HTML5, CSS3, JavaScript        |

---

## 📖 API Reference

Detailed API documentation is available via **Swagger UI** at: `http://localhost:8080/swagger-ui.html`

| Method     | Endpoint                        | Access         | Description                    |
| :--------- | :------------------------------ | :------------- | :----------------------------- |
| **GET**    | `/api/attractions`              | Public         | List all attractions           |
| **POST**   | `/api/attractions`              | Authenticated  | Add a new attraction           |
| **DELETE** | `/api/attractions/{id}`         | **Admin Only** | Remove an attraction           |
| **GET**    | `/api/attractions/{id}/reviews` | Public         | View reviews for an attraction |
| **POST**   | `/api/auth/register`            | Public         | Create a new user account      |

---

## ⚙️ Installation & Running

### Prerequisites

* **Java 21**
* **Maven 3.8+**

### Steps to Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Orionss89/worldrater
   cd worldrater
   ```
2. **Launch the application:**

   ```bash
   mvn spring-boot:run
   ```
3. **Access points:**

   * **Web UI:** `http://localhost:8080`
   * **Login Page:** `http://localhost:8080/login.html`
   * **H2 Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:worldraterdb`)

### Demo Credentials & Testing

**Default Admin Account:**
The system initializes with a default administrator for testing:

* **Username:** `admin`
* **Password:** `admin123`

**Run Automated Tests:**
To verify security filters, login flows, and API integrity:

```bash
mvn test
```
