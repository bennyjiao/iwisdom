<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.openkm.com/tags/utils" prefix="u" %>
<fieldset>
  <legend>文件浏览</legend>
  <table>
    <tr>
      <td>状态</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.statusVisible}">
            <input name="prf_filebrowser_status_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_status_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>元数据</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.massiveVisible}">
            <input name="prf_filebrowser_massive_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_massive_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>图标</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.iconVisible}">
            <input name="prf_filebrowser_icon_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_icon_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>名称</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.nameVisible}">
            <input name="prf_filebrowser_name_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_name_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>文件大小</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.sizeVisible}">
            <input name="prf_filebrowser_size_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_size_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>最后修改</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.lastModifiedVisible}">
            <input name="prf_filebrowser_lastmod_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_lastmod_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>作者</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.authorVisible}">
            <input name="prf_filebrowser_author_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_author_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>版本</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfFileBrowser.versionVisible}">
            <input name="prf_filebrowser_version_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_filebrowser_version_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>