package main;

import javax.swing.*;
import java.awt.*;
import ui.auth_panel;

// Класс создания окна с переключением разделов в будущем
public class main_app {
	
	private JFrame frame;
	private JPanel panels;
	private CardLayout card_layout;
	
	// Класс создания окна, вкладок для приложения
	private void create_frame() {
		// Создаем окно приложения
		frame = new JFrame("Онлайн-магазин цветов");
		frame.setSize(450, 450);
		
		//Запрещаем изменять размер
		//frame.setResizable(false); 
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Запихиваем в менеджер компоновки вкладки для открытия разделов
		card_layout = new CardLayout();
		panels = new JPanel(card_layout);
		
		// Создаем панели и добавляем в контейнер
		// Потом будем тянуть из ui и будет:
		// CatalogPanel catalogPanel = new CatalogPanel();
		
		JPanel catalogPanel = new JPanel();
		panels.add(catalogPanel, "Каталог");
		
		JPanel profile_panel = new JPanel();
		panels.add(profile_panel, "Профиль");
		
		JPanel basket_pannel = new JPanel();
		panels.add(basket_pannel, "Корзина");
		
		// Добавляем данную панель в frame как стартовую при запуске
		auth_panel AuthPanel = new auth_panel(this);
		panels.add(AuthPanel, "Авторизация");
		showPanel("Авторизация");
		
		frame.add(panels, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	// Создаем переключение панелей (вкладки)
	public void showPanel(String name) {
	    card_layout.show(panels, name);
	}
	
	// Запускаем окошко
	public static void main(String[] args) {
		main_app app = new main_app();
		app.create_frame();
	}
}