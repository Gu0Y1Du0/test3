package Merchants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MerchantMenuPanel extends JPanel {
    public MerchantMenuPanel() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // 创建按钮
        JButton manageProductsButton = new JButton("管理商品");
        JButton manageOrdersButton = new JButton("管理订单");
        JButton myProfileButton = new JButton("我的");

        // 设置按钮的事件监听器
        manageProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MerchantMenuPanel.this, "管理商品功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        manageOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MerchantMenuPanel.this, "管理订单功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        myProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MerchantMenuPanel.this, "我的功能正在开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        buttonPanel.add(manageProductsButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(manageOrdersButton, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(myProfileButton, gbc);

        // 将按钮面板添加到底部
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("商家菜单界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        MerchantMenuPanel merchantMenuPanel = new MerchantMenuPanel();
        frame.getContentPane().add(merchantMenuPanel);

        frame.setVisible(true);
    }
}