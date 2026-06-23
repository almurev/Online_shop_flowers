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
	private JLabel passwordRegLabel;         // Метка проверки пароля
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
	
	// UI: ФОНОВАЯ ПАНЕЛЬ
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
	
	// UI: АВТОРИЗАЦИЯ
	public void createAndShowAuthGUI() {
		removeAll();
		
		setLayout(new BorderLayout());
		
		String photoPath = "photo\\auth.jpeg";
		
		BackgroundPanel mainPanel = new BackgroundPanel(photoPath);
		
		// Панель для центрирования формы
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);
		
		// Панель с полями
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		fieldsPanel.setOpaque(false);
		fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel loginLabel = new JLabel("Логин:");
		loginLabel.setOpaque(false);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		fieldsPanel.add(loginLabel, gbc);
		
		loginField = new JTextField(18);
		setPlaceholder(loginField, "Введите логин");
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		fieldsPanel.add(loginField, gbc);
		
		JLabel passwordLabel = new JLabel("Пароль:");
		passwordLabel.setOpaque(false);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		fieldsPanel.add(passwordLabel, gbc);
		
		passwordField = new JPasswordField(18);
		setPlaceholderForPassword(passwordField, "Введите пароль");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		fieldsPanel.add(passwordField, gbc);
		
		showPasswordCheckBox = new JCheckBox("Показать пароль");
		showPasswordCheckBox.setOpaque(false);
		
		showPasswordCheckBox.addActionListener(e -> {
			if (showPasswordCheckBox.isSelected()) {
				passwordField.setEchoChar((char) 0);
			} else {
				if (passwordField.getForeground().equals(Color.GRAY)) {
					passwordField.setEchoChar((char) 0);
				} else {
					passwordField.setEchoChar('•');
				}
			}
		});
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		fieldsPanel.add(showPasswordCheckBox, gbc);
		
		// Панель кнопок авторизации
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		buttonPanel.setOpaque(false);
		
		JButton loginButton = createRoundedButton("Войти", new Color(255, 255, 255, 220));
		
		loginButton.addActionListener(e -> {
			String login = loginField.getText();
			String password = new String(passwordField.getPassword());
			
			// Проверка авторизации
			boolean success = userService.login(login, password);
			
			if (success) {
				JOptionPane.showMessageDialog(this, "Успешный вход");
				
				// Сохраняем логин текущего пользователя
				mainApp.setCurrentUsername(login);
				
				// Переключаем через метод showPanel в main_app
				mainApp.showPanel("Профиль");
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
		
		buttonPanel.add(loginButton);
		buttonPanel.add(registerLink);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		fieldsPanel.add(buttonPanel, gbc);
		
		centerPanel.add(fieldsPanel);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		add(mainPanel, BorderLayout.CENTER);
		
		revalidate();
		repaint();
	}
	
	// UI: РЕГИСТРАЦИЯ
	private JPanel createRegPanel() {
		JPanel regPanel = new JPanel(new BorderLayout());
		
		BackgroundPanel mainPanel = new BackgroundPanel("photo\\reg.jpeg");
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);
		
		JPanel fieldsPanel = new JPanel(new GridBagLayout());
		fieldsPanel.setOpaque(false);
		fieldsPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(7, 8, 7, 8);
		
		// ЛОГИН
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		fieldsPanel.add(new JLabel("Логин:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		regLoginField = new JTextField(18);
		setPlaceholder(regLoginField, "Введите логин");
		fieldsPanel.add(regLoginField, gbc);
		
		loginAvailabilityLabel = new JLabel();
		gbc.gridy = 1;
		fieldsPanel.add(loginAvailabilityLabel, gbc);
		
		regLoginField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String regLogin = regLoginField.getText();
				
				// Проверка ввода логина
				boolean successRegLogin = userService.checkUsername(regLogin);
				
				if (regLogin.isEmpty()) {
					loginAvailabilityLabel.setText("");
				} else if (successRegLogin) {
					loginAvailabilityLabel.setText("Логин доступен");
				} else {
					loginAvailabilityLabel.setText("Логин занят или некорректный");
				}
			}
		});
		
		// EMAIL
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		fieldsPanel.add(new JLabel("Email:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		regEmailField = new JTextField(18);
		setPlaceholder(regEmailField, "example@mail.com");
		fieldsPanel.add(regEmailField, gbc);
		
		emailAvailabilityLabel = new JLabel();
		gbc.gridy = 3;
		fieldsPanel.add(emailAvailabilityLabel, gbc);
		
		regEmailField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String regEmail = regEmailField.getText();
				
				boolean successRegEmail = userService.checkEmail(regEmail);
				
				if (regEmail.isEmpty()) {
					emailAvailabilityLabel.setText("");
				} else if (successRegEmail) {
					emailAvailabilityLabel.setText("Email корректный");
				} else {
					emailAvailabilityLabel.setText("Email введен некорректно");
				}
			}
		});
		
		// PHONE
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		fieldsPanel.add(new JLabel("Телефон:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		regPhoneField = new JTextField(18);
		setPlaceholder(regPhoneField, "+7 (999) 123-45-67");
		fieldsPanel.add(regPhoneField, gbc);
		
		phoneAvailabilityLabel = new JLabel();
		gbc.gridy = 5;
		fieldsPanel.add(phoneAvailabilityLabel, gbc);
		
		regPhoneField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String regPhone = regPhoneField.getText();
				
				boolean successRegPhone = userService.checkPhone(regPhone);
				
				if (regPhone.isEmpty()) {
					phoneAvailabilityLabel.setText("");
				} else if (successRegPhone) {
					phoneAvailabilityLabel.setText("Телефон корректный");
				} else {
					phoneAvailabilityLabel.setText("Телефон введен некорректно");
				}
			}
		});
		
		// PASSWORD
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0;
		fieldsPanel.add(new JLabel("Пароль:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		regPasswordField = new JPasswordField(18);
		setPlaceholderForPassword(regPasswordField, "Введите пароль");
		fieldsPanel.add(regPasswordField, gbc);
		
		passwordRegLabel = new JLabel();
		gbc.gridy = 7;
		fieldsPanel.add(passwordRegLabel, gbc);
		
		// CONFIRM
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 0;
		fieldsPanel.add(new JLabel("Повтор:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		regConfirmPasswordField = new JPasswordField(18);
		setPlaceholderForPassword(regConfirmPasswordField, "Повторите пароль");
		fieldsPanel.add(regConfirmPasswordField, gbc);
		
		passwordMatchLabel = new JLabel();
		gbc.gridy = 9;
		fieldsPanel.add(passwordMatchLabel, gbc);
		
		KeyAdapter ka = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String regPassword = new String(regPasswordField.getPassword());
				String regConfPassword = new String(regConfirmPasswordField.getPassword());
				
				boolean successRegPassword = userService.checkPassword(regPassword);
				boolean successRegConfPassword =
						userService.checkPasswordAndDoublePassword(regPassword, regConfPassword);
				
				if (regPassword.isEmpty()) {
					passwordRegLabel.setText("");
				} else if (!successRegPassword) {
					passwordRegLabel.setText("Пароль не соответствует требованиям");
				} else {
					passwordRegLabel.setText("Пароль подходит");
				}
				
				if (regConfPassword.isEmpty()) {
					passwordMatchLabel.setText("");
				} else if (!successRegConfPassword) {
					passwordMatchLabel.setText("Пароли не совпадают");
				} else {
					passwordMatchLabel.setText("Пароли совпадают");
				}
			}
		};
		
		regPasswordField.addKeyListener(ka);
		regConfirmPasswordField.addKeyListener(ka);
		
		// Кнопки
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		buttons.setOpaque(false);
		
		JButton back = createRoundedButton("Назад", new Color(255, 255, 255, 220));
		
		back.addActionListener(e -> {
			removeAll();
			createAndShowAuthGUI();
			revalidate();
			repaint();
		});
		
		JButton register = createRoundedButton("Зарегистрироваться", new Color(255, 255, 255, 220));
		
		register.addActionListener(e -> {
			String regUsername = regLoginField.getText();
			String regEmail = regEmailField.getText();
			String regPhone = regPhoneField.getText();
			String regPassword = new String(regPasswordField.getPassword());
			String regConfPassword = new String(regConfirmPasswordField.getPassword());
			
			boolean checkUsername = userService.checkUsername(regUsername);
			boolean checkEmail = userService.checkEmail(regEmail);
			boolean checkPhone = userService.checkPhone(regPhone);
			boolean checkPassword = userService.checkPassword(regPassword);
			boolean checkConfPassword =
					userService.checkPasswordAndDoublePassword(regPassword, regConfPassword);
			
			if (checkUsername && checkEmail && checkPhone && checkPassword && checkConfPassword) {
				boolean success = userService.register(regUsername, regEmail, regPhone, regPassword);
				
				if (success) {
					JOptionPane.showMessageDialog(this, "Регистрация прошла успешно");
					
					removeAll();
					createAndShowAuthGUI();
					revalidate();
					repaint();
				} else {
					JOptionPane.showMessageDialog(this, "Не удалось зарегистрировать пользователя");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Заполните все поля корректно");
			}
		});
		
		buttons.add(back);
		buttons.add(register);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 2;
		fieldsPanel.add(buttons, gbc);
		
		centerPanel.add(fieldsPanel);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		regPanel.add(mainPanel, BorderLayout.CENTER);
		
		return regPanel;
	}
	
	// UI UTILS
	
	// Для обычного текстового поля
	private void setPlaceholder(JTextField field, String text) {
		field.setText(text);
		field.setForeground(Color.GRAY);
		
		// Добавляем обработчик потери/получения фокуса
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(text)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setText(text);
					field.setForeground(Color.GRAY);
				}
			}
		});
	}
	
	// Для поля пароля
	private void setPlaceholderForPassword(JPasswordField field, String text) {
		field.setText(text);
		field.setForeground(Color.GRAY);
		field.setEchoChar((char) 0);  // Показываем placeholder как обычный текст
		
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (new String(field.getPassword()).equals(text)) {
					field.setText("");
					field.setForeground(Color.BLACK);
					field.setEchoChar('•');  // Возвращаем маскировку
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (field.getPassword().length == 0) {
					field.setText(text);
					field.setForeground(Color.GRAY);
					field.setEchoChar((char) 0);  // Показываем placeholder
				}
			}
		});
	}
	
	private JButton createRoundedButton(String text, Color bg) {
		JButton button = new JButton(text);
		button.setBackground(bg);
		button.setFocusPainted(false);
		return button;
	}
}