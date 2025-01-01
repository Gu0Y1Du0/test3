package Merchants;

import User.Category;
import User.DatabaseConnection;
import User.User;
import User.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private DatabaseConnection dbConnection;
    private User currentUser;
    private int MarchentId;

    public ManageProductsPanel(User user) {
        currentUser = user;
        setLayout(new BorderLayout());
        dbConnection = new DatabaseConnection();
        initializeComponents();
        loadProductsFromDatabase();
    }

    private void initializeComponents() {
        // 创建顶部栏
        JPanel topPanel = createTopPanel();

        // 创建表格模型
        tableModel = new DefaultTableModel(new Object[]{"名称", "描述", "售价", "存货量", "ProductID", "分类"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 4; // ProductID 列不可编辑
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2:
                        return BigDecimal.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return String.class;
                    default:
                        return String.class;
                }
            }
        };
        productTable = new JTable(tableModel);

        // 隐藏 ProductID 列
        TableColumn productIdColumn = productTable.getColumnModel().getColumn(4);
        productIdColumn.setMinWidth(0);
        productIdColumn.setMaxWidth(0);
        productIdColumn.setWidth(0);

        // 获取产品数据并填充到表格中
        loadProductsFromDatabase();

        // 创建表格面板
        JScrollPane scrollPane = new JScrollPane(productTable);

        // 创建底部栏
        JPanel bottomPanel = createBottomPanel();

        // 添加组件到主面板
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>(new String[]{"All"});
        searchButton = new JButton("搜索");

        searchButton.addActionListener(e -> performSearch());

        topPanel.add(searchField);
        topPanel.add(categoryComboBox);
        topPanel.add(searchButton);

        populateCategoryComboBox();

        return topPanel;
    }

    private void populateCategoryComboBox() {
        try {
            List<Category> categories = dbConnection.getCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category.getCategoryName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法加载类别", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        addButton = new JButton("添加商品");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });

        updateButton = new JButton("更新商品");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton = new JButton("删除商品");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(updateButton);

        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerButtonPanel.add(deleteButton);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(addButton);

        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
        bottomPanel.add(centerButtonPanel, BorderLayout.CENTER);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择一个商品进行更新", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField((String) productTable.getValueAt(selectedRow, 0));
        JTextArea descriptionArea = new JTextArea((String) productTable.getValueAt(selectedRow, 1), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JTextField priceField = new JTextField(((BigDecimal) productTable.getValueAt(selectedRow, 2)).toString());
        JTextField stockField = new JTextField(Integer.toString((int) productTable.getValueAt(selectedRow, 3)));

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("名称:"));
        panel.add(nameField);
        panel.add(new JLabel("描述:"));
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JLabel("售价:"));
        panel.add(priceField);
        panel.add(new JLabel("存货量:"));
        panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(this, panel, "更新商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String priceStr = priceField.getText().trim();
            String stockStr = stockField.getText().trim();

            try {
                BigDecimal price = new BigDecimal(priceStr);
                int stock = Integer.parseInt(stockStr);
                tableModel.setValueAt(name, selectedRow, 0);
                tableModel.setValueAt(description, selectedRow, 1);
                tableModel.setValueAt(price, selectedRow, 2);
                tableModel.setValueAt(stock, selectedRow, 3);
                updateProductInDB(getProductIdFromTableRow(selectedRow), name, description, price, stock);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "输入格式错误，请重新输入.", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productName = (String) productTable.getValueAt(selectedRow, 0);
            int productId = getProductIdFromTableRow(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除商品: " + productName + "?", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteProductFromDB(productId);
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择一个商品进行删除", "警告", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadProductsFromDatabase() {
        List<Product> products = fetchProductsFromDB(currentUser.getUserId());
        tableModel.setRowCount(0); // 清空现有数据
        for (Product product : products) {
            tableModel.addRow(new Object[]{product.getProductName(), product.getDescription(), product.getPrice(), product.getStock(), product.getProductId(), product.getCategoryName()});
        }
    }

    private List<Product> fetchProductsFromDB(int userID) {
        List<Product> products = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();

            // 获取商户ID
            PreparedStatement pstmtMerchantUser = conn.prepareStatement("SELECT MerchantID FROM users WHERE UserID = ?");
            pstmtMerchantUser.setInt(1, userID);
            ResultSet rsMerchantUser = pstmtMerchantUser.executeQuery();

            if (rsMerchantUser.next()) {
                int merchantID = rsMerchantUser.getInt("MerchantID");
                this.MarchentId = merchantID;

                // 获取产品列表及其类别
                PreparedStatement pstmtProducts = conn.prepareStatement(
                    "SELECT p.ProductID, p.ProductName, p.Description, p.Price, p.Stock, c.CategoryName FROM products p JOIN categories c ON p.CategoryID = c.CategoryID WHERE p.MerchantID = ?"
                );
                pstmtProducts.setInt(1, merchantID);
                ResultSet rsProducts = pstmtProducts.executeQuery();

                while (rsProducts.next()) {
                    int productId = rsProducts.getInt("ProductID");
                    String name = rsProducts.getString("ProductName");
                    String description = rsProducts.getString("Description");
                    BigDecimal price = rsProducts.getBigDecimal("Price");
                    int stock = rsProducts.getInt("Stock");
                    String categoryName = rsProducts.getString("CategoryName");

                    products.add(new Product(productId, name, description, price, stock, categoryName));
                }

                rsProducts.close();
                pstmtProducts.close();
            }

            rsMerchantUser.close();
            pstmtMerchantUser.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private void updateProductInDB(int productId, String name, String description, BigDecimal price, int stock) {
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE products SET ProductName = ?, Description = ?, Price = ?, Stock = ? WHERE ProductID = ?"
            );

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setBigDecimal(3, price);
            pstmt.setInt(4, stock);
            pstmt.setInt(5, productId);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProductFromDB(int productId) {
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM products WHERE ProductID = ?"
            );

            pstmt.setInt(1, productId);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        String categoryText = (String) categoryComboBox.getSelectedItem();

        tableModel.setRowCount(0); // 清空现有数据
        List<Product> products = fetchProductsFromDB(currentUser.getUserId());
        for (Product product : products) {
            String name = product.getProductName().toLowerCase();
            String description = product.getDescription().toLowerCase();

            boolean matchesCategory = categoryText.equals("All") || name.contains(categoryText.toLowerCase()) || description.contains(categoryText.toLowerCase());
            boolean matchesSearch = name.contains(searchText) || description.contains(searchText);

            if (matchesCategory && matchesSearch) {
                tableModel.addRow(new Object[]{product.getProductName(), product.getDescription()});
            }
        }
    }

    private void showAddProductDialog() {
        JFrame dialogFrame = new JFrame("添加商品");
        dialogFrame.setLayout(new GridLayout(7, 2, 5, 5));
        dialogFrame.setSize(400, 300);
        dialogFrame.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("名称:");
        JTextField nameField = new JTextField();

        JLabel descriptionLabel = new JLabel("描述:");
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        JLabel priceLabel = new JLabel("价格:");
        JTextField priceField = new JTextField();

        JLabel stockLabel = new JLabel("库存:");
        JTextField stockField = new JTextField();

        JLabel categoryLabel = new JLabel("类别:");
        JComboBox<String> categoryComboBox = new JComboBox<>();

        try {
            List<Category> categories = dbConnection.getCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category.getCategoryName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialogFrame, "无法加载类别", "错误", JOptionPane.ERROR_MESSAGE);
        }

        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            double price;
            int stock;
            String category = (String) categoryComboBox.getSelectedItem();

            try {
                price = Double.parseDouble(priceField.getText().trim());
                stock = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogFrame, "请输入有效的数字!", "输入错误", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (name.isEmpty() || description.isEmpty() || category == null) {
                JOptionPane.showMessageDialog(dialogFrame, "请填写所有必填项!", "输入错误", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int categoryId = getCategoryID(category);

            if (insertProductIntoDB(name, description, price, stock, categoryId, MarchentId)) {
                // 成功插入后，立即从数据库重新加载商品列表
                loadProductsFromDatabase(); // 刷新表格

                // 显示成功消息并关闭对话框
                JOptionPane.showMessageDialog(dialogFrame, "商品添加成功!", "成功", JOptionPane.INFORMATION_MESSAGE);
                dialogFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogFrame, "商品添加失败!", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialogFrame.dispose());

        dialogFrame.add(nameLabel);
        dialogFrame.add(nameField);
        dialogFrame.add(descriptionLabel);
        dialogFrame.add(descriptionScrollPane);
        dialogFrame.add(priceLabel);
        dialogFrame.add(priceField);
        dialogFrame.add(stockLabel);
        dialogFrame.add(stockField);
        dialogFrame.add(categoryLabel);
        dialogFrame.add(categoryComboBox);
        dialogFrame.add(saveButton);
        dialogFrame.add(cancelButton);

        dialogFrame.setVisible(true);
    }

    private int getProductIdFromTableRow(int row) {
        return (int) productTable.getModel().getValueAt(row, 4);
    }

    private int getCategoryID(String categoryName) {
        int categoryId = 0;
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT CategoryID FROM categories WHERE CategoryName = ?");
            pstmt.setString(1, categoryName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                categoryId = rs.getInt("CategoryID");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryId;
    }

    private boolean insertProductIntoDB(String name, String description, double price, int stock, int categoryId, int merchantId) {
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (ProductName, Description, Price, Stock, CategoryID, MerchantID) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);
            pstmt.setInt(5, categoryId);
            pstmt.setInt(6, merchantId);

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("管理商品界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        User currentUser = new User(1, "username", "role", "passwordHash", "email@example.com", "1234567890", "2024-12-30");
        ManageProductsPanel manageProductsPanel = new ManageProductsPanel(currentUser);
        frame.getContentPane().add(manageProductsPanel);

        frame.setVisible(true);
    }
}