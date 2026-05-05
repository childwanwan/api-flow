<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API接口网关中台</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body>
<div class="layout">
    <div class="layout-header">
        <div class="header-left">
            <button class="sidebar-toggle" onclick="toggleSidebar()" title="展开/收起菜单">
                <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor"><path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/></svg>
            </button>
            <div class="logo">
                <svg viewBox="0 0 24 24" fill="currentColor"><path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/></svg>
                API接口网关中台
            </div>
        </div>
        <div class="header-right">
            <div class="user-dropdown">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="#909399" style="flex-shrink:0;"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
                <span class="username">${username!}</span>
                <span class="arrow">▼</span>
                <div class="user-dropdown-menu">
                    <a href="${request.contextPath}/logout">退出登录</a>
                </div>
            </div>
        </div>
    </div>
    <div class="layout-body">
        <div class="layout-sidebar" id="sidebar">
            <ul class="sidebar-menu">
                <li class="menu-item active" data-menu="dashboard">
                    <a href="javascript:void(0);" onclick="openTab('dashboard','${request.contextPath}/dashboard','运行报表',this)">
                        <span class="menu-icon"><svg viewBox="0 0 24 24"><path d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z"/></svg></span>
                        <span class="menu-text">运行报表</span>
                    </a>
                </li>
                <li class="menu-item" data-menu="api-group,api-config">
                    <a href="javascript:void(0);" onclick="toggleSubmenu(this)">
                        <span class="menu-icon"><svg viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/></svg></span>
                        <span class="menu-text">API管理</span>
                        <span class="menu-arrow">▶</span>
                    </a>
                    <ul class="submenu">
                        <li class="menu-item" data-menu="api-group"><a href="javascript:void(0);" onclick="openTab('api-group','${request.contextPath}/api-group','分组管理',this)">分组管理</a></li>
                        <li class="menu-item" data-menu="api-config"><a href="javascript:void(0);" onclick="openTab('api-config','${request.contextPath}/api-config','API配置',this)">API配置</a></li>
                    </ul>
                </li>
                <li class="menu-item" data-menu="task,task-log">
                    <a href="javascript:void(0);" onclick="toggleSubmenu(this)">
                        <span class="menu-icon"><svg viewBox="0 0 24 24"><path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/></svg></span>
                        <span class="menu-text">任务中心</span>
                        <span class="menu-arrow">▶</span>
                    </a>
                    <ul class="submenu">
                        <li class="menu-item" data-menu="task"><a href="javascript:void(0);" onclick="openTab('task','${request.contextPath}/task','任务管理',this)">任务管理</a></li>
                        <li class="menu-item" data-menu="task-log"><a href="javascript:void(0);" onclick="openTab('task-log','${request.contextPath}/task-log','调度日志',this)">调度日志</a></li>
                    </ul>
                </li>
                <li class="menu-item" data-menu="user,alarm,plugin,operation-log">
                    <a href="javascript:void(0);" onclick="toggleSubmenu(this)">
                        <span class="menu-icon"><svg viewBox="0 0 24 24"><path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"/></svg></span>
                        <span class="menu-text">系统管理</span>
                        <span class="menu-arrow">▶</span>
                    </a>
                    <ul class="submenu">
                        <li class="menu-item" data-menu="user"><a href="javascript:void(0);" onclick="openTab('user','${request.contextPath}/user','用户管理',this)">用户管理</a></li>
                        <li class="menu-item" data-menu="alarm"><a href="javascript:void(0);" onclick="openTab('alarm','${request.contextPath}/alarm','告警管理',this)">告警管理</a></li>
                        <li class="menu-item" data-menu="plugin"><a href="javascript:void(0);" onclick="openTab('plugin','${request.contextPath}/plugin','插件管理',this)">插件管理</a></li>
                        <li class="menu-item" data-menu="operation-log"><a href="javascript:void(0);" onclick="openTab('operation-log','${request.contextPath}/operation-log','操作日志',this)">操作日志</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="layout-main">
            <iframe id="mainIframe" src="${request.contextPath}/dashboard" width="100%" height="100%" frameborder="0" style="border:none;min-height:calc(100vh - 50px);"></iframe>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
var sidebarCollapsed = false;

function toggleSidebar() {
    sidebarCollapsed = !sidebarCollapsed;
    var sidebar = document.getElementById('sidebar');
    if (sidebarCollapsed) {
        sidebar.classList.add('collapsed');
    } else {
        sidebar.classList.remove('collapsed');
    }
}

function toggleSubmenu(el) {
    el.parentElement.classList.toggle('open');
}

function openTab(id, url, title, el) {
    document.getElementById('mainIframe').src = url;
    document.querySelectorAll('.sidebar-menu .menu-item').forEach(function(item) {
        item.classList.remove('active');
    });
    if (el) {
        el.parentElement.classList.add('active');
        var parentSubmenu = el.closest('.submenu');
        if (parentSubmenu) {
            parentSubmenu.parentElement.classList.add('open');
        }
    }
}

var urlParams = new URLSearchParams(window.location.search);
var initPage = urlParams.get('page');
if (initPage) {
    var menuItems = document.querySelectorAll('.sidebar-menu .menu-item[data-menu]');
    for (var i = 0; i < menuItems.length; i++) {
        var menus = menuItems[i].getAttribute('data-menu');
        if (menus && menus.split(',').indexOf(initPage) >= 0) {
            var link = menuItems[i].querySelector('a');
            if (link) link.click();
            break;
        }
    }
}
</script>
</body>
</html>
