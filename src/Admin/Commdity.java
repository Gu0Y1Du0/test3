package Admin;

public class Commdity {
    private int ProductID;
    private String ProductName;
    private String ProductDescription;
    private double ProductPrice;
    private int ProductStock;
    private int CategoryID;
    private String CreateAt;
    private int MerchantID;
    public Commdity(){}
    public Commdity(int ProductID, String ProductName, String ProductDescription, double ProductPrice, int ProductStock, int CategoryID, String CreateAt, int MerchantID) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.ProductDescription = ProductDescription;
        this.ProductPrice = ProductPrice;
        this.ProductStock = ProductStock;
        this.CategoryID = CategoryID;
        this.CreateAt = CreateAt;
        this.MerchantID = MerchantID;
    }
    public int getProductID() {
        return ProductID;
    }
    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }
    public String getProductName() {
        return ProductName;
    }
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }
    public String getProductDescription() {
        return ProductDescription;
    }
    public void setProductDescription(String ProductDescription) {
        this.ProductDescription = ProductDescription;
    }
    public double getProductPrice() {
        return ProductPrice;
    }
    public void setProductPrice(double ProductPrice) {
        this.ProductPrice = ProductPrice;
    }
    public int getProductStock() {
        return ProductStock;
    }
    public void setProductStock(int ProductStock) {
        this.ProductStock = ProductStock;
    }
    public int getCategoryID() {
        return CategoryID;
    }
    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }
    public String getCreateAt() {
        return CreateAt;
    }
    public void setCreateAt(String CreateAt) {
        this.CreateAt = CreateAt;
    }
    public int getMerchantID() {
        return MerchantID;
    }
    public void setMerchantID(int MerchantID) {
        this.MerchantID = MerchantID;
    }
}
