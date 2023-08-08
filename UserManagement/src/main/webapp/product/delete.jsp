<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 8/4/2023
  Time: 12:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete product</title>
</head>
<body>
<h1>Delete product</h1>
<p>
    <a href="/products">Back to list product</a>
</p>
<form method="post">
    <h3>Are you sure?</h3>
    <fieldset>
        <legend>Product information</legend>
        <table>
            <tr>
                <td>Name: </td>
                <td>${product.name}</td>
            </tr>
            <tr>
                <td>Price: </td>
                <td>${product.price}</td>
            </tr>
            <tr>
                <td>Description: </td>
                <td>${product.description}</td>
            </tr>
            <tr>
                <td>Brand: </td>
                <td>${product.brand}</td>
            </tr>
            <tr>
                <td><input type="submit" value="Delete"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
