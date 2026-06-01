package models;

import javax.swing.ImageIcon;
import java.awt.Image;

public class product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int categoryId;
    private String categoryName;
    private String imagePath;
    private int countInStock;
    
    // Конструктор для загрузки из БД
    public product(int id, String name, String description, double price, 
                   int categoryId, String imagePath, int countInStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
        this.countInStock = countInStock;
    }
    
    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getImagePath() { return imagePath; }
    public int getCountInStock() { return countInStock; }
    
    // Сеттеры
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setCountInStock(int countInStock) { this.countInStock = countInStock; }
    
    // Получить ImageIcon из пути к картинке
    public ImageIcon getImageIcon() {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        try {
            java.io.File imgFile = new java.io.File(imagePath);
            if (imgFile.exists()) {
                return new ImageIcon(imagePath);
            } else {
                // Попробуем найти в папке photos
                java.io.File altFile = new java.io.File("photos/" + imagePath);
                if (altFile.exists()) {
                    return new ImageIcon(altFile.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Получить масштабированное изображение для карточки
    public ImageIcon getScaledImage(int width, int height) {
        ImageIcon icon = getImageIcon();
        if (icon == null) {
            return null;
        }
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
}