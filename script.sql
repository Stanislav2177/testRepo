-- Create the database

CREATE DATABASE IF NOT EXISTS coursera;
USE coursera;

-- Create the table 'courses'
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    instructor_id INT NOT NULL,
    total_time TINYINT NOT NULL,
    credit TINYINT NOT NULL,
    time_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create the table 'instructors'
CREATE TABLE instructors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    time_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create the table 'students'
CREATE TABLE students (
    pin CHAR(10) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    time_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create the table 'students_courses_xref'
CREATE TABLE students_courses_xref (
    student_pin CHAR(10) NOT NULL,
    course_id INT NOT NULL,
    completion_date DATE,
    PRIMARY KEY (student_pin, course_id),
    FOREIGN KEY (student_pin) REFERENCES students (pin),
    FOREIGN KEY (course_id) REFERENCES courses (id)
);

-- Insert data into 'courses' table
INSERT INTO courses (name, instructor_id, total_time, credit, time_created)
VALUES 
    ('Analysis', 1, 20, 10, NOW()),
    ('Linear Algebra', 1, 30, 15, NOW()),
    ('Statistics', 2, 30, 15, NOW()),
    ('Geometry', 3, 35, 20, NOW());

-- Insert data into 'instructors' table
INSERT INTO instructors (first_name, last_name, time_created)
VALUES 
    ('Neno', 'Dimitrov', NOW()),
    ('Petko', 'Valchev', NOW()),
    ('Petar', 'Penchev', NOW());

-- Insert data into 'students' table
INSERT INTO students (pin, first_name, last_name, time_created)
VALUES 
    ('9412011005', 'Krasimir', 'Petrov', NOW()),
    ('9501011014', 'Elena', 'Foteva', NOW()),
    ('9507141009', 'Ivan', 'Ivanov', NOW());

-- Insert data into 'students_courses_xref' table
INSERT INTO students_courses_xref (student_pin, course_id, completion_date)
VALUES 
    ('9412011005', 1, '2019-07-16'),
    ('9412011005', 2, '2019-08-20'),
    ('9501011014', 1, '2019-07-16'),
    ('9501011014', 2, '2019-08-01'),
    ('9501011014', 3, '2019-10-01'),
    ('9501011014', 4, '2019-12-05'),
    ('9507141009', 4, '2019-08-20');

SELECT * FROM courses;