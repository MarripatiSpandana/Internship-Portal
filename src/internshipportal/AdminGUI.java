package internshipportal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminGUI extends JFrame {
    private JTextField txtTitle, txtCompany, txtLocation, txtStipend;
    private JButton btnAdd;

    public AdminGUI() {
        setTitle("Admin - Add Internship");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Add New Internship");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(130, 20, 300, 40);
        mainPanel.add(lblTitle);

        // Title
        JLabel lbl1 = new JLabel("Internship Title:");
        lbl1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl1.setBounds(50, 80, 150, 25);
        mainPanel.add(lbl1);

        txtTitle = new JTextField();
        txtTitle.setBounds(50, 105, 400, 35);
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(txtTitle);

        // Company
        JLabel lbl2 = new JLabel("Company Name:");
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setBounds(50, 150, 150, 25);
        mainPanel.add(lbl2);

        txtCompany = new JTextField();
        txtCompany.setBounds(50, 175, 400, 35);
        txtCompany.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(txtCompany);

        // Location
        JLabel lbl3 = new JLabel("Location:");
        lbl3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl3.setBounds(50, 220, 150, 25);
        mainPanel.add(lbl3);

        txtLocation = new JTextField();
        txtLocation.setBounds(50, 245, 400, 35);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(txtLocation);

        // Stipend
        JLabel lbl4 = new JLabel("Stipend:");
        lbl4.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl4.setBounds(50, 290, 150, 25);
        mainPanel.add(lbl4);

        txtStipend = new JTextField();
        txtStipend.setBounds(50, 315, 400, 35);
        txtStipend.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(txtStipend);

        // Add Button
        btnAdd = new JButton("Add Internship");
        btnAdd.setBounds(150, 370, 200, 40);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.setBackground(new Color(34, 197, 94));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addInternship());
        mainPanel.add(btnAdd);

        add(mainPanel);
    }

    private void addInternship() {
        String title = txtTitle.getText().trim();
        String company = txtCompany.getText().trim();
        String location = txtLocation.getText().trim();
        String stipend = txtStipend.getText().trim();

        if (title.isEmpty() || company.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Company are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Internships (title, company, location, stipend) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, company);
            pstmt.setString(3, location);
            pstmt.setString(4, stipend);
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Internship added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            txtTitle.setText("");
            txtCompany.setText("");
            txtLocation.setText("");
            txtStipend.setText("");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding internship: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true));
    }
}