package ss14.th.service;

import ss14.th.model.User;
import ss14.th.util.ConnectDB;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTransaction {
    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
            " (?, ?, ?);";
    private static final String SELECT_ALL_USERS = "select * from users";

    private static final String CREATE_TABLE_EMPLOYEE = "CREATE TABLE EMPLOYEE"
            + "("
            + " ID serial,"
            + " NAME varchar(100) NOT NULL,"
            + " SALARY numeric(15, 2) NOT NULL,"
            + " CREATED_DATE timestamp,"
            + " PRIMARY KEY (ID)"
            + ")";
    private static final String DROP_TABLE_EMPLOYEE = "DROP TABLE IF EXISTS EMPLOYEE";
    private static final String INSERT_EMPLOYEE = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";
    private static final String UPDATE_EMPLOYEE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";
    public List<User> selectAllUsers(){
        List<User> users = new ArrayList<>();
        try {
            Connection connection = ConnectDB.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return users;
    };
    public void addUserTransaction(User user, int[] permissions){
        Connection conn = null;
        PreparedStatement preSt = null;
        PreparedStatement preStAssignment = null;
        ResultSet rs = null;
        try {
            conn = ConnectDB.openConnection();
            conn.setAutoCommit(false);
            preSt = conn.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            preSt.setString(1, user.getName());
            preSt.setString(2, user.getEmail());
            preSt.setString(3, user.getCountry());
            int rowAffected = preSt.executeUpdate();
            rs = preSt.getGeneratedKeys();
            int userId = 0;
            if (rs.next())
                userId = rs.getInt(1);
            if (rowAffected == 1) {
                String sqlPivot = "INSERT INTO user_permission(user_id,permission_id) " + "VALUES(?,?)";
                preStAssignment = conn.prepareStatement(sqlPivot);
                for (int permissionId : permissions) {
                    preStAssignment.setInt(1, userId);
                    preStAssignment.setInt(2, permissionId);
                    preStAssignment.executeUpdate();
                }
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            // roll back the transaction
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (preSt != null) preSt.close();
                if (preStAssignment != null) preStAssignment.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    };
    public void insertUpdateWithoutTransaction() {
        try (Connection conn = ConnectDB.openConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(INSERT_EMPLOYEE);
             PreparedStatement psUpdate = conn.prepareStatement(UPDATE_EMPLOYEE)) {
            statement.execute(DROP_TABLE_EMPLOYEE);
            statement.execute(CREATE_TABLE_EMPLOYEE);
            conn.setAutoCommit(false);
            // Run list of insert commands
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            // Run list of update commands
            // below line caused error, test transaction
            // org.postgresql.util.PSQLException: No value specified for parameter 1.
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            //psUpdate.setBigDecimal(1, new BigDecimal(999.99));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertUpdateWithTransaction(){
        try (Connection conn = ConnectDB.openConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(INSERT_EMPLOYEE);
             PreparedStatement psUpdate = conn.prepareStatement(UPDATE_EMPLOYEE)) {
            statement.execute(DROP_TABLE_EMPLOYEE);
            statement.execute(CREATE_TABLE_EMPLOYEE);
            conn.setAutoCommit(false);
            // Run list of insert commands
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            // Run list of update commands
            // below line caused error, test transaction
            // org.postgresql.util.PSQLException: No value specified for parameter 1.
            psUpdate.setBigDecimal(1, new BigDecimal(999.99));
            //psUpdate.setBigDecimal(1, new BigDecimal(999.99));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }

    }

}
