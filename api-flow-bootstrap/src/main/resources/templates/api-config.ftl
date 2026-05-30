<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API配置</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/resizable-columns.css">
    <style>
        .desc-cell {
            max-width: 220px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            cursor: default;
        }
        .desc-tooltip {
            position: fixed;
            z-index: 9999;
            max-width: 360px;
            padding: 8px 12px;
            background: var(--text-primary, #303133);
            color: #fff;
            font-size: 13px;
            line-height: 1.6;
            border-radius: var(--radius-sm, 4px);
            box-shadow: var(--shadow-md, 0 4px 16px rgba(0,0,0,0.12));
            word-break: break-all;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.15s ease;
        }
        .desc-tooltip.visible {
            opacity: 1;
        }
        .desc-tooltip::after {
            content: '';
            position: absolute;
            top: -5px;
            left: 20px;
            width: 0;
            height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-bottom: 5px solid var(--text-primary, #303133);
        }
    </style>
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
    <script src="${request.contextPath}/static/js/resizable-columns.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>API配置</h2>
        <div class="page-actions"><button class="btn btn-primary" onclick="window.location.href=contextPath+'/api-config/create'">+ 新建API</button></div>
    </div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item">
                <label>所属分组:</label>
                <select id="searchGroupNo" class="select"><option value="">全部</option></select>
            </div>
            <div class="search-item">
                <label>API编码:</label>
                <input type="text" id="searchApiCode" class="input" placeholder="请输入API编码">
            </div>
            <div class="search-item">
                <label>API名称:</label>
                <input type="text" id="searchApiName" class="input" placeholder="请输入API名称">
            </div>
            <div class="search-item">
                <label>状态:</label>
                <select id="searchStatus" class="select">
                    <option value="">全部</option>
                    <option value="ENABLED">启用</option>
                    <option value="DISABLED">禁用</option>
                </select>
            </div>
            <div class="search-actions">
                <button class="btn" onclick="resetSearch()">重置</button>
                <button class="btn btn-primary" onclick="searchConfigs()">搜索</button>
            </div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table">
            <thead>
            <tr>
                <th class="sortable" onclick="sortBy('API_CODE', 0)">API编码<span id="sort-API_CODE" class="sort-icon">↕</span></th>
                <th class="sortable" onclick="sortBy('API_NAME', 1)">API名称<span id="sort-API_NAME" class="sort-icon">↕</span></th>
                <th>分组</th>
                <th>接口地址</th>
                <th>状态</th>
                <th class="sortable" onclick="sortBy('CREATE_TIME_MS', 5)">创建时间<span id="sort-CREATE_TIME_MS" class="sort-icon active">↓</span></th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="configTableBody">
            <tr><td colspan="7" class="empty-row">暂无数据</td></tr>
            </tbody>
        </table>
        <div style="padding:0 16px;">
            <div class="pagination-wrap" id="configPagination"></div>
        </div>
    </div>
</div>

<div id="confirmModal" class="modal-overlay" style="display:none;">
    <div class="modal" style="width:400px;">
        <div class="modal-header">
            <h3 id="confirmTitle">确认操作</h3>
            <button class="modal-close" onclick="hideConfirm()">&times;</button>
        </div>
        <div class="modal-body">
            <div class="confirm-dialog">
                <p id="confirmMessage" class="confirm-message"></p>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="hideConfirm()">取消</button>
            <button id="confirmBtn" class="btn btn-danger" onclick="executeConfirm()">确定</button>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-config';}
var BASE = contextPath + '/api/v1/config';
var urlGroupNo = new URLSearchParams(window.location.search).get('groupNo');

var configPagination = new Pagination({
    el: 'configPagination',
    total: 0,
    current: 1,
    pageSize: 20,
    pageSizes: [10, 20, 30, 50, 100, 200],
    onChange: function(page, size) {
        loadConfigs();
    }
});
var sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 5}];

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function statusTag(s) { var m={ENABLED:'enabled',DISABLED:'disabled'}; var t={ENABLED:'启用',DISABLED:'禁用'}; return '<span class="status-tag status-'+(m[s]||'disabled')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }

function updateSortIcons() {
    document.querySelectorAll('.sort-icon').forEach(function(icon) {
        icon.className = 'sort-icon';
        icon.textContent = '↕';
    });
    sortOrderList.forEach(function(order) {
        var icon = document.getElementById('sort-' + order.field);
        if (icon) {
            icon.className = 'sort-icon active';
            icon.textContent = order.ascending ? '↑' : '↓';
        }
    });
}

