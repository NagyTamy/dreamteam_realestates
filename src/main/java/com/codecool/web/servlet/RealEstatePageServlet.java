package com.codecool.web.servlet;


import com.codecool.web.dao.PictureDao;
import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.dao.database.DatabasePictureDao;
import com.codecool.web.dao.database.DatabaseRealEstatetDao;
import com.codecool.web.model.RealEstate;
import com.codecool.web.service.RealEstateService;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/real-estate-profile")
public class RealEstatePageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);

            int id = Integer.parseInt(req.getParameter("id"));
            RealEstate realEstate = realEstateService.findRealEstateById(id);

            sendMessage(resp, HttpServletResponse.SC_OK, realEstate);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (NoSuchRealEstateException ex){
            ex.getMessage();
        }
    }
}
