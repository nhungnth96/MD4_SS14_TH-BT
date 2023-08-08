package ss14.th.service;

import ss14.th.model.Product;
import ss14.th.model.User;
import ss14.th.util.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IGenericService<Product,Integer> {
    // ====== QUERY SQL ======
    private final String GET_ALL = "select * from demojdbc.products";
    private final String FIND_BY_ID = "select * from demojdbc.products where id = ?";
    private final String DELETE = "delete from demojdbc.products where id = ?";
    private final String INSERT = "insert into demojdbc.products(name,price,description,brand) values (?,?,?,?)";
    private final String UPDATE = "update demojdbc.products set name=?,price=?,description=?,brand=? where id=?";
    private final String CALL_GET_ALL = "call getAll(?)";
    private final String CALL_FIND_BY_ID = "call findByID(?)";
    private final String CALL_INSERT = "call insert()";
    private final String CALL_UPDATE = "call update()";

    // ======= END SQL ======
    @Override
    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        List<Product> productList = new ArrayList<>();
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt = con.prepareStatement(GET_ALL);
        ResultSet rs = preSt.executeQuery();
        while (rs.next()){
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(Double.parseDouble(rs.getString("price")));
            product.setDescription(rs.getString("description"));
            product.setBrand(rs.getString("brand"));
            productList.add(product);
        }
        ConnectDB.closeConnection(con);
        return productList;
    }

    @Override
    public Product findByID(Integer id) throws SQLException, ClassNotFoundException {
        Product product = null;
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt = con.prepareStatement(FIND_BY_ID);
        preSt.setInt(1,id);
        ResultSet rs = preSt.executeQuery();
        while (rs.next()){
            product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(Double.parseDouble(rs.getString("price")));
            product.setDescription(rs.getString("description"));
            product.setBrand(rs.getString("brand"));
        }
        ConnectDB.closeConnection(con);
        return product;
    }

    @Override
    public void save(Product product) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt;
        // id trong DB luôn bắt đầu từ 1
        // nếu id = 0 -> mặc định thêm mới, id tự tăng theo DB
        // -> ADD
        if(product.getId()==0){
            preSt = con.prepareStatement(INSERT);
            preSt.setString(1, product.getName());
            preSt.setDouble(2, product.getPrice());
            preSt.setString(3, product.getDescription());
            preSt.setString(4, product.getBrand());
            preSt.executeUpdate();
        }
        else
        // UPDATE
        {
            preSt = con.prepareStatement(UPDATE);
            preSt.setString(1, product.getName());
            preSt.setDouble(2, product.getPrice());
            preSt.setString(3, product.getDescription());
            preSt.setString(4, product.getBrand());
            preSt.setInt(5, product.getId());
            preSt.executeUpdate();
        }
        ConnectDB.closeConnection(con);
    }

    @Override
    public void delete(Integer id) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.openConnection();
        PreparedStatement preSt = con.prepareStatement(DELETE);
        preSt.setInt(1,id);
        preSt.executeUpdate();
        ConnectDB.closeConnection(con);
    }
}
