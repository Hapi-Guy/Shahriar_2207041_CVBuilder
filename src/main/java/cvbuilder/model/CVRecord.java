package cvbuilder.model;

/**
 * Record for displaying CV list items (lightweight, doesn't load full CV data)
 */
public record CVRecord(int id, String fullName, String email, String phone, String createdAt) {
}
