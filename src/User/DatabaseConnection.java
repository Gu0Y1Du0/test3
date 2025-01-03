package User;

import Merchants.OrderWithDetails;
import Merchants.MerchantInfo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties properties = new Properties();

    static {
        try{
            properties.load(new FileInputStream(".env"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEnv(String key) {
        return properties.getProperty(key);
    }

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = getEnv("DB_USERNAME");
    private static final String PASSWORD = getEnv("DB_PASSWORD");
    private static final String databaseName= getEnv("DB_NAME");
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
        new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER, PASSWORD);
    }

    public List<Product> getProducts(String searchQuery, Integer categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE 1=1";

        if (searchQuery != null && !searchQuery.isEmpty()) {
            query += " AND ProductName LIKE ?";
        }
        if (categoryId != null) {
            query += " AND CategoryID = ?";
        }

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + searchQuery + "%");
            }
            if (categoryId != null) {
                preparedStatement.setInt(parameterIndex++, categoryId);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Description"),
                        resultSet.getBigDecimal("Price"),
                        resultSet.getInt("Stock"),
                        resultSet.getInt("CategoryID"),
                        resultSet.getDate("CreatedAt"),
                        resultSet.getInt("MerchantID")
                );
                products.add(product);
            }
        }
        return products;
    }

    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT CategoryID, CategoryName FROM categories";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("CategoryID"),
                        resultSet.getString("CategoryName")
                );
                categories.add(category);
            }
        }
        return categories;
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE Username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("UserID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Role"),
                        resultSet.getString("PasswordHash"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("RegistrationDate")
                );
            }
        }
        return null;
    }

    public boolean registerUser(String username, String password, String email, String phoneNumber) throws Exception {
        String hashedPassword = encrypt(password);

        String query = "INSERT INTO users (Username, PasswordHash, Email, PhoneNumber) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    String encrypt(String valueToEnc) throws Exception {
        SecretKey key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return Base64.getEncoder().encodeToString(encValue);
    }

    private SecretKey generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    public ArrayList<ProductWithQuantity> getCartItemsByUserID(int userID) throws SQLException {
        ArrayList<ProductWithQuantity> cartItems = new ArrayList<>();

        String query = "SELECT ci.ProductID, p.ProductName, p.Description, p.Price, p.Stock, p.CategoryID, p.CreatedAt, p.MerchantID, ci.Quantity "
                     + "FROM cartitems ci "
                     + "JOIN products p ON ci.ProductID = p.ProductID "
                     + "JOIN shoppingcart sc ON ci.CartID = sc.CartID "
                     + "WHERE sc.UserID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                String description = resultSet.getString("Description");
                BigDecimal price = resultSet.getBigDecimal("Price");
                int stock = resultSet.getInt("Stock");
                int categoryID = resultSet.getInt("CategoryID");
                Date createdAt = resultSet.getDate("CreatedAt");
                int merchantID = resultSet.getInt("MerchantID");
                int quantity = resultSet.getInt("Quantity");

                Product product = new Product(productID, productName, description, price, stock, categoryID, createdAt, merchantID);
                cartItems.add(new ProductWithQuantity(product, quantity));
            }
        }
        return cartItems;
    }

    private String hashPassword(String password) {
        // 简单的哈希示例，实际应用中应使用更强的加密算法
        return Integer.toHexString(password.hashCode());
    }

    public void addToCart(int userId, int productId, int quantity) throws SQLException {
        try (Connection connection = getConnection()) {
            // 获取或创建用户的购物车
            String getCartQuery = "SELECT CartID FROM shoppingcart WHERE UserID = ?";
            try (PreparedStatement getCartStatement = connection.prepareStatement(getCartQuery)) {
                getCartStatement.setInt(1, userId);
                ResultSet cartResultSet = getCartStatement.executeQuery();
                int cartId;
                if (!cartResultSet.next()) {
                    // 如果用户没有购物车，则创建一个新的购物车
                    String createCartQuery = "INSERT INTO shoppingcart (UserID) VALUES (?)";
                    try (PreparedStatement createCartStatement = connection.prepareStatement(createCartQuery, Statement.RETURN_GENERATED_KEYS)) {
                        createCartStatement.setInt(1, userId);
                        createCartStatement.executeUpdate();
                        ResultSet generatedKeys = createCartStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            cartId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Creating cart failed, no ID obtained.");
                        }
                    }
                } else {
                    cartId = cartResultSet.getInt("CartID");
                }

                // 检查购物车中是否已有该商品
                String checkQuery = "SELECT Quantity FROM cartitems WHERE CartID = ? AND ProductID = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                    checkStatement.setInt(1, cartId);
                    checkStatement.setInt(2, productId);
                    ResultSet resultSet = checkStatement.executeQuery();
                    if (resultSet.next()) {
                        int existingQuantity = resultSet.getInt("Quantity");
                        String updateQuery = "UPDATE cartitems SET Quantity = ? WHERE CartID = ? AND ProductID = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, existingQuantity + quantity);
                            updateStatement.setInt(2, cartId);
                            updateStatement.setInt(3, productId);
                            updateStatement.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO cartitems (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, cartId);
                            insertStatement.setInt(2, productId);
                            insertStatement.setInt(3, quantity);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void clearCart(int userId) throws SQLException {
        String deleteCartItemsQuery = "DELETE ci FROM cartitems ci JOIN shoppingcart sc ON ci.CartID = sc.CartID WHERE sc.UserID = ?";
        try (Connection connection = getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteCartItemsQuery)) {
            deleteStatement.setInt(1, userId);
            deleteStatement.executeUpdate();
        }
    }

    public int createOrder(int userId, double totalAmount) throws SQLException {
        String insertOrderQuery = "INSERT INTO orders (UserID, TotalAmount, OrderDate, Status) VALUES (?, ?, NOW(), 'Paid')";
        try (Connection connection = getConnection();
             PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertOrderStatement.setInt(1, userId);
            insertOrderStatement.setDouble(2, totalAmount);
            insertOrderStatement.executeUpdate();

            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
    }

    public void addOrderItem(int orderId, int productId, int quantity, double price) throws SQLException {
        String insertOrderItemQuery = "INSERT INTO orderitems (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement insertOrderItemStatement = connection.prepareStatement(insertOrderItemQuery)) {
            insertOrderItemStatement.setInt(1, orderId);
            insertOrderItemStatement.setInt(2, productId);
            insertOrderItemStatement.setInt(3, quantity);
            insertOrderItemStatement.setDouble(4, price);
            insertOrderItemStatement.executeUpdate();
        }
    }

    public void addPaymentRecord(int orderId, BigDecimal paymentAmount, String paymentMethod) throws SQLException {
        // 使用 BigDecimal 来处理 Amount 字段，确保精度
        String insertPaymentQuery = "INSERT INTO payments (OrderID, PaymentMethod, Amount) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement insertPaymentStatement = connection.prepareStatement(insertPaymentQuery)) {

            insertPaymentStatement.setInt(1, orderId);
            insertPaymentStatement.setString(2, paymentMethod);
            insertPaymentStatement.setBigDecimal(3, paymentAmount);

            insertPaymentStatement.executeUpdate();
        }
    }

    public MyInformation getMyInformationByUserID(int userID) throws SQLException {
        MyInformation mif = new MyInformation();
        List<UserAddress> addresses = new ArrayList<>();

        // 查询用户信息
        String query = "SELECT * FROM users WHERE UserID = ?";
        try (Connection connection = getConnection();  // 加上 getConnection()
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    mif.setUserID(resultSet.getInt("UserID"));
                    mif.setUsername(resultSet.getString("Username"));
                    mif.setPasswordHash(resultSet.getString("PasswordHash"));  // 修正拼写错误
                    mif.setEmail(resultSet.getString("Email"));
                    mif.setPhoneNumber(resultSet.getString("PhoneNumber"));
                    mif.setRegistrationDate(resultSet.getString("RegistrationDate"));
                }
            }
        }

        // 使用从第一个查询获得的 UserID 查询用户地址
        query = "SELECT * FROM useraddresses WHERE UserID = ?";
        try (Connection connection = getConnection();  // 加上 getConnection()
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);  // 设置动态查询条件
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UserAddress address = new UserAddress(
                            resultSet.getInt("AddressID"),
                            resultSet.getInt("UserID"),
                            resultSet.getString("AddressLine1"),
                            resultSet.getString("AddressLine2"),
                            resultSet.getString("City"),
                            resultSet.getString("State"),
                            resultSet.getString("PostalCode"),
                            resultSet.getString("Country"),
                            resultSet.getBoolean("IsDefault")
                    );
                    addresses.add(address);
                }
            }
        }

        mif.setAddresses(addresses);
        return mif;
    }

    public boolean updateUserInformation(int userID, String username, String passwordHash, String email, String phoneNumber) throws SQLException {
        String query = "UPDATE users SET Username = ?, PasswordHash = ?, Email = ?, PhoneNumber = ? WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, userID);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;  // 如果更新了至少一行，返回 true
        }
    }

    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE UserID = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setUserId(rs.getInt("UserID"));
                order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                order.setOrderDate(rs.getTimestamp("OrderDate").toLocalDateTime());
                order.setStatus(rs.getString("Status"));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Payment> getPaymentsByUserId(int userId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT p.* FROM payments p JOIN orders o ON p.OrderID = o.OrderID WHERE o.UserID = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("PaymentID"));
                payment.setOrderId(rs.getInt("OrderID"));
                payment.setPaymentMethod(rs.getString("PaymentMethod"));
                payment.setAmount(rs.getBigDecimal("Amount"));
                payment.setPaymentDate(rs.getTimestamp("PaymentDate").toLocalDateTime());
                payments.add(payment);
            }
        }
        return payments;
    }

    public List<Product> getProductsByMerchantID(int merchantID) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT ProductID, ProductName, Description, Price, Stock FROM products WHERE MerchantID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, merchantID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Description"),
                        resultSet.getBigDecimal("Price"),
                        resultSet.getInt("Stock")
                );
                products.add(product);
            }
        }
        return products;
    }

    public Integer getMerchantIDByUserID(int userID) throws SQLException {
        Integer merchantID = null;
        String query = "SELECT MerchantID FROM users WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                merchantID = resultSet.getInt("MerchantID");
            }
        }
        System.out.println(merchantID);
        return merchantID;
    }

    public List<OrderWithDetails> getOrdersByMerchantID(int merchantID) throws SQLException {
        List<OrderWithDetails> orders = new ArrayList<>();
        String query = "SELECT * FROM view_merchant_orders WHERE MerchantID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, merchantID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderWithDetails order = new OrderWithDetails(
                        resultSet.getString("MerchantName"),
                        resultSet.getString("ContactEmail"),
                        resultSet.getString("ContactPhone"),
                        resultSet.getString("Address"),
                        resultSet.getDouble("Rating"),
                        resultSet.getDate("RegistrationDate"),
                        resultSet.getString("ProductName"),
                        resultSet.getBigDecimal("TotalAmount"),
                        resultSet.getDate("OrderDate"),
                        resultSet.getString("Status"),
                        resultSet.getInt("Quantity"),
                        resultSet.getInt("merchantID"),
                        resultSet.getInt("orderID")

                );
                orders.add(order);
            }
        }
        return orders;
    }

    public boolean deleteOrderById(int orderId) throws SQLException {
        String query = "DELETE FROM orders WHERE OrderID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, orderId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean updateOrderStatusToCompleted(int orderId) throws SQLException {
        String query = "UPDATE orders SET Status = 'Completed' WHERE OrderID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            System.out.println(orderId);
            preparedStatement.setInt(1, orderId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    public MerchantInfo getMerchantInfoByUserID(int userID) throws SQLException {
        String query = "SELECT * FROM view_user_merchant WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MerchantInfo(
                    rs.getString("Username"),
                    rs.getString("Role"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getDate("UserRegistrationDate"),
                    rs.getString("MerchantName"),
                    rs.getString("ContactEmail"),
                    rs.getString("ContactPhone"),
                    rs.getString("Address"),
                    rs.getDouble("Rating"),
                    rs.getDate("MerchantRegistrationDate")
                );
            }
        }
        return null;
    }
}
