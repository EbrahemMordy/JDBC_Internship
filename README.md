# JDBC Learning Project for Interns

Welcome to the JDBC Learning Project! This project is designed to teach interns the basics of Java Database Connectivity (JDBC) using MySQL.

## 🎯 Learning Objectives

By completing this project, you will learn:
- How to connect Java applications to MySQL databases
- Basic CRUD operations (Create, Read, Update, Delete)
- Best practices for database programming in Java
- DAO (Data Access Object) pattern
- SQL injection prevention using PreparedStatement
- Proper resource management with try-with-resources

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Basic understanding of Java programming
- Basic understanding of SQL

## 🏗️ Project Structure

```
src/
├── main/
│   └── java/
│       └── org/
│           └── intern/
│               ├── Main.java              # Main application with demos
│               ├── config/
│               │   └── DatabaseConfig.java # Database connection configuration
│               ├── dao/
│               │   ├── StudentDAO.java     # DAO interface
│               │   └── StudentDAOImpl.java # DAO implementation
│               └── model/
│                   └── Student.java        # Student entity/model class
```

## 🚀 Getting Started

### 1. Clone and Setup
```bash
git clone https://github.com/EbrahemMordy/JDBC_Internship.git
cd JDBC_Internship
```

### 2. Configure Database Credentials
For security reasons, database credentials are not stored in the code. You have two options:

**Option A: Environment Variables (Recommended)**
```bash
# Windows
set DB_URL=jdbc:mysql://your-host:port/database?ssl-mode=REQUIRED
set DB_USERNAME=your-username
set DB_PASSWORD=your-password

# Linux/Mac
export DB_URL="jdbc:mysql://your-host:port/database?ssl-mode=REQUIRED"
export DB_USERNAME="your-username"
export DB_PASSWORD="your-password"
```

**Option B: Configuration File**
```bash
# Copy the template and edit with your credentials
cp config.properties.template config.properties
# Edit config.properties with your actual database details
```

**For this project, use these credentials:**
- Host: `jdbc-sample-ebrahemmordyasr-c010.d.aivencloud.com:20820`
- Database: `defaultdb`
- Username: `avnadmin`
- Password: `AVNS_1MG4cATA4NCpwFDsMa9`

### 3. Build the Project
```bash
mvn clean compile
```

### 4. Run the Application
```bash
mvn exec:java -Dexec.mainClass="org.intern.Main"
```

## 📚 Key Concepts Covered

### 1. Database Configuration (`DatabaseConfig.java`)
- **Connection Management**: How to establish and close database connections
- **Driver Loading**: Loading MySQL JDBC driver
- **Connection String**: Understanding database URLs
- **Error Handling**: Proper exception handling for database operations

### 2. Model/Entity Class (`Student.java`)
- **POJO Pattern**: Plain Old Java Objects for data representation
- **Encapsulation**: Private fields with public getters/setters
- **Constructor Overloading**: Different ways to create objects
- **toString(), equals(), hashCode()**: Object methods

### 3. DAO Pattern (`StudentDAO.java` & `StudentDAOImpl.java`)
- **Interface Design**: Defining contracts for data operations
- **CRUD Operations**: Create, Read, Update, Delete
- **PreparedStatement**: Preventing SQL injection
- **ResultSet Handling**: Processing query results

### 4. Main Application (`Main.java`)
- **Complete Demo**: Step-by-step demonstration of all operations
- **Interactive Practice**: Hands-on menu for practicing JDBC operations
- **Error Handling**: Comprehensive exception handling
- **User Experience**: Clear output and user-friendly interface

## 🔧 Database Schema

The application uses a `students` table with the following structure:

```sql
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    age INT NOT NULL CHECK (age > 0),
    course VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 📖 JDBC Operations Explained

### Connection Management
```java
// Get connection
Connection connection = DatabaseConfig.getConnection();

// Always close connections
DatabaseConfig.closeConnection(connection);

// Or use try-with-resources (recommended)
try (Connection connection = DatabaseConfig.getConnection()) {
    // Database operations here
} // Connection automatically closed
```

### PreparedStatement (Safe from SQL Injection)
```java
String sql = "SELECT * FROM students WHERE name = ?";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, studentName);  // Parameter index starts from 1
    ResultSet rs = stmt.executeQuery();
    // Process results...
}
```

### ResultSet Processing
```java
while (resultSet.next()) {
    int id = resultSet.getInt("id");           // Get by column name
    String name = resultSet.getString("name");  // Or by index: getString(1)
    // Create objects from data...
}
```

## 🎮 Interactive Features

The application includes an interactive menu where you can:
1. **Add New Student** - Practice INSERT operations
2. **View All Students** - Practice SELECT operations
3. **Find Student by ID** - Practice parameterized queries
4. **Update Student** - Practice UPDATE operations
5. **Delete Student** - Practice DELETE operations
6. **Search by Name** - Practice LIKE queries with wildcards
7. **Search by Course** - Practice filtered SELECT operations

## 🛡️ Best Practices Demonstrated

1. **Use PreparedStatement** - Prevents SQL injection attacks
2. **Try-with-resources** - Automatic resource management
3. **Proper Exception Handling** - Catch and handle SQLExceptions
4. **DAO Pattern** - Separation of data access logic
5. **Connection Pooling Ready** - Easy to extend with connection pools
6. **Meaningful Error Messages** - Clear feedback for debugging

## 🔍 Common JDBC Patterns

### Create Operation
```java
String sql = "INSERT INTO students (name, email, age, course) VALUES (?, ?, ?, ?)";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, student.getName());
    stmt.setString(2, student.getEmail());
    stmt.setInt(3, student.getAge());
    stmt.setString(4, student.getCourse());
    
    int rowsAffected = stmt.executeUpdate();
    return rowsAffected > 0;
}
```

### Read Operation
```java
String sql = "SELECT id, name, email, age, course FROM students WHERE id = ?";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setInt(1, id);
    
    try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age"),
                rs.getString("course")
            );
        }
    }
}
```

## 🚨 Common Mistakes to Avoid

1. **Not closing resources** - Always close Connection, Statement, ResultSet
2. **SQL Injection** - Never concatenate user input directly into SQL
3. **Wrong parameter indices** - PreparedStatement parameters start from 1, not 0
4. **Ignoring exceptions** - Always handle SQLExceptions properly
5. **Not using transactions** - For multiple related operations, use transactions

## 🎯 Next Steps for Interns

After mastering this project, you can explore:
1. **Connection Pooling** - Using HikariCP or similar
2. **Transactions** - BEGIN, COMMIT, ROLLBACK operations
3. **Batch Operations** - Processing multiple records efficiently
4. **JPA/Hibernate** - Object-Relational Mapping frameworks
5. **Spring Data JPA** - Higher-level database abstractions
6. **Database Migration Tools** - Flyway or Liquibase
7. **Testing** - Unit testing database operations with H2 or TestContainers

## 🤝 Contributing

This is a learning project. Feel free to:
- Add more complex queries
- Implement additional features
- Improve error handling
- Add unit tests
- Create more comprehensive examples

## 📞 Support

If you encounter issues:
1. Check the console output for error messages
2. Verify database connection details
3. Ensure MySQL server is running
4. Check if the database is accessible from your network

## 📝 License

This project is for educational purposes. Use it freely for learning JDBC concepts.

---

**Happy Learning! 🎓**

Remember: The best way to learn JDBC is by writing code and experimenting with different operations. Don't be afraid to break things and learn from errors!
