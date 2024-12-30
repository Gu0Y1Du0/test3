package User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton SignButton;
    private JLabel messageLabel;

    public LoginWindow() {
        // 设置窗口标题
        setTitle("网络超市大扫货");

        // 设置窗口大小和关闭操作
        setSize(600, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        // 设置窗口图标
//        URL iconURL = getClass().getResource("/icon/icon2.png");
//        ImageIcon icon = new ImageIcon(iconURL);
//        setIconImage(icon.getImage());

        // 使用GridBagLayout布局管理器以获得更灵活的布局控制
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //全局预设
        gbc.insets = new Insets(0, 0, 0, 0); // 组件之间的间距
        gbc.anchor= GridBagConstraints.WEST;

        // 创建登录标题
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        Font head =new Font("宋体",Font.BOLD,20);
        JLabel label1 = new JLabel("系统用户登录");
        label1.setFont(head);
        add(label1 ,gbc);

        // 创建用户名标签和文本框
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("用户名:"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // 创建密码标签和密码框
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("密码:"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // 创建登录按钮并添加监听器
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // 组件之间的间距
        loginButton = new JButton("登录");
        add(loginButton, gbc);
        // 创建注册按钮并添加监听器
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // 组件之间的间距
        SignButton =new JButton("注册");
        add(SignButton, gbc);

        // 消息标签用于显示登录结果
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        messageLabel = new JLabel("", SwingConstants.LEFT);
        add(messageLabel, gbc);

        // 添加事件监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                // 这里进行实际的登录验证（例如与数据库对比）
                if ("admin".equals(username) && "password".equals(new String(password))) {
                    messageLabel.setForeground(Color.GREEN);
                    messageLabel.setText("登录成功！");
                } else {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("用户名或密码错误！");
                }
            }
        });

        // 居中显示窗口
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        // 在事件调度线程中启动GUI应用
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}
