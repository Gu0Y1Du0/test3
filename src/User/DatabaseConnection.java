package User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public MyInformation getMyInformation() throws SQLException {
        MyInformation mif = new MyInformation();
        List<UserAddress> addresses = new ArrayList<>();

        // 查询用户信息
        String query = "SELECT * FROM users WHERE Username = 'john_doe'";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                mif.setUserID(resultSet.getInt("UserID"));
                mif.setUsername(resultSet.getString("Username"));
                mif.setPasswordHash(resultSet.getString("PasswordHash"));  // 修正拼写错误
                mif.setEmail(resultSet.getString("Email"));
                mif.setPhoneNumber(resultSet.getString("PhoneNumber"));
                mif.setRegistrationDate(resultSet.getString("RegistrationDate"));
            }
        }

        // 使用从第一个查询获得的 UserID 查询用户地址
//        int userID = mif.getUserID();
        int userID = 1;  // 获取查询到的 UserID
        query = "SELECT * FROM useraddresses WHERE UserID = ?";
        try (Connection connection = getConnection();
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
}