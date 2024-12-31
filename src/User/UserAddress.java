package User;

public class UserAddress {
    private int addressID;
    private int userID;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;

    // 构造器
    public UserAddress(int addressID, int userID, String addressLine1, String addressLine2, String city, String state, String postalCode, String country, boolean isDefault) {
        this.addressID = addressID;
        this.userID = userID;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }
    // Getter 和 Setter 方法
    //addressID
    public int getAddressID() {
        return addressID;
    }
    //userID
    public int getUserID() {
        return userID;
    }
    //AddressLine1
    public String getAddressLine1() {
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    //AddressLine2
    public String getAddressLine2() {
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    //City
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    //State
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    //PostlCode
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    //Country
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    //isDefault
    public boolean isDefault() {
        return isDefault;
    }
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    @Override
    public String toString() {
        return "UserAddress{" +
                "addressID=" + addressID +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}

