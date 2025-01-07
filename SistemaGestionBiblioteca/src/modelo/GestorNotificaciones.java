package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones {
private Db db;
	
	//mostrar notificaciones segun usuario 
	
	public List<Notificacion> mostrarNotificaciones(int idUsuario) {
		List<Notificacion> notificaciones = new ArrayList<>();
		String sql = "SELECT * FROM Notificaciones WHERE ID_Usuario = ?";
		try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idUsuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Notificacion notificacion = new Notificacion(rs.getInt("ID"), rs.getString("Mensaje"), rs.getInt("ID_Usuario"), rs.getInt("ID_Libro"), rs.getDate("Fecha"), rs.getBoolean("Leido"));
					notificaciones.add(notificacion);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notificaciones;
	}
	
	//borrar notificaciones segun usuario
	
	public boolean borrarNotificaciones(int idUsuario) {
		String sql = "DELETE FROM Notificaciones WHERE ID_Usuario = ?";
		try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idUsuario);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//marcar notificaciones como leidas
	
	public boolean marcarNotificacionesLeidas(int idUsuario) {
		String sql = "UPDATE Notificaciones SET Leido = true WHERE ID_Usuario = ?";
		try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idUsuario);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}