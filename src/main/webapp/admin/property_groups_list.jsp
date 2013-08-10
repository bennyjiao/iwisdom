<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.openkm.servlet.admin.BaseServlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="Shortcut icon" href="favicon.ico" />
  <link rel="stylesheet" type="text/css" href="css/style.css" />
  <title>Property Group</title>
</head>
<body>
  <c:set var="isAdmin"><%=BaseServlet.isAdmin(request)%></c:set>
  <c:choose>
    <c:when test="${isAdmin}">
      <ul id="breadcrumb">
        <li class="path">
          <a href="PropertyGroups">元数据组</a>
        </li>
        <li class="action">
          <a href="PropertyGroups?action=register">元数据组注册信息</a>
        </li>
        <li class="action">
          <a href="PropertyGroups?action=edit">修改元数据组</a>
        </li>
      </ul>
      <br/>
        <c:if test="${empty pGroups}">
          <table class="results" width="80%">
            <tr><th colspan="2">元数据组标签</th><th colspan="3">元数据组名称</th><th colspan="1">元数据组信息</th></tr>
            <tr><th>标签</th><th>名称</th><th>字段宽度 </th><th>字段高度</th><th>字段</th><th>其他</th></tr>
          </table>
        </c:if>
        <c:forEach var="pGroup" items="${pGroups}">
          <table class="results" width="80%">
            <tr><th colspan="2">元数据组标签</th><th colspan="3">元数据组名称</th><th colspan="1">元数据组信息</th></tr>
            <tr class="fuzzy">
              <td colspan="2" align="center"><b>${pGroup.key.label}</b></td>
              <td colspan="3" align="center"><b>${pGroup.key.name}</b></td>
              <td colspan="1" align="center">
                <i>Visible</i>: ${pGroup.key.visible}<br/>
                <i>ReadOnly</i>: ${pGroup.key.readonly}
              </td>
            </tr>
            <tr><th>标签</th><th>名称</th><th>字段宽度 </th><th>字段高度</th><th>字段</th><th>其他</th></tr>
            <c:forEach var="pgForm" items="${pGroup.value}" varStatus="row">
              <tr class="${row.index % 2 == 0 ? 'even' : 'odd'}">
                <td>${pgForm.label}</td>
                <td>${pgForm.name}</td>
                <td>${pgForm.width}</td>
                <td>${pgForm.height}</td>
                <td>${pgForm.field}</td>
                <td width="45%">${pgForm.others}</td>
              </tr>
            </c:forEach>
          </table>
          <br/>
        </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="error"><h3>Only admin users allowed</h3></div>
    </c:otherwise>
  </c:choose>
</body>
</html>