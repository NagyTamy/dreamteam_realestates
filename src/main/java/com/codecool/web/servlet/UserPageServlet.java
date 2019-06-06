package com.codecool.web.servlet;

import com.codecool.web.dao.*;
import com.codecool.web.dao.database.*;
import com.codecool.web.dto.UserProfileDto;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.model.user.Landlord;
import com.codecool.web.model.user.Renter;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchUserException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user-profile")
public class UserPageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new UserService(userDao);
            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            PictureService pictureService = new PictureService(pictureDao);
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);
            ReservationDao reservationDao = new DatabaseReservationDao(connection);
            ReservationService reservationService = new ReservationService(reservationDao);
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);

            String userName = req.getParameter("id");
            AbstractUser user = userService.getUserByName(userName);


            AbstractUser sessionUser = getSessionUser(req);
            boolean isOwnProfile = false;



            List<String> asideMenu = new ArrayList<>();
            asideMenu.add("Reviews");
            if(sessionUser != null){
                if(userName.equals(sessionUser.getName())){
                    isOwnProfile = true;
                }
                if(!isOwnProfile){
                    asideMenu.add("Send message");
                    if(sessionUser instanceof  Landlord || sessionUser instanceof Admin){
                        asideMenu.add("User's real estates");
                    }
                } else if(isOwnProfile){
                    asideMenu.add("My requests");
                    asideMenu.add("My reviews");
                    asideMenu.add("Messages");
                    asideMenu.add("My reservations");
                    if(sessionUser instanceof Landlord || sessionUser instanceof Admin){
                        asideMenu.add("My real estates");
                    }
                }
            } else {
                if(user instanceof Landlord || user instanceof Admin){
                    asideMenu.add("User's real estates");
                }
            }

            UserProfileDto userProfileDto = new UserProfileDto(userName, realEstateService, reservationService, messageService, asideMenu, commentService, userService, pictureService, isLoggedIn(req), isOwnProfile);

            sendMessage(resp, HttpServletResponse.SC_OK, userProfileDto);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
