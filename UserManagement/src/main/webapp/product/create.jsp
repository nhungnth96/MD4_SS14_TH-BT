
<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 8/4/2023
  Time: 12:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create new product</title>
    <style>
        .message{
            color:green;
        }
    </style>
</head>
<body>
<h1>Create new product</h1>
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
                <td><input type="text" name="name" ></td>
            </tr>
            <tr>
                <td>Price: </td>
                <td><input type="text" name="price" ></td>
            </tr>
            <tr>
                <td>Description: </td>
                <td><input type="text" name="description" ></td>
            </tr>
            <tr>
                <td>Brand: </td>
                <td><input type="text" name="brand" ></td>
            </tr>
            <tr>
                <td><input type="submit" value="Create"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>


