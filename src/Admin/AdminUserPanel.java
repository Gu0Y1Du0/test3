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

public class AdminUserPanel extends JPanel {

    private JTable UserTable;
    private DefaultTableModel tableModel;

    public AdminUserPanel() throws SQLException {
        setLayout(new BorderLayout());
        // 顶部显示商家信息
        JLabel myTitle = new JLabel("管理用户", SwingConstants.CENTER);
        myTitle.setFont(new Font("宋体", Font.BOLD, 24));
        myTitle.setForeground(new Color(51, 51, 51));
        add(myTitle, BorderLayout.NORTH);
        initializeComponents();
    }

    private void initializeComponents() throws SQLException {
        // 用户表格的列名
        String[] columnNames = {"用户ID", "用户名", "身份", "密码", "联系邮箱", "联系电话", "注册日期"};

        // 初始化表格模型
        tableModel = new DefaultTableModel(columnNames, 0);
        UserTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(UserTable);
        //拉取用户信息
        DataBaseConnection db = new DataBaseConnection();
        List<User> users = db.getUser();
        for(Object o : users.toArray()){
            tableModel.addRow(new Object[]{o});
        }

        // 创建按钮
        JButton addUserButton = new JButton("添加用户");
        JButton deleteUserButton = new JButton("注销用户");
        JButton updateUserPasswordHashButton = new JButton("修改密码");
        JButton updateUserRoleButton = new JButton("变更用户身份");

        // 设置按钮事件监听
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 模拟弹出一个窗口添加商家
                String username = JOptionPane.showInputDialog("请输入用户名称:");
                String userrole = JOptionPane.showInputDialog("请输入用户身份:");
                String userpassword = JOptionPane.showInputDialog("请输入用户密码:");
                String Email = JOptionPane.showInputDialog("请输入联系邮箱:");
                String phonenumber = JOptionPane.showInputDialog("请输入联系电话:");
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String RegistrationTime = now.format(customFormatter);
                User newuser = new User(username,userrole,userpassword,Email,phonenumber,RegistrationTime);
                if (username != null && Email != null) {
                    // 数据库操作添加商家
                    try {
                        db.AddUser(newuser);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        // 在表格中添加用户的数据
                        Object[] rowData = {tableModel.getRowCount() + 1, username, userrole, userpassword, Email, phonenumber, RegistrationTime};
                        tableModel.addRow(rowData);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AdminUserPanel.this, "密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的商家
                int selectedRow = UserTable.getSelectedRow();
                User selectedUser = new User(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()),
                        tableModel.getValueAt(selectedRow,1).toString(),
                        tableModel.getValueAt(selectedRow,2).toString(),
                        tableModel.getValueAt(selectedRow,3).toString(),
                        tableModel.getValueAt(selectedRow,4).toString(),
                        tableModel.getValueAt(selectedRow,5).toString(),
                        tableModel.getValueAt(selectedRow,6).toString()
                );
                if (selectedRow >= 0) {
                    int userID = (int) tableModel.getValueAt(selectedRow, 0);
                    // 数据库删除用户
                    try {
                        db.DeleteUser(selectedUser);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "用户ID " + userID + " 已删除", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "请先选择一个用户", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        updateUserPasswordHashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的商家
                int selectedRow = UserTable.getSelectedRow();
                User selectedUser = new User(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()),
                        tableModel.getValueAt(selectedRow,1).toString(),
                        tableModel.getValueAt(selectedRow,2).toString(),
                        tableModel.getValueAt(selectedRow,3).toString(),
                        tableModel.getValueAt(selectedRow,4).toString(),
                        tableModel.getValueAt(selectedRow,5).toString(),
                        tableModel.getValueAt(selectedRow,6).toString()
                );
                String newPasswordHash = JOptionPane.showInputDialog("请输入新密码:");
                if (selectedRow >= 0) {
                    int userID = (int) tableModel.getValueAt(selectedRow, 0);
                    // 数据库更改用户密码
                    try {
                        db.UpdateUserPasswordHash(selectedUser, newPasswordHash);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableModel.setValueAt(newPasswordHash, selectedRow, 3);
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "用户ID " + userID + " 密码已修改", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "请先选择一个用户", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        updateUserRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的商家
                int selectedRow = UserTable.getSelectedRow();
                User selectedUser = new User(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()),
                        tableModel.getValueAt(selectedRow,1).toString(),
                        tableModel.getValueAt(selectedRow,2).toString(),
                        tableModel.getValueAt(selectedRow,3).toString(),
                        tableModel.getValueAt(selectedRow,4).toString(),
                        tableModel.getValueAt(selectedRow,5).toString(),
                        tableModel.getValueAt(selectedRow,6).toString()
                );
                String newRole = JOptionPane.showInputDialog("请输入新身份:");
                if (selectedRow >= 0) {
                    int userID = (int) tableModel.getValueAt(selectedRow, 0);
                    // 数据库更改用户密码
                    try {
                        db.UpdateUserRole(selectedUser, newRole);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableModel.setValueAt(newRole, selectedRow, 2);
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "商家ID " + userID + " 身份已更新为" + newRole, "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminUserPanel.this, "请先选择一个商家", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 使用 FlowLayout 来将按钮居中
        buttonPanel.add(addUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(updateUserPasswordHashButton);
        buttonPanel.add(updateUserRoleButton);

        // 将商家表格添加到中心区域
        add(scrollPane, BorderLayout.CENTER);  // 表格添加到中心

        // 将按钮面板添加到底部（商家信息表格下方）
        add(buttonPanel, BorderLayout.SOUTH);  // 按钮添加到底部
    }
}
