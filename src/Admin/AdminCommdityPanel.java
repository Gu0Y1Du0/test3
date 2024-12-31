package Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class AdminCommdityPanel extends JPanel {

    private JTable commdityTable;
    private DefaultTableModel tableModel;

    public AdminCommdityPanel() throws SQLException{
        setLayout(new BorderLayout());
        // 顶部显示商品管理
        JLabel myTitle = new JLabel("商品管理");
        myTitle.setFont(new Font("宋体", Font.BOLD, 24));
        myTitle.setForeground(new Color(51, 51, 51));
        add(myTitle, BorderLayout.NORTH);
        initializeComponents();
    }
    public void initializeComponents() throws SQLException{
        // 商品表格的列名
        String[] columnNames = {"商品ID","商品名称","商品描述","价格","数量","商品分类","创建时间","商家ID"};

        // 初始化表格模型
        tableModel = new DefaultTableModel(columnNames, 0);
        commdityTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(commdityTable);

        // 拉取商品信息
        DataBaseConnection db = new DataBaseConnection();
        List<Commdity> commdities = db.getCommdity();
        for(Object o : commdities.toArray()){
            tableModel.addRow(new Object[]{o});
        }

        // 创建按钮
        JButton addCommdityCategoryButton = new JButton("添加商品种类");
        JButton deleteCommdityButton = new JButton("删除商品");

        // 设置按钮事件监听
        addCommdityCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出一个窗口添加商品
                String CategoryName = JOptionPane.showInputDialog("请输入商品种类：");
                Category category = new Category(CategoryName);
                if (CategoryName != null) {
                    // 数据库操作添加商品种类
                    try {
                        db.AddCategory(category);
                        JOptionPane.showMessageDialog(AdminCommdityPanel.this, "添加商品种类成功", "提示", JOptionPane.INFORMATION_MESSAGE);

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(AdminCommdityPanel.this, "添加商品种类失败", "提示", JOptionPane.INFORMATION_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }

            }
        });


        deleteCommdityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的商品
                int selectedRow = commdityTable.getSelectedRow();
                Commdity selectedCommdity = new Commdity(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()),
                        tableModel.getValueAt(selectedRow,1).toString(),
                        tableModel.getValueAt(selectedRow,2).toString(),
                        Double.parseDouble(tableModel.getValueAt(selectedRow,3).toString()),
                        Integer.parseInt(tableModel.getValueAt(selectedRow,4).toString()),
                        Integer.parseInt(tableModel.getValueAt(selectedRow,5).toString()),
                        tableModel.getValueAt(selectedRow,6).toString(),
                        Integer.parseInt(tableModel.getValueAt(selectedRow,7).toString())
                );
                if (selectedRow >= 0) {
                    int commdityID = (int) tableModel.getValueAt(selectedRow, 0);
                    // 数据库删除商品
                    try {
                        db.DeleteCommdity(selectedCommdity);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(AdminCommdityPanel.this, "商品ID " + commdityID + " 已删除", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminCommdityPanel.this, "请先选择一个商品", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 使用 FlowLayout 来将按钮居中
        buttonPanel.add(addCommdityCategoryButton);
        buttonPanel.add(deleteCommdityButton);

        // 将商家表格添加到中心区域
        add(scrollPane, BorderLayout.CENTER);  // 表格添加到中心

        // 将按钮面板添加到底部（商家信息表格下方）
        add(buttonPanel, BorderLayout.SOUTH);  // 按钮添加到底部
    }
    public static void main(String[] args) {

    }
}
