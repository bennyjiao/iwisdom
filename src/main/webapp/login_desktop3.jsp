<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.openkm.core.Config"%>
<%@ page import="com.openkm.dao.LanguageDAO"%>
<%@ page import="com.openkm.dao.bean.Language"%>
<%@ page import="com.openkm.dao.HibernateUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.openkm.com/tags/utils" prefix="u" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="Shortcut icon" href="<%=request.getContextPath() %>/favicon.ico" />
  <link rel="stylesheet" href="<%=request.getContextPath() %>/css/desktop.css" type="text/css" />
  <%
    Locale locale = request.getLocale();
    Cookie[] cookies = request.getCookies();
    String preset = null;
    
    if (cookies != null) {
      for (int i=0; i<cookies.length; i++) {
        if (cookies[i].getName().equals("lang")) {
          preset = cookies[i].getValue();
        }
      }
    }
    
    if (preset == null || preset.equals("")) {
      preset = locale.getLanguage() + "-" + locale.getCountry();
    }
  %>
  <title>iwisdom login</title>
</head>
<body onload="document.forms[0].elements[0].focus()">
  <u:constantsMap className="com.openkm.core.Config" var="Config"/>
  <div id="box">
    
    <div id="error">
      <c:if test="${not empty param.error}">
        Authentication error
        <c:if test="${Config.PRINCIPAL_ADAPTER == 'com.openkm.principal.DatabasePrincipalAdapter'}">
          (<a href="password_reset.jsp">忘记密码?</a>)
        </c:if>
      </c:if>
    </div>
  
    <div id="form">
      <form name="loginform" method="post" action="j_spring_security_check" onsubmit="setCookie()">
       <img src="" alt="logo" width="200" height="50"/><br/><br/>
        <% if (Config.SYSTEM_MAINTENANCE) { %>
          <table border="0" cellpadding="2" cellspacing="0" align="center" class="demo" style="width: 100%">
          <tr><td class="demo_alert">System under maintenance</td></tr>
          </table>
         
          <input name="j_username" id="j_username" type="hidden" value="<%=Config.SYSTEM_LOGIN_LOWERCASE?Config.ADMIN_USER.toLowerCase():Config.ADMIN_USER%>"/><br/>
        <% } else { %>
          <label for="j_username">用户名</label>
          <input name="j_username" id="j_username" type="text" <%=Config.SYSTEM_LOGIN_LOWERCASE?"onchange=\"makeLowercase();\"":""%>/><br/><br/>
        <% } %>
        <label for="j_password">密　码</label>
        <input name="j_password" id="j_password" type="password"/><br/><br/>
        <!-- 多语言选择
 
         -->
        <input type="hidden" name="j_language" id="j_language" value="zh-CN"></input>
        <input value="登录" name="submit" type="submit"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input value="重置" type="reset" name="reset" />
      </form>
    </div>
  </div>
  <% if (!Config.HIBERNATE_HBM2DDL.equals(HibernateUtil.HBM2DDL_NONE)) { %>
    <table border="0" cellpadding="2" cellspacing="0" align="center" class="demo">
      <tr><td class="demo_title">WARNING</td></tr>
      <tr><td class="demo_alert"><%=Config.PROPERTY_HIBERNATE_HBM2DDL%> = <%=Config.HIBERNATE_HBM2DDL%></td></tr>
      
      <% if (Boolean.parseBoolean(Config.HIBERNATE_CREATE_AUTOFIX)) { %>
        <tr><td class="demo_alert">But has been automatically fixed</td></tr>
      <% } else { %>
        <tr><td class="demo_alert">Need to be fixed before next restart</td></tr>
      <% } %>
    </table>
  <% } %>

  <script type="text/javascript">
    function makeLowercase() {
      var username = document.getElementById('j_username'); 
      username.value = username.value.toLowerCase();
    }

    function setCookie() {
      var exdate = new Date();
      var value = document.getElementById('j_language').value;
      exdate.setDate(exdate.getDate() + 7);
      document.cookie = "lang=" + escape(value) + ";expires=" + exdate.toUTCString();
    }
  </script>
</body>
</html>