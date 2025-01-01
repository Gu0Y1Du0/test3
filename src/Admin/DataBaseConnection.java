package Admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataBaseConnection {
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

    public List<Merchant> getMerchant() throws SQLException {
        List<Merchant> merchants = new ArrayList<>();
        String query = "SELECT * FROM merchants;";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while(resultSet.next()) {
                Merchant merchant = new Merchant(
                        resultSet.getInt("MerchantID"),
                        resultSet.getString("MerchantName"),
                        resultSet.getString("ContactEmail"),
                        resultSet.getString("ContactPhone"),
                        resultSet.getString("Address"),
                        resultSet.getDouble("Rating"),
                        resultSet.getString("RegistrationDate")
                );
                merchants.add(merchant);
            }
        }
        return merchants;
    }

    public void AddMerchant(Merchant merchant) throws SQLException {
        // 使用占位符 '?' 来防止 SQL 注入
        String query = "INSERT INTO merchants (MerchantName, ContactEmail, ContactPhone, Address, Rating, RegistrationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // 设置占位符的值
            preparedStatement.setString(1, merchant.getMerchantName()); // MerchantName
            preparedStatement.setString(2, merchant.getContactEmail()); // ContactEmail
            preparedStatement.setString(3, merchant.getContactPhone()); // ContactPhone
            preparedStatement.setString(4, merchant.getAddress());      // Address
            preparedStatement.setBigDecimal(5, new BigDecimal(String.valueOf(merchant.getRating())));    // Rating

            // 将 String 类型的 RegistrationDate 转换为 Timestamp
            String registrationDateStr = merchant.getRegistrationDate();
            Timestamp registrationDate = Timestamp.valueOf(registrationDateStr); // 转换成 Timestamp
            preparedStatement.setTimestamp(6, registrationDate); // RegistrationDate

            // 执行插入操作
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void AddMerchanttoUser(Merchant merchant, User user) throws SQLException {
        String query = "Select MerchantID from merchants where MerchantName=?";
        int merchantID;
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setString(1, merchant.getMerchantName());
            merchantID = resultSet.getInt("MerchantID");
        }

        // 使用占位符 '?' 来防止 SQL 注入
        query = "INSERT INTO users (UserName, Role, PasswordHash, Email, PhoneNumber, RegistrationDate, MerchantID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // 设置占位符的值
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setBigDecimal(6, new BigDecimal(String.valueOf(user.getRegistrationDate())));
            preparedStatement.setInt(7, merchantID);
            preparedStatement.executeUpdate();

            // 将 String 类型的 RegistrationDate 转换为 Timestamp
            String registrationDateStr = merchant.getRegistrationDate();
            Timestamp registrationDate = Timestamp.valueOf(registrationDateStr); // 转换成 Timestamp
            preparedStatement.setTimestamp(6, registrationDate); // RegistrationDate

            // 执行插入操作
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteMerchant(Merchant merchant) throws SQLException {
        // 使用占位符 '?' 来防止 SQL 注入
        String query = "DELETE FROM merchants WHERE MerchantID = ?;";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // 设置占位符的值
            preparedStatement.setInt(1, merchant.getMerchantID());  // MerchantID
            // 执行删除操作
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Merchant with ID " + merchant.getMerchantID() + " was deleted successfully.");
            } else {
                System.out.println("No merchant found with ID " + merchant.getMerchantID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Commdity> getCommdity() throws SQLException {
        List<Commdity> commdities = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Commdity commdity = new Commdity(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("Price"),
                        resultSet.getInt("Stock"),
                        resultSet.getInt("CategoryID"),
                        resultSet.getString("CreatedAt"),
                        resultSet.getInt("MerchantID")
                );
                commdities.add(commdity);
            }
        }
        return commdities;
    }

    public void AddCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (CategoryName) VALUES (?);";  // 使用占位符 "?"
        try (Connection connection = getConnection();  // 获取数据库连接
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {  // 创建 PreparedStatement

            // 设置参数
            preparedStatement.setString(1, category.getCategoryName());  // 将 CategoryName 参数绑定到占位符

            // 执行插入操作
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // 捕获并打印 SQL 异常
        }
    }

    public void DeleteCommdity(Commdity commdity) throws SQLException {
        // 修正拼写错误：DELETE 语法，使用占位符 "?"
        String query = "DELETE FROM categories WHERE CategoryID = ?;";

        try (Connection connection = getConnection();  // 获取数据库连接
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {  // 创建 PreparedStatement

            // 设置参数：将 commdity 的 CategoryID 绑定到 SQL 查询中的占位符
            preparedStatement.setInt(1, commdity.getCategoryID());

            // 执行删除操作
            preparedStatement.executeUpdate();  // 执行 SQL 语句

        } catch (SQLException e) {
            e.printStackTrace();  // 捕获并打印 SQL 异常
        }
    }

    public List<User> getUser() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE Role != 'Admin';";
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("UserID"),
                        resultSet.getString("UserName"),
                        resultSet.getString("Role"),
                        resultSet.getString("PasswordHash"),
                        resultSet.getString("Email"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("RegistrationDate")
                );
                users.add(user);
            }
        }
        return users;
    }

    public void AddUser(User user) throws SQLException {
        String query = "INSERT INTO users (Username, Role, PasswordHash, Email, PhoneNumber, RegistrationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername()); // Username
            preparedStatement.setString(2, user.getRole()); // Role
            preparedStatement.setString(3, user.getPasswordHash()); // PasswordHash
            preparedStatement.setString(4, user.getEmail()); // Email
            preparedStatement.setString(5, user.getPhoneNumber()); // PhoneNumber

            if (user.getRegistrationDate() != null && !user.getRegistrationDate().isEmpty()) {
                Timestamp registrationTimestamp = Timestamp.valueOf(user.getRegistrationDate());
                preparedStatement.setTimestamp(6, registrationTimestamp); // RegistrationDate
            } else {
                preparedStatement.setNull(6, java.sql.Types.TIMESTAMP); // Null value if registrationDate is empty
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void DeleteUser(User user) throws SQLException {
        String query = "DELETE FROM users WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getUserID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void UpdateUserPasswordHash(User user, String newPasswordHash) throws SQLException {
        String query = "UPDATE users SET PasswordHash = ? WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // 使用PreparedStatement设置参数
            preparedStatement.setString(1, newPasswordHash);
            preparedStatement.setInt(2, user.getUserID());
            // 执行更新操作
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password hash updated successfully.");
            } else {
                System.out.println("No user found with the given UserID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // 可选择重新抛出异常或者进行日志记录
        }
    }

    public void UpdateUserRole(User user, String newRole) throws SQLException {
        String query = "UPDATE users SET Role = ? WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // 设置查询参数
            preparedStatement.setString(1, newRole);
            preparedStatement.setInt(2, user.getUserID());
            // 执行更新
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User role updated successfully.");
            } else {
                System.out.println("User not found or no change in role.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 或者使用日志记录工具来记录异常
            throw e;  // 抛出异常，或你可以处理异常以便上层调用者能够获知
        }
    }

    public void getNewMerchantIDtoUser(User user) throws SQLException {
        String query = "SELECT MerchantID FROM merchants WHERE UserID = ?";
        int newMerchantID;
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, user.getUserID());
            newMerchantID = resultSet.getInt("MerchantID");
        }
        query = "UPDATE users SET MerchantID = ? WHERE UserID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // 设置查询参数
            preparedStatement.setInt(1, newMerchantID);
            preparedStatement.setInt(2, user.getUserID());
            // 执行更新
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User MerchantID updated successfully.");
            } else {
                System.out.println("User not found or no change in role.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 或者使用日志记录工具来记录异常
            throw e;  // 抛出异常，或你可以处理异常以便上层调用者能够获知
        }
    }
}
