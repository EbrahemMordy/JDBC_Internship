package org.intern;

import org.intern.config.DatabaseConfig;
import org.intern.dao.StudentDAO;
import org.intern.dao.StudentDAOImpl;
import org.intern.model.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

/**
 * JDBC Learning Demo - Main Application
 * 
 * This is a comprehensive demonstration of JDBC operations for interns.
 * It covers all basic database operations (CRUD) with detailed explanations.
 * 
 * Learning Objectives:
 * 1. Understand how to connect to a MySQL database
 * 2. Learn to create database tables
 * 3. Perform CRUD operations (Create, Read, Update, Delete)
 * 4. Handle SQL exceptions properly
 * 5. Use PreparedStatement for safe database operations
 * 6. Understand DAO pattern for organizing database code
 */
public class Main {
    
    // DAO instance for database operations
    private static final StudentDAO studentDAO = new StudentDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("         JDBC LEARNING DEMO FOR INTERNS");
        System.out.println("============================================================");
        System.out.println();
        
        try {
            // Step 1: Test database connection
            System.out.println("Step 1: Testing Database Connection");
            System.out.println("----------------------------------------");
            testDatabaseConnection();
            System.out.println();
            
            // Step 2: Create database table
            System.out.println("Step 2: Creating Database Table");
            System.out.println("----------------------------------------");
            createStudentsTable();
            System.out.println();
            
            // Step 3: Demonstrate CRUD operations
            System.out.println("Step 3: Demonstrating CRUD Operations");
            System.out.println("----------------------------------------");
            demonstrateCRUDOperations();
            System.out.println();
            
            // Step 4: Interactive menu for hands-on practice
            System.out.println("Step 4: Interactive Practice Menu");
            System.out.println("----------------------------------------");
            runInteractiveMenu();
            
        } catch (SQLException e) {
            System.err.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        
        System.out.println("\nThank you for learning JDBC!");
    }
    
    /**
     * Test Database Connection
     * 
     * This method demonstrates how to test if database connection is working.
     * It's a good practice to test connectivity before performing operations.
     */
    private static void testDatabaseConnection() {
        System.out.println("Testing connection to MySQL database...");
        
        if (DatabaseConfig.testConnection()) {
            System.out.println("✅ Database connection successful!");
            System.out.println("You can now perform database operations.");
        } else {
            System.out.println("❌ Database connection failed!");
            System.out.println("Please check your database configuration.");
            return;
        }
    }
    
