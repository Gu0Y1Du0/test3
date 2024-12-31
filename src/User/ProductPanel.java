package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductPanel extends JPanel {
    private JLabel cartLabel;
    private JTextField searchField;
    private JComboBox<Category> categoryComboBox;
    private JButton searchButton;
    private int currentPage = 0;
    private final int itemsPerPage = 6;
    private List<Product> productList = new ArrayList<>();
    private User user;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private CartPanel cartPanel;

    public ProductPanel(JLabel cartLabel, User user, CardLayout cardLayout, JPanel contentPanel, CartPanel cartPanel) {
        this.cartLabel = cartLabel;
        this.user = user;
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.cartPanel = cartPanel;
        setLayout(new BorderLayout());

        // 搜索栏和类别选择
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem(null); // 默认选项
        loadCategories(); // 加载类别
        searchPanel.add(categoryComboBox);

        searchButton = new JButton("搜索");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 商品展示区域
        JPanel productsDisplayPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productsDisplayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(productsDisplayPanel, BorderLayout.CENTER);

        // 分页控件
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("上一页");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage == 0) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "您已经在第一页！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    currentPage--;
                    updateProductsDisplay();
                }
            }
        });
        paginationPanel.add(prevButton);

        JButton nextButton = new JButton("下一页");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * itemsPerPage >= productList.size()) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "您已经在最后一页！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    currentPage++;
                    updateProductsDisplay();
                }
            }
        });
        paginationPanel.add(nextButton);
        add(paginationPanel, BorderLayout.SOUTH);

        performSearch(); // 初始化加载商品列表
    }

    private void loadCategories() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            List<Category> categories = dbConnection.getCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "无法获取类别数据: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String searchQuery = searchField.getText().trim();
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        Integer categoryId = selectedCategory != null ? selectedCategory.getCategoryId() : null;

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            productList = dbConnection.getProducts(searchQuery, categoryId);
            currentPage = 0; // 重置当前页码
            updateProductsDisplay();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "无法获取商品数据: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProductsDisplay() {
        removeAll();
        setLayout(new BorderLayout());

        // 搜索栏和类别选择
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(searchField);
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 商品展示区域
        JPanel productsDisplayPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productsDisplayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = currentPage * itemsPerPage; i < Math.min((currentPage + 1) * itemsPerPage, productList.size()); i++) {
            Product product = productList.get(i);
            JPanel productItem = createProductItem(product);
            productsDisplayPanel.add(productItem);
        }
        add(productsDisplayPanel, BorderLayout.CENTER);

        // 分页控件
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("上一页");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage == 0) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "您已经在第一页！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    currentPage--;
                    updateProductsDisplay();
                }
            }
        });
        paginationPanel.add(prevButton);

        JButton nextButton = new JButton("下一页");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * itemsPerPage >= productList.size()) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "您已经在最后一页！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    currentPage++;
                    updateProductsDisplay();
                }
            }
        });
        paginationPanel.add(nextButton);
        add(paginationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createProductItem(Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);

        JLabel priceLabel = new JLabel("￥" + product.getPrice().toString());
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(priceLabel);

        JButton addButton = new JButton("加入购物车");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart(product.getProductId(), 1);
                updateCartCount(1);
                JOptionPane.showMessageDialog(ProductPanel.this, product.getProductName() + " 已添加到购物车！", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 重新加载购物车数据
                if (cartPanel != null) {
                    cartPanel.loadCartItems(user.getUserId());
                }
            }
        });
        panel.add(addButton);

        return panel;
    }

    private void updateCartCount(int increment) {
        String currentText = cartLabel.getText();
        int itemCount = extractItemCount(currentText);
        itemCount += increment;
        cartLabel.setText("购物车: " + itemCount + "项");
    }

    private int extractItemCount(String text) {
        try {
            // 提取纯数字部分
            String numberPart = text.replaceAll("[^0-9]", "");
            if (numberPart.isEmpty()) {
                return 0; // 如果没有找到数字，则返回默认值 0
            }
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse integer from string: " + text);
            return 0; // 返回默认值 0
        }
    }

    private void addToCart(int productId, int quantity) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.addToCart(user.getUserId(), productId, quantity);
            JOptionPane.showMessageDialog(null, "商品已成功添加到购物车！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "无法添加商品到购物车: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}