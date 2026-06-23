package ui;

import javax.swing.*;
import java.awt.*;
import main.main_app;
import models.CartItem;
import services.cart_service;
import services.order_service;
import services.user_service;

public class basket_panel extends JPanel {

    private final main_app mainApp;
    private cart_service cartService;
    private JPanel itemsPanel;
    private JLabel totalLabel;

    public basket_panel(main_app app, cart_service cartService) {
        this.mainApp = app;
        this.cartService = cartService;
        setLayout(new BorderLayout());
        createUI();
        refreshBasket();
    }

    private void createUI() {
        JLabel title = new JLabel("Корзина", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(new Color(255, 248, 245));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.getViewport().setBackground(new Color(255, 248, 245));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 248, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        totalLabel = new JLabel("Итого: 0 руб.");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(180, 50, 50));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton clearButton = new JButton("Очистить корзину");
        clearButton.setBackground(new Color(200, 100, 100));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> clearBasket());

        JButton orderButton = new JButton("Оформить заказ");
        orderButton.setBackground(new Color(100, 180, 100));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFocusPainted(false);
        orderButton.addActionListener(e -> showOrderDialog());

        JButton catalogButton = new JButton("Назад в каталог");
        catalogButton.setBackground(new Color(100, 150, 200));
        catalogButton.setForeground(Color.WHITE);
        catalogButton.setFocusPainted(false);
        catalogButton.addActionListener(e -> mainApp.showPanel("Каталог"));

        buttonPanel.add(clearButton);
        buttonPanel.add(orderButton);
        buttonPanel.add(catalogButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showOrderDialog() {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Корзина пуста!");
            return;
        }

        String currentUser = mainApp.getCurrentUsername();
        if (currentUser == null || currentUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, войдите в систему!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Оформление заказа", true);
        dialog.setSize(600, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(255, 248, 245));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 248, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Детали заказа");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(80, 45, 60));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Покупатель:"), gbc);
        gbc.gridx = 1;
        JLabel userLabel = new JLabel(currentUser);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(userLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Общая сумма:"), gbc);
        gbc.gridx = 1;
        JLabel totalAmountLabel = new JLabel(String.format("%.2f руб.", cartService.getTotalAmount()));
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(new Color(180, 50, 50));
        mainPanel.add(totalAmountLabel, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Товары:"), gbc);
        gbc.gridx = 1;
        StringBuilder itemsText = new StringBuilder("<html>");
        for (CartItem item : cartService.getCartItems()) {
            itemsText.append("• ")
                    .append(item.getProduct().getName())
                    .append(" - ")
                    .append(item.getQuantity())
                    .append(" шт. = ")
                    .append(String.format("%.2f", item.getTotalPrice()))
                    .append(" руб.<br>");
        }
        itemsText.append("</html>");
        JLabel itemsLabel = new JLabel(itemsText.toString());
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        mainPanel.add(itemsLabel, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Адрес доставки:*"), gbc);
        gbc.gridx = 1;
        JTextField addressField = new JTextField(25);
        addressField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(addressField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Телефон для связи:*"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(25);
        phoneField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(phoneField, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Способ оплаты:"), gbc);
        gbc.gridx = 1;
        String[] paymentMethods = {"Наличные", "Карта при получении", "Карта онлайн", "СБП"};
        JComboBox<String> paymentCombo = new JComboBox<>(paymentMethods);
        paymentCombo.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(paymentCombo, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Комментарий:"), gbc);
        gbc.gridx = 1;
        JTextArea commentArea = new JTextArea(3, 25);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(280, 60));
        mainPanel.add(commentScroll, gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton confirmButton = new JButton("Подтвердить заказ");
        confirmButton.setBackground(new Color(100, 180, 100));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setPreferredSize(new Dimension(170, 38));

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(200, 100, 100));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 38));

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        dialog.add(mainPanel, BorderLayout.CENTER);

        confirmButton.addActionListener(e -> {
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String paymentMethod = (String) paymentCombo.getSelectedItem();

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Введите адрес доставки!");
                return;
            }
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Введите телефон для связи!");
                return;
            }

            user_service userService = new user_service();
            int userId = userService.getUserIdByUsername(currentUser);
            if (userId == -1) {
                JOptionPane.showMessageDialog(dialog, "Ошибка: пользователь не найден!");
                return;
            }

            int paymentMethodId = getPaymentMethodId(paymentMethod);
            if (paymentMethodId == -1) {
                JOptionPane.showMessageDialog(dialog, "Ошибка: выбран неверный способ оплаты!");
                return;
            }

            order_service orderService = new order_service();
            String comment = commentArea.getText().trim();

            int confirm = JOptionPane.showConfirmDialog(
                dialog,
                "Подтвердить заказ на сумму " + String.format("%.2f", cartService.getTotalAmount()) + " руб.?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (orderService.createOrder(userId, cartService.getCartItems(), address, phone, paymentMethodId, comment)) {
                    cartService.clearCart();
                    refreshBasket();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Заказ оформлен успешно!");
                    mainApp.showPanel("Каталог");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Ошибка при оформлении заказа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private int getPaymentMethodId(String methodName) {
        switch (methodName) {
            case "Наличные": return 1;
            case "Карта при получении": return 2;
            case "Карта онлайн": return 3;
            case "СБП": return 4;
            default: return -1;
        }
    }

    public void refreshBasket() {
        itemsPanel.removeAll();

        if (cartService.isEmpty()) {
            JLabel emptyLabel = new JLabel("Корзина пуста");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemsPanel.add(emptyLabel);
        } else {
            for (CartItem item : cartService.getCartItems()) {
                JPanel row = createItemRow(item);
                itemsPanel.add(row);
                itemsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        totalLabel.setText("Итого: " + String.format("%.2f", cartService.getTotalAmount()) + " руб.");
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private JPanel createItemRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        row.setMaximumSize(new Dimension(650, 70));
        row.setPreferredSize(new Dimension(650, 70));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(item.getProduct().getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nameLabel.setForeground(new Color(80, 50, 60));
        
        JLabel detailsLabel = new JLabel(
            "Количество: " + item.getQuantity() 
            + " шт. | Сумма: " + String.format("%.2f", item.getTotalPrice()) + " руб."
        );
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsLabel.setForeground(Color.GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);
        
        row.add(infoPanel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("🗑️ Удалить");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setBackground(new Color(220, 80, 80));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Удалить \"" + item.getProduct().getName() + "\" из корзины?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                cartService.removeFromCart(item.getProduct().getId());
                refreshBasket();
            }
        });
        
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(180, 50, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(220, 80, 80));
            }
        });

        row.add(deleteButton, BorderLayout.EAST);

        return row;
    }

    private void clearBasket() {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Корзина уже пуста!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Вы уверены, что хотите очистить корзину?",
            "Подтверждение",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            cartService.clearCart();
            refreshBasket();
        }
    }
}