package Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminMerchantPanel extends JPanel {

    private JTable merchantsTable;
    private DefaultTableModel tableModel;

    public AdminMerchantPanel() throws SQLException {
        setLayout(new BorderLayout());
        // 顶部显示商家信息
        JLabel myTitle = new JLabel("商家信息", SwingConstants.CENTER);
        myTitle.setFont(new Font("宋体", Font.BOLD, 24));
        myTitle.setForeground(new Color(51, 51, 51));
        add(myTitle, BorderLayout.NORTH);
        initializeComponents();
    }

    private void initializeComponents() throws SQLException {
        // 商家表格的列名
        String[] columnNames = {"商家ID", "商家名称", "联系邮箱", "联系电话", "地址", "评分", "注册日期"};

        // 初始化表格模型
        tableModel = new DefaultTableModel(columnNames, 0);
        merchantsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(merchantsTable);
        //拉取商家信息
        DataBaseConnection db = new DataBaseConnection();
        List<Merchant> merchants = db.getMerchant();
        for(Object o : merchants.toArray()){
            tableModel.addRow(new Object[]{o});
        }

        // 创建按钮
        JButton addMerchantButton = new JButton("添加商家");
        JButton deleteMerchantButton = new JButton("删除商家");

        // 设置按钮事件监听
        addMerchantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 模拟弹出一个窗口添加商家
                String merchantName = JOptionPane.showInputDialog("请输入商家名称:");
                String contactEmail = JOptionPane.showInputDialog("请输入联系邮箱:");
                String contactPhone = JOptionPane.showInputDialog("请输入联系电话:");
                String address = JOptionPane.showInputDialog("请输入商家地址:");
                double Rating = Double.parseDouble(JOptionPane.showInputDialog("请输入商家评分:"));
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String RegistrationTime = now.format(customFormatter);
                Merchant newMerchant = new Merchant(merchantName,contactEmail,contactPhone,address,Rating,RegistrationTime);

                if (merchantName != null && contactEmail != null) {
                    // 数据库操作添加商家
                    try {
                        db.AddMerchant(newMerchant);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        // 在表格中添加商家的数据
                        Object[] rowData = {tableModel.getRowCount() + 1, merchantName, contactEmail, contactPhone, address, Rating, "当前时间"};
                        tableModel.addRow(rowData);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AdminMerchantPanel.this, "评分请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteMerchantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的商家
                int selectedRow = merchantsTable.getSelectedRow();
                Merchant selectedMerchant = new Merchant(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()),
                        tableModel.getValueAt(selectedRow,1).toString(),
                        tableModel.getValueAt(selectedRow,2).toString(),
                        tableModel.getValueAt(selectedRow,3).toString(),
                        tableModel.getValueAt(selectedRow,4).toString(),
                        Double.parseDouble(tableModel.getValueAt(selectedRow,5).toString()),
                        tableModel.getValueAt(selectedRow,6).toString()
                );
                if (selectedRow >= 0) {
                    int merchantID = (int) tableModel.getValueAt(selectedRow, 0);
                    // 数据库删除商家
                    try {
                        db.DeleteMerchant(selectedMerchant);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(AdminMerchantPanel.this, "商家ID " + merchantID + " 已删除", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminMerchantPanel.this, "请先选择一个商家", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 使用 FlowLayout 来将按钮居中
        buttonPanel.add(addMerchantButton);
        buttonPanel.add(deleteMerchantButton);

        // 将商家表格添加到中心区域
        add(scrollPane, BorderLayout.CENTER);  // 表格添加到中心

        // 将按钮面板添加到底部（商家信息表格下方）
        add(buttonPanel, BorderLayout.SOUTH);  // 按钮添加到底部
    }

    // 模拟从数据库加载商家数据的方法
    public void loadMerchantsData() {
        // 在实际应用中，可以从数据库中获取商家的数据，并将其加载到表格中
        // 这里只是模拟填充一些数据
        Object[][] sampleData = {
                {1, "商家A", "emailA@example.com", "123456789", "地址A", 4.5, "2024-01-01 12:00:00"},
                {2, "商家B", "emailB@example.com", "987654321", "地址B", 4.0, "2024-02-01 12:00:00"},
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("商家管理");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        AdminMerchantPanel adminMerchantPanel = null;
        try {
            adminMerchantPanel = new AdminMerchantPanel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        adminMerchantPanel.loadMerchantsData();
        frame.getContentPane().add(adminMerchantPanel);

        frame.setVisible(true);
    }
}
