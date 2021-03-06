package com.codecool.web.servlet;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.dao.database.DatabasePictureDao;
import com.codecool.web.dao.database.DatabaseRealEstatetDao;
import com.codecool.web.dto.RealEstateOffersDto;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.model.user.Landlord;
import com.codecool.web.model.user.Renter;
import com.codecool.web.service.PictureService;
import com.codecool.web.service.RealEstateService;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/home")
public class HomePageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);
            List<String> menuList = new ArrayList<>();

            RealEstateOffersDto onLoadOffers;

            AbstractUser user = getSessionUser(req);

            if (user != null){
                setSessionUser(req, user);
                menuList.add("Profile");
                menuList.add("Favs");
                menuList.add("Log out");
                if (user instanceof Admin){
                    menuList.add(2, "Admin");
                }
                onLoadOffers = new RealEstateOffersDto(realEstateService, menuList, user.getName());
            } else {
                menuList.add("Log in");
                menuList.add("Register");
                onLoadOffers = new RealEstateOffersDto(realEstateService, menuList);
            }




            sendMessage(resp, HttpServletResponse.SC_OK, onLoadOffers);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
