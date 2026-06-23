package main;

import javax.swing.*;
import java.awt.*;

import services.service_basket;
import ui.auth_panel;
import ui.basket_panel;
import ui.catalog_panel;
import ui.profile_panel;

public class main_app {

    private JFrame frame;
    private JPanel panels;
    private CardLayout card_layout;

    // Текущий пользователь
    private String currentUsername;

    // Общая корзина для всего приложения
    private final service_basket basketService =
            new service_basket();

    // Панели
    private profile_panel profilePanel;
    private basket_panel basketPanel;

    // Создание окна
    private void create_frame() {

        frame = new JFrame("Онлайн-магазин цветов");

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        card_layout = new CardLayout();
        panels = new JPanel(card_layout);

        // Каталог
        catalog_panel catalogPanel =
                new catalog_panel(this);

        panels.add(catalogPanel, "Каталог");

        // Профиль
        profilePanel =
                new profile_panel(this);

        panels.add(profilePanel, "Профиль");

        // Корзина
        basketPanel =
                new basket_panel(this);

        panels.add(basketPanel, "Корзина");

        // Авторизация
        auth_panel authPanel =
                new auth_panel(this);

        panels.add(authPanel, "Авторизация");

        frame.add(
                panels,
                BorderLayout.CENTER);

        frame.setSize(600, 600);

        frame.setMinimumSize(
                new Dimension(550, 500));

        frame.setVisible(true);

        showPanel("Авторизация");
    }

    // Изменение размера окна
    public void setPanelSize(String panelName) {

        if (panelName.equals("Авторизация")) {

            frame.setSize(600, 600);

            frame.setMinimumSize(
                    new Dimension(550, 500));

        } else {

            frame.setSize(1000, 750);

            frame.setMinimumSize(
                    new Dimension(800, 600));
        }

        frame.setLocationRelativeTo(null);
    }

    // Переключение между панелями
    public void showPanel(String name) {

        setPanelSize(name);

        if (name.equals("Профиль")) {
            profilePanel.loadUser(currentUsername);
        }

        if (name.equals("Корзина")) {
            basketPanel.refreshBasket();
        }

        card_layout.show(panels, name);
    }

    // Выход из аккаунта
    public void logout() {

        currentUsername = null;

        showPanel("Авторизация");
    }

    // Сохранить логин
    public void setCurrentUsername(
            String username) {

        this.currentUsername = username;
    }

    // Получить логин
    public String getCurrentUsername() {

        return currentUsername;
    }

    // Получить корзину
    public service_basket getBasketService() {

        return basketService;
    }

    // Запуск приложения
    public static void main(String[] args) {

        main_app app = new main_app();

        app.create_frame();
    }
}