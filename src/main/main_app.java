package main;

import services.cart_service;
import ui.auth_panel;
import ui.basket_panel;
import ui.catalog_panel;
import ui.profile_panel;

import javax.swing.*;
import java.awt.*;

public class main_app {
    private JFrame frame;
    private JPanel panels;
    private CardLayout card_layout;
    private String currentUsername;
    private profile_panel profilePanel;
    private catalog_panel catalogPanel;
    private basket_panel basketPanel;
    private cart_service cartService = new cart_service(); // Добавьте это

    private void create_frame() {
        frame = new JFrame("Онлайн-магазин цветов");
        frame.setSize(700, 600);
        frame.setMinimumSize(new Dimension(450, 450));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        card_layout = new CardLayout();
        panels = new JPanel(card_layout);

        // Создаем панели с передачей cartService
        profilePanel = new profile_panel(this);
        catalogPanel = new catalog_panel(this, cartService); // ← Передаем cartService
        basketPanel = new basket_panel(this, cartService);   // ← Передаем cartService

        // Добавляем панели в CardLayout
        panels.add(catalogPanel, "Каталог");
        panels.add(profilePanel, "Профиль");
        panels.add(basketPanel, "Корзина");

        // Панель авторизации - стартовая
        auth_panel AuthPanel = new auth_panel(this);
        panels.add(AuthPanel, "Авторизация");

        frame.add(panels, BorderLayout.CENTER);
        showPanel("Авторизация");
        frame.setVisible(true);
    }

    public void showPanel(String name) {
        if (name.equals("Профиль")) {
            profilePanel.loadUser(currentUsername);
        } else if (name.equals("Каталог")) {
            catalogPanel.refreshCatalog();
        } else if (name.equals("Корзина")) {
            basketPanel.refreshBasket();
        }
        card_layout.show(panels, name);
    }

    // Добавьте этот метод, если он нужен в basket_panel
    public cart_service getBasketService() {
        return cartService;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public static void main(String[] args) {
        main_app app = new main_app();
        app.create_frame();
    }
}