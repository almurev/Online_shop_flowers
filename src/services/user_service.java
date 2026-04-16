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
	
}
