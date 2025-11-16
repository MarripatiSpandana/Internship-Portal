package internshipportal;

public class Internship {
    private int id;
    private String title;
    private String company;
    private String location;
    private String stipend;

    public Internship(int id, String title, String company, String location, String stipend) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.stipend = stipend;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
}