function sortBy(field, order) {
    var existingIndex = sortOrderList.findIndex(function(o) { return o.field === field; });
    if (existingIndex >= 0) {
        sortOrderList[existingIndex].ascending = !sortOrderList[existingIndex].ascending;
    } else {
        sortOrderList = [{field: field, ascending: true, order: order}];
    }
    updateSortIcons();
    loadConfigs();
}

function loadGroups() {
    fetch(contextPath+'/api/v1/group/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var opts = '<option value="">全部</option>';
            (res.data||[]).forEach(function(g){ opts+='<option value="'+g.groupNo+'">'+g.groupName+'('+g.groupCode+')</option>'; });
            document.getElementById('searchGroupNo').innerHTML = opts;
            if (urlGroupNo) {
                document.getElementById('searchGroupNo').value = urlGroupNo;
                loadConfigs();
                urlGroupNo = null;
            }
        }
    }).catch(function(e){
        console.error('Failed to load groups:', e);
    });
}

function loadConfigs() {
    var apiCode = document.getElementById('searchApiCode').value;
    var apiName = document.getElementById('searchApiName').value;
    var groupNo = document.getElementById('searchGroupNo').value;
    var status = document.getElementById('searchStatus').value;
    var state = configPagination.getState();
    var requestData = {
        groupNo: groupNo || undefined,
        apiCodeLike: apiCode || undefined,
        apiNameLike: apiName || undefined,
        status: status || undefined,
        current: state.current,
        size: state.pageSize,
        sortOrderList: sortOrderList
    };
    fetch(BASE + '/page', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requestData)
    }).then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var pageData = res.data;
            configPagination.update(pageData.total || 0, pageData.current || state.current);
            renderConfigs(pageData.records || []);
        }
    });
}

function renderConfigs(configs) {
    var tbody = document.getElementById('configTableBody');
    if(!configs.length) { tbody.innerHTML='<tr><td colspan="7" class="empty-row"><div class="empty-guide"><div class="empty-icon"><svg viewBox="0 0 24 24"><path d="M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm4 18H6V4h7v5h5v11zM8 15.01l1.41 1.41L11 14.84V19h2v-4.16l1.59 1.59L16 15.01 12.01 11z"/></svg></div><div class="empty-text">暂无API配置，点击新建开始创建</div><button class="btn btn-primary" onclick="window.location.href=contextPath+\'/api-config/create\'">+ 新建API</button></div></td></tr>'; configPagination.update(0); return; }
    tbody.innerHTML = configs.map(function(c){
        var ec = c.extraConfig || {};
        var targetUrl = ec.targetUrl || '-';
        var groupNameDisplay = c.groupName && c.groupCode ? c.groupName+'('+c.groupCode+')' : '-';
        return '<tr>'+
            '<td class="desc-cell" data-desc="'+c.apiCode+'">'+
                '<a href="javascript:void(0)" onclick="viewDetail(\''+c.apiCode+'\')" class="api-code-link">'+c.apiCode+'</a>'+
            '</td>'+
            '<td class="desc-cell" data-desc="'+(c.apiName||'')+'">'+(c.apiName||'')+'</td>'+
            '<td class="desc-cell" data-desc="'+groupNameDisplay+'">'+groupNameDisplay+'</td>'+
            '<td class="desc-cell" data-desc="'+(ec.targetUrl||'')+'">'+targetUrl+'</td>'+
            '<td>'+statusTag(c.status||'ENABLED')+'</td>'+
            '<td>'+formatTime(c.createTimeMs)+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editConfig(\''+c.apiCode+'\')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteConfig(\''+c.apiCode+'\')">删除</button>'+
                '<span class="action-divider">|</span>'+
                '<span class="dropdown" id="more_'+c.apiCode+'">'+
                    '<button class="btn-text btn-more" onclick="toggleDropdown(\''+c.apiCode+'\', event)">⋯</button>'+
                    '<div class="dropdown-menu" onclick="event.stopPropagation()">'+
                        '<button class="dropdown-item" onclick="toggleConfigStatus(\''+c.apiCode+'\',\''+c.status+'\')">'+(c.status==='ENABLED'?'禁用':'启用')+'</button>'+
                        '<button class="dropdown-item" onclick="viewChangeLog(\''+c.apiCode+'\')">变更日志</button>'+
                    '</div>'+
                '</span>'+
            '</td></tr>';
    }).join('');
}

var openDropdownId = null;
function closeDropdown() {
    if (openDropdownId) {
        var prev = document.getElementById(openDropdownId);
        if (prev) {
            var m = prev.querySelector('.dropdown-menu');
            if (m) { m.style.cssText = ''; }
        }
        openDropdownId = null;
    }
}
function toggleDropdown(code, event) {
    if (event) event.stopPropagation();
    var el = document.getElementById('more_'+code);
    if (!el) return;
    var wasOpen = openDropdownId === 'more_'+code;
    closeDropdown();
    if (!wasOpen) {
        var menu = el.querySelector('.dropdown-menu');
        var rect = el.getBoundingClientRect();
        menu.style.cssText = 'position:fixed;display:block;top:'+(rect.bottom+4)+'px;left:'+Math.min(rect.left,window.innerWidth-160)+'px;right:auto;z-index:9999;';
        openDropdownId = 'more_'+code;
    }
}

document.addEventListener('click', function(e) {
    closeDropdown();
});

function searchConfigs() { configPagination.update(0, 1); loadConfigs(); }
function resetSearch() { document.getElementById('searchApiCode').value=''; document.getElementById('searchApiName').value=''; document.getElementById('searchGroupNo').value=''; document.getElementById('searchStatus').value=''; configPagination.update(0, 1); sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 5}]; updateSortIcons(); loadConfigs(); }
document.getElementById('searchApiCode').addEventListener('keydown', function(e) { if (e.key === 'Enter') searchConfigs(); });
document.getElementById('searchApiName').addEventListener('keydown', function(e) { if (e.key === 'Enter') searchConfigs(); });

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g,'&amp;').replace(/"/g,'&quot;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function tryParseJson(str) {
    if (!str || !str.trim()) return null;
    try { return JSON.parse(str); } catch(e) { return str; }
}

