package User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyInformation {
    private int userID;
    private String username;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String registrationDate;
    private List<UserAddress> addresses;
    // 构造器
    public MyInformation() {}
    public MyInformation(int userID, String username, String passwordHash, String email, String phoneNumber, String registrationDate) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }
    // Get 和 Set 方法
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    //可用于改名字
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    //可用于改密码
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    //可用于改邮箱
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    //可用于改手机号
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //查注册时间
    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    //改地址
    public List<UserAddress> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<UserAddress> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return  "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", addresses=" + addresses;
    }
}
