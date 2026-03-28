package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import main.main_app; // Импортируем main_app

public class auth_panel extends JPanel { // Теперь наследуемся от JPanel
    // ========== БАЗА ДАННЫХ (имитация) ==========
    private static Map<String, User> database = new HashMap<>();
    
    // ========== КОМПОНЕНТЫ ОКНА АВТОРИЗАЦИИ ==========
    private JTextField loginField;         // Поле для логина
    private JPasswordField passwordField;  // Поле для пароля
    private JCheckBox showPasswordCheckBox; // Чекбокс показа пароля
    
    // ========== КОМПОНЕНТЫ ОКНА РЕГИСТРАЦИИ ==========
    private JTextField regLoginField;                 // Поле логина
    private JTextField regEmailField;                 // Поле email
    private JTextField regPhoneField;                 // Поле телефона
    private JPasswordField regPasswordField;          // Поле пароля
    private JPasswordField regConfirmPasswordField;   // Поле подтверждения пароля
    
    // ========== МЕТКИ ДЛЯ ПРОВЕРКИ ==========
    private JLabel loginAvailabilityLabel;   // Метка доступности логина
    private JLabel emailAvailabilityLabel;   // Метка доступности email
    private JLabel phoneAvailabilityLabel;   // Метка доступности телефона
    private JLabel passwordMatchLabel;       // Метка совпадения паролей
    
    // Ссылка на главное приложение
    private main_app mainApp;
    
    // Конструктор, принимающий ссылку на main_app
    public auth_panel(main_app app) {
        this.mainApp = app;
        createAndShowAuthGUI();
    }
    
    /**
     * КЛАСС ДЛЯ ФОНОВОЙ ПАНЕЛИ
     * Этот класс умеет рисовать фоновое изображение
     */
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;  // Здесь будет храниться наше фото
        
