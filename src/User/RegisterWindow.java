package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends JFrame {

    // 构造函数
    public RegisterWindow() {
        // 设置窗口标题
        setTitle("用户注册");

        // 设置窗口大小
        setSize(400, 400);

        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);  // 使用绝对布局

        // 创建用户名标签
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(50, 50, 80, 30);
        panel.add(userLabel);

        // 创建用户名输入框
        JTextField userText = new JTextField();
        userText.setBounds(150, 50, 150, 30);
        panel.add(userText);

        // 创建密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(50, 100, 80, 30);
        panel.add(passwordLabel);

        // 创建密码输入框
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);
        panel.add(passwordField);

        // 创建确认密码标签
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setBounds(50, 150, 80, 30);
        panel.add(confirmPasswordLabel);

        // 创建确认密码输入框
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 150, 150, 30);
        panel.add(confirmPasswordField);

        // 创建邮箱标签
        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setBounds(50, 200, 80, 30);
        panel.add(emailLabel);

        // 创建邮箱输入框
        JTextField emailText = new JTextField();
        emailText.setBounds(150, 200, 150, 30);
        panel.add(emailText);

        // 创建手机号码标签
        JLabel phoneLabel = new JLabel("手机号码:");
        phoneLabel.setBounds(50, 250, 80, 30);
        panel.add(phoneLabel);

        // 创建手机号码输入框
        JTextField phoneText = new JTextField();
        phoneText.setBounds(150, 250, 150, 30);
        panel.add(phoneText);

        // 创建注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(50, 300, 100, 30);
        panel.add(registerButton);

        // 创建返回按钮
        JButton backButton = new JButton("返回");
        backButton.setBounds(200, 300, 100, 30);
        panel.add(backButton);

        // 给注册按钮添加事件监听
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailText.getText();
                String phoneNumber = phoneText.getText();

                // 验证输入是否为空
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "所有字段都是必填的！");
                    return;
                }

                // 验证密码和确认密码是否一致
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "密码和确认密码不一致！");
                    return;
                }

                try {
                    DatabaseConnection dbConnection = new DatabaseConnection();
                    boolean isRegistered = dbConnection.registerUser(username, password, email, phoneNumber);

                    if (isRegistered) {
                        JOptionPane.showMessageDialog(null, "注册成功！");
                        dispose(); // 关闭注册窗口
                        new LoginWindow().setVisible(true); // 返回登录窗口
                    } else {
                        JOptionPane.showMessageDialog(null, "注册失败，请重试！");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "注册失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 给返回按钮添加事件监听
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 关闭当前注册窗口并返回登录窗口
                dispose();
                new LoginWindow().setVisible(true); // 创建并显示登录窗口
            }
        });

        // 将面板添加到窗口
        add(panel);

        // 设置窗口居中显示
        setLocationRelativeTo(null);
    }

    // 主函数
    public static void main(String[] args) {
        // 创建注册窗口对象
        RegisterWindow window = new RegisterWindow();
        window.setVisible(true);
    }
}