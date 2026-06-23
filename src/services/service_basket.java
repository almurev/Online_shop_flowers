package services;

import models.basket_item;
import models.product;

import java.util.ArrayList;
import java.util.List;

// Сервис корзины
public class service_basket {

    // Список товаров в корзине
    private final List<basket_item> basketItems =
            new ArrayList<>();

    // Добавить товар в корзину
    public void addProduct(product product) {

        // Проверяем есть ли уже товар
        for (basket_item item : basketItems) {

            if (item.getProduct().getId() ==
                    product.getId()) {

                item.setQuantity(
                        item.getQuantity() + 1);

                return;
            }
        }

        basketItems.add(
                new basket_item(product, 1));
    }

    // Удалить товар из корзины
    public void removeProduct(product product) {

        basketItems.removeIf(item ->
                item.getProduct().getId()
                        == product.getId());
    }

    // Очистить корзину
    public void clearBasket() {
        basketItems.clear();
    }

    // Получить все товары
    public List<basket_item> getBasketItems() {
        return basketItems;
    }

    // Общая стоимость корзины
    public double getTotalPrice() {

        double total = 0;

        for (basket_item item : basketItems) {

            total += item.getTotalPrice();
        }

        return total;
    }

    // Проверка пустая ли корзина
    public boolean isEmpty() {
        return basketItems.isEmpty();
    }
}