public class Student {
    // Private fields = encapsulation (data hidden from outside classes)
    private String name;
    private int rollNumber;
    private double marks;

    // Constructor: creates a Student object with all values set at once
    public Student(String name, int rollNumber, double marks) {
        this.name = name;             // 'this' refers to the current object's field
        this.rollNumber = rollNumber; // distinguishes field from parameter (same name)
        this.marks = marks;
    }

    // Getter: allows other classes to READ the name
    public String getName() {
        return name;
    }

    // Setter: allows other classes to CHANGE the name safely
    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // Simple method to print this student's details in a readable format
    public void display() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Name: " + name);
        System.out.println("Marks: " + marks);
    }
}