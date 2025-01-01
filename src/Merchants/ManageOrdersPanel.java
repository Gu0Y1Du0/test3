package Merchants;

import User.DatabaseConnection;

import User.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ManageOrdersPanel extends JPanel {
    private User currentUser;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton processButton;

    public ManageOrdersPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        initializeComponents();
        loadOrders();
    }

    private void initializeComponents() {
        JLabel titleLabel = new JLabel("管理订单", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 表格模型
        String[] columnNames = {"商户名称", "联系邮箱", "联系电话", "地址", "评分", "注册日期", "产品名称", "总金额", "下单日期", "状态", "数量", "MerchantID", "OrderID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        ordersTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        ordersTable.setRowHeight(30);

        // 隐藏最后两列 (MerchantID 和 OrderID)
        ordersTable.getColumnModel().getColumn(11).setMinWidth(0);
        ordersTable.getColumnModel().getColumn(11).setMaxWidth(0);
        ordersTable.getColumnModel().getColumn(11).setWidth(0);

        ordersTable.getColumnModel().getColumn(12).setMinWidth(0);
        ordersTable.getColumnModel().getColumn(12).setMaxWidth(0);
        ordersTable.getColumnModel().getColumn(12).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);

        // 删除按钮
//        deleteButton = new JButton("删除订单");
//        deleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteSelectedOrder();
//            }
//        });

        // 处理订单按钮
        processButton = new JButton("处理订单");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedOrder();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(processButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Integer merchantID = dbConnection.getMerchantIDByUserID(currentUser.getUserId());

            if (merchantID == null) {
                JOptionPane.showMessageDialog(this, "未找到与用户关联的商户ID", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println(merchantID);
            List<OrderWithDetails> orders = dbConnection.getOrdersByMerchantID(merchantID);

            tableModel.setRowCount(0); // 清空表格

            for (OrderWithDetails order : orders) {
                Object[] row = {
                    order.getMerchantName(),
                    order.getContactEmail(),
                    order.getContactPhone(),
                    order.getAddress(),
                    order.getRating(),
                    order.getRegistrationDate(),
                    order.getProductName(),
                    order.getTotalAmount(),
                    order.getOrderDate(),
                    order.getStatus(),
                    order.getQuantity(),
                    order.getMerchantID(),
                    order.getOrderID()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println("调试: 加载订单内容时出错: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法加载订单内容: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的订单", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 12); // OrderID 列是第13列（索引为12）

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            boolean isDeleted = dbConnection.deleteOrderById(orderId);

            if (isDeleted) {
                JOptionPane.showMessageDialog(this, "订单删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrders(); // 刷新订单列表
            } else {
                JOptionPane.showMessageDialog(this, "订单删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("调试: 删除订单时出错: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法删除订单: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要处理的订单", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 12); // OrderID 列是第13列（索引为12）

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            boolean isUpdated = dbConnection.updateOrderStatusToCompleted(orderId);

            if (isUpdated) {
                JOptionPane.showMessageDialog(this, "订单状态已更新为完成", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrders(); // 刷新订单列表
            } else {
                JOptionPane.showMessageDialog(this, "订单状态更新失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("调试: 更新订单状态时出错: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "无法更新订单状态: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // 示例用户数据
        User user = new User(1, "商家A", "merchant", "passwordHash", "merchant@example.com", "1234567890", "2024-12-30");

        JFrame frame = new JFrame("管理订单界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        ManageOrdersPanel manageOrdersPanel = new ManageOrdersPanel(user);
        frame.getContentPane().add(manageOrdersPanel);

        frame.setVisible(true);
    }
}