package User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductWithQuantity {
    private Product product;
    private int quantity;

    public ProductWithQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "产品ID: " + product.getProductId() +
               ", 产品名称: " + product.getProductName() +
               ", 描述: " + product.getDescription() +
               ", 单价: " + product.getPrice() +
               ", 库存: " + product.getStock() +
               ", 分类ID: " + product.getCategoryId() +
               ", 创建时间: " + sdf.format(product.getCreatedAt()) +
               ", 商家ID: " + product.getMerchantId() +
               ", 数量: " + quantity;
    }
}