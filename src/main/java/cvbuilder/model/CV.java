package cvbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class CV {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private List<String> education = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private List<String> experience = new ArrayList<>();
    private List<String> projects = new ArrayList<>();

    // getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<String> getEducation() { return education; }
    public List<String> getSkills() { return skills; }
    public List<String> getExperience() { return experience; }
    public List<String> getProjects() { return projects; }
}
