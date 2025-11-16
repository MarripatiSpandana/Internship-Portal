package internshipportal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CompanyPortalGUI extends JFrame {
    private Company currentCompany;
    private JTable internshipTable;
    private DefaultTableModel tableModel;

    public CompanyPortalGUI(Company company) {
        this.currentCompany = company;
        
        setTitle("Company Portal - Internship Management");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header Panel - GREEN COLOR for Company
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(16, 185, 129));
        headerPanel.setPreferredSize(new Dimension(1000, 70));
        headerPanel.setLayout(null);
        
        JLabel welcomeLabel = new JLabel("Company Dashboard - " + currentCompany.getName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(30, 20, 600, 30);
        headerPanel.add(welcomeLabel);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(850, 20, 120, 35);
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final CompanyPortalGUI currentFrame = this;
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
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Posted Internships");
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
        internshipTable.setSelectionBackground(new Color(167, 243, 208));
        internshipTable.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(internshipTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 250, 252));
        
        JButton postBtn = new JButton("Post New Internship");
        postBtn.setPreferredSize(new Dimension(200, 40));
        postBtn.setBackground(new Color(16, 185, 129));
        postBtn.setForeground(Color.WHITE);
        postBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        postBtn.setFocusPainted(false);
        postBtn.setBorderPainted(false);
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                postInternship();
            }
        });
        buttonPanel.add(postBtn);
        
        JButton viewApplicationsBtn = new JButton("View Applications");
        viewApplicationsBtn.setPreferredSize(new Dimension(180, 40));
        viewApplicationsBtn.setBackground(new Color(99, 102, 241));
        viewApplicationsBtn.setForeground(Color.WHITE);
        viewApplicationsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewApplicationsBtn.setFocusPainted(false);
        viewApplicationsBtn.setBorderPainted(false);
        viewApplicationsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewApplicationsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewApplications();
            }
        });
        buttonPanel.add(viewApplicationsBtn);
        
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setPreferredSize(new Dimension(160, 40));
        deleteBtn.setBackground(new Color(239, 68, 68));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteInternship();
            }
        });
        buttonPanel.add(deleteBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(107, 114, 128));
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
        
        loadInternships();
    }
    
    private void loadInternships() {
        tableModel.setRowCount(0);
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Internships WHERE company = '" + currentCompany.getName() + "'");
            
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
    
    private void postInternship() {
        JDialog dialog = new JDialog(this, "Post New Internship", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Internship Title:");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setBounds(30, 30, 440, 25);
        dialog.add(lblTitle);
        
        JTextField txtTitle = new JTextField();
        txtTitle.setBounds(30, 55, 440, 35);
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        dialog.add(txtTitle);
        
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLocation.setBounds(30, 110, 440, 25);
        dialog.add(lblLocation);
        
        JTextField txtLocation = new JTextField();
        txtLocation.setBounds(30, 135, 440, 35);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLocation.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        dialog.add(txtLocation);
        
        JLabel lblStipend = new JLabel("Stipend:");
        lblStipend.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStipend.setBounds(30, 190, 440, 25);
        dialog.add(lblStipend);
        
        JTextField txtStipend = new JTextField();
        txtStipend.setBounds(30, 215, 440, 35);
        txtStipend.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtStipend.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        dialog.add(txtStipend);
        
        JButton btnSubmit = new JButton("Post Internship");
        btnSubmit.setBounds(150, 320, 200, 45);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSubmit.setBackground(new Color(16, 185, 129));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = txtTitle.getText().trim();
                String location = txtLocation.getText().trim();
                String stipend = txtStipend.getText().trim();
                
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Title is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
                    PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Internships (title, company, location, stipend) VALUES (?, ?, ?, ?)");
                    
                    pstmt.setString(1, title);
                    pstmt.setString(2, currentCompany.getName());
                    pstmt.setString(3, location);
                    pstmt.setString(4, stipend);
                    
                    pstmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(dialog, "Internship posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadInternships();
                    
                    pstmt.close();
                    conn.close();
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error posting internship: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        dialog.add(btnSubmit);
        
        dialog.setVisible(true);
    }
    
    private void deleteInternship() {
        int selectedRow = internshipTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an internship to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int internshipId = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete: " + title + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Internships WHERE id = ?");
                
                pstmt.setInt(1, internshipId);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Internship deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadInternships();
                
                pstmt.close();
                conn.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting internship: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void viewApplications() {
        final JFrame appFrame = new JFrame("Applications Received");
        appFrame.setSize(800, 500);
        appFrame.setLocationRelativeTo(this);
        
        String[] columns = {"App ID", "Student Name", "Student Email", "Internship Title"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String sql = "SELECT a.id, s.name, s.email, i.title " +
                     "FROM Applications a " +
                     "JOIN Students s ON a.student_id = s.id " +
                     "JOIN Internships i ON a.internship_id = i.id " +
                     "WHERE i.company = ?";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, currentCompany.getName());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("title")
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