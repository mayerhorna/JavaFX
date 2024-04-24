package com.commerceapp.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnectionController {
	public static boolean validarInicioSesion(String login, String password) throws SQLException {
		String sql = "SELECT * FROM ba_user WHERE login_user = ? AND password_user = ?";
		try (Connection connection = MySQLConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, login);
			statement.setString(2, password);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	

}
