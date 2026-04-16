package services;

import java.util.HashMap;
import java.util.Map;

public class user_service {
	private final Map<String, String> users = new HashMap<>();
	
	public user_service() {
	    users.put("admin", "1234");
	}
	
	public boolean login(String username, String password) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		return users.containsKey(username) && users.get(username).equals(password);
	}
	
	public boolean register(String username, String password) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		if (users.containsKey(username)) {
			return false;
		}
		
		users.put(username, password);
		return true;
	}  
	
	public boolean check_username(String username) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
		if (users.containsKey(username)) {
			return false;
		}	
		
		return true;
	}
	
	// Проверка введенного пароля
	public boolean check_password(String password) {
		if (password == null || password.isEmpty()) {
			return false;
		}
		
		if (password.length() < 8) {
			return false;
		} else {
			boolean is_alphabet = false;
			boolean is_number = false;;
			for (int element = 0; element < password.length(); element++) {
				char c = password.charAt(element);
				if (Character.isLetter(c)) {
					is_alphabet = true;
				}
				
				if (Character.isDigit(c)) {
					is_number = true;
				}
			}
			
			if (is_alphabet && is_number ) { return true; } else return false;
		}
	}	
	
	// Проверка повтора и первого пароля
	/*public boolean check_password_and_double_password(String password, String conf_password) {
		
	}*/
}
