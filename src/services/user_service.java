package services;

import db.db_connection;
import models.user;

// Объект соединения с БД
import java.sql.Connection;

// Для подстановки значений в SQL-запросы
import java.sql.PreparedStatement;

// Для результата SELECT-запроса
import java.sql.ResultSet;

// Для обработки ошибок БД
import java.sql.SQLException;

public class user_service {
	
	// Авторизация пользователя
	public boolean login(String username, String password) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		// SQL-запрос для поиска пользователя в таблице users
		String sql = "select * from users where username = ? and password = ?";
		
		try {
			// Получаем соединение с БД
			Connection connection = db_connection.getConnection();
			
			// Подготавливаем SQL-запрос
			PreparedStatement statement = connection.prepareStatement(sql);
			
			// Вместо первого ? подставляем username
			statement.setString(1, username);
			
			// Вместо второго ? подставляем password
			statement.setString(2, password);
			
			// Выполняем SELECT-запрос
			ResultSet result = statement.executeQuery();
			
			// Если пользователь найден
			if (result.next()) {
				connection.close();
				return true;
			} else {
				connection.close();
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Регистрация пользователя
	public boolean register(String username, String email, String phone, String password) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		if (email == null || email.isEmpty()) {
			return false;
		}
		
		if (phone == null || phone.isEmpty()) {
			return false;
		}
		
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		// Если логин уже занят, регистрацию не выполняем
		if (!checkUsername(username)) {
			return false;
		}
		
		// Очищаем телефон от скобок, пробелов и дефисов
		String cleanPhone = getOnlyDigits(phone);
		
		// SQL-запрос для добавления пользователя
		String sql = "insert into users (username, email, phone, password) values (?, ?, ?, ?)";
		
		try {
			Connection connection = db_connection.getConnection();
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			statement.setString(2, email);
			statement.setString(3, cleanPhone);
			statement.setString(4, password);
			
			// executeUpdate используется для insert, update, delete
			int rows = statement.executeUpdate();
			
			connection.close();
			
			if (rows > 0) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Проверка логина
	public boolean checkUsername(String username) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		// SQL-запрос для поиска пользователя с таким логином
		String sql = "select * from users where username = ?";
		
		try {
			Connection connection = db_connection.getConnection();
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			
			// Если пользователь найден, значит логин занят
			if (result.next()) {
				connection.close();
				return false;
			} else {
				connection.close();
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Получение данных пользователя по логину
	public user getUserByUsername(String username) {
		if (username == null || username.isEmpty()) {
			return null;
		}
		
		String sql = "select id, username, email, phone from users where username = ?";
		
		try {
			Connection connection = db_connection.getConnection();
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				int id = result.getInt("id");
				String userName = result.getString("username");
				String email = result.getString("email");
				String phone = result.getString("phone");
				
				connection.close();
				
				return new user(id, userName, email, phone);
			} else {
				connection.close();
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Проверка введенного пароля
	public boolean checkPassword(String password) {
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		if (password.length() < 8) {
			return false;
		}
		
		boolean isAlphabet = false;
		boolean isNumber = false;
		
		for (int element = 0; element < password.length(); element++) {
			// charAt когда посимвольно идем по строке
			char c = password.charAt(element);
			
			if (Character.isLetter(c)) {
				isAlphabet = true;
			}
			
			if (Character.isDigit(c)) {
				isNumber = true;
			}
		}
		
		if (isAlphabet && isNumber) {
			return true;
		} else {
			return false;
		}
	}
	
	// Проверка повтора и первого пароля
	public boolean checkPasswordAndDoublePassword(String password, String confPassword) {
		if (password == null || confPassword == null) {
			return false;
		}
		
		if (password.isEmpty() || confPassword.isEmpty()) {
			return false;
		}
		
		return password.equals(confPassword);
	}
	
	// Проверка email
	public boolean checkEmail(String email) {
		if (email == null || email.isEmpty()) {
			return false;
		}
		
		// contains для проверки наличия символа
		if (!email.contains("@") || !email.contains(".")) {
			return false;
		}
		
		return true;
	}
	
	// Проверка телефона
	public boolean checkPhone(String numberPhone) {
		if (numberPhone == null || numberPhone.isEmpty()) {
			return false;
		}
		
		String cleanPhone = getOnlyDigits(numberPhone);
		
		// Для нашего проекта номер должен содержать 11 цифр
		if (cleanPhone.length() == 11) {
			return true;
		} else {
			return false;
		}
	}
	
	// Метод оставляет в строке только цифры
	private String getOnlyDigits(String text) {
		String result = "";
		
		for (int element = 0; element < text.length(); element++) {
			char c = text.charAt(element);
			
			if (Character.isDigit(c)) {
				result = result + c;
			}
		}
		
		return result;
	}
}