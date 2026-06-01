package main;

import javax.swing.*;
import java.awt.*;

import ui.auth_panel;
import ui.catalog_panel;
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
	    frame = new JFrame("Онлайн-магазин цветов");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    
	    card_layout = new CardLayout();
	    panels = new JPanel(card_layout);
	    
	    // Создаем панели
	    JPanel catalogPanel = new catalog_panel(this);
	    panels.add(catalogPanel, "Каталог");
	    
	    profilePanel = new profile_panel(this);
	    panels.add(profilePanel, "Профиль");
	    
	    JPanel basketPanel = new JPanel(new BorderLayout());
	    basketPanel.add(new JLabel("Корзина", SwingConstants.CENTER), BorderLayout.CENTER);
	    panels.add(basketPanel, "Корзина");
	    
	    auth_panel AuthPanel = new auth_panel(this);
	    panels.add(AuthPanel, "Авторизация");
	    
	    frame.add(panels, BorderLayout.CENTER);
	    
	    // Устанавливаем маленький размер для авторизации
	    frame.setSize(600, 600);
	    frame.setMinimumSize(new Dimension(550, 500));
	    frame.setVisible(true);
	    
	    showPanel("Авторизация");
	}
	
	// Метод для установки размера окна при переключении панелей
	public void setPanelSize(String panelName) {
	    if (panelName.equals("Авторизация")) {
	        frame.setSize(600, 600);
	        frame.setMinimumSize(new Dimension(550, 500));
	    } else {
	        frame.setSize(1000, 750);
	        frame.setMinimumSize(new Dimension(800, 600));
	    }
	    frame.setLocationRelativeTo(null);  // центрируем окно после изменения размера
	}

	// Переопредели метод showPanel (или создай новый)
	public void showPanel(String name) {
	    // Меняем размер в зависимости от панели
	    setPanelSize(name);
	    
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