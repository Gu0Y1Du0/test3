package User;
import java.sql.*;
import java.util.Scanner;

public class Login {
    Scanner sc=new Scanner(System.in);
    public void login() throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://localhost:3306/javadb";
        System.out.println("Enter the name of the user");
        String name=sc.nextLine();
        String user="user";
        System.out.println("Enter the password of the user");
        String password=sc.nextLine();
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
