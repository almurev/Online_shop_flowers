package main;

import javax.swing.*;
import java.awt.*;

import ui.auth_panel;
import ui.profile_panel;

// Класс создания окна с переключением разделов
public class main_app {
	
	private JFrame frame;
	private JPanel panels;
	private CardLayout card_layout;
	
	// Текущий пользователь после входа
	private String currentUsername;
	
	// Панель профиля, чтобы потом подгружать туда данные пользователя
	private profile_panel profilePanel;
	
	// Класс создания окна, вкладок для приложения
	private void create_frame() {
		// Создаем окно приложения
		frame = new JFrame("Онлайн-магазин цветов");
		frame.setSize(700, 600);
		frame.setMinimumSize(new Dimension(450, 450));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Запихиваем в менеджер компоновки вкладки для открытия разделов
		card_layout = new CardLayout();
		panels = new JPanel(card_layout);
		
		// Создаем панели и добавляем в контейнер
		JPanel catalogPanel = new JPanel(new BorderLayout());
		catalogPanel.add(new JLabel("Каталог", SwingConstants.CENTER), BorderLayout.CENTER);
		panels.add(catalogPanel, "Каталог");
		
		profilePanel = new profile_panel(this);
		panels.add(profilePanel, "Профиль");
		
		JPanel basketPanel = new JPanel(new BorderLayout());
		basketPanel.add(new JLabel("Корзина", SwingConstants.CENTER), BorderLayout.CENTER);
		panels.add(basketPanel, "Корзина");
		
		// Добавляем панель авторизации как стартовую
		auth_panel AuthPanel = new auth_panel(this);
		panels.add(AuthPanel, "Авторизация");
		
		frame.add(panels, BorderLayout.CENTER);
		
		showPanel("Авторизация");
		
		frame.setVisible(true);
	}
	
	// Создаем переключение панелей
	public void showPanel(String name) {
		// Если открываем профиль, загружаем данные текущего пользователя
		if (name.equals("Профиль")) {
			profilePanel.loadUser(currentUsername);
		}
		
		card_layout.show(panels, name);
	}
	
	// Сохраняем логин текущего пользователя
	public void setCurrentUsername(String username) {
		this.currentUsername = username;
	}
	
	// Получаем логин текущего пользователя
	public String getCurrentUsername() {
		return currentUsername;
	}
	
	// Запускаем окошко
	public static void main(String[] args) {
		main_app app = new main_app();
		app.create_frame();
	}
}