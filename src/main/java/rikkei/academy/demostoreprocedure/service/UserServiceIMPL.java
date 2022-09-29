package rikkei.academy.demostoreprocedure.service;

import rikkei.academy.demostoreprocedure.config.ConnectMySQL;
import rikkei.academy.demostoreprocedure.model.User;
import rikkei.academy.demostoreprocedure.service.IUserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceIMPL implements IUserService {

    private  final Connection connection = ConnectMySQL.getConnection();
    private static final String INSERT_USER_SQL = "INSERT INTO user(name,email,country) VALUES (?,?,?);";
    private static final String SELECT_USER_BY_ID = "SELECT id,name,email,country FROM user WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String DELETE_USER_SQL = "DELETE FROM user WHERE id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE user SET name = ?, email = ?,country = ? WHERE id = ?";
    @Override
    public void insertUser(User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
           throw new RuntimeException();

        }

    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(name,email,country);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDelete;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);
            preparedStatement.setInt(1,id);
            rowDelete = preparedStatement.executeUpdate() >0 ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rowDelete;
    }

    @Override
    public boolean updateUser(User user)  {
        boolean rowUpdate;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            preparedStatement.setInt(4,user.getId());
            rowUpdate = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowUpdate;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{CALL get_user_by_id(?) }";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1,id);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user  = new User(id,name,email,country);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void insertUserStore(User user)  {
        String query = "{CALL insert_user(?,?,?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1,user.getName());
            callableStatement.setString(2,user.getEmail());
            callableStatement.setString(3, user.getCountry());
            callableStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
