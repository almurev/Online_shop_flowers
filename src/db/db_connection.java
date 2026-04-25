package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Класс подключения к БД
public class db_connection {
	// Как подключаемся к БД через локальный хост
	private static final String URL = "jdbc:mysql://localhost:3306/online_shop_flowers_db";
	// Логин и пароль для входа в MySQL
	private static final String USER = "root";
	private static final String PASSWORD = "1910";
	
	// Метод для получения соединения с базой данных
	public static Connection getConnection() throws SQLException {
	    
	    // DriverManager.getConnection(...) пытается подключиться к БД
	    // используя URL, логин и пароль
	    return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
