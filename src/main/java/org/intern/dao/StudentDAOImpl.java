package org.intern.dao;

import org.intern.config.DatabaseConfig;
import org.intern.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student DAO Implementation
 * 
 * This class implements the StudentDAO interface and contains actual JDBC code
 * for database operations. This is where interns will learn JDBC concepts:
 * 
 * Key JDBC Components:
 * 1. Connection - represents database connection
 * 2. Statement/PreparedStatement - for executing SQL queries
 * 3. ResultSet - for handling query results
 * 4. SQLException - for handling database errors
 * 
 * Important JDBC Best Practices:
 * - Always use PreparedStatement to prevent SQL injection
 * - Always close resources (Connection, Statement, ResultSet)
 * - Use try-with-resources for automatic resource management
 * - Handle SQL exceptions properly
 */
public class StudentDAOImpl implements StudentDAO {
    
    // SQL queries as constants - easier to maintain and read
    
    // CREATE operation - Insert new student
    private static final String INSERT_STUDENT = 
        "INSERT INTO students (name, email, age, course) VALUES (?, ?, ?, ?)";
    
    // READ operations - Select queries
    private static final String SELECT_STUDENT_BY_ID = 
        "SELECT id, name, email, age, course FROM students WHERE id = ?";
    
    private static final String SELECT_ALL_STUDENTS = 
        "SELECT id, name, email, age, course FROM students ORDER BY name";
    
    private static final String SELECT_STUDENTS_BY_NAME = 
        "SELECT id, name, email, age, course FROM students WHERE name LIKE ? ORDER BY name";
    
    private static final String SELECT_STUDENTS_BY_COURSE = 
        "SELECT id, name, email, age, course FROM students WHERE course = ? ORDER BY name";
    
    // UPDATE operation - Modify existing student
    private static final String UPDATE_STUDENT = 
        "UPDATE students SET name = ?, email = ?, age = ?, course = ? WHERE id = ?";
    
    // DELETE operation - Remove student
    private static final String DELETE_STUDENT = 
        "DELETE FROM students WHERE id = ?";
    
    // Utility queries
    private static final String COUNT_STUDENTS = 
        "SELECT COUNT(*) FROM students";
    
    private static final String CHECK_STUDENT_EXISTS = 
        "SELECT 1 FROM students WHERE id = ?";
    
