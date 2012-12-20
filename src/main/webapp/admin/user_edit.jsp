<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.openkm.servlet.admin.BaseServlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.openkm.com/tags/utils" prefix="u" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="Shortcut icon" href="favicon.ico" />
  <link rel="stylesheet" type="text/css" href="css/style.css" />
  <script src="../js/jquery-1.7.1.min.js" type="text/javascript"></script>
  <script src="../js/vanadium-min.js" type="text/javascript"></script>
  <script type="text/javascript">
    $(document).ready(function() {
    	$('form').bind('submit', function(event) {
        	var error = $('input[name="usr_id"] + span.vanadium-invalid');
    		
    		if (error == null || error.text() == '') {
        		return true;
        	} else {
        		return false;
            }
	   	});
	});
  </script>
  <title>编辑用户</title>
</head>
<body>
  <c:set var="isAdmin"><%=BaseServlet.isAdmin(request)%></c:set>
  <c:choose>
    <c:when test="${isAdmin}">
      <ul id="breadcrumb">
        <li class="path">
          <a href="Auth">用户列表</a>
        </li>
        <li class="path">
          <c:choose>
            <c:when test="${action == 'userCreate'}">创建用户</c:when>
            <c:when test="${action == 'userEdit'}">编辑用户</c:when>
            <c:when test="${action == 'userDelete'}">删除用户</c:when>
          </c:choose>
        </li>
      </ul>
      <br/>
      <form action="Auth">
        <input type="hidden" name="action" value="${action}"/>
        <input type="hidden" name="persist" value="${persist}"/>
        <table class="form" width="372px">
          <tr>
            <td>登录名</td>
            <td width="100%">
              <c:choose>
                <c:when test="${action != 'userCreate'}">
                  <input class=":required :only_on_blur" name="usr_id" value="${usr.id}" readonly="readonly"/>
                </c:when>
                <c:otherwise>
                  <input class=":required :only_on_blur :ajax;Auth?action=validateUser" name="usr_id" value=""/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <td>密码</td>
            <td>
              <c:choose>
                <c:when test="${action == 'userCreate'}">
                  <input class=":required :only_on_blur" type="password" name="usr_password" id="usr_password" value="" autocomplete="off"/>
                </c:when>
                <c:otherwise>
                  <input class="" type="password" name="usr_password" id="usr_password" value="" autocomplete="off"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <td nowrap="nowrap">确认密码</td>
            <td><input class=":same_as;usr_password :only_on_blur" type="password" value="" autocomplete="off"/></td>
          </tr>
          <tr>
            <td>用户名</td>
            <td><input class="" name="usr_name" value="${usr.name}"/></td>
          </tr>
          <tr>
            <td>邮件地址</td>
            <td><input class=":email :required :only_on_blur" name="usr_email" value="${usr.email}"/></td>
          </tr>
          <tr>
            <td>是否启用</td>
            <td>
              <c:choose>
                <c:when test="${usr.active}">
                  <input name="usr_active" type="checkbox" checked="checked"/>
                </c:when>
                <c:otherwise>
                  <input name="usr_active" type="checkbox"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <td>权限组</td>
            <td>
              <select multiple="multiple" name="usr_roles" size="10">
                <c:forEach var="role" items="${roles}">
                  <c:choose>
                    <c:when test="${u:contains(usr.roles, role)}">
                      <option value="${role.id}" selected="selected">${role.id}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${role.id}">${role.id}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </td>
          </tr>
          <tr>
            <td colspan="2" align="right">
              <input type="submit" value="确定"/>
              <input type="button" onclick="javascript:window.history.back()" value="取消"/>
              
            </td>
          </tr>
        </table>
      </form>
    </c:when>
    <c:otherwise>
      <div class="error"><h3>Only admin users allowed</h3></div>
    </c:otherwise>
  </c:choose>
</body>
</html>