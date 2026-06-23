package ui;

import main.main_app;
import models.CartItem;
import models.product; // ← ДОБАВЬТЕ ЭТОТ ИМПОРТ
import services.cart_service;
import services.product_service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class cart_panel extends JPanel {
    private main_app mainApp;
    private cart_service cartService;
    private product_service productService = new product_service();
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel itemCountLabel;

    public cart_panel(main_app app, cart_service cartService) {
        this.mainApp = app;
        this.cartService = cartService;
        createCartGUI();
        updateCartDisplay();
    }

    private void createCartGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 240, 235));

        // Заголовок
        JLabel title = new JLabel("Корзина", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(80, 45, 60));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Информационная панель
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        infoPanel.setBackground(new Color(245, 240, 235));
        
        itemCountLabel = new JLabel("Товаров: 0");
        itemCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(itemCountLabel);
        
        totalLabel = new JLabel("Итого: 0 руб.");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(180, 50, 50));
        infoPanel.add(totalLabel);
        
        add(infoPanel, BorderLayout.NORTH);

        // Таблица корзины
        String[] columns = {"ID", "Название", "Цена (руб.)", "Количество", "Сумма (руб.)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cartTable.getTableHeader().setBackground(new Color(200, 170, 180));
        
        cartTable.getColumnModel().getColumn(0).setMinWidth(0);
        cartTable.getColumnModel().getColumn(0).setMaxWidth(0);

        // Обработчик изменения количества
        cartTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 3 && e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                try {
                    int newQuantity = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
                    int productId = (int) tableModel.getValueAt(row, 0);
                    if (cartService.updateQuantity(productId, newQuantity)) {
                        updateCartDisplay();
                    } else {
                        JOptionPane.showMessageDialog(this, "Недостаточно товара на складе!");
                        updateCartDisplay();
                    }
                } catch (NumberFormatException ex) {
                    updateCartDisplay();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(new Color(245, 240, 235));

        JButton removeBtn = createButton("Удалить выбранное", new Color(200, 80, 80));
        removeBtn.addActionListener(e -> removeSelectedItem());
        controlPanel.add(removeBtn);

        JButton clearBtn = createButton("Очистить корзину", new Color(200, 100, 100));
        clearBtn.addActionListener(e -> clearCart());
        controlPanel.add(clearBtn);

        JButton orderBtn = createButton("Оформить заказ", new Color(100, 180, 100));
        orderBtn.addActionListener(e -> placeOrder());
        controlPanel.add(orderBtn);

        JButton backBtn = createButton("В каталог", new Color(100, 150, 200));
        backBtn.addActionListener(e -> mainApp.showPanel("Каталог"));
        controlPanel.add(backBtn);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void updateCartDisplay() {
        tableModel.setRowCount(0);
        List<CartItem> items = cartService.getCartItems();
        
        for (CartItem item : items) {
            Object[] row = {
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getTotalPrice()
            };
            tableModel.addRow(row);
        }

        int totalItems = cartService.getTotalItems();
        double totalAmount = cartService.getTotalAmount();
        
        itemCountLabel.setText("Товаров: " + totalItems);
        totalLabel.setText("Итого: " + String.format("%.2f", totalAmount) + " руб.");
    }

    private void removeSelectedItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите товар для удаления!");
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        if (cartService.removeFromCart(productId)) {
            updateCartDisplay();
        }
    }

    private void clearCart() {
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
            updateCartDisplay();
        }
    }

    private void placeOrder() {
        if (cartService.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Корзина пуста!");
            return;
        }

        List<CartItem> items = cartService.getCartItems();
        for (CartItem item : items) {
            // ← ИСПОЛЬЗУЙТЕ models.product (с маленькой буквы)
            product prod = productService.getProductById(item.getProduct().getId());
            if (prod == null || prod.getCountInStock() < item.getQuantity()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Товара \"" + item.getProduct().getName() + "\" недостаточно на складе!"
                );
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Оформить заказ на сумму " + String.format("%.2f", cartService.getTotalAmount()) + " руб.?",
            "Подтверждение заказа",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean allUpdated = true;
            for (CartItem item : items) {
                if (!productService.decreaseStock(item.getProduct().getId(), item.getQuantity())) {
                    allUpdated = false;
                    JOptionPane.showMessageDialog(
                        this,
                        "Ошибка при обновлении склада для товара: " + item.getProduct().getName()
                    );
                    break;
                }
            }

            if (allUpdated) {
                cartService.clearCart();
                updateCartDisplay();
                JOptionPane.showMessageDialog(this, "Заказ оформлен успешно!");
                mainApp.showPanel("Каталог");
            }
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public void refreshCart() {
        updateCartDisplay();
    }
}