package internshipportal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentPortalGUI extends JFrame {
    private Student currentStudent;
    private JTable internshipTable;
    private DefaultTableModel tableModel;

    public StudentPortalGUI(Student student) {
        this.currentStudent = student;
        
        setTitle("Student Portal - Internship Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel - BLUE COLOR (different from register)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(63, 81, 181));
        headerPanel.setPreferredSize(new Dimension(900, 70));
        headerPanel.setLayout(null);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentStudent.getName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(30, 20, 400, 30);
        headerPanel.add(welcomeLabel);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(750, 20, 120, 35);
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final StudentPortalGUI currentFrame = this;
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginGUI loginWindow = new LoginGUI();
                loginWindow.setVisible(true);
                currentFrame.dispose();
            }
        });
        headerPanel.add(logoutBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 240, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Available Internships");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 30, 30));
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Title", "Company", "Location", "Stipend"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        internshipTable = new JTable(tableModel);
        internshipTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        internshipTable.setRowHeight(30);
        internshipTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        internshipTable.getTableHeader().setBackground(new Color(224, 224, 224));
        internshipTable.setSelectionBackground(new Color(159, 168, 218));
        internshipTable.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(internshipTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton applyBtn = new JButton("Apply for Internship");
        applyBtn.setPreferredSize(new Dimension(180, 40));
        applyBtn.setBackground(new Color(76, 175, 80));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        applyBtn.setFocusPainted(false);
        applyBtn.setBorderPainted(false);
        applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyForInternship();
            }
        });
        buttonPanel.add(applyBtn);
        
        JButton myApplicationsBtn = new JButton("My Applications");
        myApplicationsBtn.setPreferredSize(new Dimension(180, 40));
        myApplicationsBtn.setBackground(new Color(63, 81, 181));
        myApplicationsBtn.setForeground(Color.WHITE);
        myApplicationsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        myApplicationsBtn.setFocusPainted(false);
        myApplicationsBtn.setBorderPainted(false);
        myApplicationsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        myApplicationsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewMyApplications();
            }
        });
        buttonPanel.add(myApplicationsBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(96, 125, 139));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadInternships();
            }
        });
        buttonPanel.add(refreshBtn);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Load internships when window opens
        loadInternships();
    }
    
    private void loadInternships() {
        tableModel.setRowCount(0);
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Internships");
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("location"),
                    rs.getString("stipend")
                };
                tableModel.addRow(row);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading internships: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void applyForInternship() {
        int selectedRow = internshipTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an internship to apply!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int internshipId = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Check if already applied
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Applications WHERE student_id = ? AND internship_id = ?");
            
            pstmt.setInt(1, currentStudent.getId());
            pstmt.setInt(2, internshipId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "You have already applied for this internship!", "Info", JOptionPane.INFORMATION_MESSAGE);
                rs.close();
                pstmt.close();
                conn.close();
                return;
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Apply for: " + title + "?", "Confirm Application", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Applications (student_id, internship_id) VALUES (?, ?)");
                
                pstmt.setInt(1, currentStudent.getId());
                pstmt.setInt(2, internshipId);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                pstmt.close();
                conn.close();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error submitting application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void viewMyApplications() {
        final JFrame appFrame = new JFrame("My Applications");
        appFrame.setSize(700, 400);
        appFrame.setLocationRelativeTo(this);
        
        String[] columns = {"Application ID", "Internship Title", "Company", "Location"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String sql = "SELECT a.id, i.title, i.company, i.location FROM Applications a JOIN Internships i ON a.internship_id = i.id WHERE a.student_id = ?";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, currentStudent.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("location")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(appFrame, "Error loading applications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        appFrame.add(new JScrollPane(table));
        appFrame.setVisible(true);
    }
}