package com.codecool.web.servlet;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.dao.database.DatabasePictureDao;
import com.codecool.web.dao.database.DatabaseRealEstatetDao;
import com.codecool.web.dto.SearchResultDto;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.RealEstateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/search")
public class SimpleSearchServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);

            AbstractUser user = getSessionUser(req);

            String searchKey = req.getParameter("searchKey");
            List<RealEstate> searchResult = realEstateService.addMainPictures(realEstateService.doSimpleSearch(searchKey));

            SearchResultDto searchResultDto = new SearchResultDto(searchResult);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, searchResultDto);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
