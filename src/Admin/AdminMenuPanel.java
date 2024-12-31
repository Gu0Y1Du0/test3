package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminMenuPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AdminMenuPanel() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // 创建按钮
        JButton manageMerchantsButton = new JButton("管理商家");
        JButton manageProductsButton = new JButton("管理商品");
        JButton manageUsersButton = new JButton("管理用户");

        // 设置按钮的事件监听器
        manageMerchantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到商家管理面板
                cardLayout.show(cardPanel, "merchants");
            }
        });

        manageProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到商品管理面板
                cardLayout.show(cardPanel, "commodity");
            }
        });

        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到用户管理面板
                cardLayout.show(cardPanel, "user");
            }
        });

        // 创建面板来放置按钮
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 设置网格布局约束
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(manageMerchantsButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(manageProductsButton, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(manageUsersButton, gbc);

        // 创建 CardLayout 管理不同面板
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 创建商家管理面板
        AdminMerchantPanel adminMerchantPanel = null;
        try {
            adminMerchantPanel = new AdminMerchantPanel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 将商家管理面板添加到卡片布局
        cardPanel.add(adminMerchantPanel, "merchants");
        // 将 cardPanel 添加到中心
        add(cardPanel, BorderLayout.CENTER);
        // 将按钮面板添加到底部
        add(buttonPanel, BorderLayout.SOUTH);

        // 创建商品管理面板
        AdminCommdityPanel adminCommdityPanel = null;
        try{
            adminCommdityPanel = new AdminCommdityPanel();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        // 将商家管理面板添加到卡片布局
        cardPanel.add(adminCommdityPanel, "commdity");
        // 将 cardPanel 添加到中心
        add(cardPanel, BorderLayout.CENTER);
        // 将按钮面板添加到底部
        add(buttonPanel, BorderLayout.SOUTH);

        // 创建用户管理面板
        AdminUserPanel adminUserPanel = null;
        try {
            adminUserPanel = new AdminUserPanel();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        cardPanel.add(adminUserPanel, "user");
        add(cardPanel, BorderLayout.SOUTH );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("管理员菜单界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        AdminMenuPanel adminMenuPanel = new AdminMenuPanel();
        frame.getContentPane().add(adminMenuPanel);



        frame.setVisible(true);
    }
}