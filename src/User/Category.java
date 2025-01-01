package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Category {
    private int categoryId;
    private String categoryName;

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    // 静态变量存储类别信息
    private static Map<String, Integer> categoryMapByName;
    private static Map<Integer, String> categoryMapById;

    // 初始化类别信息
    public static void initializeCategories(DatabaseConnection dbConnection) {
        categoryMapByName = new HashMap<>();
        categoryMapById = new HashMap<>();

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT `CategoryID`, `CategoryName` FROM `categories`");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
                categoryMapByName.put(categoryName, categoryId);
                categoryMapById.put(categoryId, categoryName);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取类别名称到类别ID的映射
    public static Map<String, Integer> getCategoryMapByName() {
        return categoryMapByName;
    }

    // 获取类别ID到类别名称的映射
    public static Map<Integer, String> getCategoryMapById() {
        return categoryMapById;
    }
}