    /**
     * CREATE Operation - Add new student to database
     * 
     * JDBC Steps Explained:
     * 1. Get database connection
     * 2. Create PreparedStatement with INSERT SQL
     * 3. Set parameter values (?, ?, ?, ?)
     * 4. Execute the statement
     * 5. Check if insertion was successful
     * 6. Close resources
     */
    @Override
    public boolean addStudent(Student student) throws SQLException {
        // Try-with-resources ensures automatic closing of resources
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT)) {
            
            System.out.println("Adding new student: " + student.getName());
            
            // Set parameter values for the SQL query
            // Parameter index starts from 1, not 0!
            statement.setString(1, student.getName());    // First ? in SQL
            statement.setString(2, student.getEmail());   // Second ? in SQL
            statement.setInt(3, student.getAge());        // Third ? in SQL
            statement.setString(4, student.getCourse());  // Fourth ? in SQL
            
            // Execute the INSERT statement
            // executeUpdate() returns number of rows affected
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Student added successfully!");
                return true;
            } else {
                System.out.println("Failed to add student.");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            throw e; // Re-throw to let caller handle
        }
    }
    
    /**
     * READ Operation - Get student by ID
     * 
     * JDBC Steps Explained:
     * 1. Get database connection
     * 2. Create PreparedStatement with SELECT SQL
     * 3. Set parameter value (student ID)
     * 4. Execute query and get ResultSet
     * 5. Extract data from ResultSet and create Student object
     * 6. Close resources
     */
    @Override
    public Student getStudentById(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_BY_ID)) {
            
            System.out.println("Searching for student with ID: " + id);
            
            // Set the parameter value
            statement.setInt(1, id);
            
            // Execute query and get results
            // executeQuery() returns ResultSet for SELECT statements
            try (ResultSet resultSet = statement.executeQuery()) {
                
                // Check if any row was returned
                if (resultSet.next()) {
                    // Extract data from current row and create Student object
                    Student student = extractStudentFromResultSet(resultSet);
                    System.out.println("Student found: " + student.getName());
                    return student;
                } else {
                    System.out.println("No student found with ID: " + id);
                    return null;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving student: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * READ Operation - Get all students
     * 
     * This method demonstrates how to handle multiple rows from ResultSet
     */
    @Override
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STUDENTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            System.out.println("Retrieving all students from database...");
            
            // Loop through all rows in ResultSet
            while (resultSet.next()) {
                Student student = extractStudentFromResultSet(resultSet);
                students.add(student);
            }
            
            System.out.println("Retrieved " + students.size() + " students.");
            return students;
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all students: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * READ Operation - Search students by name
     * 
     * This demonstrates LIKE operator for partial matching
     */
    @Override
    public List<Student> getStudentsByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_BY_NAME)) {
            
            System.out.println("Searching students by name: " + name);
            
            // Use % wildcards for partial matching
            statement.setString(1, "%" + name + "%");
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = extractStudentFromResultSet(resultSet);
                    students.add(student);
                }
            }
            
            System.out.println("Found " + students.size() + " students matching name: " + name);
            return students;
            
        } catch (SQLException e) {
            System.err.println("Error searching students by name: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * READ Operation - Get students by course
     */
    @Override
    public List<Student> getStudentsByCourse(String course) throws SQLException {
        List<Student> students = new ArrayList<>();
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_BY_COURSE)) {
            
            System.out.println("Retrieving students in course: " + course);
            
            statement.setString(1, course);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = extractStudentFromResultSet(resultSet);
                    students.add(student);
                }
            }
            
            System.out.println("Found " + students.size() + " students in course: " + course);
            return students;
            
        } catch (SQLException e) {
            System.err.println("Error retrieving students by course: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * UPDATE Operation - Modify existing student
     * 
     * This demonstrates how to update existing records
     */
    @Override
    public boolean updateStudent(Student student) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT)) {
            
            System.out.println("Updating student with ID: " + student.getId());
            
            // Set parameter values for UPDATE
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setInt(3, student.getAge());
            statement.setString(4, student.getCourse());
            statement.setInt(5, student.getId()); // WHERE clause parameter
            
            // Execute update
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully!");
                return true;
            } else {
                System.out.println("No student found with ID: " + student.getId());
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * DELETE Operation - Remove student from database
     * 
     * This demonstrates how to delete records
     */
    @Override
    public boolean deleteStudent(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT)) {
            
            System.out.println("Deleting student with ID: " + id);
            
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
                return true;
            } else {
                System.out.println("No student found with ID: " + id);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Utility Operation - Count total students
     * 
     * This demonstrates aggregate functions (COUNT)
     */
    @Override
    public int getStudentCount() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_STUDENTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Get first column value
                System.out.println("Total students in database: " + count);
                return count;
            }
            return 0;
            
        } catch (SQLException e) {
            System.err.println("Error counting students: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Utility Operation - Check if student exists
     */
    @Override
    public boolean studentExists(int id) throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_STUDENT_EXISTS)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean exists = resultSet.next();
                System.out.println("Student with ID " + id + (exists ? " exists" : " does not exist"));
                return exists;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking student existence: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Helper method to extract Student object from ResultSet
     * 
     * This method shows how to read different data types from ResultSet:
     * - getInt() for integer values
     * - getString() for string values
     * 
     * Column names or indices can be used to access data
     */
    private Student extractStudentFromResultSet(ResultSet resultSet) throws SQLException {
        // Extract data using column names (recommended approach)
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        int age = resultSet.getInt("age");
        String course = resultSet.getString("course");
        
        // Alternative: Extract data using column indices (1-based)
        // int id = resultSet.getInt(1);
        // String name = resultSet.getString(2);
        // String email = resultSet.getString(3);
        // int age = resultSet.getInt(4);
        // String course = resultSet.getString(5);
        
        return new Student(id, name, email, age, course);
    }
}
