<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>文件夹面板</legend>
  <table>
    <tr>
      <td>属性</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfFolder.propertiesVisible}">
            <input name="prf_tab_folder_properties_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_folder_properties_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>安全</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfFolder.securityVisible}">
            <input name="prf_tab_folder_security_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_folder_security_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>备注</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfFolder.notesVisible}">
            <input name="prf_tab_folder_notes_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_folder_notes_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>