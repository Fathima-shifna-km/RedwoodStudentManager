package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String address;
    private String contactNumber;
    private Date dateOfBirth;
    private String fatherName;
    private String motherName;
    private int enrolledYear;
    private double cgpa;
    private List<Subject> subjects; // List of subjects for the student

    public Student() {
        subjects = new ArrayList<>();
    }

    public Student(String firstName, String lastName, String address, String contactNumber,
                   Date dateOfBirth, String fatherName, String motherName, int enrolledYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dateOfBirth = dateOfBirth;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.enrolledYear = enrolledYear;
        subjects = new ArrayList<>();
    }

    // Getters and setters for all attributes

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public int getEnrolledYear() {
        return enrolledYear;
    }

    public void setEnrolledYear(int enrolledYear) {
        this.enrolledYear = enrolledYear;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    //Calculates the CGPA for the student based on the enrolled subjects.
    public void calculateCgpa() {
        if (subjects.isEmpty()) {
            cgpa = 0.0;
            return;
        }

        double totalGradePoints = 0.0;
        int subjectCount = 0;

        for (Subject subject : subjects) {
            totalGradePoints += subject.getGradePoints();
            subjectCount++;
        }

        cgpa = totalGradePoints / subjectCount;
    }
}
