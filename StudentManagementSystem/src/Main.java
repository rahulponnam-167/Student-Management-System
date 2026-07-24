import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Swing GUIs must be started on the Event Dispatch Thread (EDT),
        // not on the main thread — this is standard Swing practice.
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI();
            gui.setVisible(true);
        });
    }
}