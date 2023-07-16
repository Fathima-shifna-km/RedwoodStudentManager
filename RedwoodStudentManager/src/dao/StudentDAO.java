package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Student;
import model.Subject;

public class StudentDAO {
    private Connection connection;

    // Establishes the database connection
    public StudentDAO() {
        String url = "jdbc:mysql://localhost:3306/SchoolManagementDB";
        String username = "root";
        String password = "Fathima@2000";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Logs student details in the database
    public void logStudent(Student student) {
        String query = "INSERT INTO Students (first_name, last_name, address, contact_number, date_of_birth, father_name, mother_name, enrolled_year, cgpa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getAddress());
            statement.setString(4, student.getContactNumber());
            statement.setDate(5, new java.sql.Date(student.getDateOfBirth().getTime()));
            statement.setString(6, student.getFatherName());
            statement.setString(7, student.getMotherName());
            statement.setInt(8, student.getEnrolledYear());
            statement.setDouble(9, student.getCgpa()); // Set the CGPA value

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int studentId = generatedKeys.getInt(1);
                student.setStudentId(studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Retrieves all saved students from the database
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setStudentId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setAddress(resultSet.getString("address"));
                student.setContactNumber(resultSet.getString("contact_number"));
                student.setDateOfBirth(resultSet.getDate("date_of_birth"));
                student.setFatherName(resultSet.getString("father_name"));
                student.setMotherName(resultSet.getString("mother_name"));
                student.setEnrolledYear(resultSet.getInt("enrolled_year"));
                student.setCgpa(resultSet.getDouble("cgpa"));

                // Retrieve subjects for the student
                List<Subject> subjects = getSubjectsByStudentId(student.getStudentId());
                student.setSubjects(subjects);

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    private List<Subject> getSubjectsByStudentId(int studentId) {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM Subjects WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectName(resultSet.getString("subject_name"));
                    subject.setGradePoints(resultSet.getDouble("grade_points"));

                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }


    // Finds and modifies a student by student ID
    public void modifyStudent(Student student) {
        String query = "UPDATE Students SET first_name = ?, last_name = ?, address = ?, contact_number = ?, " +
                "date_of_birth = ?, father_name = ?, mother_name = ?, enrolled_year = ? WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getAddress());
            statement.setString(4, student.getContactNumber());
            statement.setDate(5, new java.sql.Date(student.getDateOfBirth().getTime()));
            statement.setString(6, student.getFatherName());
            statement.setString(7, student.getMotherName());
            statement.setInt(8, student.getEnrolledYear());
            statement.setInt(9, student.getStudentId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculates the CGPA for a given student
    public void calculateCgpa(Student student) {
        String query = "SELECT grade_points FROM Subjects WHERE student_id = ?";
        double totalGradePoints = 0.0;
        int subjectCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getStudentId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    double gradePoints = resultSet.getDouble("grade_points");
                    totalGradePoints += gradePoints;
                    subjectCount++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (subjectCount > 0) {
            double cgpa = totalGradePoints / subjectCount;
            student.setCgpa(cgpa);
        } else {
            student.setCgpa(0.0);
        }
    }

    // Enrolls a subject for a student
    public void enrollSubject(Student student, Subject subject) {
        String query = "INSERT INTO Subjects (student_id, subject_name, grade_points) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getStudentId());
            statement.setString(2, subject.getSubjectName());
            statement.setDouble(3, subject.getGradePoints());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Retrieves the count of students enrolled in a particular year
    public int getEnrollmentCount(int enrolledYear) {
        String query = "SELECT COUNT(*) FROM Students WHERE enrolled_year = ?";
        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, enrolledYear);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    // Closes the database connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
