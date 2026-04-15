package services;

import java.util.HashMap;
import java.util.Map;

public class user_service {
	Map<String, String> users = new HashMap<>();
	
	public user_service() {
	    users.put("admin", "1234");
	}
	
	public boolean login(String username, String password) {
		if (!users.containsKey(username)) {
			return false;
		} else {
			return users.get(username).equals(password);
		}
	}
	
	public boolean register(String username, String password) {
		if (!users.containsKey(username)) {
			users.put(username, password);
			return true;
		} else {
			return false;
		}
	}  
}
