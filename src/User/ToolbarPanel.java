package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public ToolbarPanel(CardLayout cardLayout, JPanel contentPanel) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        setLayout(new BorderLayout());

        // 左侧商品按钮
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton productsButton = new JButton("商品");
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Products");
            }
        });
        leftPanel.add(productsButton);
        add(leftPanel, BorderLayout.WEST);

        // 中间购物车按钮
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cartButton = new JButton("购物车");
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Cart");
            }
        });
        centerPanel.add(cartButton);
        add(centerPanel, BorderLayout.CENTER);

        // 右侧我的按钮
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton myButton = new JButton("我的");
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "My");
            }
        });
        rightPanel.add(myButton);
        add(rightPanel, BorderLayout.EAST);
    }
}