package cvbuilder.controllers;

import cvbuilder.model.CV;
import javafx.fxml.FXML;
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

    public void setCv(CV cv) {
        this.cv = cv;
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
}
