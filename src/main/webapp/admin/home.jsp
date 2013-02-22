<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.openkm.servlet.admin.BaseServlet" %>
<%@ page import="com.openkm.util.WarUtils"%>
<%@ page import="com.openkm.api.OKMRepository"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="Shortcut icon" href="favicon.ico" />
  <link rel="stylesheet" href="css/style.css" type="text/css" />
  <title>Main</title>
</head>
<body>
  <c:set var="isAdmin"><%=BaseServlet.isAdmin(request)%></c:set>
  <c:choose>
    <c:when test="${isAdmin}">
      <ul id="breadcrumb">
        <li class="path">
          <a href="home.jsp">iwisdom 管理控制台</a>
        </li>
      </ul>
      <br/>
      <table width="234px" class="form" style="margin-top: 25px">
        <tr><td><b>欢迎使用iwisdom管理控制台</b></td></tr>
        
      </table>
    </c:when>
    <c:otherwise>
      <div class="error"><h3>没有管理员权限</h3></div>
    </c:otherwise>
  </c:choose>
</body>
</html>