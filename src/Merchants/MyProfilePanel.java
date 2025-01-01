package Merchants;

import User.DatabaseConnection;

import User.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyProfilePanel extends JPanel {
    private User currentUser;
    private DatabaseConnection dbConnection;

    public MyProfilePanel(User user) {
        this.currentUser = user;
        this.dbConnection = new DatabaseConnection();
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        try {
            MerchantInfo merchantInfo = dbConnection.getMerchantInfoByUserID(currentUser.getUserId());

            if (merchantInfo != null) {
                // Create a panel for the form
                JPanel formPanel = new JPanel();
                formPanel.setLayout(new GridLayout(7, 2));

                Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
                Border titledBorder = BorderFactory.createTitledBorder("商家信息");
                formPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, padding));

                // Add labels and text fields to the form panel
                addLabeledField(formPanel, "用户名:", merchantInfo.getUsername());
                addLabeledField(formPanel, "角色:", merchantInfo.getRole());
                addLabeledField(formPanel, "电子邮件:", merchantInfo.getEmail());
                addLabeledField(formPanel, "电话号码:", merchantInfo.getPhoneNumber());
                addLabeledField(formPanel, "用户注册日期:", merchantInfo.getUserRegistrationDate().toString());
                addLabeledField(formPanel, "商户名称:", merchantInfo.getMerchantName());
                addLabeledField(formPanel, "联系电子邮件:", merchantInfo.getContactEmail());
                addLabeledField(formPanel, "联系电话:", merchantInfo.getContactPhone());
                addLabeledField(formPanel, "地址:", merchantInfo.getAddress());
                addLabeledField(formPanel, "评分:", String.valueOf(merchantInfo.getRating()));
                addLabeledField(formPanel, "商户注册日期:", merchantInfo.getMerchantRegistrationDate().toString());

                // Add the form panel to a scroll pane in case there are many fields
                JScrollPane scrollPane = new JScrollPane(formPanel);
                add(scrollPane, BorderLayout.CENTER);
            } else {
                JLabel label = new JLabel("未找到商家信息", SwingConstants.CENTER);
                add(label, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            JLabel label = new JLabel("加载商家信息时出错: " + e.getMessage(), SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
            e.printStackTrace();
        }
    }

    private void addLabeledField(JPanel panel, String labelText, String fieldValue) {
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        JTextField textField = new JTextField(fieldValue);
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(label);
        panel.add(textField);
    }
}



