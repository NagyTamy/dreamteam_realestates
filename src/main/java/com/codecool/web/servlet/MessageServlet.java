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
import java.time.LocalDateTime;

@WebServlet("/message")
public class MessageServlet extends AbstractServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            String content = req.getParameter("content");
            String title = req.getParameter("title");
            int history = Integer.parseInt(req.getParameter("history"));
            String sender = req.getParameter("sender");
            String receiver = req.getParameter("receiver");

            messageService.addNewPrivateAnswer(sender, receiver, history, title, content);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Message sent. Redirecting to homepage in 5 sec.");
        } catch (
        SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            String content = req.getParameter("content");
            String title = req.getParameter("title");
            String receiver = req.getParameter("receiver");

            messageService.addNewPrivateMessage(user.getName(), receiver, title, content);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Message sent. Redirecting to homepage in 5 sec.");
        } catch (
                SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
