package volcano.java.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import volcano.java.usermanagement.dao.UserDAO;
import volcano.java.usermanagement.model.User;

@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDao;

	public UserServlet() {
		this.userDao = new UserDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				showEditForm(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateUser(request, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			try {
				listUser(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<User> listUsers = userDao.selectAllUser();
		request.setAttribute("listUser", listUsers);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	// direct to user-form
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	// redirect to edit form
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existUser = userDao.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existUser);
		dispatcher.forward(request, response);
	}

	// add new function and redirect to list Users
	private void insertUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}

	// delete function
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDao.deleteUser(id);
		response.sendRedirect("list");
	}

	// update function
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User choiceUser = new User(id, name, email, country);
		userDao.updateUser(choiceUser);
		response.sendRedirect("list");

	}
}
