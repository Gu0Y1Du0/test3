package Merchants;

import User.User;

import javax.swing.*;
import java.awt.*;

public class MerchantMenuPanel extends JPanel {
    private JPanel contentPanel;
    private ManageProductsPanel manageProductsPanel;
    private ManageOrdersPanel manageOrdersPanel;
    private MyProfilePanel myProfilePanel;
    private User currentUser;

    public MerchantMenuPanel(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.currentUser = user;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // 创建内容面板
        contentPanel = new JPanel(new CardLayout());
        manageProductsPanel = new ManageProductsPanel(currentUser); // 确保正确实例化并传递 currentUser
        manageOrdersPanel = new ManageOrdersPanel(currentUser);     // 确保正确实例化并传递 currentUser
        myProfilePanel = new MyProfilePanel(currentUser);         // 确保正确实例化并传递 currentUser

        contentPanel.add(manageProductsPanel, "ManageProducts");
        contentPanel.add(manageOrdersPanel, "ManageOrders");
        contentPanel.add(myProfilePanel, "MyProfile");

        // 创建导航栏
        JPanel navPanel = createNavPanel();

        // 添加组件到主面板
        add(navPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton manageProductsButton = new JButton("管理商品");
        JButton manageOrdersButton = new JButton("管理订单");
        JButton myProfileButton = new JButton("我的");

        manageProductsButton.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ManageProducts"));
        manageOrdersButton.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ManageOrders"));
        myProfileButton.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "MyProfile"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        navPanel.add(manageProductsButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        navPanel.add(manageOrdersButton, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        navPanel.add(myProfileButton, gbc);

        return navPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("商家菜单界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        User currentUser = new User(1, "username", "role", "passwordHash", "email@example.com", "1234567890", "2024-12-30");
        MerchantMenuPanel merchantMenuPanel = new MerchantMenuPanel(currentUser);
        frame.getContentPane().add(merchantMenuPanel);

        frame.setVisible(true);
    }
}



