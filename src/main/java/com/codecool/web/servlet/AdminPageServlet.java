package com.codecool.web.servlet;

import com.codecool.web.dao.*;
import com.codecool.web.dao.database.*;
import com.codecool.web.dto.AdminPageDto;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.service.CommentService;
import com.codecool.web.service.LogService;
import com.codecool.web.service.MessageService;
import com.codecool.web.service.RealEstateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/admin")
public class AdminPageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);
            LogDao logDao = new DatabaseLogDao(connection);
            LogService logService = new LogService(logDao);
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            AdminPageDto adminPageDto = new AdminPageDto(realEstateService, messageService, logService, commentService);
            AbstractUser user = getSessionUser(req);
            setSessionUser(req, user);

            if(user instanceof Admin) {
                sendMessage(resp, HttpServletResponse.SC_OK, adminPageDto);
            } else {
                String message = "You are not authorized to access this page";
                sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, message);
            }

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
