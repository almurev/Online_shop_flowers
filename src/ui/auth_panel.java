package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import main.main_app; 
import services.user_service;

public class auth_panel extends JPanel { // Теперь наследуемся от JPanel
    
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
    private JLabel passwordRegLabel;
    private JLabel passwordMatchLabel;       // Метка совпадения паролей
    
    // ========== ИНИЦИАЛИЗАЦИЯ МЕТОДОВ ПРОВЕРКИ ==========
    // Создаем экземпляр класса для доступа к методам
    private user_service userService = new user_service(); 
    
    // Ссылка на главное приложение
    private main_app mainApp;
    
    public auth_panel(main_app app) {
        this.mainApp = app;
        createAndShowAuthGUI();
    }

    // =========================================================
    // UI: ФОНОВАЯ ПАНЕЛЬ
    // =========================================================
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                java.io.File imageFile = new java.io.File(imagePath);

                if (imageFile.exists()) {
                    backgroundImage = new ImageIcon(imagePath).getImage();
                    System.out.println("✓ Фото успешно загружено!");
                } else {
                    System.err.println("✗ ОШИБКА: Файл не найден!");
                }
            } catch (Exception e) {
                System.err.println("✗ ОШИБКА при загрузке фото: " + e.getMessage());
            }

            setLayout(new BorderLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g.create();

                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // =========================================================
    // UI: АВТОРИЗАЦИЯ
    // =========================================================
    public void createAndShowAuthGUI() {

        setLayout(new BorderLayout());

        String photoPath = "photo\\auth.png";

        BackgroundPanel mainPanel = new BackgroundPanel(photoPath);

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldsPanel.setOpaque(false);

        loginField = new JTextField();
        setPlaceholder(loginField, "Введите логин");

        passwordField = new JPasswordField();
        setPlaceholder(passwordField, "Введите пароль");
        
        

        showPasswordCheckBox = new JCheckBox("Показать пароль");
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        JLabel loginLabel = new JLabel("Логин:");
        JLabel passwordLabel = new JLabel("Пароль:");

        fieldsPanel.add(loginLabel);
        fieldsPanel.add(loginField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setOpaque(false);

        JButton loginButton = createRoundedButton("Войти", new Color(255, 255, 255, 220));
        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            
            // Проверка авторизации
            boolean success = userService.login(login, password);
            if (success) {
            	JOptionPane.showMessageDialog(this, "Успешный вход");
            } else {
            	JOptionPane.showMessageDialog(this, "Неверный логин или пароль");
            }
        });

        JButton registerLink = createRoundedButton("Регистрация", new Color(255, 255, 255, 220));
        registerLink.addActionListener(e -> {
            removeAll();
            add(createRegPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        buttonPanel.add(showPasswordCheckBox);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerLink);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        container.add(fieldsPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(container, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // =========================================================
    // UI: РЕГИСТРАЦИЯ (ЧИСТАЯ ВЕРСИЯ)
    // =========================================================
    private JPanel createRegPanel() {

        JPanel regPanel = new JPanel(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel("photo\\reg1.png");

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // ЛОГИН
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Логин:"), gbc);

        gbc.gridx = 1;
        regLoginField = new JTextField();
        setPlaceholder(regLoginField, "Введите логин");
        fieldsPanel.add(regLoginField, gbc);

        loginAvailabilityLabel = new JLabel();
        gbc.gridy = 1;
        fieldsPanel.add(loginAvailabilityLabel, gbc);

        regLoginField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String reg_login = regLoginField.getText();
                
                // Проверка ввода логина
                boolean success_reg_login = userService.checkUsername(reg_login);
                if (reg_login.isEmpty()){
                	loginAvailabilityLabel.setText("");
                } else if (success_reg_login) {
                	loginAvailabilityLabel.setText("Логин доступен");
                } else {
                	loginAvailabilityLabel.setText("Логин занят или некорректный");
                }
            }
        });

        // EMAIL
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        regEmailField = new JTextField();
        setPlaceholder(regEmailField, "example@mail.com");
        fieldsPanel.add(regEmailField, gbc);

        emailAvailabilityLabel = new JLabel();
        gbc.gridy = 3;
        fieldsPanel.add(emailAvailabilityLabel, gbc);

        regEmailField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	String reg_email = regEmailField.getText();
            	
            	boolean success_reg_email = userService.checkEmail(reg_email);
            	if (reg_email.isEmpty()){
            		emailAvailabilityLabel.setText("");
                } else if (success_reg_email) {
                	emailAvailabilityLabel.setText("Email корректный");
                } else {
                	emailAvailabilityLabel.setText("Email введен некорректно");
                }
            }
        });

        // PHONE
        gbc.gridx = 0;
        gbc.gridy = 4;
        fieldsPanel.add(new JLabel("Телефон:"), gbc);

        gbc.gridx = 1;
        regPhoneField = new JTextField();
        setPlaceholder(regPhoneField, "+7 (999) 123-45-67");
        fieldsPanel.add(regPhoneField, gbc);

        phoneAvailabilityLabel = new JLabel();
        gbc.gridy = 5;
        fieldsPanel.add(phoneAvailabilityLabel, gbc);

        regPhoneField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	String reg_phone = regPhoneField.getText();
    	
            	boolean success_reg_phone = userService.checkPhone(reg_phone);
            	
            	if (reg_phone.isEmpty()){
            		phoneAvailabilityLabel.setText("");
                } else if (success_reg_phone) {
                	phoneAvailabilityLabel.setText("Телефон корректный");
                } else {
                	phoneAvailabilityLabel.setText("Телефон введен некорректно");
                } 
            }
        });

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy = 6;
        fieldsPanel.add(new JLabel("Пароль:"), gbc);

        gbc.gridx = 1;
        regPasswordField = new JPasswordField();
        setPlaceholder(regPasswordField, "Введите пароль");
        fieldsPanel.add(regPasswordField, gbc);
        passwordRegLabel = new JLabel();
        
        gbc.gridy = 7;
        fieldsPanel.add(passwordRegLabel, gbc);

        // CONFIRM
        gbc.gridx = 0;
        gbc.gridy = 8;
        fieldsPanel.add(new JLabel("Повтор:"), gbc);

        gbc.gridx = 1;
        regConfirmPasswordField = new JPasswordField();
        setPlaceholder(regConfirmPasswordField, "Повторите пароль");
        fieldsPanel.add(regConfirmPasswordField, gbc);
        
        gbc.gridy = 9;
        passwordMatchLabel = new JLabel();
        fieldsPanel.add(passwordMatchLabel, gbc);

        KeyAdapter ka = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	String reg_password = new String(regPasswordField.getPassword());
            	String reg_conf_password = new String(regConfirmPasswordField.getPassword());
            	
            	boolean success_reg_password = userService.check_password(reg_password);
            	boolean success_reg_conf_password = userService.checkPasswordAndDoublePassword(reg_password, reg_conf_password);
            	
            	if (reg_password.isEmpty()){
            		passwordRegLabel.setText("");
                } else if (!success_reg_password) {
                	passwordRegLabel.setText("Пароль не соответствует требованиям");
                } else {
                	passwordRegLabel.setText("Пароль подходит");
                }
            	
            	if (reg_conf_password.isEmpty()) {
            		passwordMatchLabel.setText("");
            	} else if (!success_reg_conf_password) {
            		passwordMatchLabel.setText("Пароли не совпадают");
            	} else {
            		passwordMatchLabel.setText("Пароли совпадают");
            	}
            }
        };

        regPasswordField.addKeyListener(ka);
        regConfirmPasswordField.addKeyListener(ka);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);

        JButton register = createRoundedButton("Зарегистрироваться", new Color(255, 255, 255, 220));
        register.addActionListener(e -> {
            
        });

        JButton back = createRoundedButton("Назад", new Color(255, 255, 255, 220));
        back.addActionListener(e -> {
            removeAll();
            createAndShowAuthGUI();
            revalidate();
            repaint();
        });

        buttons.add(back);
        buttons.add(register);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        regPanel.add(mainPanel, BorderLayout.CENTER);
        return regPanel;
    }

    // =========================================================
    // UI UTILS
    // =========================================================
    private void setPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
    }

    private void setPlaceholder(JPasswordField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
    }

    private JButton createRoundedButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        return button;
    }
}