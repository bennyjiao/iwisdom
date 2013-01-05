<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fieldset>
  <legend>Toolbar</legend>
  <table>
    <tr>
      <td>创建文件夹</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.createFolderVisible}">
            <input name="prf_toolbar_create_folder_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_create_folder_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>查找文件夹</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.findFolderVisible}">
            <input name="prf_toolbar_find_folder_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_find_folder_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>查找文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.findDocumentVisible}">
            <input name="prf_toolbar_find_document_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_find_document_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>下载</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.downloadVisible}">
            <input name="prf_toolbar_download_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_download_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>下载为PDF</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.downloadPdfVisible}">
            <input name="prf_toolbar_download_pdf_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_download_pdf_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>锁定</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.lockVisible}">
            <input name="prf_toolbar_lock_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_lock_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>取消锁定</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.unlockVisible}">
            <input name="prf_toolbar_unlock_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_unlock_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.addDocumentVisible}">
            <input name="prf_toolbar_add_document_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_add_document_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>签出</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.checkoutVisible}">
            <input name="prf_toolbar_checkout_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_checkout_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>签入</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.checkinVisible}">
            <input name="prf_toolbar_checkin_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_checkin_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>撤销签出</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.cancelCheckoutVisible}">
            <input name="prf_toolbar_cancel_checkout_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_cancel_checkout_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.deleteVisible}">
            <input name="prf_toolbar_delete_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_delete_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加属性组</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.addPropertyGroupVisible}">
            <input name="prf_toolbar_add_property_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_add_property_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除属性组</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.removePropertyGroupVisible}">
            <input name="prf_toolbar_remove_property_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_remove_property_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>启用工作流</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.startWorkflowVisible}">
            <input name="prf_toolbar_start_workflow_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_start_workflow_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加注解</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.addSubscriptionVisible}">
            <input name="prf_toolbar_add_subscription_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_add_subscription_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除注解</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.removeSubscriptionVisible}">
            <input name="prf_toolbar_remove_subscription_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_remove_subscription_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>刷新</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.refreshVisible}">
            <input name="prf_toolbar_refresh_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_refresh_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>主目录</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.homeVisible}">
            <input name="prf_toolbar_home_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_home_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>扫描仪</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.scannerVisible}">
            <input name="prf_toolbar_scanner_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_scanner_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>文件上传工具</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfToolbar.uploaderVisible}">
            <input name="prf_toolbar_uploader_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_toolbar_uploader_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>