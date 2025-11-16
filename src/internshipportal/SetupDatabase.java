package internshipportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SetupDatabase {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:internship.db");
            Statement stmt = conn.createStatement();
            
            // Create Students table
            String createStudents = "CREATE TABLE IF NOT EXISTS Students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");";
            
            // Create Companies table
            String createCompanies = "CREATE TABLE IF NOT EXISTS Companies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");";
            
            // Create Admins table
            String createAdmins = "CREATE TABLE IF NOT EXISTS Admins (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");";
            
            // Create Internships table
            String createInternships = "CREATE TABLE IF NOT EXISTS Internships (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "company TEXT NOT NULL," +
                    "location TEXT," +
                    "stipend TEXT" +
                    ");";
            
            // Create Applications table
            String createApplications = "CREATE TABLE IF NOT EXISTS Applications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER," +
                    "internship_id INTEGER," +
                    "FOREIGN KEY(student_id) REFERENCES Students(id)," +
                    "FOREIGN KEY(internship_id) REFERENCES Internships(id)" +
                    ");";
            
            stmt.execute(createStudents);
            stmt.execute(createCompanies);
            stmt.execute(createAdmins);
            stmt.execute(createInternships);
            stmt.execute(createApplications);
            
            System.out.println("All tables created successfully!");
            System.out.println("- Students table");
            System.out.println("- Companies table");
            System.out.println("- Admins table");
            System.out.println("- Internships table");
            System.out.println("- Applications table");
            
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}