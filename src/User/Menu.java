package User;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private JLabel cartLabel;

    public Menu() {
        setTitle("商城购物系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // 购物车标签
        cartLabel = new JLabel("购物车: 0项");

        // 内容面板
        JPanel contentPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // 商品面板
        ProductPanel productPanel = new ProductPanel(cartLabel);
        contentPanel.add(productPanel, "Products");

        // 购物车面板
        CartPanel cartPanel = new CartPanel();
        contentPanel.add(cartPanel, "Cart");

        // 我的面板
        MyPanel myPanel = new MyPanel();
        contentPanel.add(myPanel, "My");

        // 底部工具栏
        ToolbarPanel toolbarPanel = new ToolbarPanel(cardLayout, contentPanel);
        mainPanel.add(toolbarPanel, BorderLayout.SOUTH);

        // 初始显示商品页面
        cardLayout.show(contentPanel, "Products");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu app = new Menu();
            app.setVisible(true);
        });
    }
}