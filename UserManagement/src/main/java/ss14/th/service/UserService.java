package ss14.th.service;

import ss14.th.model.User;
import ss14.th.util.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserService implements IGenericService<User,Integer>{
    // ====== QUERY SQL ======
    private final String SEARCH_BY_COUNTRY = "select * from demojdbc.users where country like concat('%', ?, '%') or country like concat(?, '%')";
    private final String SORT_BY_NAME = "select * from demojdbc.users order by name";
    private final String CALL_GET_ALL = "call getAllUser()";
    private final String CALL_FIND_BY_ID = "call findUserById(?)";
    private final String CALL_INSERT = "call createUser(?,?,?)";
    private final String CALL_UPDATE = "call editUser(?,?,?,?)";
    private final String CALL_DELETE = "call deleteUser(?)";
    // ======= END SQL ======
    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.openConnection();
        List<User> userList = new ArrayList<>();
        CallableStatement preST = con.prepareCall(CALL_GET_ALL);
        ResultSet rs = preST.executeQuery();
        while (rs.next()){
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setCountry(rs.getString("country"));
            userList.add(user);
        }
        ConnectDB.closeConnection(con);
        return userList;
    }

    @Override
    public User findByID(Integer id) throws SQLException, ClassNotFoundException {
        User user = null;
        Connection con = ConnectDB.openConnection();
        CallableStatement callSt = con.prepareCall(CALL_FIND_BY_ID);
        callSt.setInt(1,id);
        ResultSet rs = callSt.executeQuery();
        while (rs.next()){
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setCountry(rs.getString("country"));
        }
        ConnectDB.closeConnection(con);
        return user;
    }

    @Override
    public void save(User user) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.openConnection();
       CallableStatement callSt;
        // id trong DB luôn bắt đầu từ 1
        // nếu id = 0 -> mặc định thêm mới, id tự tăng theo DB
        // -> ADD
        if(user.getId()==0){
            callSt = con.prepareCall(CALL_INSERT);
            callSt.setString(1, user.getName());
            callSt.setString(2, user.getEmail());
            callSt.setString(3, user.getCountry());
            callSt.executeUpdate();
        }
        else
        // UPDATE
        {
            callSt = con.prepareCall(CALL_UPDATE);
            callSt.setString(1, user.getName());
            callSt.setString(2, user.getEmail());
            callSt.setString(3, user.getCountry());
            callSt.setInt(4, user.getId());
            callSt.executeUpdate();
        }
        ConnectDB.closeConnection(con);
    }

    @Override
    public void delete(Integer id) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.openConnection();
        CallableStatement callSt = con.prepareCall(CALL_DELETE);
        callSt.setInt(1,id);
        callSt.executeUpdate();
        ConnectDB.closeConnection(con);
    }
    public List<User> searchByCountry(String country) throws SQLException, ClassNotFoundException {
        List<User> searchList = new ArrayList<>();
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt = con.prepareStatement(SEARCH_BY_COUNTRY);
        preSt.setString(1,country);
        preSt.setString(2,country);
        ResultSet rs = preSt.executeQuery();
        while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setCountry(rs.getString("country"));
                searchList.add(user);
        }
        ConnectDB.closeConnection(con);
        return searchList;
    }
    public List<User> sortByName(String selectSort) throws SQLException, ClassNotFoundException {
        List<User> sortList = new ArrayList<>();
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt = con.prepareStatement(SORT_BY_NAME);
        ResultSet rs = preSt.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setCountry(rs.getString("country"));
                    sortList.add(user);
                }
           if (selectSort.equals("DESC")){
               sortList.sort(Comparator.comparing(User::getName).reversed());
           }
        ConnectDB.closeConnection(con);
        return sortList;
    }

}
