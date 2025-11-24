package cvbuilder.controllers;

import cvbuilder.model.CV;
import cvbuilder.services.DatabaseService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PreviewController {
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private VBox educationBox;
    @FXML private VBox skillsBox;
    @FXML private VBox experienceBox;
    @FXML private VBox projectsBox;

    private CV cv;
    private Integer cvId = null; // null = new CV, number = existing CV

    public void setCv(CV cv) {
        this.cv = cv;
        this.cvId = null;
        render();
    }
    
    public void setCv(CV cv, int id) {
        this.cv = cv;
        this.cvId = id;
        render();
    }

    private void render() {
        if (cv == null) return;
        nameLabel.setText(cv.getFullName());
        emailLabel.setText(cv.getEmail());
        phoneLabel.setText(cv.getPhone());
        addressLabel.setText(cv.getAddress());

        cv.getEducation().forEach(item -> {
            javafx.scene.control.Label l = new javafx.scene.control.Label(item);
            l.getStyleClass().add("cv-item");
            educationBox.getChildren().add(l);
        });

        cv.getSkills().forEach(item -> {
            javafx.scene.control.Label l = new javafx.scene.control.Label(item);
            l.getStyleClass().add("cv-item");
            skillsBox.getChildren().add(l);
        });

        cv.getExperience().forEach(item -> {
            javafx.scene.control.Label l = new javafx.scene.control.Label(item);
            l.getStyleClass().add("cv-item");
            experienceBox.getChildren().add(l);
        });

        cv.getProjects().forEach(item -> {
            javafx.scene.control.Label l = new javafx.scene.control.Label(item);
            l.getStyleClass().add("cv-item");
            projectsBox.getChildren().add(l);
        });
    }
    
    @FXML
    private void onSaveCV() {
        if (cv == null) return;
        
        if (cvId == null) {
            // Insert new CV
            Task<Integer> saveTask = DatabaseService.createSaveTask(cv);
            
            saveTask.setOnSucceeded(event -> {
                Integer newCvId = saveTask.getValue();
                this.cvId = newCvId; // Update local ID
                showSuccess("CV saved to database successfully!\nCV ID: " + newCvId);
            });
            
            saveTask.setOnFailed(event -> {
                showError("Failed to save CV: " + saveTask.getException().getMessage());
            });
            
            new Thread(saveTask).start();
        } else {
            // Update existing CV
            Task<Void> updateTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    cvbuilder.database.DatabaseHelper.updateCV(cv, cvId);
                    return null;
                }
            };
            
            updateTask.setOnSucceeded(event -> {
                showSuccess("CV updated successfully!\nCV ID: " + cvId);
            });
            
            updateTask.setOnFailed(event -> {
                showError("Failed to update CV: " + updateTask.getException().getMessage());
            });
            
            new Thread(updateTask).start();
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void onBackToHome(javafx.event.ActionEvent event) throws java.io.IOException {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        javafx.scene.Parent root = loader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
