<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fieldset>
  <legend>存储</legend>
  <table>
    <tr>
      <td>公共文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.taxonomyVisible}">
            <input name="prf_stack_taxonomy_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_taxonomy_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>自定义分类</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.categoriesVisible}">
            <input name="prf_stack_categories_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_categories_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>知识库</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.thesaurusVisible}">
            <input name="prf_stack_thesaurus_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_thesaurus_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>模板</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.templatesVisible}">
            <input name="prf_stack_templates_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_templates_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>个人文档</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.personalVisible}">
            <input name="prf_stack_personal_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_personal_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>邮件</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.mailVisible}">
            <input name="prf_stack_mail_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_mail_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>回收站</td>
      <td>
        <c:choose>
          <c:when test="${prf.prfStack.trashVisible}">
            <input name="prf_stack_trash_visible" type="checkbox" checked="checked"/>
          </c:when>
          <c:otherwise>
            <input name="prf_stack_trash_visible" type="checkbox"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
</fieldset>