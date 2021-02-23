package volcano.java.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import volcano.java.usermanagement.model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "18091998";
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name,email,country) VALUES"
			+ " ( ?, ?, ?);";
	private static final String SELECT_USERS_BY_ID_SQL = "SELECT id,name,email,country FROM users WHERE id = ?";
	private static final String SELECT_ALL_USERS = "SELECT * FROM users";
	private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
	private static final String UPDATE_USER_SQL = "UPDATE users set name = ? , email = ?, country=? WHERE id=?";

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	// Create User
	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Update User
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	// Select User
	public User selectUser(int id) {
		User userSelect = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ID_SQL)) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet resultset = preparedStatement.executeQuery();
			while (resultset.next()) {
				String name = resultset.getString("name");
				String email = resultset.getString("email");
				String country = resultset.getString("country");
				userSelect = new User(id, name, email, country);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userSelect;
	}

	// Select All Users -- list Users
	public List<User> selectAllUser() {
		List<User> users = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
			System.out.println(preparedStatement);
			ResultSet resultset = preparedStatement.executeQuery();
			while (resultset.next()) {
				int id = resultset.getInt("id");
				String name = resultset.getString("name");
				String email = resultset.getString("email");
				String country = resultset.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	// Delete user
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);) {
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
}
