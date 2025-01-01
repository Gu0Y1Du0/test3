package Merchants;

import java.math.BigDecimal;
import java.sql.Date;

public class OrderWithDetails {
    private String merchantName;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private double rating;
    private Date registrationDate;
    private String productName;
    private BigDecimal totalAmount;
    private Date orderDate;
    private String status;
    private int quantity;
    private int merchantID;
    private int orderID;

    public OrderWithDetails(String merchantName, String contactEmail, String contactPhone, String address, double rating, Date registrationDate,
                             String productName, BigDecimal totalAmount, Date orderDate, String status, int quantity, int merchantID, int orderID) {
        this.merchantName = merchantName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.address = address;
        this.rating = rating;
        this.registrationDate = registrationDate;
        this.productName = productName;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.quantity = quantity;
        this.merchantID = merchantID;
        this.orderID = orderID;
    }

    // Getters and Setters

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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}