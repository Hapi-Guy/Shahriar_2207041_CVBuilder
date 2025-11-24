package cvbuilder.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cvbuilder.model.CV;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple SQLite database helper for CV storage
 * Uses JSON to store lists (skills, education, experience, projects)
 */
public class DatabaseHelper {
    
    private static final String DB_URL = "jdbc:sqlite:cvbuilder.db";
    private static final Gson gson = new Gson();
    
    /**
     * Get database connection
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Create the CVs table if it doesn't exist
     */
    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cvs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT,
                address TEXT,
                skills_json TEXT,
                education_json TEXT,
                experience_json TEXT,
                projects_json TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database table created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    
    /**
     * Insert a new CV into the database
     */
    public static int insertCV(CV cv) throws SQLException {
        String sql = """
            INSERT INTO cvs (full_name, email, phone, address, skills_json, education_json, experience_json, projects_json)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cv.getFullName());
            pstmt.setString(2, cv.getEmail());
            pstmt.setString(3, cv.getPhone());
            pstmt.setString(4, cv.getAddress());
            pstmt.setString(5, listToJson(cv.getSkills()));
            pstmt.setString(6, listToJson(cv.getEducation()));
            pstmt.setString(7, listToJson(cv.getExperience()));
            pstmt.setString(8, listToJson(cv.getProjects()));
            
            pstmt.executeUpdate();
            
            // SQLite way to get last inserted ID
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    /**
     * Update an existing CV in the database
     */
    public static void updateCV(CV cv, int id) throws SQLException {
        String sql = """
            UPDATE cvs SET full_name = ?, email = ?, phone = ?, address = ?,
            skills_json = ?, education_json = ?, experience_json = ?, projects_json = ?
            WHERE id = ?
            """;
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cv.getFullName());
            pstmt.setString(2, cv.getEmail());
            pstmt.setString(3, cv.getPhone());
            pstmt.setString(4, cv.getAddress());
            pstmt.setString(5, listToJson(cv.getSkills()));
            pstmt.setString(6, listToJson(cv.getEducation()));
            pstmt.setString(7, listToJson(cv.getExperience()));
            pstmt.setString(8, listToJson(cv.getProjects()));
            pstmt.setInt(9, id);
            
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Delete a CV from the database
     */
    public static void deleteCV(int id) throws SQLException {
        String sql = "DELETE FROM cvs WHERE id = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Fetch all CVs from the database
     */
    public static List<CV> fetchAllCVs() throws SQLException {
        List<CV> cvList = new ArrayList<>();
        String sql = "SELECT * FROM cvs ORDER BY created_at DESC";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                CV cv = new CV();
                cv.setFullName(rs.getString("full_name"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setAddress(rs.getString("address"));
                
                // Convert JSON back to lists
                cv.getSkills().setAll(jsonToList(rs.getString("skills_json")));
                cv.getEducation().setAll(jsonToList(rs.getString("education_json")));
                cv.getExperience().setAll(jsonToList(rs.getString("experience_json")));
                cv.getProjects().setAll(jsonToList(rs.getString("projects_json")));
                
                cvList.add(cv);
            }
        }
        return cvList;
    }
    
    /**
     * Fetch a single CV by ID
     */
    public static CV fetchCVById(int id) throws SQLException {
        String sql = "SELECT * FROM cvs WHERE id = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                CV cv = new CV();
                cv.setFullName(rs.getString("full_name"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setAddress(rs.getString("address"));
                
                cv.getSkills().setAll(jsonToList(rs.getString("skills_json")));
                cv.getEducation().setAll(jsonToList(rs.getString("education_json")));
                cv.getExperience().setAll(jsonToList(rs.getString("experience_json")));
                cv.getProjects().setAll(jsonToList(rs.getString("projects_json")));
                
                return cv;
            }
        }
        return null;
    }
    
    /**
     * Helper: Convert List to JSON string
     */
    private static String listToJson(List<String> list) {
        return gson.toJson(list);
    }
    
    /**
     * Helper: Convert JSON string to List
     */
    private static List<String> jsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }
}
