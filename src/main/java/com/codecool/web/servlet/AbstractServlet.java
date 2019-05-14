package com.codecool.web.servlet;

import com.codecool.web.model.user.AbstractUser;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractServlet extends HttpServlet {

    Connection getConnection(ServletContext sce) throws SQLException {
        DataSource dataSource = (DataSource) sce.getAttribute("dataSource");
        return dataSource.getConnection();
    }

    AbstractUser getSessionUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return  (AbstractUser) session.getAttribute("user");
    }
}
