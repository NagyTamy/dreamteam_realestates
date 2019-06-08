package com.codecool.web.servlet;

import com.codecool.web.dao.ReservationDao;
import com.codecool.web.dao.database.DatabaseReservationDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/reservation")
public class ReservationServlet extends AbstractServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())){
            ReservationDao reservationDao = new DatabaseReservationDao(connection);
            ReservationService reservationService = new ReservationService(reservationDao);

            AbstractUser user = getSessionUser(req);

            int id = Integer.parseInt(req.getParameter("id"));

            reservationService.removeReservation(id);

            setSessionUser(req, user);

            sendMessage(resp, HttpServletResponse.SC_OK, "Reservation cancelled. Redirecting to homepage in 5 sec.");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (Throwable ex){
            throw new ServletException(ex);
        }
    }

}

