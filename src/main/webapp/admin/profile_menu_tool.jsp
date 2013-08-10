<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>工具菜单项目</legend>
  <table>
  <!-- 
    <tr>
      <td>多语言</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfTool.languagesVisible}">
            <input name="prf_menu_tool_languages_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_tool_languages_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
     -->
    <tr>
      <td>皮肤</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfTool.skinVisible}">
            <input name="prf_menu_tool_skin_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_tool_skin_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <!-- 
    <tr>
      <td>调试</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfTool.debugVisible}">
            <input name="prf_menu_tool_debug_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_tool_debug_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    -->
    <tr>
      <td>管理</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfTool.administrationVisible}">
            <input name="prf_menu_tool_administration_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_tool_administration_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
     
    <tr>
      <td>用户配置</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfTool.preferencesVisible}">
            <input name="prf_menu_tool_preferences_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_tool_preferences_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>