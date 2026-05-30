<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>操作日志</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/resizable-columns.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
    <script src="${request.contextPath}/static/js/resizable-columns.js"></script>
    <style>
        .sortable { cursor: pointer; user-select: none; }
        .sortable:hover { background-color: #f5f5f5; }
        .sort-icon { margin-left: 4px; opacity: 0.3; }
        .sort-icon.active { opacity: 1; }
        .detail-cell { max-width:400px; white-space:pre-line; word-break:break-all; line-height:1.5; }
    </style>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header"><h2>操作日志</h2></div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item"><label>业务编码:</label><input type="text" id="searchBizCode" class="input" placeholder="请输入业务编码"></div>
            <div class="search-item"><label>业务类型:</label><input type="text" id="searchLogType" class="input" placeholder="请输入业务类型"></div>
            <div class="search-item"><label>操作人:</label><input type="text" id="searchOperator" class="input" placeholder="请输入操作人"></div>
            <div class="search-item"><label>操作时间:</label>
                <input type="date" id="searchTimeStart" class="input"> ~ <input type="date" id="searchTimeEnd" class="input">
            </div>
            <div class="search-actions"><button class="btn" onclick="resetSearch()">重置</button><button class="btn btn-primary" onclick="searchLogs()">搜索</button></div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table"><thead><tr>
            <th class="sortable" onclick="sortBy('CREATE_TIME_MS', 0)">操作时间<span id="sort-CREATE_TIME_MS" class="sort-icon active">↓</span></th>
            <th>业务编码</th>
            <th>业务类型</th>
            <th>操作人</th>
            <th>操作详情</th>
        </tr></thead>
        <tbody id="logTableBody"><tr><td colspan="5" class="empty-row">暂无数据</td></tr></tbody></table>
        <div style="padding:0 16px;"><div class="pagination-wrap" id="logPagination"></div></div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=operation-log';}
var BASE = contextPath + '/api/v1/operation-log';
var sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 0}];

var logPagination = new Pagination({
    el: 'logPagination',
    total: 0,
    current: 1,
    pageSize: 20,
    pageSizes: [10, 20, 30, 50, 100, 200],
    onChange: function(page, size) {
        loadLogs();
    }
});

function formatTime(ms) {
    if(!ms) return '-';
    var d=new Date(ms);
    return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0');
}

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
        sortOrderList.push({field: field, ascending: true, order: order});
    }
    sortOrderList.sort(function(a, b) { return (a.order || 999) - (b.order || 999); });
    updateSortIcons();
    loadLogs();
}

function loadLogs() {
    var bizCode = document.getElementById('searchBizCode').value;
    var logType = document.getElementById('searchLogType').value;
    var operator = document.getElementById('searchOperator').value;
    var timeStart = document.getElementById('searchTimeStart').value;
    var timeEnd = document.getElementById('searchTimeEnd').value;
    var state = logPagination.getState();

    var requestData = {
        bizCode: bizCode || undefined,
        logType: logType || undefined,
        operator: operator || undefined,
        operateTimeMsStart: timeStart ? new Date(timeStart).getTime() : undefined,
        operateTimeMsEnd: timeEnd ? new Date(timeEnd + 'T23:59:59').getTime() : undefined,
        current: state.current,
        size: state.pageSize,
        sortOrderList: sortOrderList
    };

    fetch(BASE + '/page', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
    })
    .then(function(r){return r.json();})
    .then(function(result){
        if(result.success) {
            var pageData = result.data;
            logPagination.update(pageData.total || 0, pageData.current || state.current);
            renderLogs(pageData.records || []);
        }
    });
}

function escapeHtml(s) { if(!s) return ''; return s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;'); }

function formatDetail(s) {
    if(!s) return '-';
    var escaped = escapeHtml(s);
    return escaped
        .replace(/\\r\\n/g,'<br>')
        .replace(/\\n/g,'<br>')
        .replace(/\\r/g,'<br>')
        .replace(/\\t/g,'&emsp;&emsp;')
        .replace(/\n/g,'<br>')
        .replace(/\r/g,'<br>');
}

function renderLogs(logs) {
    var tbody=document.getElementById('logTableBody');
    if(!logs||!logs.length){tbody.innerHTML='<tr><td colspan="5" class="empty-row">暂无数据</td></tr>';return;}
    tbody.innerHTML=logs.map(function(l){
        return '<tr>'+
            '<td>'+formatTime(l.operateTimeMs || l.createTimeMs)+'</td>'+
            '<td>'+(l.bizCode||'-')+'</td>'+
            '<td>'+(l.logType||'-')+'</td>'+
            '<td>'+(l.operator||'-')+'</td>'+
            '<td class="detail-cell">'+formatDetail(l.showDetail)+'</td>'+
            '</tr>';
    }).join('');
}

function searchLogs() {
    logPagination.update(0, 1);
    loadLogs();
}

function resetSearch() {
    document.getElementById('searchBizCode').value='';
    document.getElementById('searchLogType').value='';
    document.getElementById('searchOperator').value='';
    document.getElementById('searchTimeStart').value='';
    document.getElementById('searchTimeEnd').value='';
    logPagination.update(0, 1);
    sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 0}];
    updateSortIcons();
    loadLogs();
}

updateSortIcons();
loadLogs();
ResizableColumns.init(document.querySelector('.data-table'), {pageKey: '/operation-log'});
</script>
</body>
</html>