function viewDetail(apiCode) {
    window.location.href = contextPath + '/api-config/' + apiCode;
}

function editConfig(apiCode) {
    window.location.href = contextPath + '/api-config/' + apiCode + '/edit?from=list';
}

var confirmCallback = null;
function showConfirm(msg, onConfirm) {
    document.getElementById('confirmMessage').textContent = msg;
    confirmCallback = onConfirm;
    document.getElementById('confirmModal').style.display = 'flex';
}
function hideConfirm() {
    document.getElementById('confirmModal').style.display = 'none';
    confirmCallback = null;
}
function executeConfirm() {
    if (typeof confirmCallback === 'function') {
        confirmCallback();
    }
    hideConfirm();
}

function deleteConfig(apiCode) {
    showConfirm('确定要删除该API配置吗？此操作不可恢复。', function() {
        fetch(BASE+'/'+apiCode,{method:'DELETE'}).then(function(r){return r.json();}).then(function(res){
            if(res.success){showToast('删除成功','success');loadConfigs();}
            else showToast(res.error&&res.error.message||'删除失败','error');
        });
    });
}

function toggleConfigStatus(apiCode,currentStatus) {
    closeDropdown();
    var newStatus=currentStatus==='ENABLED'?'DISABLED':'ENABLED';
    var label=newStatus==='ENABLED'?'启用':'禁用';
    showConfirm('确定要'+label+'该API配置吗？', function() {
        fetch(BASE+'/'+apiCode+'/'+(newStatus==='ENABLED'?'enable':'disable'),{method:'POST'}).then(function(r){return r.json();}).then(function(res){
            if(res.success){showToast('操作成功','success');loadConfigs();}
            else showToast(res.error&&res.error.message||'操作失败','error');
        });
    });
}

function viewChangeLog(apiCode) { closeDropdown(); parent.openTab('config-log',contextPath+'/config-log?apiCode='+apiCode,'配置变更日志',null); }

document.getElementById('confirmModal').addEventListener('click', function(e){ if(e.target===this) hideConfirm(); });

var descTooltip = document.createElement('div');
descTooltip.className = 'desc-tooltip';
document.body.appendChild(descTooltip);

document.getElementById('configTableBody').addEventListener('mouseenter', function(e) {
    var cell = e.target.closest('.desc-cell');
    if (!cell) return;
    if (cell.scrollWidth <= cell.clientWidth) return;
    descTooltip.textContent = cell.getAttribute('data-desc');
    var rect = cell.getBoundingClientRect();
    descTooltip.style.left = rect.left + 'px';
    descTooltip.style.top = (rect.bottom + 6) + 'px';
    descTooltip.classList.add('visible');
}, true);

document.getElementById('configTableBody').addEventListener('mouseleave', function(e) {
    var cell = e.target.closest('.desc-cell');
    if (!cell) return;
    descTooltip.classList.remove('visible');
}, true);

updateSortIcons();
loadGroups();
if (!urlGroupNo) {
    loadConfigs();
}
ResizableColumns.init(document.querySelector('.data-table'), {pageKey: '/api-config'});
</script>
</body>
</html>
