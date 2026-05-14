package ui;

import javax.swing.*;
import java.awt.*;

import main.main_app;
import models.user;
import services.user_service;

public class profile_panel extends JPanel {
	
	// Ссылка на главное приложение
	private main_app mainApp;
	
	// Сервис для получения пользователя из БД
	private user_service userService = new user_service();
	
	// Метки для вывода данных пользователя
	private JLabel usernameValueLabel;
	private JLabel emailValueLabel;
	private JLabel phoneValueLabel;
	
	public profile_panel(main_app app) {
		this.mainApp = app;
		createProfileGUI();
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
					System.out.println("✓ Фото личного кабинета успешно загружено!");
				} else {
					System.err.println("✗ ОШИБКА: Файл фона личного кабинета не найден!");
				}
			} catch (Exception e) {
				System.err.println("✗ ОШИБКА при загрузке фото личного кабинета: " + e.getMessage());
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
				g.setColor(new Color(245, 235, 230));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
	}
	
	// Создание личного кабинета
	private void createProfileGUI() {
		setLayout(new BorderLayout());
		
		// Путь к фону личного кабинета
		// Если нет такого файла, поменяй на photo\\reg.jpeg
		String photoPath = "photo\\reg.jpeg";
		
		BackgroundPanel mainPanel = new BackgroundPanel(photoPath);
		
		JLabel title = new JLabel("Личный кабинет", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 28));
		title.setForeground(new Color(80, 45, 60));
		title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
		
		mainPanel.add(title, BorderLayout.NORTH);
		
		// Центральная часть, чтобы карточка стояла по центру
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);
		
		JPanel cardPanel = new JPanel(new GridBagLayout());
		cardPanel.setBackground(new Color(255, 250, 248, 220));
		cardPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(210, 170, 180), 2),
				BorderFactory.createEmptyBorder(25, 35, 25, 35)
		));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		
		JLabel cardTitle = new JLabel("Данные пользователя");
		cardTitle.setFont(new Font("Arial", Font.BOLD, 20));
		cardTitle.setForeground(new Color(100, 55, 75));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		cardPanel.add(cardTitle, gbc);
		
		gbc.gridwidth = 1;
		
		// Логин
		gbc.gridx = 0;
		gbc.gridy = 1;
		cardPanel.add(createNameLabel("Логин:"), gbc);
		
		gbc.gridx = 1;
		usernameValueLabel = createValueLabel("-");
		cardPanel.add(usernameValueLabel, gbc);
		
		// Email
		gbc.gridx = 0;
		gbc.gridy = 2;
		cardPanel.add(createNameLabel("Email:"), gbc);
		
		gbc.gridx = 1;
		emailValueLabel = createValueLabel("-");
		cardPanel.add(emailValueLabel, gbc);
		
		// Телефон
		gbc.gridx = 0;
		gbc.gridy = 3;
		cardPanel.add(createNameLabel("Телефон:"), gbc);
		
		gbc.gridx = 1;
		phoneValueLabel = createValueLabel("-");
		cardPanel.add(phoneValueLabel, gbc);
		
		centerPanel.add(cardPanel);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		// Нижние кнопки
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
		buttonsPanel.setOpaque(false);
		
		JButton catalogButton = createButton("Каталог");
		catalogButton.addActionListener(e -> {
			mainApp.showPanel("Каталог");
		});
		
		JButton basketButton = createButton("Корзина");
		basketButton.addActionListener(e -> {
			mainApp.showPanel("Корзина");
		});
		
		JButton exitButton = createButton("Выйти");
		exitButton.addActionListener(e -> {
			mainApp.setCurrentUsername(null);
			mainApp.showPanel("Авторизация");
		});
		
		buttonsPanel.add(catalogButton);
		buttonsPanel.add(basketButton);
		buttonsPanel.add(exitButton);
		
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	// Загрузка данных пользователя
	public void loadUser(String username) {
		if (username == null || username.isEmpty()) {
			usernameValueLabel.setText("-");
			emailValueLabel.setText("-");
			phoneValueLabel.setText("-");
			return;
		}
		
		user currentUser = userService.getUserByUsername(username);
		
		if (currentUser == null) {
			usernameValueLabel.setText("Пользователь не найден");
			emailValueLabel.setText("-");
			phoneValueLabel.setText("-");
		} else {
			usernameValueLabel.setText(currentUser.getUsername());
			emailValueLabel.setText(currentUser.getEmail());
			phoneValueLabel.setText(currentUser.getPhone());
		}
	}
	
	// Создание подписи слева
	private JLabel createNameLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 16));
		label.setForeground(new Color(70, 45, 55));
		return label;
	}
	
	// Создание значения справа
	private JLabel createValueLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 16));
		label.setForeground(new Color(40, 40, 40));
		return label;
	}
	
	// Создание кнопки
	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(120, 35));
		button.setBackground(new Color(255, 255, 255, 220));
		button.setForeground(new Color(70, 45, 55));
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		return button;
	}
}