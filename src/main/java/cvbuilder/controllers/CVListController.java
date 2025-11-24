package cvbuilder.controllers;

import cvbuilder.database.DatabaseHelper;
import cvbuilder.model.CV;
import cvbuilder.model.CVRecord;
import cvbuilder.services.DatabaseService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for CV List screen - displays all saved CVs with CRUD operations
 */
public class CVListController {
    
    @FXML private TableView<CVRecord> cvTable;
    @FXML private TableColumn<CVRecord, Number> idColumn;
    @FXML private TableColumn<CVRecord, String> nameColumn;
    @FXML private TableColumn<CVRecord, String> emailColumn;
    @FXML private TableColumn<CVRecord, String> phoneColumn;
    @FXML private TableColumn<CVRecord, String> dateColumn;
    
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    
    @FXML private Button viewButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    
    private ObservableList<CVRecord> cvRecords = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Setup table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().id()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().fullName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().email()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().phone()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().createdAt()));
        
        cvTable.setItems(cvRecords);
        
        // Enable buttons only when a row is selected
        cvTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            viewButton.setDisable(!hasSelection);
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });
        
        // Setup search filter
        searchField.textProperty().addListener((obs, oldText, newText) -> filterCVs(newText));
        
        // Load CVs on startup
        loadAllCVs();
    }
    
    @FXML
    private void onRefresh() {
        loadAllCVs();
    }
    
    private void loadAllCVs() {
        statusLabel.setText("Loading CVs...");
        
        Task<List<CVRecord>> loadTask = new Task<List<CVRecord>>() {
            @Override
            protected List<CVRecord> call() throws Exception {
                return fetchAllCVRecords();
            }
        };
        
        loadTask.setOnSucceeded(event -> {
            cvRecords.clear();
            cvRecords.addAll(loadTask.getValue());
            statusLabel.setText("Loaded " + cvRecords.size() + " CV(s)");
        });
        
        loadTask.setOnFailed(event -> {
            statusLabel.setText("Error loading CVs: " + loadTask.getException().getMessage());
            showError("Failed to load CVs: " + loadTask.getException().getMessage());
        });
        
        new Thread(loadTask).start();
    }
    
    private void filterCVs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // Show all if search is empty
            return;
        }
        
        String lowerSearch = searchText.toLowerCase();
        ObservableList<CVRecord> filtered = cvRecords.filtered(record -> 
            record.fullName().toLowerCase().contains(lowerSearch) ||
            record.email().toLowerCase().contains(lowerSearch)
        );
        cvTable.setItems(filtered);
    }
    
    @FXML
    private void onViewCV(ActionEvent event) {
        CVRecord selected = cvTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        loadAndShowCV(selected.id(), event, false);
    }
    
    @FXML
    private void onEditCV(ActionEvent event) {
        CVRecord selected = cvTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        loadAndShowCV(selected.id(), event, true);
    }
    
    private void loadAndShowCV(int cvId, ActionEvent event, boolean editMode) {
        Task<CV> loadTask = new Task<CV>() {
            @Override
            protected CV call() throws Exception {
                return DatabaseHelper.fetchCVById(cvId);
            }
        };
        
        loadTask.setOnSucceeded(e -> {
            try {
                CV cv = loadTask.getValue();
                if (cv == null) {
                    showError("CV not found!");
                    return;
                }
                
                if (editMode) {
                    // Load into form for editing
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form.fxml"));
                    Parent root = loader.load();
                    FormController controller = loader.getController();
                    controller.loadCV(cv, cvId);
                    
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(root);
                } else {
                    // Show in preview
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/preview.fxml"));
                    Parent root = loader.load();
                    PreviewController controller = loader.getController();
                    controller.setCv(cv, cvId);
                    
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(root);
                }
            } catch (IOException ex) {
                showError("Failed to load screen: " + ex.getMessage());
            }
        });
        
        loadTask.setOnFailed(e -> {
            showError("Failed to load CV: " + loadTask.getException().getMessage());
        });
        
        new Thread(loadTask).start();
    }
    
    @FXML
    private void onDeleteCV() {
        CVRecord selected = cvTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete CV: " + selected.fullName());
        confirm.setContentText("Are you sure you want to delete this CV? This action cannot be undone.");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteCV(selected.id());
        }
    }
    
    private void deleteCV(int cvId) {
        Task<Void> deleteTask = DatabaseService.createDeleteTask(cvId);
        
        deleteTask.setOnSucceeded(event -> {
            showSuccess("CV deleted successfully!");
            loadAllCVs(); // Refresh list
        });
        
        deleteTask.setOnFailed(event -> {
            showError("Failed to delete CV: " + deleteTask.getException().getMessage());
        });
        
        new Thread(deleteTask).start();
    }
    
    @FXML
    private void onBackToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
    
    /**
     * Fetch CV records (ID, name, email, date only - not full CV data)
     */
    private List<CVRecord> fetchAllCVRecords() throws Exception {
        List<CVRecord> records = new ArrayList<>();
        String sql = "SELECT id, full_name, email, phone, created_at FROM cvs ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                records.add(new CVRecord(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("created_at")
                ));
            }
        }
        return records;
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
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
