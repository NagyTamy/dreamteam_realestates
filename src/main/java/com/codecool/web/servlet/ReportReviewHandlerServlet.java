package com.codecool.web.servlet;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.dao.MessageDao;
import com.codecool.web.dao.database.DatabaseCommentDao;
import com.codecool.web.dao.database.DatabaseMessageDao;
import com.codecool.web.model.comment.Comment;
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

@WebServlet("/report-review")
public class ReportReviewHandlerServlet extends AbstractServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            commentService.flagComment(id);

            setSessionUser(req, user);
            if(commentService.getCommentById(id).getFlagged()){
                sendMessage(resp, HttpServletResponse.SC_OK, "Review reported. Redirecting to homepage in 5 sec.");
            } else {
                sendMessage(resp, HttpServletResponse.SC_OK, "Comment confirmed as acceptable by DreamTeam Inc's policy. Redirecting to homepage in 5 sec.");
            }

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            Comment comment = commentService.getCommentById(id);
            messageService.createNewAlertMessage(comment.getReviewerName(), "Removed comment!", "Your comment below has been removed due to company policy: " + comment.getReview());

            commentService.removeComment(id);
            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Comment removed, user notified! Redirecting...");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
