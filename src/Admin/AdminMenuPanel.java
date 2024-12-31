package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenuPanel extends JPanel {
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
                JOptionPane.showMessageDialog(AdminMenuPanel.this, "管理商家功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        manageProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminMenuPanel.this, "管理商品功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminMenuPanel.this, "管理用户功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
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

        // 将按钮面板添加到底部
        add(buttonPanel, BorderLayout.SOUTH);
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