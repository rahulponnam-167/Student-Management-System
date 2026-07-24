import java.util.ArrayList;

public class StudentManagement {
    private ArrayList<Student> students = new ArrayList<>();

    // ---------- ADD ----------
    public void addStudent(String name, int rollNumber, double marks) {
        Student student = new Student(name, rollNumber, marks);
        students.add(student);
        System.out.println("Student added successfully.");
    }

    // ---------- VIEW ----------
    public void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        for (Student s : students) {
            s.display();
            System.out.println("-----------------------------");
        }
    }

    // ---------- UPDATE ----------
    public void updateStudent(int rollNumber, String name, double marks) {
        for (Student s : students) {
            if (s.getRollNumber() == rollNumber) {
                s.setName(name);
                s.setMarks(marks);
                System.out.println("Student updated successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // ---------- DELETE ----------
    public void deleteStudent(int rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber() == rollNumber) {
                students.remove(s);
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // ---------- Needed by the GUI to populate the JTable ----------
    public ArrayList<Student> getAllStudents() {
        return students;
    }
}