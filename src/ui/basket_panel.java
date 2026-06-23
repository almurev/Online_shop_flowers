package ui;

import javax.swing.*;
import java.awt.*;

import main.main_app;
import models.basket_item;
import services.service_basket;

public class basket_panel extends JPanel {

    private final main_app mainApp;

    private JPanel itemsPanel;
    private JLabel totalLabel;

    public basket_panel(main_app app) {

        this.mainApp = app;

        setLayout(new BorderLayout());

        createUI();
    }

    private void createUI() {

        // Заголовок
        JLabel title = new JLabel(
                "Корзина",
                SwingConstants.CENTER);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28));

        add(title, BorderLayout.NORTH);

        // Панель товаров
        itemsPanel = new JPanel();
        itemsPanel.setLayout(
                new BoxLayout(
                        itemsPanel,
                        BoxLayout.Y_AXIS));

        JScrollPane scrollPane =
                new JScrollPane(itemsPanel);

        add(scrollPane, BorderLayout.CENTER);

        // Нижняя панель
        JPanel bottomPanel =
                new JPanel(new BorderLayout());

        totalLabel =
                new JLabel("Итого: 0 руб.");

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18));

        bottomPanel.add(
                totalLabel,
                BorderLayout.WEST);

        JButton catalogButton =
                new JButton("Назад в каталог");

        catalogButton.addActionListener(e ->
                mainApp.showPanel("Каталог"));

        bottomPanel.add(
                catalogButton,
                BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshBasket() {

        itemsPanel.removeAll();

        service_basket basket =
                mainApp.getBasketService();

        if (basket.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel("Корзина пуста");

            emptyLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT);

            itemsPanel.add(emptyLabel);

        } else {

            for (basket_item item :
                    basket.getBasketItems()) {

                JPanel row =
                        new JPanel(
                                new FlowLayout(
                                        FlowLayout.LEFT));

                JLabel label =
                        new JLabel(
                                item.getProduct().getName()
                                        + " | "
                                        + item.getQuantity()
                                        + " шт. | "
                                        + item.getTotalPrice()
                                        + " руб.");

                row.add(label);

                itemsPanel.add(row);
            }
        }

        totalLabel.setText(
                "Итого: "
                        + basket.getTotalPrice()
                        + " руб.");

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
}