package User;

import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {
    public CartPanel() {
        setLayout(new BorderLayout());
        JLabel cartTitle = new JLabel("购物车", SwingConstants.CENTER);
        add(cartTitle, BorderLayout.NORTH);
        JTextArea cartItems = new JTextArea("当前购物车为空！");
        cartItems.setEditable(false);
        add(new JScrollPane(cartItems), BorderLayout.CENTER);
    }
}