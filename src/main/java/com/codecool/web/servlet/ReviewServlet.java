package com.codecool.web.servlet;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.dao.database.DatabaseCommentDao;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.comment.RealEstateComment;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;



@WebServlet("/review")
public class ReviewServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            AbstractUser user = getSessionUser(req);

            String content = req.getParameter("content");
            int rating = Integer.parseInt(req.getParameter("rating"));
            int id = Integer.parseInt(req.getParameter("id"));
            commentService.addRealEstateComment(user.getName(), content, LocalDateTime.now(), rating, id);
            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Review sent. Redirecting to homepage in 5 sec.");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection =getConnection(req.getServletContext())){
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            RealEstateComment realEstateComment = (RealEstateComment)commentService.getCommentById(id);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, realEstateComment);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));
            String content = req.getParameter("content") + "\n" +  "Updated: " + timeFormatter(LocalDateTime.now());
            int rating = Integer.parseInt(req.getParameter("rating"));

            RealEstateComment comment = (RealEstateComment)commentService.getCommentById(id);
            comment.setReview(content);
            comment.setRealEstateRating(rating);

            commentService.editComment(comment);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Review updated. Redirecting to homepage in 5 sec.");
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

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            commentService.removeComment(id);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Review removed. Redirecting to homepage in 5 sec.");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }


}
