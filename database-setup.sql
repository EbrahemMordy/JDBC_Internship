-- ============================================================================
-- JDBC Learning Project - Database Setup Script
-- ============================================================================
-- This SQL script shows the database structure used in the JDBC learning project.
-- You can run this manually if needed, but the Java application will create
-- the table automatically when you run it.

-- Create the students table
-- This table will store information about students for our JDBC learning demo
CREATE TABLE IF NOT EXISTS students (
    -- Primary key with auto-increment
    -- This means the database will automatically assign unique IDs
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    -- Student's full name (required, max 100 characters)
    name VARCHAR(100) NOT NULL,
    
    -- Student's email address (required, unique, max 150 characters)
    -- UNIQUE constraint ensures no two students can have the same email
    email VARCHAR(150) UNIQUE NOT NULL,
    
    -- Student's age (required, must be positive number)
    -- CHECK constraint ensures age is always greater than 0
    age INT NOT NULL CHECK (age > 0),
    
    -- Student's course/major (required, max 100 characters)
    course VARCHAR(100) NOT NULL,
    
    -- Timestamp when the record was created (automatically set)
    -- DEFAULT CURRENT_TIMESTAMP means it will be set to current date/time automatically
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- Sample Data (Optional)
-- ============================================================================
-- The Java application will insert sample data automatically, but here are
-- some examples of INSERT statements for learning purposes:

-- Insert sample students (these will be done by the Java application)
/*
INSERT INTO students (name, email, age, course) VALUES 
    ('John Doe', 'john.doe@email.com', 20, 'Computer Science'),
    ('Jane Smith', 'jane.smith@email.com', 22, 'Mathematics'),
    ('Bob Johnson', 'bob.johnson@email.com', 21, 'Physics'),
    ('Alice Brown', 'alice.brown@email.com', 19, 'Computer Science'),
    ('Charlie Davis', 'charlie.davis@email.com', 23, 'Chemistry');
*/

-- ============================================================================
-- Useful SQL Queries for Learning
-- ============================================================================

-- 1. View all students
-- SELECT * FROM students;

-- 2. Find student by ID
-- SELECT * FROM students WHERE id = 1;

-- 3. Search students by name (partial match)
-- SELECT * FROM students WHERE name LIKE '%John%';

-- 4. Get students by course
-- SELECT * FROM students WHERE course = 'Computer Science';

-- 5. Count total students
-- SELECT COUNT(*) as total_students FROM students;

-- 6. Get students ordered by name
-- SELECT * FROM students ORDER BY name ASC;

-- 7. Get students in a specific age range
-- SELECT * FROM students WHERE age BETWEEN 20 AND 22;

-- 8. Update a student's information
-- UPDATE students SET age = 21, email = 'john.doe.new@email.com' WHERE id = 1;

-- 9. Delete a student
-- DELETE FROM students WHERE id = 1;

-- 10. Get course statistics
-- SELECT course, COUNT(*) as student_count, AVG(age) as average_age 
-- FROM students 
-- GROUP BY course 
-- ORDER BY student_count DESC;

-- ============================================================================
-- Table Information Queries
-- ============================================================================

-- Show table structure
-- DESCRIBE students;

-- Show table creation statement
-- SHOW CREATE TABLE students;

-- Show indexes on the table
-- SHOW INDEX FROM students;

-- ============================================================================
-- Notes for Interns:
-- ============================================================================
-- 
-- 1. PRIMARY KEY: Uniquely identifies each row in the table
-- 2. AUTO_INCREMENT: Database automatically generates sequential numbers
-- 3. NOT NULL: Field must have a value, cannot be empty
-- 4. UNIQUE: No two rows can have the same value for this field
-- 5. CHECK: Validates that data meets certain conditions
-- 6. TIMESTAMP: Stores date and time information
-- 7. VARCHAR(n): Variable-length string with maximum n characters
-- 8. INT: Integer number (whole numbers)
--
-- SQL Command Types:
-- - DDL (Data Definition Language): CREATE, ALTER, DROP - define structure
-- - DML (Data Manipulation Language): INSERT, UPDATE, DELETE - modify data
-- - DQL (Data Query Language): SELECT - retrieve data
-- - DCL (Data Control Language): GRANT, REVOKE - control access
--
-- JDBC corresponds to these SQL operations:
-- - executeUpdate() for DDL and DML commands (returns number of affected rows)
-- - executeQuery() for DQL commands (returns ResultSet)
--
-- ============================================================================
