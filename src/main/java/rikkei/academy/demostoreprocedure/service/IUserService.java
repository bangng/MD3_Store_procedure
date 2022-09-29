package rikkei.academy.demostoreprocedure.service;

import rikkei.academy.demostoreprocedure.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    void insertUser(User user);
    User selectUser(int id);
    List<User> selectAllUser();
    boolean deleteUser(int id) ;
    boolean updateUser(User user) ;
    User getUserById(int id);
    void insertUserStore(User user) ;
}
