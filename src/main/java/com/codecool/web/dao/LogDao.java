package com.codecool.web.dao;

import com.codecool.web.model.Log;
import com.codecool.web.model.user.Admin;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public interface LogDao {

    List<Log> getLogs() throws SQLException;

    Admin findUserByAdminId(int adminId) throws SQLException, NoSuchUserException;
}
