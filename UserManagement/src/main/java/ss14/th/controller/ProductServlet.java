package ss14.th.controller;

import ss14.th.model.Product;
import ss14.th.service.ProductService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/products")
public class ProductServlet extends HttpServlet {
        protected ProductService productService;

        @Override
        public void init() throws ServletException {
                productService = new ProductService();
        }

        protected void showListProducts(List<Product> productList, HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
                request.setAttribute("products", productList);
                request.getRequestDispatcher("/product/list.jsp").forward(request, response);
        }
        private void viewProduct(HttpServletRequest request, HttpServletResponse response)
                throws SQLException, ClassNotFoundException, ServletException, IOException {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findByID(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/product/view.jsp").forward(request, response);

        }
        protected void showCreateForm(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
                request.getRequestDispatcher("/product/create.jsp").forward(request, response);

        }
        private void createProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException {
                int id = 0;
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                String description = request.getParameter("description");
                String brand = request.getParameter("brand");
                Product product = new Product(id, name, price, description, brand);
                productService.save(product);
                request.setAttribute("message", "New product was created");
                try {
                        request.getRequestDispatcher("/product/create.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                        e.printStackTrace();
                }

        }
        protected void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, ServletException, IOException {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findByID(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/product/edit.jsp").forward(request, response);
                }
        protected void updateProduct(HttpServletRequest request, HttpServletResponse response)
                throws SQLException, ClassNotFoundException, ServletException, IOException {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                String description = request.getParameter("description");
                String brand = request.getParameter("brand");
                Product product = new Product(id,name,price,description,brand);
                productService.save(product);
                request.setAttribute("product", product);
                request.setAttribute("message", "Product information was updated");
                request.getRequestDispatcher("/product/edit.jsp").forward(request, response);
        }


        protected void showDeleteForm(HttpServletRequest request, HttpServletResponse response)
                throws SQLException, ClassNotFoundException, ServletException, IOException {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findByID(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/product/delete.jsp").forward(request, response);
        }
        protected void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findByID(id);
                if (product == null) {
                        request.getRequestDispatcher("error-404.jsp");
                } else {
                        productService.delete(id);
                        response.sendRedirect("/products");
                }
        }



        //        private void searchProductByName(HttpServletRequest request, HttpServletResponse response){
//                String keyword = request.getParameter("search");
//                List<Product> searchList = new ArrayList<>();
//                for (Product product : productList) {
//                        if(product.getName().toLowerCase().contains(keyword.toLowerCase())){
//                                searchList.add(product);
//                        }
//                }
//                request.setAttribute("searchName",keyword);
//                request.setAttribute("products",searchList);
//                try {
//                        request.getRequestDispatcher("/product/list.jsp").forward(request,response);
//                } catch (ServletException | IOException e){
//                        e.printStackTrace();
//                }
//        }
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String action = request.getParameter("action");
                if (action == null) {
                        action = "";
                }
                try {
                        switch (action) {
                                case "create":
                                        showCreateForm(request, response);
                                        break;
                                case "edit":
                                        showEditForm(request, response);
                                        break;
                                case "delete":
                                        showDeleteForm(request, response);
                                        break;
                                case "view":
                                        viewProduct(request, response);
                                        break;
                                case "Search":
//                                       searchProductByName(request, response);
                                        break;
                                default:
                                        break;
                        }
                        showListProducts(productService.getAll(), request, response);
                } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                }
        }






        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String action = request.getParameter("action");
                if (action == null) {
                        action = "";
                }
                try {
                        switch (action) {
                                case "create":
                                        createProduct(request, response);
                                        break;
                                case "edit":
                                        updateProduct(request, response);
                                        break;
                                case "delete":
                                        deleteProduct(request, response);
                                        break;
                                default:
                                       break;
                        }
                        showListProducts(productService.getAll(),request,response);
                } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                }

        }
}