package Admin;

public class Merchant {
    private int MerchantID;
    private String MerchantName;
    private String ContactEmail;
    private String ContactPhone;
    private String Address;
    private double Rating;
    private String RegistrationDate;
    public Merchant(){}
    public Merchant(int MerchantID, String MerchantName, String ContactEmail, String ContactPhone, String Address, double Rating,String RegistrationDate) {
        this.MerchantID = MerchantID;
        this.MerchantName = MerchantName;
        this.ContactEmail = ContactEmail;
        this.ContactPhone = ContactPhone;
        this.Address = Address;
        this.RegistrationDate = RegistrationDate;
        this.Rating = Rating;
    }
    public Merchant(String MerchantName, String ContactEmail, String ContactPhone, String Address, double Rating, String RegistrationDate) {
        this.MerchantName = MerchantName;
        this.ContactEmail = ContactEmail;
        this.ContactPhone = ContactPhone;
        this.Address = Address;
        this.Rating = Rating;
        this.RegistrationDate = RegistrationDate;
    }
    public int getMerchantID() {return MerchantID;}
    public String getMerchantName() {return MerchantName;}
    public String getContactEmail() {return ContactEmail;}
    public String getContactPhone() {return ContactPhone;}
    public String getAddress() {return Address;}
    public double getRating() {return Rating;}
    public String getRegistrationDate() {return RegistrationDate;}
    public void setMerchantID(int MerchantID) {this.MerchantID = MerchantID;}
    public void setMerchantName(String MerchantName) {this.MerchantName = MerchantName;}
    public void setContactEmail(String ContactEmail) {this.ContactEmail = ContactEmail;}
    public void setContactPhone(String ContactPhone) {this.ContactPhone = ContactPhone;}
    public void setAddress(String Address) {this.Address = Address;}
    public void setRating(double Rating) {this.Rating = Rating;}
    public void setRegistrationDate(String RegistrationDate) {this.RegistrationDate = RegistrationDate;}
}
