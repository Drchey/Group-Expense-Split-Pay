# GroupSplitPay

A Spring Boot REST API for creating groups, tracking shared expenses, and splitting costs between group members — with JWT-based authentication and role-based access control.

## Features

- **User authentication** — registration and login secured with JWT
- **Groups** — create groups, with the creator automatically added as an admin member
- **Group membership** — admins can add members; roles (`ADMIN`, `MEMBER`) control permissions
- **Expenses** — group members can log expenses tied to a group
- **Splits** — expense creators can split costs among group members, with validation to ensure splits never exceed the total expense amount
- **Payment tracking** — mark individual splits as paid/unpaid

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Security)
- **Database:** PostgreSQL
- **ORM:** Hibernate
- **Auth:** JWT (JSON Web Tokens)
- **Build tool:** Maven
- **Other:** Lombok

## Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 14+ (running locally or accessible remotely)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/GroupSplitPay.git
cd GroupSplitPay
```

### 2. Configure the database

Create a PostgreSQL database:

```sql
CREATE DATABASE groupsplitpay;
```

### 3. Set environment variables

Create an `.env` file or configure `application.properties` / `application.yml` with:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/groupsplitpay
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.jwt.secret=your_jwt_secret_key
app.jwt.expirationms=3600000
```

> **Note:** Never commit real secrets to version control. Use environment variables or a secrets manager in production.

### 4. Build and run

```bash
mvn clean install
mvn spring-boot:run
```

The app will start on `http://localhost:8080` by default.

## API Overview

All endpoints (except registration/login) require a valid JWT in the `Authorization: Bearer <token>` header.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate and receive a JWT |
| POST | `/api/group` | Create a new group (creator becomes admin) |
| POST | `/api/group_member/{groupId}/{userId}` | Add a user to a group (admin only) |
| POST | `/api/expense/{groupId}` | Create an expense within a group |
| POST | `/api/split/{expenseId}` | Create a split for an expense (creator only) |
| GET | `/api/split/{expenseId}` | List all splits for an expense |
| GET | `/api/split/detail/{splitId}` | Get a single split by ID |
| PUT | `/api/split/{splitId}` | Update a split (creator only) |
| PATCH | `/api/split/{splitId}/paid` | Toggle a split's paid status |
| DELETE | `/api/split/{splitId}` | Delete a split (creator only) |

> Adjust the table above to match your actual `@RequestMapping` paths.

## Business Rules

- A group's creator is automatically added as an `ADMIN` member.
- Only group admins can add new members.
- Only the expense creator can create, update, or delete splits for that expense.
- An expense can have a maximum of **3 splits**.
- The sum of all splits for an expense cannot exceed the expense's total amount.
- Only users involved in a split (creator or debtor) can toggle its paid status.

## Project Structure

```
src/main/java/com/richey/groupsplitpay/
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Spring Data JPA repositories
├── model/          # JPA entities
├── dto/            # Request/response records
├── security/       # JWT filter and security config
└── exception/      # Global exception handling
```

## Running Tests

```bash
mvn test
```

## Roadmap

- [ ] Settle-up / balance summary per group
- [ ] Expense categories
- [ ] Notifications for new splits
- [ ] Frontend client
- [ ] OAuth2 


## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to the branch and open a pull request
