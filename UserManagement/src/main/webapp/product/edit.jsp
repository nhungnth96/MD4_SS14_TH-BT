
<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 8/4/2023
  Time: 12:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit product</title>
    <style>
        .message{
            color:green;
        }
    </style>
</head>
<body>
<h1>Edit product</h1>
<p>
    <c:if test='${message != null}'>
        <span class="message">${message}</span>
    </c:if>
</p>
<p>
    <a href="/products">Back to list product</a>
</p>
<form method="post">
    <fieldset>
        <legend>Product information</legend>
        <table>
            <tr>
                <td>Name: </td>
                <td><input type="text" name="name" value="${product.name}"></td>
            </tr>
            <tr>
                <td>Price: </td>
                <td><input type="text" name="price" value="${product.price}"></td>
            </tr>
            <tr>
                <td>Description: </td>
                <td><input type="text" name="description" value="${product.description}"></td>
            </tr>
            <tr>
                <td>Brand: </td>
                <td><input type="text" name="brand" value="${product.brand}"></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
