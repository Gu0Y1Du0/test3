package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyPanel extends JPanel {
    private JPanel userInfoPanel;  // 声明面板
    private User user;  // 用户对象

    public MyPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.user = user;  // 设置传入的用户对象

        // 初始化 userInfoPanel
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridBagLayout());
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // 设置组件间的间距
        gbc.anchor = GridBagConstraints.WEST;

        // 顶部欢迎栏
        JLabel myTitle = new JLabel("Welcome, Dear " + user.getUsername() + "!", SwingConstants.CENTER);
        myTitle.setFont(new Font("Arial", Font.BOLD, 24));
        myTitle.setForeground(new Color(51, 51, 51));
        add(myTitle, BorderLayout.NORTH);

        // 用户信息字段
        addLabeledField(userInfoPanel, "Name:", user.getUsername(), gbc, 0, 0);
        addLabeledField(userInfoPanel, "ID:", String.valueOf(user.getUserId()), gbc, 0, 1);
        addLabeledField(userInfoPanel, "Email:", user.getEmail(), gbc, 0, 2);
        addLabeledField(userInfoPanel, "Phone Number:", user.getPhoneNumber(), gbc, 0, 3);
        addLabeledField(userInfoPanel, "Registration Date:", user.getRegistrationDate().toString(), gbc, 0, 4);

        // 地址部分：首先显示默认地址
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setBackground(Color.WHITE);
        addressPanel.setBorder(BorderFactory.createTitledBorder("Addresses"));

        UserAddress defaultAddress = findDefaultAddress(user.getAddresses());
        if (defaultAddress != null) {
            JTextArea defaultAddressArea = new JTextArea(defaultAddress.toString());
            defaultAddressArea.setEditable(false);
            defaultAddressArea.setLineWrap(true);
            defaultAddressArea.setWrapStyleWord(true);
            defaultAddressArea.setPreferredSize(new Dimension(300, 100));  // 调整宽度以匹配其他字段
            defaultAddressArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            addressPanel.add(new JLabel("Default Address:"));
            addressPanel.add(defaultAddressArea);
        } else {
            addressPanel.add(new JLabel("No default address found."));
        }

        // 使用 JComboBox 显示其他地址
        JComboBox<UserAddress> addressComboBox = new JComboBox<>();
        for (UserAddress address : user.getAddresses()) {
            if (!address.isDefault()) {
                addressComboBox.addItem(address);
            }
        }
        addressComboBox.setPreferredSize(new Dimension(300, 30));  // 调整宽度以匹配其他字段
        addressPanel.add(new JLabel("Other Addresses:"));
        addressPanel.add(addressComboBox);

        // 添加地址面板到用户信息面板
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        userInfoPanel.add(addressPanel, gbc);

        // 我的订单按钮
        JButton ordersButton = new JButton("我的订单");
        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrdersWindow();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        userInfoPanel.add(ordersButton, gbc);

        // 支付记录按钮
        JButton paymentsButton = new JButton("支付记录");
        paymentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPaymentsWindow();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 6;
        userInfoPanel.add(paymentsButton, gbc);

        add(userInfoPanel, BorderLayout.CENTER);
    }

    private void addLabeledField(JPanel panel, String label, String value, GridBagConstraints gbc, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(51, 51, 51));
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(lbl, gbc);

        JTextField textField = new JTextField(value);
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(new Color(51, 51, 51));
        textField.setBackground(new Color(240, 240, 240));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setPreferredSize(new Dimension(300, 30));  // 调整宽度以匹配其他字段
        gbc.gridx = x + 1;
        panel.add(textField, gbc);
    }

    private UserAddress findDefaultAddress(List<UserAddress> addresses) {
        for (UserAddress address : addresses) {
            if (address.isDefault()) {
                return address;
            }
        }
        return null;
    }

    private void showOrdersWindow() {
        JFrame ordersFrame = new JFrame("我的订单");
        ordersFrame.setSize(600, 400);
        ordersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ordersFrame.setLocationRelativeTo(null);

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            List<Order> orders = dbConnection.getOrdersByUserId(user.getUserId());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Order order : orders) {
                listModel.addElement(order.toString());
            }

            JList<String> orderList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(orderList);
            ordersFrame.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "无法加载订单信息: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        ordersFrame.setVisible(true);
    }

    private void showPaymentsWindow() {
        JFrame paymentsFrame = new JFrame("支付记录");
        paymentsFrame.setSize(600, 400);
        paymentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        paymentsFrame.setLocationRelativeTo(null);

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            List<Payment> payments = dbConnection.getPaymentsByUserId(user.getUserId());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Payment payment : payments) {
                listModel.addElement(payment.toString());
            }

            JList<String> paymentList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(paymentList);
            paymentsFrame.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "无法加载支付记录: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        paymentsFrame.setVisible(true);
    }
}
