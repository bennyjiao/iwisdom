<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>系统功能面板</legend>
  <table>
    <tr>
      <td>公共桌面</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.desktopVisible}">
            <input name="prf_tab_desktop_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_desktop_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>搜索</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.searchVisible}">
            <input name="prf_tab_search_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_search_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>个人仪表盘</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.dashboardVisible}">
            <input name="prf_tab_dashboard_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_dashboard_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr> 
      <td>系统管理</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.administrationVisible}">
            <input name="prf_tab_administration_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_administration_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>