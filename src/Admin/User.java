package Admin;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String username;
    private String role;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String registrationDate;
    public User(String username, String role, String passwordHash, String email, String phoneNumber, String registrationDate){
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }
    public User(int userID, String username, String role, String passwordHash, String email, String phoneNumber, String registrationDate) {
        this.userID = userID;
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }
    public void setUserId(int userId) {
        this.userID = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}



