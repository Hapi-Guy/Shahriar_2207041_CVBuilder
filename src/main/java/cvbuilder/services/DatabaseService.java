package cvbuilder.services;

import cvbuilder.database.DatabaseHelper;
import cvbuilder.model.CV;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Async database service using JavaFX Service and Task
 * Prevents UI freezing during database operations
 */
public class DatabaseService {
    
    /**
     * Service to save a CV asynchronously
     */
    public static class SaveCVService extends Service<Integer> {
        private final CV cv;
        
        public SaveCVService(CV cv) {
            this.cv = cv;
        }
        
        @Override
        protected Task<Integer> createTask() {
            return new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    updateMessage("Saving CV to database...");
                    int id = DatabaseHelper.insertCV(cv);
                    updateMessage("CV saved successfully!");
                    return id;
                }
            };
        }
    }
    
    /**
     * Service to update a CV asynchronously
     */
    public static class UpdateCVService extends Service<Void> {
        private final CV cv;
        private final int id;
        
        public UpdateCVService(CV cv, int id) {
            this.cv = cv;
            this.id = id;
        }
        
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    updateMessage("Updating CV in database...");
                    DatabaseHelper.updateCV(cv, id);
                    updateMessage("CV updated successfully!");
                    return null;
                }
            };
        }
    }
    
    /**
     * Service to load all CVs asynchronously
     */
    public static class LoadAllCVsService extends Service<List<CV>> {
        
        @Override
        protected Task<List<CV>> createTask() {
            return new Task<List<CV>>() {
                @Override
                protected List<CV> call() throws Exception {
                    updateMessage("Loading CVs from database...");
                    List<CV> cvs = DatabaseHelper.fetchAllCVs();
                    updateMessage("CVs loaded successfully!");
                    return cvs;
                }
            };
        }
    }
    
    /**
     * Service to load a single CV by ID asynchronously
     */
    public static class LoadCVService extends Service<CV> {
        private final int id;
        
        public LoadCVService(int id) {
            this.id = id;
        }
        
        @Override
        protected Task<CV> createTask() {
            return new Task<CV>() {
                @Override
                protected CV call() throws Exception {
                    updateMessage("Loading CV from database...");
                    CV cv = DatabaseHelper.fetchCVById(id);
                    updateMessage("CV loaded successfully!");
                    return cv;
                }
            };
        }
    }
    
    /**
     * Service to delete a CV asynchronously
     */
    public static class DeleteCVService extends Service<Void> {
        private final int id;
        
        public DeleteCVService(int id) {
            this.id = id;
        }
        
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    updateMessage("Deleting CV from database...");
                    DatabaseHelper.deleteCV(id);
                    updateMessage("CV deleted successfully!");
                    return null;
                }
            };
        }
    }
    
    /**
     * Simple Task wrapper for one-time save operation (alternative to Service)
     */
    public static Task<Integer> createSaveTask(CV cv) {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return DatabaseHelper.insertCV(cv);
            }
        };
    }
    
    /**
     * Simple Task wrapper for one-time load all operation
     */
    public static Task<List<CV>> createLoadAllTask() {
        return new Task<List<CV>>() {
            @Override
            protected List<CV> call() throws Exception {
                return DatabaseHelper.fetchAllCVs();
            }
        };
    }
    
    /**
     * Simple Task wrapper for one-time delete operation
     */
    public static Task<Void> createDeleteTask(int id) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DatabaseHelper.deleteCV(id);
                return null;
            }
        };
    }
}
