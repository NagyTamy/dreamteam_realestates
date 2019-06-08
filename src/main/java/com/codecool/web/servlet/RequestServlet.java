package com.codecool.web.servlet;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.dao.MessageDao;
import com.codecool.web.dao.database.DatabaseCommentDao;
import com.codecool.web.dao.database.DatabaseMessageDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.CommentService;
import com.codecool.web.service.MessageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/request")
public class RequestServlet extends AbstractServlet {

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            messageService.removeMessage(id);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Request cancelled. Redirecting to homepage in 5 sec.");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
