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
	
	public boolean checkUsername(String username) {
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
			boolean isAlphabet = false;
			boolean isNumber = false;;
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
			
			if (isAlphabet && isNumber ) { return true; } else return false;
		}
	}	
	
	// Проверка повтора и первого пароля
	public boolean checkPasswordAndDoublePassword(String password, String conf_password) {
		if (password == null || conf_password == null) {
			return false;
		}
		
		if (password.isEmpty() || conf_password.isEmpty()) {
			return false;
		}
		
		return password.equals(conf_password);
	}
	
	public boolean checkEmail(String email) {
		if (email == null || email.isEmpty()) {
			return false;
		}
		
		// Contains для проверки наличия символа
		if (!email.contains("@") || !email.contains(".")) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkPhone(String numberPhone) {
		if (numberPhone == null || numberPhone.isEmpty()) {
			return false;
		}
		
		boolean isNumberPhone = false;
		for (int element = 0; element < numberPhone.length(); element++) {
			// charAt когда посимвольно идем по строке
			char c = numberPhone.charAt(element);
			
			if (Character.isDigit(c)) {
				isNumberPhone = true;
			}
		}
		return isNumberPhone;
	}
}
