package com.codecool.web.servlet;

import com.codecool.web.model.user.AbstractUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class AbstractServlet extends HttpServlet {

    private final ObjectMapper om = new ObjectMapper();

    Connection getConnection(ServletContext sce) throws SQLException {
        DataSource dataSource = (DataSource) sce.getAttribute("dataSource");
        return dataSource.getConnection();
    }

    AbstractUser getSessionUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        return  (AbstractUser) session.getAttribute("user");
    }

    void setSessionUser(HttpServletRequest request, AbstractUser user){
        request.getSession().setAttribute("user", user);
    }

    void handleSqlError(HttpServletResponse resp, SQLException ex) throws IOException {
        sendMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        ex.printStackTrace();
    }

    void sendMessage(HttpServletResponse resp, int status, Object object) throws IOException {
        resp.setStatus(status);
        om.writeValue(resp.getOutputStream(), object);
    }

    boolean isLoggedIn(HttpServletRequest request){
        AbstractUser user = getSessionUser(request);
        return user != null;
    }

    String timeFormatter(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return time.format(formatter);
    }
}
