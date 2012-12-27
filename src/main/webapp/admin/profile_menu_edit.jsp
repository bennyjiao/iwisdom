<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>Menu edit</legend>
  <table>
    <tr>
      <td>锁定</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.lockVisible}">
            <input name="prf_menu_edit_lock_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_lock_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>解锁</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.unlockVisible}">
            <input name="prf_menu_edit_unlock_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_unlock_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>签入</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.checkInVisible}">
            <input name="prf_menu_edit_check_in_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_check_in_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>签出</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.checkOutVisible}">
            <input name="prf_menu_edit_check_out_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_check_out_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>撤销签出</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.cancelCheckOutVisible}">
            <input name="prf_menu_edit_cancel_check_out_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_cancel_check_out_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.deleteVisible}">
            <input name="prf_menu_edit_delete_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_delete_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>重命名</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.renameVisible}">
            <input name="prf_menu_edit_rename_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_rename_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>复制</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.copyVisible}">
            <input name="prf_menu_edit_copy_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_copy_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>移动</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.moveVisible}">
            <input name="prf_menu_edit_move_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_move_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加注释</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.addSubscriptionVisible}">
            <input name="prf_menu_edit_add_subscription_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_add_subscription_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除注释</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.removeSubscriptionVisible}">
            <input name="prf_menu_edit_remove_subscription_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_remove_subscription_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加属性组</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.addPropertyGroupVisible}">
            <input name="prf_menu_edit_add_property_group_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_add_property_group_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除属性组</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.removePropertyGroupVisible}">
            <input name="prf_menu_edit_remove_property_group_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_remove_property_group_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加备注</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.addNoteVisible}">
            <input name="prf_menu_edit_add_note_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_add_note_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除备注</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.removeNoteVisible}">
            <input name="prf_menu_edit_remove_note_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_remove_note_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加分类</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.addCategoryVisible}">
            <input name="prf_menu_edit_add_category_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_add_category_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除分类</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.removeCategoryVisible}">
            <input name="prf_menu_edit_remove_category_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_remove_category_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>添加关键字</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.addKeywordVisible}">
            <input name="prf_menu_edit_add_keyword_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_add_keyword_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>删除关键字</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.removeKeywordVisible}">
            <input name="prf_menu_edit_remove_keyword_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_remove_keyword_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>合并为PDF</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfMenu.prfEdit.mergePdfVisible}">
            <input name="prf_menu_edit_merge_pdf_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_menu_edit_merge_pdf_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>