package User;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int stock;
    private int categoryId;
    private Date createdAt;
    private int merchantId;
    private String categoryName;

    public Product(int productId, String productName, String description, BigDecimal price, int stock, int categoryId, Date createdAt, int merchantId) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.merchantId = merchantId;
    }

    public Product(int id, String name, String description, BigDecimal price, int stock) {
        this.productId = id;
        this.productName = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name,String description,BigDecimal price,int stock) {
        this.productName = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name,String description,BigDecimal price,int stock,String categoryName) {
        this.productName = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public Product(int productId, String name, String description, BigDecimal price, int stock, String categoryName) {
        this.productId = productId;
        this.productName = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getCategoryName() { return categoryName; }
}