    /**
     * Create Students Table
     * 
     * This method demonstrates DDL (Data Definition Language) operations.
     * It creates the 'students' table if it doesn't exist.
     * 
     * Key Learning Points:
     * - DDL operations use executeUpdate() method
     * - CREATE TABLE syntax
     * - Primary key and auto-increment
     * - Different data types (INT, VARCHAR, etc.)
     */
    private static void createStudentsTable() throws SQLException {
        System.out.println("Creating 'students' table if it doesn't exist...");
        
        // SQL to create students table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(150) UNIQUE NOT NULL," +
                "age INT NOT NULL CHECK (age > 0)," +
                "course VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Execute DDL statement
            statement.executeUpdate(createTableSQL);
            System.out.println("✅ Table 'students' is ready!");
            
            // Show table structure (optional)
            System.out.println("\nTable structure:");
            System.out.println("- id: Auto-increment primary key");
            System.out.println("- name: Student's full name (max 100 chars)");
            System.out.println("- email: Unique email address (max 150 chars)");
            System.out.println("- age: Student's age (must be positive)");
            System.out.println("- course: Student's course/major (max 100 chars)");
            System.out.println("- created_at: Timestamp when record was created");
            
        } catch (SQLException e) {
            System.err.println("❌ Error creating table: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrate CRUD Operations
     * 
     * This method shows all basic database operations with sample data.
     * Each operation is explained step by step.
     */
    private static void demonstrateCRUDOperations() throws SQLException {
        System.out.println("Demonstrating CRUD operations with sample data...\n");
        
        // CREATE - Insert sample students
        System.out.println("🔹 CREATE Operations (Adding Students)");
        insertSampleStudents();
        System.out.println();
        
        // READ - Retrieve students
        System.out.println("🔹 READ Operations (Retrieving Students)");
        demonstrateReadOperations();
        System.out.println();
        
        // UPDATE - Modify student data
        System.out.println("🔹 UPDATE Operations (Modifying Students)");
        demonstrateUpdateOperations();
        System.out.println();
        
        // DELETE - Remove students
        System.out.println("🔹 DELETE Operations (Removing Students)");
        demonstrateDeleteOperations();
        System.out.println();
    }
    
    /**
     * Insert Sample Students
     * 
     * Demonstrates INSERT operations using DAO methods
     */
    private static void insertSampleStudents() throws SQLException {
        System.out.println("Adding sample students to database...");
        
        // Create sample student objects
        Student[] sampleStudents = {
            new Student("John Doe", "john.doe@email.com", 20, "Computer Science"),
            new Student("Jane Smith", "jane.smith@email.com", 22, "Mathematics"),
            new Student("Bob Johnson", "bob.johnson@email.com", 21, "Physics"),
            new Student("Alice Brown", "alice.brown@email.com", 19, "Computer Science"),
            new Student("Charlie Davis", "charlie.davis@email.com", 23, "Chemistry")
        };
        
        // Insert each student
        for (Student student : sampleStudents) {
            try {
                boolean success = studentDAO.addStudent(student);
                if (success) {
                    System.out.println("  ✅ Added: " + student.getName());
                } else {
                    System.out.println("  ❌ Failed to add: " + student.getName());
                }
            } catch (SQLException e) {
                // Handle duplicate email error gracefully
                if (e.getMessage().contains("Duplicate entry")) {
                    System.out.println("  ⚠️ Student already exists: " + student.getName());
                } else {
                    System.out.println("  ❌ Error adding " + student.getName() + ": " + e.getMessage());
                }
            }
        }
        
        System.out.println("Sample data insertion completed!");
    }
    
    /**
     * Demonstrate Read Operations
     * 
     * Shows different ways to retrieve data from database
     */
    private static void demonstrateReadOperations() throws SQLException {
        // Get all students
        System.out.println("1. Getting all students:");
        List<Student> allStudents = studentDAO.getAllStudents();
        displayStudentList(allStudents);
        System.out.println();
        
        // Get student by ID
        System.out.println("2. Getting student by ID (ID = 1):");
        Student student = studentDAO.getStudentById(1);
        if (student != null) {
            System.out.println("  " + student);
        } else {
            System.out.println("  No student found with ID 1");
        }
        System.out.println();
        
        // Search students by name
        System.out.println("3. Searching students by name (containing 'John'):");
        List<Student> johnStudents = studentDAO.getStudentsByName("John");
        displayStudentList(johnStudents);
        System.out.println();
        
        // Get students by course
        System.out.println("4. Getting students by course (Computer Science):");
        List<Student> csStudents = studentDAO.getStudentsByCourse("Computer Science");
        displayStudentList(csStudents);
        System.out.println();
        
        // Get student count
        System.out.println("5. Total number of students:");
        int count = studentDAO.getStudentCount();
        System.out.println("  Total students: " + count);
    }
    
    /**
     * Demonstrate Update Operations
     * 
     * Shows how to modify existing records
     */
    private static void demonstrateUpdateOperations() throws SQLException {
        System.out.println("Updating student information...");
        
        // First, get a student to update
        Student student = studentDAO.getStudentById(1);
        if (student != null) {
            System.out.println("Original data: " + student);
            
            // Modify student data
            student.setAge(student.getAge() + 1); // Increase age by 1
            student.setEmail("john.doe.updated@email.com"); // Update email
            
            // Update in database
            boolean success = studentDAO.updateStudent(student);
            if (success) {
                System.out.println("✅ Student updated successfully!");
                
                // Retrieve updated student to confirm changes
                Student updatedStudent = studentDAO.getStudentById(1);
                System.out.println("Updated data: " + updatedStudent);
            } else {
                System.out.println("❌ Failed to update student");
            }
        } else {
            System.out.println("No student found to update");
        }
    }
    
    /**
     * Demonstrate Delete Operations
     * 
     * Shows how to remove records from database
     */
    private static void demonstrateDeleteOperations() throws SQLException {
        System.out.println("Demonstrating delete operation...");
        
        // Get current count
        int beforeCount = studentDAO.getStudentCount();
        System.out.println("Students before deletion: " + beforeCount);
        
        // Delete a student (let's delete the last one)
        List<Student> allStudents = studentDAO.getAllStudents();
        if (!allStudents.isEmpty()) {
            Student lastStudent = allStudents.get(allStudents.size() - 1);
            System.out.println("Deleting student: " + lastStudent.getName());
            
            boolean success = studentDAO.deleteStudent(lastStudent.getId());
            if (success) {
                System.out.println("✅ Student deleted successfully!");
                
                // Verify deletion
                int afterCount = studentDAO.getStudentCount();
                System.out.println("Students after deletion: " + afterCount);
            } else {
                System.out.println("❌ Failed to delete student");
            }
        } else {
            System.out.println("No students to delete");
        }
    }
    
    /**
     * Interactive Menu for Hands-on Practice
     * 
     * Allows interns to practice JDBC operations interactively
     */
    private static void runInteractiveMenu() throws SQLException {
        while (true) {
            displayMenu();
            System.out.print("Enter your choice (1-8): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        handleAddStudent();
                        break;
                    case 2:
                        handleViewAllStudents();
                        break;
                    case 3:
                        handleFindStudent();
                        break;
                    case 4:
                        handleUpdateStudent();
                        break;
                    case 5:
                        handleDeleteStudent();
                        break;
                    case 6:
                        handleSearchByName();
                        break;
                    case 7:
                        handleSearchByCourse();
                        break;
                    case 8:
                        System.out.println("Exiting interactive menu...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter 1-8.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Display Interactive Menu Options
     */
    private static void displayMenu() {
        System.out.println("\n==================================================");
        System.out.println("           JDBC PRACTICE MENU");
        System.out.println("==================================================");
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Find Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Search Students by Name");
        System.out.println("7. Search Students by Course");
        System.out.println("8. Exit");
        System.out.println("==================================================");
    }
    
    // Interactive menu handlers
    private static void handleAddStudent() throws SQLException {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter course: ");
        String course = scanner.nextLine();
        
        Student student = new Student(name, email, age, course);
        boolean success = studentDAO.addStudent(student);
        
        if (success) {
            System.out.println("✅ Student added successfully!");
        } else {
            System.out.println("❌ Failed to add student!");
        }
    }
    
    private static void handleViewAllStudents() throws SQLException {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentDAO.getAllStudents();
        displayStudentList(students);
    }
    
    private static void handleFindStudent() throws SQLException {
        System.out.println("\n--- Find Student by ID ---");
        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Student student = studentDAO.getStudentById(id);
        if (student != null) {
            System.out.println("Student found: " + student);
        } else {
            System.out.println("No student found with ID: " + id);
        }
    }
    
    private static void handleUpdateStudent() throws SQLException {
        System.out.println("\n--- Update Student ---");
        System.out.print("Enter student ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }
        
        System.out.println("Current data: " + student);
        System.out.println("Enter new values (press Enter to keep current value):");
        
        System.out.print("Name [" + student.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) student.setName(name);
        
        System.out.print("Email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) student.setEmail(email);
        
        System.out.print("Age [" + student.getAge() + "]: ");
        String ageStr = scanner.nextLine();
        if (!ageStr.trim().isEmpty()) student.setAge(Integer.parseInt(ageStr));
        
        System.out.print("Course [" + student.getCourse() + "]: ");
        String course = scanner.nextLine();
        if (!course.trim().isEmpty()) student.setCourse(course);
        
        boolean success = studentDAO.updateStudent(student);
        if (success) {
            System.out.println("✅ Student updated successfully!");
        } else {
            System.out.println("❌ Failed to update student!");
        }
    }
    
    private static void handleDeleteStudent() throws SQLException {
        System.out.println("\n--- Delete Student ---");
        System.out.print("Enter student ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }
        
        System.out.println("Student to delete: " + student);
        System.out.print("Are you sure? (y/N): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.toLowerCase().startsWith("y")) {
            boolean success = studentDAO.deleteStudent(id);
            if (success) {
                System.out.println("✅ Student deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete student!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    private static void handleSearchByName() throws SQLException {
        System.out.println("\n--- Search Students by Name ---");
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        
        List<Student> students = studentDAO.getStudentsByName(name);
        displayStudentList(students);
    }
    
    private static void handleSearchByCourse() throws SQLException {
        System.out.println("\n--- Search Students by Course ---");
        System.out.print("Enter course name: ");
        String course = scanner.nextLine();
        
        List<Student> students = studentDAO.getStudentsByCourse(course);
        displayStudentList(students);
    }
    
    /**
     * Helper method to display list of students in a formatted way
     */
    private static void displayStudentList(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("  No students found.");
        } else {
            System.out.println("  Found " + students.size() + " student(s):");
            System.out.println("  --------------------------------------------------------------------------------");
            System.out.printf("  %-5s %-20s %-25s %-5s %-15s%n", "ID", "Name", "Email", "Age", "Course");
            System.out.println("  --------------------------------------------------------------------------------");
            
            for (Student student : students) {
                System.out.printf("  %-5d %-20s %-25s %-5d %-15s%n",
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getAge(),
                    student.getCourse());
            }
        }
    }
}
