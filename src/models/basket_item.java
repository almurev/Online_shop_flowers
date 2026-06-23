package models;

// Товар в корзине
public class basket_item {

    private product product;
    private int quantity;

    public basket_item(product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}