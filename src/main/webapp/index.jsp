<%--<%@ page language="java" contentType="text/html; charset=UTF-8"--%>
         <%--pageEncoding="UTF-8" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    pageContext.setAttribute("path", request.getContextPath());
%>
<html>
<head>
    <title>Canoe</title>
</head>
<body>
    <div id = 'container'>
        <div id = 'titles'>Canoe's Data System</div>
    </div>
    <div>
        <a href="${path}/person/allPerson">点击我</a>
    </div>
</body>

</html>
