package org.intern.dao;

import org.intern.model.Student;
import java.sql.SQLException;
import java.util.List;

/**
 * Student Data Access Object (DAO) Interface
 * 
 * DAO Pattern Explanation for Interns:
 * - DAO (Data Access Object) is a design pattern
 * - It separates database operations from business logic
 * - Provides a clean interface for database operations
 * - Makes code more maintainable and testable
 * 
 * This interface defines all the database operations we can perform on Student data:
 * - CREATE: Add new students to database
 * - READ: Retrieve students from database (single or multiple)
 * - UPDATE: Modify existing student data
 * - DELETE: Remove students from database
 * 
 * This is called CRUD operations (Create, Read, Update, Delete)
 */
public interface StudentDAO {
    
    /**
     * CREATE Operation - Add a new student to database
     * 
     * @param student Student object containing data to insert
     * @return true if student was added successfully, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean addStudent(Student student) throws SQLException;
    
    /**
     * READ Operation - Get a student by their ID
     * 
     * @param id Student's unique identifier
     * @return Student object if found, null if not found
     * @throws SQLException if database operation fails
     */
    Student getStudentById(int id) throws SQLException;
    
    /**
     * READ Operation - Get all students from database
     * 
     * @return List of all students in database
     * @throws SQLException if database operation fails
     */
    List<Student> getAllStudents() throws SQLException;
    
    /**
     * READ Operation - Search students by name
     * 
     * @param name Name to search for (can be partial name)
     * @return List of students matching the name
     * @throws SQLException if database operation fails
     */
    List<Student> getStudentsByName(String name) throws SQLException;
    
    /**
     * READ Operation - Get students by course
     * 
     * @param course Course name to search for
     * @return List of students in the specified course
     * @throws SQLException if database operation fails
     */
    List<Student> getStudentsByCourse(String course) throws SQLException;
    
    /**
     * UPDATE Operation - Modify existing student data
     * 
     * @param student Student object with updated data (must have valid ID)
     * @return true if student was updated successfully, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean updateStudent(Student student) throws SQLException;
    
    /**
     * DELETE Operation - Remove a student from database
     * 
     * @param id ID of student to delete
     * @return true if student was deleted successfully, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean deleteStudent(int id) throws SQLException;
    
    /**
     * Utility Operation - Count total number of students
     * 
     * @return Total number of students in database
     * @throws SQLException if database operation fails
     */
    int getStudentCount() throws SQLException;
    
    /**
     * Utility Operation - Check if a student exists
     * 
     * @param id Student ID to check
     * @return true if student exists, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean studentExists(int id) throws SQLException;
}
