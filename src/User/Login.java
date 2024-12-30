package User;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Login {
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

    public void login() throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://localhost:3306/";
        String user=getEnv("DB_USERNAME");
        String password=getEnv("DB_PASSWORD");
        String dabaseName=getEnv("DB_NAME");
        //加载库
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接对象
        Connection connection=DriverManager.getConnection(url,user,password);
        //获取执行者
        Statement statement=connection.createStatement();
        //判断连接成功与否
        if(connection == null){
            System.out.println("Connection is null");
        }
        else{
            System.out.println("Connection is successful");
        }
    }
}