package User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartPanel extends JPanel {
    private JTable cartItemsTable;
    private DefaultTableModel tableModel;
    private User user;

    public CartPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        JLabel cartTitle = new JLabel("ShoppingCart", SwingConstants.CENTER);
        cartTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(cartTitle, BorderLayout.NORTH);

        // 表格模型
        String[] columnNames = {"ProductID", "ProductName", "Price", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        cartItemsTable = new JTable(tableModel);
        cartItemsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cartItemsTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(cartItemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部按钮面板
        JPanel buttonPanel = new JPanel();
        JButton payButton = new JButton("付款");
        JButton resetButton = new JButton("重置");

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetCart();
            }
        });

        buttonPanel.add(payButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadCartItems(user.getUserId());
    }

    void loadCartItems(int userID) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            ArrayList<ProductWithQuantity> cartItems = dbConnection.getCartItemsByUserID(userID);

            tableModel.setRowCount(0); // 清空表格

            if (cartItems.isEmpty()) {
                System.out.println("Debug: Cart is empty for user ID: " + userID);
                // 不在这里显示消息
            } else {
                System.out.println("Debug: Cart items found for user ID: " + userID);
                for (ProductWithQuantity item : cartItems) {
                    Object[] row = {
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Debug: Error loading cart items: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法加载购物车内容: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPayment() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            ArrayList<ProductWithQuantity> cartItems = dbConnection.getCartItemsByUserID(user.getUserId());

            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "购物车为空，无法支付！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 计算总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (ProductWithQuantity item : cartItems) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }

            int orderId = dbConnection.createOrder(user.getUserId(), totalAmount.doubleValue());

            // 将购物车中的商品添加到订单项中
            for (ProductWithQuantity item : cartItems) {
                dbConnection.addOrderItem(orderId, item.getProduct().getProductId(), item.getQuantity(), item.getProduct().getPrice().doubleValue());
            }

            // 清空购物车
            dbConnection.clearCart(user.getUserId());

            // 刷新购物车显示
            loadCartItems(user.getUserId());

            JOptionPane.showMessageDialog(this, "订单已成功创建！订单号: " + orderId, "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.out.println("Debug: Error processing payment: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法处理付款: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetCart() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.clearCart(user.getUserId());

            // 刷新购物车显示
            loadCartItems(user.getUserId());

            JOptionPane.showMessageDialog(this, "购物车已清空！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.out.println("Debug: Error resetting cart: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法清空购物车: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}