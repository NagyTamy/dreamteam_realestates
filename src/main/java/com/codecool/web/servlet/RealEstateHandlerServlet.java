package com.codecool.web.servlet;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.dao.MessageDao;
import com.codecool.web.dao.PictureDao;
import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.dao.database.DatabaseCommentDao;
import com.codecool.web.dao.database.DatabaseMessageDao;
import com.codecool.web.dao.database.DatabasePictureDao;
import com.codecool.web.dao.database.DatabaseRealEstatetDao;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.CommentService;
import com.codecool.web.service.MessageService;
import com.codecool.web.service.RealEstateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/real-estate-handler")
public class RealEstateHandlerServlet extends AbstractServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            RealEstate realEstate = realEstateService.findRealEstateById(id);

            setSessionUser(req, user);
            if(realEstate.getPublic()){
                realEstateService.changeRealEstateState(id);
                sendMessage(resp, HttpServletResponse.SC_OK, "Real estate is now private. Redirecting to homepage in 5 sec.");
            } else {
                messageService.addNewSystemMessage(user.getName(), "Request for making real estate " + realEstate.getName() + " public", "UPDATE real_estates SET is_public='true' WHERE real_estate_id="+id+";", id);
                sendMessage(resp, HttpServletResponse.SC_OK, "Making real estate public request sent, please wait for our admin's reply. Redirecting to homepage in 5 sec.");
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
            RealEstateDao realEstateDao = new DatabaseRealEstatetDao(connection);
            PictureDao pictureDao = new DatabasePictureDao(connection);
            RealEstateService realEstateService = new RealEstateService(realEstateDao, pictureDao);
            MessageDao messageDao = new DatabaseMessageDao(connection);
            MessageService messageService = new MessageService(messageDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            RealEstate realEstate = realEstateService.findRealEstateById(id);

            setSessionUser(req, user);

            messageService.addNewSystemMessage(user.getName(), "Request for removing " + realEstate.getName(), "DELETE * FROM real_estates WHERE real_estate_id="+id+";", id);

            sendMessage(resp, HttpServletResponse.SC_OK, "Removing real estate request sent, please wait for our admin's reply. Redirecting to homepage in 5 sec.");

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }
}
