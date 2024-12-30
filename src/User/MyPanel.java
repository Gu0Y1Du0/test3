package User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MyPanel extends JPanel {
    private JPanel userInfoPanel;  // 声明面板
    private MyInformation myInformation = new MyInformation();  // 用户信息

    public MyPanel() {
        setLayout(new BorderLayout());

        // 初始化 userInfoPanel
        userInfoPanel = new JPanel();  // 必须在使用之前初始化

        loadInformation();

        // 顶部欢迎栏
        JLabel myTitle = new JLabel("Welcome, Dear " + myInformation.getUsername() + "!", SwingConstants.CENTER);
        add(myTitle, BorderLayout.NORTH);

        // 创建用户信息面板
        userInfoPanel.setLayout(new GridLayout(6, 2, 10, 10));  // 6行2列的表单布局

        // 用户信息字段
        userInfoPanel.add(new JLabel("Name:"));
        userInfoPanel.add(new JTextField(myInformation.getUsername()));

        userInfoPanel.add(new JLabel("ID:"));
        userInfoPanel.add(new JTextField(String.valueOf(myInformation.getUserID())));

        userInfoPanel.add(new JLabel("Email:"));
        userInfoPanel.add(new JTextField(myInformation.getEmail()));

        userInfoPanel.add(new JLabel("Phone Number:"));
        userInfoPanel.add(new JTextField(myInformation.getPhoneNumber()));

        userInfoPanel.add(new JLabel("Registration Date:"));
        userInfoPanel.add(new JTextField(myInformation.getRegistrationDate()));

        // 地址部分：首先显示默认地址
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));

        addressPanel.add(new JLabel("Default Address:"));
        JTextArea defaultAddressArea = new JTextArea(myInformation.getAddresses().getFirst().toString());
        defaultAddressArea.setEditable(false);
        addressPanel.add(defaultAddressArea);

        // 使用 JComboBox 显示其他地址
        JComboBox<UserAddress> addressComboBox = new JComboBox<>();
        for (UserAddress address : myInformation.getAddresses()) {
            if (!address.isDefault()) {
                addressComboBox.addItem(address);
            }
        }
        addressPanel.add(new JLabel("Other Addresses:"));
        addressPanel.add(addressComboBox);

        // 添加地址面板到用户信息面板
        userInfoPanel.add(addressPanel);
        add(userInfoPanel, BorderLayout.CENTER);
    }

    private void loadInformation() {
        try {
            // 假设从数据库中加载用户信息
            DatabaseConnection dbConnection = new DatabaseConnection();
            myInformation = dbConnection.getMyInformation();  // 直接赋值给成员变量
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "无法获取类别数据: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
