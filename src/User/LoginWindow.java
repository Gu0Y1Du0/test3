package User;

import Admin.AdminMenuPanel;
import Merchants.MerchantMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    // 构造函数
    public LoginWindow() {
        // 设置窗口的标题
        setTitle("网购商城登录");

        // 设置窗口的大小
        setSize(400, 300);

        // 设置窗口的关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口图标
        ImageIcon icon = new ImageIcon("./icon/icon2.png");  // 替换为你图标的路径
        setIconImage(icon.getImage());

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);  // 设置为绝对布局

        // 创建字体
        Font font = new Font("宋体", Font.PLAIN, 16);

        // 创建用户名标签
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(50, 50, 80, 30);
        userLabel.setFont(font);  // 设置字体
        panel.add(userLabel);

        // 创建用户名文本框
        JTextField userText = new JTextField();
        userText.setBounds(150, 50, 150, 30);
        userText.setFont(font);  // 设置字体
        panel.add(userText);

        // 创建密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(50, 100, 80, 30);
        passwordLabel.setFont(font);  // 设置字体
        panel.add(passwordLabel);

        // 创建密码输入框
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);
        passwordField.setFont(font);  // 设置字体
        panel.add(passwordField);

        // 创建登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.setBounds(50, 160, 100, 30);
        loginButton.setFont(font);  // 设置字体
        panel.add(loginButton);

        // 创建注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(200, 160, 100, 30);
        registerButton.setFont(font);  // 设置字体
        panel.add(registerButton);

        // 给登录按钮添加事件监听
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordField.getPassword());

                try {
                    DatabaseConnection dbConnection = new DatabaseConnection();
                    User user = dbConnection.getUserByUsername(username);

                    if (user != null && user.getPasswordHash().equals(dbConnection.encrypt(password))) {
                        JOptionPane.showMessageDialog(null, "登录成功!");

                        // 根据用户角色打开不同的界面
                        switch (user.getRole()) {
                            case "Admin":
                                new AdminMenuPanel().setVisible(true);
                                break;
                            case "Merchant":
                                new MerchantMenuPanel().setVisible(true);
                                break;
                            default:
                                // 默认打开用户界面（假设有一个用户界面）
                                new Menu(user).setVisible(true);
                                break;
                        }
                        dispose(); // 关闭登录窗口
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "登录失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 给注册按钮添加事件监听
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 打开注册窗口
                new RegisterWindow().setVisible(true);
                dispose(); // 关闭登录窗口
            }
        });

        // 将面板添加到窗口
        add(panel);

        // 设置窗口居中显示
        setLocationRelativeTo(null);
    }

    // 主函数
    public static void main(String[] args) {
        // 创建登录窗口对象
        LoginWindow window = new LoginWindow();
        window.setVisible(true);
    }
}