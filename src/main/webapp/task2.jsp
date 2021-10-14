<%--
  Created by IntelliJ IDEA.
  User: nik20
  Date: 09.10.2021
  Time: 21:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="get" action="/task2Servlet">
    <table>
        <tr>
            <td>Message(one, two, three)</td>
            <td><input type="text" name="message"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Submit"></td>
        </tr>
    </table>
    <h3>${message}</h3>
</form>
</body>
</html>
