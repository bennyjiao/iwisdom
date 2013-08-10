<%@ page import="com.openkm.core.Config"%>
<%@ page import="com.openkm.servlet.admin.BaseServlet"%>
<%@ page import="com.openkm.extension.dao.ExtensionDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- http://stackoverflow.com/questions/1708054/center-ul-li-into-div -->
<div style="text-align: center">
  <ul style="display: inline-block;">
    <li >
      <a target="frame" href="home.jsp" title="管理首页">
        <img src="img/toolbar/home.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="Config"  title="系统参数配置">
        <img src="img/toolbar/config.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="MimeType" title="Mime类型管理">
        <img src="img/toolbar/mime.png">
      </a>
    </li>
    <li>
      <a target="frame" href="stats.jsp" title="系统状态">
        <img src="img/toolbar/stats.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="scripting.jsp" title="执行脚本">
        <img src="img/toolbar/scripting.png">
      </a>
    </li>
    <li>
      <a target="frame" href="PropertyGroups" title="元数据">
        <img src="img/toolbar/properties.png">
      </a>
    </li>
    <li>
      <a target="frame" href="Auth" title="用户管理">
        <img src="img/toolbar/users.png">
      </a>
    </li>
    <li>
      <a target="frame" href="Profile"  title="权限管理">
        <img src="img/toolbar/profile.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="DatabaseQuery" title="数据查询">
        <img src="img/toolbar/database.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="Report" title="报表">
        <img src="img/toolbar/report.png" title="报表">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="ActivityLog" title="日志">
        <img src="img/toolbar/activity.png">
      </a>
    </li>
    <li >
      <a target="frame" href="Workflow" title="工作流">
        <img src="img/toolbar/workflow.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="Automation" title="Automation">
        <img src="img/toolbar/automation.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="CronTab" title="Crontab">
        <img src="img/toolbar/crontab.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="generate_thesaurus.jsp" title="知识树">
        <img src="img/toolbar/thesaurus.png">
      </a>
    </li>
    <li style="display:none;">
      <a target="frame" href="Language" title="多语言">
        <img src="img/toolbar/language.png">
      </a>
    </li>
    <li>
      <a target="frame" href="repository_import.jsp" title="批量导入">
        <img src="img/toolbar/import.png">
      </a>
    </li>
    <li>
      <a target="frame" href="repository_export.jsp" title="批量导出">
        <img src="img/toolbar/export.png">
       </a>
    </li>
    <li >
      <a target="frame" href="utilities.jsp" title="工具">
        <img src="img/toolbar/utilities.png">
       </a>
    </li>
    <script type="text/javascript">
      // Identify if being loaded inside an iframe
      if (self == top) {
         document.write('&nbsp;\n');
         document.write('<li>\n');
         document.write('<a href="logout.jsp" title="Exit"><img src="img/toolbar/exit.png"></a>\n');
         document.write('</li>\n');
      }
    </script>
  </ul>
</div>