package model;

public class Subject {
    private String subjectName;
    private double gradePoints;

    public Subject() {
    }

    public Subject(String subjectName, double gradePoints) {
        this.subjectName = subjectName;
        this.gradePoints = gradePoints;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(double gradePoints) {
        this.gradePoints = gradePoints;
    }
}
