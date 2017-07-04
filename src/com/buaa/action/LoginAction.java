package com.buaa.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

//首先这个类继承 ActionSupport类
public class LoginAction extends ActionSupport {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 此处验证用户必须等hello 密码登录word才会返回成功页面。当然，真实开发中肯定不会这样写，这里只是做一个模拟。
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("text/html;charset=UTF-8");
//		request.setCharacterEncoding("UTF-8");
		String result = "";
		// 获取用户名
		String username = request.getParameter("username");
		String psw = request.getParameter("password");
		if ((username == "") || (username == null) || (username.length() > 20)) {
			try {
				result = "请输入用户名(不能超过20个字符)!";
				request.setAttribute("message", result);
				response.sendRedirect("login.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ((psw == "") || (psw == null) || (psw.length() > 20)) {
			try {
				result = "请输入密码(不能超过20个字符)!";
				request.setAttribute("message", result);
				response.sendRedirect("login.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 登记JDBC驱动程序
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Class Not Found Exception . ");
		}
		// 连接URL
		String url = "jdbc:mysql://localhost:3306/learn_jsp?user=root&password=root&useUnicode=true&characterEncoding=utf-8";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			// SQL语句
			String sql = "select * from userInfo where username='" + username
					+ "' and userpsw= '" + psw + "'";
			rs = stmt.executeQuery(sql);// 返回查询结果
		} catch (SQLException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		try {
			if (rs.next()) { // 如果记录集非空，表明有匹配的用户名和密码，登陆成功
				// 登录成功后将username设置为session变量的username
				// 这样在后面就可以通过 session.getAttribute("username") 来获取用户名，
				// 同时这样还可以作为用户登录与否的判断依据
				session.setAttribute("age", rs.getString("age"));
				session.setAttribute("sex", rs.getString("sex"));
				session.setAttribute("weight", rs.getString("weight"));
				return "success";
			} else {
				session.setAttribute("message", "用户名或密码不匹配。");
				return "fail";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
}