<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>Menu file</legend>
  <table>
    <tr>
      <td>创建文件夹</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.createFolderVisible}">
            <input name="prf_menu_file_create_folder_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_create_folder_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>查找文件夹</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.findFolderVisible}">
            <input name="prf_menu_file_find_folder_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_find_folder_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>查找文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.findDocumentVisible}">
            <input name="prf_menu_file_find_document_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_find_document_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>Similar documents</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.similarDocumentVisible}">
            <input name="prf_menu_file_similar_document_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_similar_document_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>Go folder</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.goFolderVisible}">
            <input name="prf_menu_file_go_folder_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_go_folder_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>下载</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.downloadVisible}">
            <input name="prf_menu_file_download_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_download_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>下载为PDF</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.downloadPdfVisible}">
            <input name="prf_menu_file_download_pdf_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_download_pdf_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>新建文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.addDocumentVisible}">
            <input name="prf_menu_file_add_document_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_add_document_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>清除</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.purgeVisible}">
            <input name="prf_menu_file_purge_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_purge_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>清除回收站</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.purgeTrashVisible}">
            <input name="prf_menu_file_purge_trash_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_purge_trash_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>恢复</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.restoreVisible}">
            <input name="prf_menu_file_restore_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_restore_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>启用工作流</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.startWorkflowVisible}">
            <input name="prf_menu_file_start_workflow_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_start_workflow_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>刷新</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.refreshVisible}">
            <input name="prf_menu_file_refresh_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_refresh_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>扫描仪</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.scannerVisible}">
            <input name="prf_menu_file_scanner_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_scanner_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>文件上传工具</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.uploaderVisible}">
            <input name="prf_menu_file_uploader_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_uploader_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>导出ZIP</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.exportVisible}">
            <input name="prf_menu_file_export_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_export_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>按照模板创建</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.createFromTemplateVisible}">
            <input name="prf_menu_file_create_from_template_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_create_from_template_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>发送文档链接</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.sendDocumentLinkVisible}">
            <input name="prf_menu_file_send_document_link_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_send_document_link_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>以附件形式发送文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfFile.sendDocumentAttachmentVisible}">
            <input name="prf_menu_file_send_document_attachment_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_file_send_document_attachment_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>