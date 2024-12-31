package Admin;

import java.io.FileInputStream;
import java.io.IOException;
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
        String query = "SELECT * FROM merchants";

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
        String query="INSERT INTO merchants(MerchantName,ContactEmail,ContactPhone,Address,Rating,RegistrationDate) VALUES("+
                merchant.getMerchantName()+","+merchant.getContactEmail()+","+merchant.getContactPhone()+","+merchant.getAddress()+","+merchant.getRating()+","+merchant.getRegistrationDate()+");";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteMerchant(Merchant merchant) throws SQLException {
        String query="DELETE FROM merchants WHERE MerchantID = "+merchant.getMerchantID()+";";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
        }catch (SQLException e) {
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
        String query = "INSERT INTO categories(CategoryName) VALUES("+category.getCategoryName()+");";
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteCommdity(Commdity commdity) throws SQLException {
        String query = "DLETE FROM categories WHERE CategoryID = "+commdity.getCategoryID()+";";
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
        }catch (SQLException e){
            e.printStackTrace();
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
        String query = "INSERT INTO users(Username, Role, PasswordHash, Email, PhoneNumer, RegistrationDate) VALUE("+
                user.getUsername()+","+user.getRole()+","+user.getPasswordHash()+","+user.getEmail()+","+user.getPhoneNumber()+","+user.getRegistrationDate()+");";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void DeleteUser(User user) throws SQLException {
        String query = "DELETE FROM users WHERE UserID = "+user.getUserID()+";";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateUserPasswordHash(User user, String newPasswordHash) throws SQLException {
        String query = "UPDATE users SET PasswordHash = '"+newPasswordHash+"' WHERE UserID = "+user.getUserID()+";";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateUserRole(User user, String newRole) throws SQLException {
        String query ="UPDATE users SET Role = "+newRole+" WHERE UserID = "+user.getUserID()+";";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
