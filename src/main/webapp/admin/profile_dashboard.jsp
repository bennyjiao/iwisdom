<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>自定义面板</legend>
  <table>
    <tr>
      <td>用户</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.userVisible}">
            <input name="prf_dashboard_user_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_user_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>邮件</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.mailVisible}">
            <input name="prf_dashboard_mail_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_mail_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>最新</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.newsVisible}">
            <input name="prf_dashboard_news_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_news_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>常用</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.generalVisible}">
            <input name="prf_dashboard_general_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_general_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>工作流</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.workflowVisible}">
            <input name="prf_dashboard_workflow_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_workflow_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>关键字</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfDashboard.keywordsVisible}">
            <input name="prf_dashboard_keywords_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_dashboard_keywords_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>