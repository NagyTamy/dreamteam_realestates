package com.codecool.web.servlet;


import com.codecool.web.dao.*;
import com.codecool.web.dao.database.*;
import com.codecool.web.dto.RealEstatePageDto;
import com.codecool.web.model.Picture;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/real-estate-profile")
public class RealEstatePageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            PictureDao pictureDao = new DatabasePictureDao(connection);
            PictureService pictureService = new PictureService(pictureDao);

            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);

            CommentDao commentDao = new DatabaseCommentDao(connection);
            CommentService commentService = new CommentService(commentDao);

            ReservationDao reservationDao = new DatabaseReservationDao(connection);
            ReservationService reservationService = new ReservationService(reservationDao);

            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new UserService(userDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            boolean isOwner;
            boolean isLoggedIn;

            if(user != null){
                isLoggedIn = true;
                setSessionUser(req, user);
                isOwner = realEstateService.isOwner(id, user.getName());
            } else {
                isOwner = false;
                isLoggedIn = false;
            }


            RealEstatePageDto realEstatePageDto = new RealEstatePageDto(id, commentService, userService, realEstateService, pictureService, reservationService, isLoggedIn, isOwner);
            sendMessage(resp, HttpServletResponse.SC_OK, realEstatePageDto);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
