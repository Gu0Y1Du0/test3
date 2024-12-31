package Admin;

public class Category {
    private int categoryId;
    private String categoryName;
    public Category(String categoryName){
        this.categoryName = categoryName;
    }
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
}