package User;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private JLabel cartLabel;
    private User user;

    public Menu(User user) {
        this.user = user;
        setTitle("E-commerce Shopping System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // 购物车标签
        cartLabel = new JLabel("Cart: 0 items");

        // 内容面板
        JPanel contentPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // 购物车面板
        CartPanel cartPanel = new CartPanel(user);

        // 商品面板
        ProductPanel productPanel = new ProductPanel(cartLabel, user, cardLayout, contentPanel, cartPanel);
        contentPanel.add(productPanel, "Products");

        contentPanel.add(cartPanel, "Cart");

        // 我的面板
        MyPanel myPanel = new MyPanel(user);
        contentPanel.add(myPanel, "My");

        // 底部工具栏
        ToolbarPanel toolbarPanel = new ToolbarPanel(cardLayout, contentPanel);
        mainPanel.add(toolbarPanel, BorderLayout.SOUTH);

        // 初始显示商品页面
        cardLayout.show(contentPanel, "Products");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 假设我们已经有一个用户登录并获取了用户对象
            User user = new User(1, "username", "role", "passwordHash", "email@example.com", "1234567890", "2024-12-30");
            Menu app = new Menu(user);
            app.setVisible(true);
        });
    }
}