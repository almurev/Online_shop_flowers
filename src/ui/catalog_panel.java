package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.main_app;
import models.product;
import services.product_service;
import services.cart_service;

public class catalog_panel extends JPanel {

    private main_app mainApp;
    private product_service productService = new product_service();
    private List<product> allProducts = new ArrayList<>();
    private List<product> filteredProducts = new ArrayList<>();
    
    private cart_service cartService;
    
    // UI компоненты
    private JPanel cardPanel;
    private JTextField searchField;
    private JTextField priceFromField;
    private JTextField priceToField;
    private JCheckBox cbFlowers;
    private JCheckBox cbBouquets;
    private JCheckBox cbPostcards;
    private JCheckBox cbToys;
    
    public catalog_panel(main_app app, cart_service cartService) {
        this.mainApp = app;
        this.cartService = cartService;
        setLayout(new BorderLayout());
        initUI();
        loadProducts();
    }
    
    private void initUI() {
        // Главная панель с фоном
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 248, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ========== ВЕРХНЯЯ ПАНЕЛЬ ==========
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // ========== ЦЕНТРАЛЬНАЯ ЧАСТЬ ==========
        JPanel centerArea = new JPanel(new BorderLayout(15, 10));
        centerArea.setOpaque(false);
        
        // Поисковая строка
        JPanel searchPanel = createSearchPanel();
        centerArea.add(searchPanel, BorderLayout.NORTH);
        
        // Левая панель с фильтрами
        JPanel filterPanel = createFilterPanel();
        centerArea.add(filterPanel, BorderLayout.WEST);
        
        // Правая панель с карточками товаров
        JPanel rightPanel = createRightPanel();
        centerArea.add(rightPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerArea, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        // Название магазина
        JLabel shopTitle = new JLabel("Магазин цветов");
        shopTitle.setFont(new Font("Arial", Font.BOLD, 24));
        shopTitle.setForeground(new Color(200, 100, 120));
        topPanel.add(shopTitle, BorderLayout.WEST);
        
        // Кнопки справа
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightButtons.setOpaque(false);
        
        JButton cartButton = new JButton("Корзина");
        cartButton.setBackground(new Color(255, 240, 235));
        cartButton.setFocusPainted(false);
        cartButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cartButton.addActionListener(e -> mainApp.showPanel("Корзина"));
        
        JButton profileButton = new JButton("Профиль");
        profileButton.setBackground(new Color(255, 240, 235));
        profileButton.setFocusPainted(false);
        profileButton.setFont(new Font("Arial", Font.PLAIN, 14));
        profileButton.addActionListener(e -> mainApp.showPanel("Профиль"));
        
        rightButtons.add(cartButton);
        rightButtons.add(profileButton);
        topPanel.add(rightButtons, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton searchButton = new JButton("Найти");
        searchButton.setBackground(new Color(255, 220, 210));
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> applyFilters());
        
        // Поиск при нажатии Enter
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    applyFilters();
                }
            }
        });
        
        searchPanel.add(new JLabel("Поиск: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }
    
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(255, 248, 245));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        filterPanel.setPreferredSize(new Dimension(220, 0));
        
        // Заголовок
        JLabel filterTitle = new JLabel("Фильтры");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 18));
        filterTitle.setForeground(new Color(180, 90, 110));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Категории
        JLabel categoryLabel = new JLabel("Категории");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(new Color(150, 80, 100));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(categoryLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        cbFlowers = new JCheckBox("Цветы поштучно");
        cbBouquets = new JCheckBox("Букеты");
        cbPostcards = new JCheckBox("Открытки");
        cbToys = new JCheckBox("Мягкие игрушки");
        
        cbFlowers.setOpaque(false);
        cbBouquets.setOpaque(false);
        cbPostcards.setOpaque(false);
        cbToys.setOpaque(false);
        cbFlowers.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbBouquets.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbPostcards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbToys.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Добавляем слушатели для обновления фильтров
        cbFlowers.addActionListener(e -> applyFilters());
        cbBouquets.addActionListener(e -> applyFilters());
        cbPostcards.addActionListener(e -> applyFilters());
        cbToys.addActionListener(e -> applyFilters());
        
        filterPanel.add(cbFlowers);
        filterPanel.add(cbBouquets);
        filterPanel.add(cbPostcards);
        filterPanel.add(cbToys);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Фильтр по цене
        JLabel priceLabel = new JLabel("Цена (руб.)");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(150, 80, 100));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(priceLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPanel pricePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pricePanel.setOpaque(false);
        pricePanel.add(new JLabel("от:"));
        priceFromField = new JTextField(6);
        pricePanel.add(priceFromField);
        pricePanel.add(new JLabel("до:"));
        priceToField = new JTextField(6);
        pricePanel.add(priceToField);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(pricePanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Кнопка применения цены
        JButton applyPriceButton = new JButton("Применить цену");
        applyPriceButton.setBackground(new Color(255, 220, 210));
        applyPriceButton.setFocusPainted(false);
        applyPriceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyPriceButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyPriceButton);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Кнопка сброса фильтров
        JButton resetFiltersButton = new JButton("Сбросить фильтры");
        resetFiltersButton.setBackground(new Color(255, 220, 210));
        resetFiltersButton.setFocusPainted(false);
        resetFiltersButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetFiltersButton.addActionListener(e -> resetFilters());
        filterPanel.add(resetFiltersButton);
        
        filterPanel.add(Box.createVerticalGlue());
        
        return filterPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        
        // Панель для карточек
        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(255, 248, 245));
        
        // Скролл панель
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(255, 248, 245));
        scrollPane.getViewport().setBackground(new Color(255, 248, 245));
        
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    private void loadProducts() {
        allProducts = productService.getAllProducts();
        filteredProducts = new ArrayList<>(allProducts);
        displayProducts();
    }
    
    private void applyFilters() {
        filteredProducts = new ArrayList<>(allProducts);
        
        // Фильтр по поиску
        String searchText = searchField.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        }
        
        // Фильтр по категориям
        List<Integer> selectedCategories = new ArrayList<>();
        if (cbFlowers.isSelected()) selectedCategories.add(1);
        if (cbBouquets.isSelected()) selectedCategories.add(2);
        if (cbPostcards.isSelected()) selectedCategories.add(3);
        if (cbToys.isSelected()) selectedCategories.add(4);
        
        if (!selectedCategories.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                .filter(p -> selectedCategories.contains(p.getCategoryId()))
                .collect(Collectors.toList());
        }
        
        // Фильтр по цене
        String fromText = priceFromField.getText().trim();
        String toText = priceToField.getText().trim();
        
        if (!fromText.isEmpty()) {
            try {
                double from = Double.parseDouble(fromText);
                filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getPrice() >= from)
                    .collect(Collectors.toList());
            } catch (NumberFormatException e) {}
        }
        
        if (!toText.isEmpty()) {
            try {
                double to = Double.parseDouble(toText);
                filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getPrice() <= to)
                    .collect(Collectors.toList());
            } catch (NumberFormatException e) {}
        }
        
        displayProducts();
    }
    
    private void resetFilters() {
        searchField.setText("");
        priceFromField.setText("");
        priceToField.setText("");
        cbFlowers.setSelected(false);
        cbBouquets.setSelected(false);
        cbPostcards.setSelected(false);
        cbToys.setSelected(false);
        
        filteredProducts = new ArrayList<>(allProducts);
        displayProducts();
    }
    
    private void displayProducts() {
        cardPanel.removeAll();
        
        if (filteredProducts.isEmpty()) {
            JLabel emptyLabel = new JLabel("Товары не найдены");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(emptyLabel);
        } else {
            for (product p : filteredProducts) {
                addProductCard(p);
            }
        }
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void addProductCard(product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(600, 130));
        card.setPreferredSize(new Dimension(600, 130));
        
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(100, 100));
        
        // Пробуем загрузить картинку
        ImageIcon productImage = product.getScaledImage(90, 90);
        JLabel imageLabel;
        
        if (productImage != null) {
            imageLabel = new JLabel(productImage);
        } else {
            // Картинки нет - показываем заглушку
            imageLabel = new JLabel("🖼️");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(90, 90));
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(230, 200, 200), 1));
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nameLabel.setForeground(new Color(80, 50, 60));
        
        String desc = product.getDescription() != null ? product.getDescription() : "";
        String shortDesc = desc.length() > 80 ? desc.substring(0, 77) + "..." : desc;
        JLabel descLabel = new JLabel("<html><body style='width:280px'>" + shortDesc + "</body></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        
        JLabel priceLabel = new JLabel(String.format("Цена: %.2f руб.", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(200, 80, 100));
        
        String stockText = product.getCountInStock() > 0 
            ? "В наличии: " + product.getCountInStock() + " шт." 
            : "Нет в наличии";
        JLabel stockLabel = new JLabel(stockText);
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        stockLabel.setForeground(product.getCountInStock() > 0 ? new Color(100, 150, 100) : Color.RED);
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(descLabel, BorderLayout.CENTER);
        
        JPanel bottomInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomInfo.setOpaque(false);
        bottomInfo.add(priceLabel);
        bottomInfo.add(stockLabel);
        infoPanel.add(bottomInfo, BorderLayout.SOUTH);
        
        JButton addToCartButton = new JButton("В корзину");
        addToCartButton.setBackground(new Color(255, 220, 210));
        addToCartButton.setFocusPainted(false);
        addToCartButton.setPreferredSize(new Dimension(100, 35));
        addToCartButton.setEnabled(product.getCountInStock() > 0);

        addToCartButton.addActionListener(e -> {
            // Спрашиваем количество
            String quantityStr = JOptionPane.showInputDialog(
                this,
                "Введите количество:",
                "Добавление в корзину",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (quantityStr == null) return; // Пользователь отменил
            
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Количество должно быть больше 0!");
                    return;
                }
                
                if (cartService.addToCart(product.getId(), quantity)) {
                    JOptionPane.showMessageDialog(this, 
                        "Товар добавлен в корзину!", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Недостаточно товара на складе или товар не найден!", 
                        "Ошибка", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите корректное число!", 
                    "Ошибка", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Собираем карточку
        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addToCartButton, BorderLayout.EAST);
        
        cardPanel.add(card);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    // Метод для обновления каталога (вызывать после добавления нового товара)
    public void refreshCatalog() {
        loadProducts();
    }
}