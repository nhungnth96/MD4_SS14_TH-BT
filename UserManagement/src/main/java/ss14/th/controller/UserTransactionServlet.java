package ss14.th.controller;

import ss14.th.model.User;
import ss14.th.service.UserServiceTransaction;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserTransactionServlet", value = "/permission")
public class UserTransactionServlet extends HttpServlet {
    private UserServiceTransaction userServiceTransaction;

    @Override
    public void init() throws ServletException {
        userServiceTransaction = new UserServiceTransaction();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "permission":
                    addUserPermission(request, response);
                    break;
                case "test-without-tran":
                    testWithoutTran(request,response);
                case "test-with-tran":
                    testWithTran(request,response);
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void addUserPermission(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("chinhnd", "chinhnd@rikkei.academy", "vn");
        int[] permission = {1, 2, 4};
        userServiceTransaction.addUserTransaction(user, permission);
    }
    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        userServiceTransaction.insertUpdateWithoutTransaction();
    }
    private void testWithTran(HttpServletRequest request, HttpServletResponse response) {
        userServiceTransaction.insertUpdateWithTransaction();
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userServiceTransaction.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/listTransaction.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}