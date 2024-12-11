package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

	//Hay que cambiar la url por la de la base de datos final	
	private String url = "jdbc:mysql://localhost:3306/biblioteca";
	private String username = "root";
	private String password = "root";
	
	public Db(String url, String username, String password) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;

	}
	
	public Connection getConnection() {
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}
	
}