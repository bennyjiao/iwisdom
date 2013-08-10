<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>邮件面板</legend>
  <table>
    <tr>
      <td>属性</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfMail.propertiesVisible}">
            <input name="prf_tab_mail_properties_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_mail_properties_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>查看</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfMail.previewVisible}">
            <input name="prf_tab_mail_preview_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_mail_preview_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>安全</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfMail.securityVisible}">
            <input name="prf_tab_mail_security_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_mail_security_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>备注</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfTab.prfMail.notesVisible}">
            <input name="prf_tab_mail_notes_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_tab_mail_notes_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>