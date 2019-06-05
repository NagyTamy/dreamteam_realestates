package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.PasswordHashService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LogInServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new UserService(userDao);
            PasswordHashService passwordHashService = new PasswordHashService();

            AbstractUser user = userService.getUserByName(req.getParameter("name"));
            System.out.println(user);

            if (user != null){
                String password = req.getParameter("password");
                boolean isPasswordValid = passwordHashService.validatePassword(password, user.getPassword());
                if(isPasswordValid){
                    setSessionUser(req, user);
                    resp.sendRedirect("home");
                } else {
                    throw new ServletException("Invalid password");
                }
            } else {
                throw new ServiceException("User with this name does not exists!");
            }


        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
