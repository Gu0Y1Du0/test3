package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductPanel extends JPanel {
    private JLabel cartLabel;

    public ProductPanel(JLabel cartLabel) {
        this.cartLabel = cartLabel;
        setLayout(new GridLayout(3, 3, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] productNames = {"产品A", "产品B", "产品C", "产品D", "产品E", "产品F"};
        int[] productPrices = {99, 149, 199, 249, 299, 349};

        for (int i = 0; i < productNames.length; i++) {
            JPanel productItem = createProductItem(productNames[i], productPrices[i]);
            add(productItem);
        }
    }

    private JPanel createProductItem(String name, int price) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);

        JLabel priceLabel = new JLabel("￥" + price);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(priceLabel);

        JButton addButton = new JButton("加入购物车");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCartCount(1);
            }
        });
        panel.add(addButton);

        return panel;
    }

    private void updateCartCount(int increment) {
        String currentText = cartLabel.getText();
        int itemCount = Integer.parseInt(currentText.split(": ")[1].split("项")[0]);
        itemCount += increment;
        cartLabel.setText("购物车: " + itemCount + "项");
    }
}