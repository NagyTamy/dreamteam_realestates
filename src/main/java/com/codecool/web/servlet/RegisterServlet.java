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

@WebServlet("/register")
public class RegisterServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new UserService(userDao);
            PasswordHashService passwordHashService = new PasswordHashService();

            String userName = req.getParameter("name");
            String eMail = req.getParameter("email");
            String password = req.getParameter("password");
            String passwordChk = req.getParameter("passwordChk");

            if(!password.equals(passwordChk)){
                throw new ServiceException("Passwords doesn't match!");
            }

            if(userService.isUserNameExist(userName)){
                throw new ServiceException("User with this name already exists!");
            }

            if(userService.isEmailExist(eMail)){
                throw new ServiceException("User with this e-mail address already exists!");
            }

            String hashedPassword = passwordHashService.getHashedPassword(password);
            userService.addUser(userName, eMail, hashedPassword);

            setSessionUser(req, userService.getUserByName(userName));
            resp.sendRedirect("home");

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

}
