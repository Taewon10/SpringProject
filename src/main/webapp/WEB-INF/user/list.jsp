<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>list</title>
<link rel="stylesheet" href="../css/list.css">
</head>
<body>

<table>
    <thead>
        <tr>
            <th>이름</th>
            <th>아이디</th>
            <th>비밀번호</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="userDTO" items="${map2.list}">
            <tr>
                <td>${userDTO.name}</td>
                <td><a href="/spring/user/updateForm?id=${userDTO.id}&pg=${map2.pg }">${userDTO.id}</a></td>
                <td>${userDTO.pwd}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<div id="pagingDiv">
 ${map2.userPaging.pagingHTML }</div>
</body>
</html>