package services;

import db.db_connection;
import models.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class user_service {
	
	public boolean login(String username, String password) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		if (password == null || password.isEmpty()) {
			return false;
		}
		String sql = "select * from users where username = ? and password = ?";
		try {
			Connection connection = db_connection.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();
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
		if (!checkUsername(username)) {
			return false;
		}
		String cleanPhone = getOnlyDigits(phone);
		String sql = "insert into users (username, email, phone, password) values (?, ?, ?, ?)";
		try {
			Connection connection = db_connection.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, email);
			statement.setString(3, cleanPhone);
			statement.setString(4, password);
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
	
	public boolean checkUsername(String username) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		String sql = "select * from users where username = ?";
		try {
			Connection connection = db_connection.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
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
	
	public int getUserIdByUsername(String username) {
		if (username == null || username.isEmpty()) {
			return -1;
		}
		String sql = "SELECT id FROM users WHERE username = ?";
		try (Connection conn = db_connection.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
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
	
	public boolean checkPasswordAndDoublePassword(String password, String confPassword) {
		if (password == null || confPassword == null) {
			return false;
		}
		if (password.isEmpty() || confPassword.isEmpty()) {
			return false;
		}
		return password.equals(confPassword);
	}
	
	public boolean checkEmail(String email) {
		if (email == null || email.isEmpty()) {
			return false;
		}
		if (!email.contains("@") || !email.contains(".")) {
			return false;
		}
		return true;
	}
	
	public boolean checkPhone(String numberPhone) {
		if (numberPhone == null || numberPhone.isEmpty()) {
			return false;
		}
		String cleanPhone = getOnlyDigits(numberPhone);
		if (cleanPhone.length() == 11) {
			return true;
		} else {
			return false;
		}
	}
	
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