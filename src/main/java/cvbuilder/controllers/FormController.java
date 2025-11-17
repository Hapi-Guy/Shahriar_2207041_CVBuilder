package cvbuilder.controllers;

import java.io.IOException;
import java.util.Arrays;

import cvbuilder.model.CV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private TextArea educationArea;
    @FXML private TextArea skillsArea;
    @FXML private TextArea experienceArea;
    @FXML private TextArea projectsArea;

    @FXML
    private void onGenerateCV(ActionEvent event) throws IOException {
        // Validate Full Name
        String fullName = fullNameField.getText().trim();
        if (fullName.isBlank()) {
            showError("Full Name is required.");
            return;
        }
        if (!fullName.matches("^[a-zA-Z\\s.'-]+$")) {
            showError("Full Name cannot contain numbers or special characters.\nOnly letters, spaces, dots, hyphens, and apostrophes are allowed.");
            return;
        }
        if (fullName.length() < 2) {
            showError("Full Name must be at least 2 characters long.");
            return;
        }

        // Validate Email
        String email = emailField.getText().trim();
        if (email.isBlank()) {
            showError("Email is required.");
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showError("Invalid email format.\nPlease enter a valid email address (e.g., user@example.com).");
            return;
        }

        // Validate Phone (optional but must be valid if provided)
        String phone = phoneField.getText().trim();
        if (!phone.isBlank() && !phone.matches("^[+]?[0-9\\s()-]{7,20}$")) {
            showError("Invalid phone number format.\nPhone can only contain numbers, spaces, +, (, ), and - symbols.");
            return;
        }

        CV cv = new CV();
        cv.setFullName(fullName);
        cv.setEmail(email);
        cv.setPhone(phone);
        cv.setAddress(addressArea.getText().trim());

        Arrays.stream(educationArea.getText().split("\n"))
                .map(String::trim).filter(s->!s.isEmpty()).forEach(cv.getEducation()::add);
        Arrays.stream(skillsArea.getText().split("\n"))
                .map(String::trim).filter(s->!s.isEmpty()).forEach(cv.getSkills()::add);
        Arrays.stream(experienceArea.getText().split("\n"))
                .map(String::trim).filter(s->!s.isEmpty()).forEach(cv.getExperience()::add);
        Arrays.stream(projectsArea.getText().split("\n"))
                .map(String::trim).filter(s->!s.isEmpty()).forEach(cv.getProjects()::add);

        // Show success message
        showSuccess();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/preview.fxml"));
        Parent root = loader.load();
        // pass cv to preview controller
        PreviewController ctrl = loader.getController();
        ctrl.setCv(cv);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("CV Created Successfully");
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
