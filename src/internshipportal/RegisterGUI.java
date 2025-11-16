package internshipportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterGUI extends JFrame {
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cmbUserType;
    private JButton btnRegister;

    public RegisterGUI() {
        setTitle("Register - Internship Portal");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(99, 102, 241), 0, getHeight(), new Color(139, 92, 246));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(110, 30, 300, 40);
        mainPanel.add(lblTitle);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(50, 90, 350, 430);
        cardPanel.setLayout(null);

        JLabel lblName = new JLabel("Full Name");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblName.setBounds(30, 20, 290, 25);
        cardPanel.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(30, 45, 290, 35);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtName);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(30, 95, 290, 25);
        cardPanel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(30, 120, 290, 35);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtEmail);

        JLabel lblUserType = new JLabel("Register As");
        lblUserType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserType.setBounds(30, 170, 290, 25);
        cardPanel.add(lblUserType);

        String[] userTypes = {"Student", "Company", "Admin"};
        cmbUserType = new JComboBox<>(userTypes);
        cmbUserType.setBounds(30, 195, 290, 35);
        cmbUserType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbUserType.setBackground(Color.WHITE);
        cardPanel.add(cmbUserType);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(30, 245, 290, 25);
        cardPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 270, 290, 35);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblConfirmPassword.setBounds(30, 320, 290, 25);
        cardPanel.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(30, 345, 290, 35);
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtConfirmPassword);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(30, 395, 290, 40);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.setBackground(new Color(34, 197, 94));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final RegisterGUI currentFrame = this;
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser(currentFrame);
            }
        });
        cardPanel.add(btnRegister);

        mainPanel.add(cardPanel);

        JLabel lblLoginLink = new JLabel("<html><u>Already have an account? Login</u></html>");
        lblLoginLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLoginLink.setForeground(Color.WHITE);
        lblLoginLink.setBounds(130, 535, 250, 25);
        lblLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLoginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginGUI loginWindow = new LoginGUI();
                loginWindow.setVisible(true);
                currentFrame.dispose();
            }
        });
        mainPanel.add(lblLoginLink);

        add(mainPanel);
    }

    private void registerUser(final JFrame parentFrame) {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String userType = cmbUserType.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(parentFrame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(parentFrame, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql;
        if (userType.equals("Student")) {
            sql = "INSERT INTO Students (name, email, password) VALUES (?, ?, ?)";
        } else if (userType.equals("Company")) {
            sql = "INSERT INTO Companies (name, email, password) VALUES (?, ?, ?)";
        } else {
            sql = "INSERT INTO Admins (name, email, password) VALUES (?, ?, ?)";
        }
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(parentFrame, "Registration successful! Logging you in...", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            pstmt.close();
            
            String selectSql;
            if (userType.equals("Student")) {
                selectSql = "SELECT * FROM Students WHERE email = ?";
            } else if (userType.equals("Company")) {
                selectSql = "SELECT * FROM Companies WHERE email = ?";
            } else {
                selectSql = "SELECT * FROM Admins WHERE email = ?";
            }
            
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                if (userType.equals("Student")) {
                    Student student = new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
                    new StudentPortalGUI(student).setVisible(true);
                } else if (userType.equals("Company")) {
                    Company company = new Company(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
                    new CompanyPortalGUI(company).setVisible(true);
                } else {
                    new AdminGUI().setVisible(true);
                }
                parentFrame.dispose();
            }
            
            rs.close();
            selectStmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(parentFrame, "Email already registered!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (ex.getMessage().contains("no such table")) {
                JOptionPane.showMessageDialog(parentFrame, "Database tables not created! Please run SetupDatabase.java first.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegisterGUI frame = new RegisterGUI();
                frame.setVisible(true);
            }
        });
    }
}