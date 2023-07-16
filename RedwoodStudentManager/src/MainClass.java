import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import dao.StudentDAO;
import model.Student;
import model.Subject;

public class MainClass {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) {
        showMenu();
    }

    //Displays the menu options for the Student Management System and handles user input.
    private static void showMenu() {
        int choice;
        do {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Log student details");
            System.out.println("2. List all saved students");
            System.out.println("3. Find and modify a student");
            System.out.println("4. Identify the number of students enrolled in a year");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    logStudentDetails();
                    break;
                case 2:
                    listAllStudents();
                    break;
                case 3:
                    findAndModifyStudent();
                    break;
                case 4:
                    countStudentsByYear();
                    break;
                case 5:
                    exit();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    //Logs the student details by prompting the user for input and saving the student record
    private static void logStudentDetails() {
        System.out.println("\n--- Log Student Details ---");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();

        System.out.print("Enter date of birth (yyyy-MM-dd): ");
        String dateOfBirthString = scanner.nextLine();
        Date dateOfBirth = parseDate(dateOfBirthString);

        System.out.print("Enter father's name: ");
        String fatherName = scanner.nextLine();

        System.out.print("Enter mother's name: ");
        String motherName = scanner.nextLine();

        System.out.print("Enter enrolled year: ");
        int enrolledYear = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Student student = new Student(firstName, lastName, address, contactNumber,
                dateOfBirth, fatherName, motherName, enrolledYear);

        System.out.print("Enter the number of subjects: ");
        int subjectCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        for (int i = 0; i < subjectCount; i++) {
            System.out.print("Enter subject name: ");
            String subjectName = scanner.nextLine();

            System.out.print("Enter grade points: ");
            double gradePoints = scanner.nextDouble();
            scanner.nextLine(); // Consume newline character

            Subject subject = new Subject(subjectName, gradePoints);
            student.addSubject(subject);
        }

        student.calculateCgpa(); // Calculate the CGPA before saving

        studentDAO.logStudent(student);
        
        for (Subject subject : student.getSubjects()) {
            studentDAO.enrollSubject(student, subject);
        }

        System.out.println("Student details logged successfully.");
    }
    
    //Displays the list of all saved students
    private static void listAllStudents() {
        System.out.println("\n--- List of Saved Students ---");

        List<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : students) {
                System.out.println(studentToString(student));
                
            }
        }
    }


    //Finds a student by their ID and allows modification of their information
    private static void findAndModifyStudent() {
        System.out.println("\n--- Find and Modify a Student ---");

        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Student student = findStudentById(studentId);

        if (student != null) {
            System.out.println("Found student:");
            System.out.println(studentToString(student));

            System.out.println("Enter new details:");

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.nextLine();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            String dateOfBirthString = scanner.nextLine();
            Date dateOfBirth = parseDate(dateOfBirthString);

            System.out.print("Enter father's name: ");
            String fatherName = scanner.nextLine();

            System.out.print("Enter mother's name: ");
            String motherName = scanner.nextLine();

            System.out.print("Enter enrolled year: ");
            int enrolledYear = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setAddress(address);
            student.setContactNumber(contactNumber);
            student.setDateOfBirth(dateOfBirth);
            student.setFatherName(fatherName);
            student.setMotherName(motherName);
            student.setEnrolledYear(enrolledYear);

            studentDAO.modifyStudent(student);
            System.out.println("Student details modified successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    //Counts the number of students enrolled in a specific year and displays the count
    private static void countStudentsByYear() {
        System.out.println("\n--- Count Students by Year ---");

        System.out.print("Enter enrolled year: ");
        int enrolledYear = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        int count = studentDAO.getEnrollmentCount(enrolledYear);
        System.out.println("Number of students enrolled in year " + enrolledYear + ": " + count);
    }

    //Finds and returns a student based on the given student ID
    private static Student findStudentById(int studentId) {
        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return student;
            }
        }

        return null;
    }

    //Parses a string representation of a date in the format "yyyy-MM-dd" and returns a Date object
    private static Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //Creates a string representation of a Student object by concatenating its attributes and returns the built string.
    private static String studentToString(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("Student ID: ").append(student.getStudentId()).append("\n");
        sb.append("First Name: ").append(student.getFirstName()).append("\n");
        sb.append("Last Name: ").append(student.getLastName()).append("\n");
        sb.append("Address: ").append(student.getAddress()).append("\n");
        sb.append("Contact Number: ").append(student.getContactNumber()).append("\n");
        sb.append("Date of Birth: ").append(student.getDateOfBirth()).append("\n");
        sb.append("Father's Name: ").append(student.getFatherName()).append("\n");
        sb.append("Mother's Name: ").append(student.getMotherName()).append("\n");
        sb.append("Enrolled Year: ").append(student.getEnrolledYear()).append("\n");
        sb.append("CGPA: ").append(student.getCgpa()).append("\n");

        List<Subject> subjects = student.getSubjects();
        sb.append("Subjects: ").append("\n");
        for (Subject subject : subjects) {
            sb.append("   - ").append(subject.getSubjectName()).append(": ").append(subject.getGradePoints()).append("\n");
        }

        return sb.toString();
    }

    //Closes the connection and prints exit message
    private static void exit() {
        studentDAO.closeConnection();
        System.out.println("Exiting the program.");
    }
}
