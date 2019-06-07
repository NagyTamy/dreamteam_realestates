package com.codecool.web.service;

import com.codecool.web.dao.LogDao;
import com.codecool.web.model.Log;
import com.codecool.web.model.user.Admin;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public class LogService {

    private LogDao logDao;

    public LogService(LogDao logDao){
        this.logDao = logDao;
    }

    public List<Log> getLogs() throws SQLException, NoSuchUserException{
        return setAdminForEachLog(logDao.getLogs());
    }

    private Admin findUserByAdminId(int adminId) throws SQLException{
        try {
            return logDao.findUserByAdminId(adminId);
        } catch (NoSuchUserException ex){
            return null;
        }
    }

    private List<Log> setAdminForEachLog(List<Log> list) throws SQLException, NoSuchUserException{
        for(Log log : list){
            Admin admin = findUserByAdminId(log.getAdminId());
            if(admin != null){
                log.setAdmin(admin);
                log.setAdminUser(true);
            } else {
                log.setAdminUser(false);
            }
        } return list;
    }
}
