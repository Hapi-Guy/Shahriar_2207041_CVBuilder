package cvbuilder.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CV {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private ObservableList<String> education = FXCollections.observableArrayList();
    private ObservableList<String> skills = FXCollections.observableArrayList();
    private ObservableList<String> experience = FXCollections.observableArrayList();
    private ObservableList<String> projects = FXCollections.observableArrayList();

    // getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public ObservableList<String> getEducation() { return education; }
    public ObservableList<String> getSkills() { return skills; }
    public ObservableList<String> getExperience() { return experience; }
    public ObservableList<String> getProjects() { return projects; }
}
