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

public class LoginGUI extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbUserType;
    private JButton btnLogin;

    public LoginGUI() {
        setTitle("Login - Internship Portal");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(59, 130, 246), 0, getHeight(), new Color(147, 51, 234));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(100, 40, 300, 45);
        mainPanel.add(lblTitle);

        JLabel lblSubtitle = new JLabel("Sign in to continue");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(230, 230, 250));
        lblSubtitle.setBounds(140, 85, 200, 25);
        mainPanel.add(lblSubtitle);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(50, 140, 350, 300);
        cardPanel.setLayout(null);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(30, 30, 290, 25);
        cardPanel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(30, 55, 290, 40);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtEmail);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(30, 110, 290, 25);
        cardPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 135, 290, 40);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(txtPassword);

        JLabel lblUserType = new JLabel("Login As");
        lblUserType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserType.setBounds(30, 190, 290, 25);
        cardPanel.add(lblUserType);

        String[] userTypes = {"Student", "Company", "Admin"};
        cmbUserType = new JComboBox<>(userTypes);
        cmbUserType.setBounds(30, 215, 290, 35);
        cmbUserType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbUserType.setBackground(Color.WHITE);
        cardPanel.add(cmbUserType);

        mainPanel.add(cardPanel);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(125, 455, 200, 40);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(59, 130, 246));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final LoginGUI currentFrame = this;
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser(currentFrame);
            }
        });
        mainPanel.add(btnLogin);

        JLabel lblRegisterLink = new JLabel("<html><u>Don't have an account? Register</u></html>");
        lblRegisterLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRegisterLink.setForeground(Color.WHITE);
        lblRegisterLink.setBounds(125, 505, 250, 25);
        lblRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegisterLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegisterGUI registerWindow = new RegisterGUI();
                registerWindow.setVisible(true);
                currentFrame.dispose();
            }
        });
        mainPanel.add(lblRegisterLink);

        add(mainPanel);

        txtPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser(currentFrame);
            }
        });
    }

    private void loginUser(final JFrame parentFrame) {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String userType = cmbUserType.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter email and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql;
        if (userType.equals("Student")) {
            sql = "SELECT * FROM Students WHERE email = ? AND password = ?";
        } else if (userType.equals("Company")) {
            sql = "SELECT * FROM Companies WHERE email = ? AND password = ?";
        } else {
            sql = "SELECT * FROM Admins WHERE email = ? AND password = ?";
        }
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
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
                
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("no such table")) {
                JOptionPane.showMessageDialog(parentFrame, "Database tables not created! Please run SetupDatabase.java first.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });
    }
}