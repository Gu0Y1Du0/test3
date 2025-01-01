package Merchants;

import java.util.Date;

public class MerchantInfo {
    private String username;
    private String role;
    private String email;
    private String phoneNumber;
    private Date userRegistrationDate;
    private String merchantName;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private double rating;
    private Date merchantRegistrationDate;

    public MerchantInfo(String username, String role, String email, String phoneNumber, Date userRegistrationDate,
                         String merchantName, String contactEmail, String contactPhone, String address, double rating,
                         Date merchantRegistrationDate) {
        this.username = username;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRegistrationDate = userRegistrationDate;
        this.merchantName = merchantName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.address = address;
        this.rating = rating;
        this.merchantRegistrationDate = merchantRegistrationDate;
    }

    // Getters and setters
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

    public Date getUserRegistrationDate() {
        return userRegistrationDate;
    }

    public void setUserRegistrationDate(Date userRegistrationDate) {
        this.userRegistrationDate = userRegistrationDate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getMerchantRegistrationDate() {
        return merchantRegistrationDate;
    }

    public void setMerchantRegistrationDate(Date merchantRegistrationDate) {
        this.merchantRegistrationDate = merchantRegistrationDate;
    }
}