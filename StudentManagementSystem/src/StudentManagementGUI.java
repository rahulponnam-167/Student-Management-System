import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentManagementGUI extends JFrame {

    // ---------- Color palette (single accent color = purple) ----------
    private static final Color BG_COLOR = new Color(0xF3, 0xF4, 0xF6);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color ACCENT = new Color(0x6C, 0x63, 0xFF);
    private static final Color ACCENT_HOVER = new Color(0x584FE0);
    private static final Color DANGER = new Color(0xEF, 0x44, 0x44);
    private static final Color DANGER_HOVER = new Color(0xDC, 0x26, 0x26);
    private static final Color NEUTRAL = new Color(0x9C, 0xA3, 0xAF);
    private static final Color NEUTRAL_HOVER = new Color(0x6B, 0x72, 0x80);
    private static final Color TEXT_DARK = new Color(0x1F, 0x29, 0x37);
    private static final Color TEXT_GRAY = new Color(0x6B, 0x72, 0x80);
    private static final Color BORDER_GRAY = new Color(0xD1, 0xD5, 0xDB);

    // ---------- Backend object (unchanged logic) ----------
    private final StudentManagement manager = new StudentManagement();

    // ---------- UI components referenced by multiple methods ----------
    private JTextField nameField, rollField, marksField;
    private DefaultTableModel tableModel;
    private JTable table;

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setSize(820, 640);
        setMinimumSize(new Dimension(650, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout());

        // Outer padding wrapper so the card floats with margin on all sides
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(25, 25, 25, 25));
        wrapper.add(buildCard(), BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    // ---------- Builds the rounded white card containing everything ----------
    private JComponent buildCard() {
        RoundedPanel card = new RoundedPanel(28);
        card.setBackground(CARD_COLOR);
        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        card.add(buildHeader(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(0, 20));
        middle.setOpaque(false);
        middle.add(buildFormPanel(), BorderLayout.NORTH);
        middle.add(buildTablePanel(), BorderLayout.CENTER);

        card.add(middle, BorderLayout.CENTER);
        return card;
    }

    // ---------- Title + subtitle ----------
    private JComponent buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage Student Records");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 4)));
        header.add(subtitle);
        header.add(Box.createRigidArea(new Dimension(0, 15)));
        return header;
    }

    // ---------- Name / Roll Number / Marks fields + buttons ----------
    private JComponent buildFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = createRoundedField();
        rollField = createRoundedField();
        marksField = createRoundedField();

        addFormRow(formPanel, gbc, 0, "Name", nameField);
        addFormRow(formPanel, gbc, 1, "Roll Number", rollField);
        addFormRow(formPanel, gbc, 2, "Marks", marksField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        buttonPanel.setOpaque(false);

        RoundedButton addBtn = new RoundedButton("Add", ACCENT, ACCENT_HOVER);
        RoundedButton updateBtn = new RoundedButton("Update", ACCENT, ACCENT_HOVER);
        RoundedButton deleteBtn = new RoundedButton("Delete", DANGER, DANGER_HOVER);
        RoundedButton clearBtn = new RoundedButton("Clear", NEUTRAL, NEUTRAL_HOVER);

        // ---- Connect buttons to existing StudentManagement CRUD methods ----
        addBtn.addActionListener(e -> handleAdd());
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());
        clearBtn.addActionListener(e -> clearFields());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(formPanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.SOUTH);
        return container;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_DARK);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JTextField createRoundedField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new RoundedBorder(14, BORDER_GRAY));
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }

    // ---------- Table showing all student records ----------
    private JComponent buildTablePanel() {
        JLabel label = new JLabel("Student Records");
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(TEXT_DARK);

        tableModel = new DefaultTableModel(new Object[]{"Roll No", "Name", "Marks"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // records are edited via the form, not directly in the table
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(0xE5, 0xE7, 0xEB));
        table.setSelectionBackground(new Color(0xED, 0xEB, 0xFF));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(TEXT_DARK);

        // Clicking a row loads its data into the form fields (for Update/Delete convenience)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                rollField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                marksField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new RoundedBorder(14, BORDER_GRAY));

        JPanel container = new JPanel(new BorderLayout(0, 8));
        container.setOpaque(false);
        container.add(label, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    // ---------- CRUD event handlers: call the unchanged backend ----------
    private void handleAdd() {
        try {
            String name = nameField.getText().trim();
            int roll = Integer.parseInt(rollField.getText().trim());
            double marks = Double.parseDouble(marksField.getText().trim());
            if (name.isEmpty()) {
                showError("Name cannot be empty.");
                return;
            }
            manager.addStudent(name, roll, marks); // existing backend method
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("Roll Number must be an integer and Marks must be a number.");
        }
    }

    private void handleUpdate() {
        try {
            int roll = Integer.parseInt(rollField.getText().trim());
            String name = nameField.getText().trim();
            double marks = Double.parseDouble(marksField.getText().trim());
            manager.updateStudent(roll, name, marks); // existing backend method
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("Roll Number must be an integer and Marks must be a number.");
        }
    }

    private void handleDelete() {
        try {
            int roll = Integer.parseInt(rollField.getText().trim());
            manager.deleteStudent(roll); // existing backend method
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("Roll Number must be an integer.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getAllStudents()) {
            tableModel.addRow(new Object[]{s.getRollNumber(), s.getName(), s.getMarks()});
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        marksField.setText("");
        table.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    // =====================================================================
    // Custom rounded components (Swing has no built-in rounded shapes)
    // =====================================================================

    // Rounded rectangle panel used for the main card
    static class RoundedPanel extends JPanel {
        private final int radius;
        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Rounded border used for text fields and the table's scroll pane
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }
    }

    // Rounded, color-filled button with a hover effect
    static class RoundedButton extends JButton {
        private final Color base;
        private final Color hover;
        RoundedButton(String text, Color base, Color hover) {
            super(text);
            this.base = base;
            this.hover = hover;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(100, 38));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(base);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hover);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(base);
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}