        /**
         * КОНСТРУКТОР: принимает путь к фото
         * @param imagePath - полный путь к файлу с фото
         */
        public BackgroundPanel(String imagePath) {
            try {
                // 1. Создаем объект File для проверки существования файла
                java.io.File imageFile = new java.io.File(imagePath);
                
                // 2. Проверяем, существует ли файл
                if (imageFile.exists()) {
                    // 3. Загружаем изображение по пути
                    backgroundImage = new ImageIcon(imagePath).getImage();
                    System.out.println("✓ Фото успешно загружено!");
                    System.out.println("  Путь: " + imagePath);
                    System.out.println("  Размер файла: " + imageFile.length() + " байт");
                } else {
                    System.err.println("✗ ОШИБКА: Файл не найден!");
                    System.err.println("  Искали по пути: " + imagePath);
                    System.err.println("  Проверьте правильность пути и имя файла!");
                }
            } catch (Exception e) {
                System.err.println("✗ ОШИБКА при загрузке фото: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Устанавливаем менеджер компоновки (как будут располагаться элементы)
            setLayout(new BorderLayout());
            
            // Делаем панель прозрачной, чтобы был виден наш фон
            setOpaque(false);
        }
        
        /**
         * МЕТОД ОТРИСОВКИ
         * Swing вызывает этот метод каждый раз, когда нужно нарисовать панель
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);  // Сначала рисуем стандартные компоненты
            
            // Если фото загружено - рисуем его
            if (backgroundImage != null) {
                // Преобразуем Graphics в Graphics2D (более мощный инструмент)
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Включаем сглаживание для плавного масштабирования
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                     RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Рисуем фото, растягивая на всю панель
                // 0, 0 - координаты верхнего левого угла
                // getWidth(), getHeight() - размеры панели
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                
                // Освобождаем ресурсы
                g2d.dispose();
            } else {
                // Если фото не загрузилось - заливаем белым фоном
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * СОЗДАЕТ И ПОКАЗЫВАЕТ ОКНО АВТОРИЗАЦИИ
     */
    public void createAndShowAuthGUI() {
        // Устанавливаем менеджер компоновки для панели
        setLayout(new BorderLayout());
        
        // ========== 1. ВСТАВЛЯЕМ ФОТО (ВАШ ПУТЬ!) ==========
        // ⭐⭐⭐ ВАЖНО: УКАЖИТЕ ПРАВИЛЬНОЕ ИМЯ ФАЙЛА! ⭐⭐⭐
        String photoPath = "photo\\auth.png";
        
        System.out.println("Пытаюсь загрузить фото по пути: " + photoPath);
        
        // Создаем панель с фоновым изображением
        BackgroundPanel mainPanel = new BackgroundPanel(photoPath);
        // =====================================================
        
        // ========== 2. СОЗДАЕМ ПАНЕЛЬ С ПОЛЯМИ ВВОДА ==========
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldsPanel.setOpaque(false);  // ВАЖНО! Делаем прозрачной, чтобы видеть фон
        
        // ========== 3. НАСТРАИВАЕМ ПОЛЕ ЛОГИНА ==========
        loginField = new JTextField();
        loginField.setPreferredSize(new Dimension(200, 35));  // Размер: ширина 200, высота 35
        loginField.setForeground(Color.BLACK);  // Черный текст
        // Добавляем рамку (серая линия + отступы внутри)
        loginField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(loginField, "Введите логин");  // Текст-подсказка
        
        // ========== 4. НАСТРАИВАЕМ ПОЛЕ ПАРОЛЯ ==========
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(passwordField, "Введите пароль");
        
        // ========== 5. ЧЕКБОКС ДЛЯ ПОКАЗА ПАРОЛЯ ==========
        showPasswordCheckBox = new JCheckBox("Показать пароль");
        showPasswordCheckBox.setOpaque(false);  // Прозрачный фон
        showPasswordCheckBox.setForeground(Color.BLACK);
        showPasswordCheckBox.setFocusPainted(false);  // Убираем рамку фокуса
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0);  // Показываем пароль
                } else {
                    passwordField.setEchoChar('•');      // Скрываем пароль
                }
            }
        });
        
        // ========== 6. СОЗДАЕМ МЕТКИ ==========
        JLabel loginLabel = new JLabel("Логин:");
        loginLabel.setForeground(Color.BLACK);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Добавляем метки и поля на панель
        fieldsPanel.add(loginLabel);     
        fieldsPanel.add(loginField);                
        fieldsPanel.add(passwordLabel);     
        fieldsPanel.add(passwordField);
        
        // ========== 7. ПАНЕЛЬ ДЛЯ КНОПОК ==========
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setOpaque(false);  // Прозрачный фон
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // ========== 8. КНОПКА "ВОЙТИ" ==========
        JButton loginButton = createRoundedButton("Войти", new Color(255, 255, 255, 220));
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                
                // Проверяем, существует ли пользователь в базе
                if (database.containsKey(login)) {
                    User user = database.get(login);
                    if (user.getPassword().equals(password)) { 
                        JOptionPane.showMessageDialog(auth_panel.this, 
                            "Добро пожаловать, " + login + "!", 
                            "Успех", 
                            JOptionPane.INFORMATION_MESSAGE);
                        // Переключаемся на панель каталога
                        if (mainApp != null) {
                            mainApp.showPanel("Каталог");
                        }
                    } else {
                        JOptionPane.showMessageDialog(auth_panel.this, 
                            "Неверный пароль!", 
                            "Ошибка", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(auth_panel.this, 
                        "Пользователь не найден!", 
                        "Ошибка", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // ========== 9. ССЫЛКА НА РЕГИСТРАЦИЮ ==========
        JButton registerLink = createRoundedButton("Регистрация", new Color(255, 255, 255, 220));
        registerLink.setForeground(Color.BLACK);
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Курсор-рука
        registerLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Очищаем текущую панель и показываем панель регистрации
                removeAll();
                add(createRegPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
        
        // Добавляем компоненты на панель кнопок
        buttonPanel.add(showPasswordCheckBox);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerLink);
        
        // ========== 10. КОНТЕЙНЕР С ОТСТУПАМИ ==========
        JPanel containerPanel = new JPanel(new BorderLayout(10, 10));
        containerPanel.setOpaque(false);  // Прозрачный
        containerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        containerPanel.add(fieldsPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // ========== 11. ДОБАВЛЯЕМ ВСЕ НА ФОНОВУЮ ПАНЕЛЬ ==========
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        // ========== 12. ДОБАВЛЯЕМ НА ПАНЕЛЬ ==========
        add(mainPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * СОЗДАЕТ КНОПКУ С ОВАЛЬНЫМИ УГЛАМИ
     */
    private JButton createRoundedButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Включаем сглаживание для плавных краев
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                // Рисуем закругленный прямоугольник (радиус 15)
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                // Рисуем текст
                super.paintComponent(g2);
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Не рисуем рамку
            }
        };
        
        button.setText(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Эффект при наведении мыши
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245, 220));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * УСТАНАВЛИВАЕТ ТЕКСТ-ПОДСКАЗКУ (PLACEHOLDER) ДЛЯ JTextField
     */
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);  // Серый цвет для подсказки
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);  // Черный при вводе
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);  // Серый для подсказки
                }
            }
        });
    }
    
    /**
     * УСТАНАВЛИВАЕТ ТЕКСТ-ПОДСКАЗКУ ДЛЯ JPasswordField
     */
    private void setPlaceholder(JPasswordField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);  // Показываем текст подсказки
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');  // Скрываем пароль
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });
    }
    
    /**
     * Создает панель регистрации
     */
    private JPanel createRegPanel() {
        // Создаем панель регистрации
        JPanel regPanel = new JPanel(new BorderLayout());
        
        // ========== ДОБАВЛЯЕМ ФОН В ПАНЕЛЬ РЕГИСТРАЦИИ ==========
        // Путь к фото для регистрации
        String regPhotoPath = "photo\\reg1.png";
        
        System.out.println("Пытаюсь загрузить фото регистрации по пути: " + regPhotoPath);
        
        // Создаем фоновую панель
        BackgroundPanel mainPanel = new BackgroundPanel(regPhotoPath);
        // ======================================================
        
        // Используем GridBagLayout для гибкого размещения
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false); // Делаем прозрачным, чтобы видеть фон
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Размер для всех полей ввода
        Dimension fieldSize = new Dimension(250, 35);
        
        // ========== ПОЛЕ ЛОГИНА ==========
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel loginLabel = new JLabel("Логин:");
        loginLabel.setForeground(Color.BLACK); // Черный текст
        loginLabel.setOpaque(true);
        loginLabel.setBackground(new Color(255, 255, 255, 200)); // Полупрозрачный фон
        loginLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldsPanel.add(loginLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        regLoginField = new JTextField();
        regLoginField.setPreferredSize(fieldSize);
        regLoginField.setForeground(Color.BLACK); // Черный текст
        regLoginField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(regLoginField, "Введите логин");
        fieldsPanel.add(regLoginField, gbc);
        
        // Метка проверки доступности логина
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginAvailabilityLabel = new JLabel();
        loginAvailabilityLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        loginAvailabilityLabel.setOpaque(true);
        loginAvailabilityLabel.setBackground(new Color(255, 255, 255, 200));
        loginAvailabilityLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        fieldsPanel.add(loginAvailabilityLabel, gbc);
        
        regLoginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkLoginAvailability();
            }
        });
        
        // ========== ПОЛЕ EMAIL ==========
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.BLACK); // Черный текст
        emailLabel.setOpaque(true);
        emailLabel.setBackground(new Color(255, 255, 255, 200));
        emailLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldsPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        regEmailField = new JTextField();
        regEmailField.setPreferredSize(fieldSize);
        regEmailField.setForeground(Color.BLACK); // Черный текст
        regEmailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(regEmailField, "example@mail.com");
        fieldsPanel.add(regEmailField, gbc);
        
        // Метка проверки доступности email
        gbc.gridx = 1;
        gbc.gridy = 3;
        emailAvailabilityLabel = new JLabel();
        emailAvailabilityLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        emailAvailabilityLabel.setOpaque(true);
        emailAvailabilityLabel.setBackground(new Color(255, 255, 255, 200));
        emailAvailabilityLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        fieldsPanel.add(emailAvailabilityLabel, gbc);
        
        regEmailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkEmailAvailability();
            }
        });
        
        // ========== ПОЛЕ ТЕЛЕФОНА ==========
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel phoneLabel = new JLabel("Номер телефона:");
        phoneLabel.setForeground(Color.BLACK); // Черный текст
        phoneLabel.setOpaque(true);
        phoneLabel.setBackground(new Color(255, 255, 255, 200));
        phoneLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldsPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        regPhoneField = new JTextField();
        regPhoneField.setPreferredSize(fieldSize);
        regPhoneField.setForeground(Color.BLACK); // Черный текст
        regPhoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(regPhoneField, "+7 (999) 123-45-67");
        fieldsPanel.add(regPhoneField, gbc);
        
        // Метка проверки доступности телефона
        gbc.gridx = 1;
        gbc.gridy = 5;
        phoneAvailabilityLabel = new JLabel();
        phoneAvailabilityLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        phoneAvailabilityLabel.setOpaque(true);
        phoneAvailabilityLabel.setBackground(new Color(255, 255, 255, 200));
        phoneAvailabilityLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        fieldsPanel.add(phoneAvailabilityLabel, gbc);
        
        regPhoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPhoneAvailability();
            }
        });
        
        // ========== ПОЛЕ ПАРОЛЯ ==========
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setForeground(Color.BLACK); // Черный текст
        passwordLabel.setOpaque(true);
        passwordLabel.setBackground(new Color(255, 255, 255, 200));
        passwordLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldsPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        regPasswordField = new JPasswordField();
        regPasswordField.setPreferredSize(fieldSize);
        regPasswordField.setForeground(Color.BLACK); // Черный текст
        regPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(regPasswordField, "Введите пароль");
        fieldsPanel.add(regPasswordField, gbc);
        
        // ========== ПОЛЕ ПОДТВЕРЖДЕНИЯ ПАРОЛЯ ==========
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel confirmLabel = new JLabel("Подтверждение пароля:");
        confirmLabel.setForeground(Color.BLACK); // Черный текст
        confirmLabel.setOpaque(true);
        confirmLabel.setBackground(new Color(255, 255, 255, 200));
        confirmLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldsPanel.add(confirmLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        regConfirmPasswordField = new JPasswordField();
        regConfirmPasswordField.setPreferredSize(fieldSize);
        regConfirmPasswordField.setForeground(Color.BLACK); // Черный текст
        regConfirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setPlaceholder(regConfirmPasswordField, "Повторите пароль");
        fieldsPanel.add(regConfirmPasswordField, gbc);
        
        // Метка проверки совпадения паролей
        gbc.gridx = 1;
        gbc.gridy = 8;
        passwordMatchLabel = new JLabel();
        passwordMatchLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        passwordMatchLabel.setOpaque(true);
        passwordMatchLabel.setBackground(new Color(255, 255, 255, 200));
        passwordMatchLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        fieldsPanel.add(passwordMatchLabel, gbc);
        
        KeyAdapter passwordCheckAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPasswordMatch();
            }
        };
        regPasswordField.addKeyListener(passwordCheckAdapter);
        regConfirmPasswordField.addKeyListener(passwordCheckAdapter);
        
        // ========== КНОПКИ ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false); // Прозрачный фон, чтобы видеть фоновое изображение
        
        // Кнопка регистрации с овальными углами
        JButton registerButton = createRoundedButton("Зарегистрироваться", new Color(255, 255, 255, 220));
        registerButton.setForeground(Color.BLACK); // Черный текст
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        // Кнопка "Назад" с овальными углами
        JButton backButton = createRoundedButton("Назад", new Color(255, 255, 255, 220));
        backButton.setForeground(Color.BLACK); // Черный текст
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Возвращаемся к панели авторизации
                removeAll();
                createAndShowAuthGUI();
                revalidate();
                repaint();
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        regPanel.add(mainPanel, BorderLayout.CENTER);
        
        return regPanel;
    }
    
    /**
     * Проверка email
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && 
               email.indexOf("@") < email.lastIndexOf(".");
    }
    
    /**
     * Проверка доступности логина
     */
    private void checkLoginAvailability() {
        String login = regLoginField.getText();
        if (login.isEmpty() || login.equals("Введите логин")) {
            loginAvailabilityLabel.setText("");
            return;
        }
        
        if (database.containsKey(login)) {
            loginAvailabilityLabel.setText("✗ Логин уже занят");
            loginAvailabilityLabel.setForeground(Color.RED);
        } else {
            loginAvailabilityLabel.setText("✓ Логин доступен");
            loginAvailabilityLabel.setForeground(new Color(0, 150, 0));
        }
    }
    
    /**
     * Проверка доступности email
     */
    private void checkEmailAvailability() {
        String email = regEmailField.getText();
        if (email.isEmpty() || email.equals("example@mail.com")) {
            emailAvailabilityLabel.setText("");
            return;
        }
        
        boolean emailExists = false;
        for (User user : database.values()) {
            if (user.getEmail().equals(email)) {
                emailExists = true;
                break;
            }
        }
        
        if (emailExists) {
            emailAvailabilityLabel.setText("✗ Email уже используется");
            emailAvailabilityLabel.setForeground(Color.RED);
        } else if (!isValidEmail(email)) {
            emailAvailabilityLabel.setText("✗ Неверный формат email");
            emailAvailabilityLabel.setForeground(Color.RED);
        } else {
            emailAvailabilityLabel.setText("✓ Email доступен");
            emailAvailabilityLabel.setForeground(new Color(0, 150, 0));
        }
    }
    
    /**
     * Проверка доступности телефона
     */
    private void checkPhoneAvailability() {
        String phone = regPhoneField.getText();
        if (phone.isEmpty() || phone.equals("+7 (999) 123-45-67")) {
            phoneAvailabilityLabel.setText("");
            return;
        }
        
        boolean phoneExists = false;
        for (User user : database.values()) {
            if (user.getPhone().equals(phone)) {
                phoneExists = true;
                break;
            }
        }
        
        if (phoneExists) {
            phoneAvailabilityLabel.setText("✗ Телефон уже используется");
            phoneAvailabilityLabel.setForeground(Color.RED);
        } else {
            phoneAvailabilityLabel.setText("✓ Телефон доступен");
            phoneAvailabilityLabel.setForeground(new Color(0, 150, 0));
        }
    }
    
    /**
     * Проверка совпадения паролей
     */
    private void checkPasswordMatch() {
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());
        
        if (password.isEmpty() || confirmPassword.isEmpty() || 
            password.equals("Введите пароль") || confirmPassword.equals("Повторите пароль")) {
            passwordMatchLabel.setText("");
            return;
        }
        
        if (password.equals(confirmPassword)) {
            passwordMatchLabel.setText("✓ Пароли совпадают");
            passwordMatchLabel.setForeground(new Color(0, 150, 0));
        } else {
            passwordMatchLabel.setText("✗ Пароли не совпадают");
            passwordMatchLabel.setForeground(Color.RED);
        }
    }
    
    /**
     * Регистрация пользователя
     */
    private void registerUser() {
        String login = regLoginField.getText();
        String email = regEmailField.getText();
        String phone = regPhoneField.getText();
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());
        
        // Проверка заполнения полей
        if (login.isEmpty() || login.equals("Введите логин") ||
            email.isEmpty() || email.equals("example@mail.com") ||
            phone.isEmpty() || phone.equals("+7 (999) 123-45-67") ||
            password.isEmpty() || password.equals("Введите пароль") ||
            confirmPassword.isEmpty() || confirmPassword.equals("Повторите пароль")) {
            JOptionPane.showMessageDialog(auth_panel.this, "Заполните все поля!", 
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка логина
        if (database.containsKey(login)) {
            JOptionPane.showMessageDialog(auth_panel.this, "Логин уже существует!", 
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка email
        boolean emailExists = false;
        for (User user : database.values()) {
            if (user.getEmail().equals(email)) {
                emailExists = true;
                break;
            }
        }
        if (emailExists || !isValidEmail(email)) {
            JOptionPane.showMessageDialog(auth_panel.this, "Email недействителен или уже используется!", 
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка телефона
        boolean phoneExists = false;
        for (User user : database.values()) {
            if (user.getPhone().equals(phone)) {
                phoneExists = true;
                break;
            }
        }
        if (phoneExists) {
            JOptionPane.showMessageDialog(auth_panel.this, "Телефон уже используется!", 
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка пароля
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(auth_panel.this, "Пароли не совпадают!", 
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Добавляем пользователя
        User newUser = new User(login, email, password, phone);
        database.put(login, newUser);
        
        JOptionPane.showMessageDialog(auth_panel.this, "Регистрация успешна!", 
                                     "Успех", JOptionPane.INFORMATION_MESSAGE);
        
        // Возвращаемся к панели авторизации
        removeAll();
        createAndShowAuthGUI();
        revalidate();
        repaint();
    }
    
    /**
     * Внутренний класс User
     */
    class User {
        private String login;
        private String email;
        private String password;
        private String phone;
        
        public User(String login, String email, String password, String phone) {
            this.login = login;
            this.email = email;
            this.password = password;
            this.phone = phone;
        }
        
        public String getLogin() { return login; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getPhone() { return phone; }
    }
}