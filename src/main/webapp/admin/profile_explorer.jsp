<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>浏览器</legend>
  <table>
    <tr>
      <td>类型过滤器</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfExplorer.typeFilterEnabled}">
            <input name="prf_explorer_type_filter_enabled" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_explorer_type_filter_enabled" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>