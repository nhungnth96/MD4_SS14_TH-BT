<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 8/4/2023
  Time: 12:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List Product</title>
</head>
<body>
<h1>Products</h1>
<p>
    <a href="/products?action=create">Create new product</a>
</p>
<form action="/products">
    <input type="text" name="search" placeholder="search" value="${searchName}" >
    <input type="submit" value="Search" name="action">
</form>
<table border="1">
   <thead>
       <tr>
    <th>STT</th>
    <th>Name</th>
    <th>Price</th>
    <th>Description</th>
    <th>Brand</th>
    <th colspan="3">Action</th>
    </tr>
   </thead>
    <tbody>
    <c:forEach items='${products}' var="product" varStatus="iterator">
        <tr>
            <td>${iterator.count}</td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.description}</td>
            <td>${product.brand}</td>
            <td><a href="/products?action=view&id=${product.id}">VIEW</a></td>
            <td><a href="/products?action=edit&id=${product.id}">EDIT</a></td>
            <td><a href="/products?action=delete&id=${product.id}">DELETE</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

