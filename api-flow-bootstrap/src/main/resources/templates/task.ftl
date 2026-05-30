<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>任务管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/resizable-columns.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
    <script src="${request.contextPath}/static/js/resizable-columns.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>任务管理</h2>
        <div class="page-actions"><button class="btn" onclick="exportTasks()">导出</button></div>
    </div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item"><label>API:</label><select id="searchApiCode" class="select"><option value="">全部</option></select></div>
        </div>
        <div class="search-row" style="margin-top:8px;">
            <div class="search-item"><label>任务号:</label><input type="text" id="searchTaskNo" class="input" placeholder="请输入任务号"></div>
            <div class="search-item"><label>任务组:</label><input type="text" id="searchGroupNo" class="input" placeholder="请输入任务组"></div>
            <div class="search-item"><label>状态:</label><select id="searchStatus" class="select"><option value="">全部</option><option value="PENDING">待执行</option><option value="RUNNING">执行中</option><option value="SUCCESS">已完成</option><option value="FAILED">失败</option><option value="CANCELED">已取消</option></select></div>
            <div class="search-item"><label>来源:</label><select id="searchSource" class="select"><option value="">全部</option><option value="API">API</option><option value="SCHEDULE">定时任务</option><option value="MANUAL">手动</option></select></div>
            <div class="search-item"><label>补偿状态:</label><select id="searchCompensate" class="select"><option value="">全部</option><option value="NONE">无</option><option value="PENDING">待补偿</option><option value="RUNNING">补偿中</option><option value="SUCCESS">补偿成功</option><option value="FAILED">补偿失败</option></select></div>
            <div class="search-actions"><button class="btn" onclick="resetSearch()">重置</button><button class="btn btn-primary" onclick="searchTasks()">搜索</button></div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table">
            <thead><tr>
                <th class="checkbox-col"><input type="checkbox" onclick="toggleAll(this)"></th>
                <th>任务编号</th><th>API名称</th><th>来源</th><th>状态</th><th>补偿</th><th>操作</th>
            </tr></thead>
            <tbody id="taskTableBody"><tr><td colspan="7" class="empty-row">暂无任务数据</td></tr></tbody>
        </table>
        <div style="padding:0 16px;"><div class="pagination-wrap" id="taskPagination"></div></div>
    </div>
</div>

<div id="taskDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer">
        <div class="drawer-header"><h3>任务详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div>
        <div class="drawer-body" id="taskDetailContent"></div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=task';}
var BASE = contextPath + '/api/v1/task';

var taskPagination = new Pagination({
    el: 'taskPagination',
    total: 0,
    pageSize: 20,
    pageSizes: [10, 20, 30, 50, 100, 200],
    mode: 'simple'
});

function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }
function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function formatDuration(ms) { if(!ms) return '-'; return ms<1000 ? ms+' ms' : (ms/1000).toFixed(2)+' s'; }

function taskStatusTag(s) {
    var m={PENDING:'pending',RUNNING:'running',SUCCESS:'success',FAILED:'failed',CANCELED:'canceled'};
    var t={PENDING:'待执行',RUNNING:'执行中',SUCCESS:'已完成',FAILED:'失败',CANCELED:'已取消'};
    return '<span class="status-tag status-'+(m[s]||'pending')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>';
}
function compensateStatusTag(s) {
    var m={NONE:'disabled',PENDING:'pending',RUNNING:'running',SUCCESS:'success',FAILED:'failed'};
    var t={NONE:'无',PENDING:'待补偿',RUNNING:'补偿中',SUCCESS:'补偿成功',FAILED:'补偿失败'};
    return '<span class="status-tag status-'+(m[s]||'disabled')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>';
}
function sourceText(s) { var m={API:'API',SCHEDULE:'SCHED',MANUAL:'MANUAL'}; return m[s]||s; }
function priorityText(p) { if(p<=3) return p+' (高)'; if(p<=7) return p+' (普通)'; return p+' (低)'; }

function loadApis() {
    fetch(contextPath+'/api/v1/config/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) { var opts='<option value="">全部</option>'; (res.data||[]).forEach(function(c){opts+='<option value="'+c.apiCode+'">'+c.apiName+'</option>';}); document.getElementById('searchApiCode').innerHTML = opts; }
    }).catch(function(e){
        console.error('Failed to load APIs:', e);
    });
}

function loadTasks() {
    var taskNo=document.getElementById('searchTaskNo').value;
    var groupNo=document.getElementById('searchGroupNo').value;
    var status=document.getElementById('searchStatus').value;
    var source=document.getElementById('searchSource').value;
    var compensate=document.getElementById('searchCompensate').value;
    var apiCode=document.getElementById('searchApiCode').value;
    var params=new URLSearchParams();
    if(taskNo) params.append('taskNo',taskNo);
    if(groupNo) params.append('groupNo',groupNo);
    if(status) params.append('status',status);
    if(source) params.append('source',source);
    if(compensate) params.append('compensateStatus',compensate);
    if(apiCode) params.append('apiCode',apiCode);
    params.append('page',1);
    params.append('size', taskPagination.getState().pageSize);
    fetch(BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderTasks(res.data||[]);
    });
}

function renderTasks(tasks) {
    var tbody=document.getElementById('taskTableBody');
    if(!tasks||!tasks.length){tbody.innerHTML='<tr><td colspan="7" class="empty-row">暂无任务数据</td></tr>';taskPagination.update(0);return;}
    tbody.innerHTML=tasks.map(function(t){
        var moreActions='';
        if(t.status==='PENDING'||t.status==='RUNNING') moreActions+='<button class="dropdown-item" onclick="cancelTask(\''+t.taskNo+'\')">取消任务</button>';
        if(t.status==='PENDING') moreActions+='<button class="dropdown-item" onclick="executeNow(\''+t.taskNo+'\')">立即执行</button>';
        if(t.status==='FAILED') moreActions+='<button class="dropdown-item" onclick="retryTask(\''+t.taskNo+'\')">重试</button>';
        return '<tr>'+
            '<td class="checkbox-col"><input type="checkbox" class="row-checkbox"></td>'+
            '<td style="font-family:monospace;font-size:12px;">'+(t.taskNo||'')+'</td>'+
            '<td>'+(t.apiName||t.apiCode||'')+'</td>'+
            '<td>'+sourceText(t.source)+'</td>'+
            '<td>'+taskStatusTag(t.status)+'</td>'+
            '<td>'+compensateStatusTag(t.compensateStatus||'NONE')+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="showDetail(\''+t.taskNo+'\')">日志</button>'+
                (moreActions?'<span class="action-divider">|</span><span class="dropdown" id="more_'+t.taskNo+'"><button class="btn-text" onclick="toggleDropdown(\''+t.taskNo+'\')">更多▼</button><div class="dropdown-menu">'+moreActions+'</div></span>':'')+
            '</td></tr>';
    }).join('');
    taskPagination.update(tasks.length);
}

function toggleDropdown(taskNo) {
    var el = document.getElementById('more_'+taskNo);
    if (!el) return;
    var isOpen = el.classList.contains('open');
    document.querySelectorAll('.dropdown.open').forEach(function(d){ d.classList.remove('open'); });
    if (!isOpen) el.classList.add('open');
}

document.addEventListener('click', function(e) {
    var inside = false;
    document.querySelectorAll('.dropdown.open').forEach(function(d) {
        if (d.contains(e.target)) inside = true;
    });
    if (!inside) {
        document.querySelectorAll('.dropdown.open').forEach(function(d){ d.classList.remove('open'); });
    }
});

function searchTasks() { loadTasks(); }
function resetSearch() { document.getElementById('searchTaskNo').value=''; document.getElementById('searchGroupNo').value=''; document.getElementById('searchStatus').value=''; document.getElementById('searchSource').value=''; document.getElementById('searchCompensate').value=''; document.getElementById('searchApiCode').value=''; loadTasks(); }
function exportTasks() { showToast('导出功能开发中','info'); }

function showDetail(taskNo) {
    fetch(BASE+'/'+taskNo).then(function(r){return r.json();}).then(function(res){
        if(res.success && res.data) {
            var t=res.data;
            var actionBtns='';
            if(t.status==='PENDING') actionBtns='<button class="btn btn-sm" onclick="cancelTask(\''+t.taskNo+'\')">取消任务</button><button class="btn btn-sm btn-primary" onclick="executeNow(\''+t.taskNo+'\')">立即执行</button>';
            else if(t.status==='RUNNING') actionBtns='<button class="btn btn-sm btn-warning" onclick="cancelTask(\''+t.taskNo+'\')">取消任务</button>';
            else if(t.status==='FAILED') actionBtns='<button class="btn btn-sm btn-primary" onclick="retryTask(\''+t.taskNo+'\')">重试</button>';

            var html='<div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">'+
                '<div><strong>任务编号:</strong> <span style="font-family:monospace;">'+t.taskNo+'</span></div>'+
                '<div>'+taskStatusTag(t.status)+' '+actionBtns+'</div></div>'+
                '<div class="detail-section"><div class="detail-section-title">基本信息</div><table class="detail-table">'+
                '<tr><td>任务组</td><td>'+(t.groupNo||'-')+'</td></tr>'+
                '<tr><td>API编码</td><td>'+(t.apiCode||'-')+'</td></tr>'+
                '<tr><td>API名称</td><td>'+(t.apiName||'-')+'</td></tr>'+
                '<tr><td>执行类型</td><td>'+(t.actionType||'-')+'</td></tr>'+
                '<tr><td>来源</td><td>'+(t.source||'-')+'</td></tr>'+
                '<tr><td>优先级</td><td>'+priorityText(t.priority||5)+'</td></tr>'+
                '<tr><td>补偿状态</td><td>'+compensateStatusTag(t.compensateStatus||'NONE')+'</td></tr>'+
                '<tr><td>创建时间</td><td>'+formatTime(t.createTimeMs)+'</td></tr>'+
                '<tr><td>开始执行</td><td>'+formatTime(t.startExecuteTimeMs)+'</td></tr>'+
                '<tr><td>执行耗时</td><td>'+formatDuration(t.executeDurationMs)+'</td></tr>'+
                '</table></div>'+
                '<div class="detail-section"><div class="detail-section-title">重试信息</div><table class="detail-table">'+
                '<tr><td>已重试次数</td><td>'+(t.retryCount||0)+' / '+(t.maxRetryCount||64)+' 次</td></tr>'+
                '<tr><td>下次重试时间</td><td>'+formatTime(t.nextRetryTimeMs)+'</td></tr>'+
                '</table></div>'+
                '<div class="detail-section"><div class="detail-section-title">请求参数</div><div class="json-viewer">'+(t.requestContext?JSON.stringify(t.requestContext.params||{},null,2):'-')+'</div></div>'+
                (t.requestContext&&t.requestContext.customData&&Object.keys(t.requestContext.customData).length?'<div class="detail-section"><div class="detail-section-title">自定义数据</div><div class="json-viewer">'+JSON.stringify(t.requestContext.customData,null,2)+'</div></div>':'')+
                '<div class="detail-section"><div class="detail-section-title">响应数据</div><div class="json-viewer">'+(t.responseData?JSON.stringify(t.responseData,null,2):'-')+'</div></div>';
            document.getElementById('taskDetailContent').innerHTML=html;
            document.getElementById('taskDrawer').style.display='block';
        }
    });
}
function closeDrawer() { document.getElementById('taskDrawer').style.display='none'; }

function cancelTask(taskNo) { if(!confirm('确定要取消该任务吗？'))return; fetch(BASE+'/cancel',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({taskNo:taskNo,cancelReason:'用户主动取消'})}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('取消成功','success');loadTasks();closeDrawer();}else showToast(res.message||'取消失败','error');}); }
function retryTask(taskNo) { if(!confirm('确定要重试该任务吗？'))return; fetch(BASE+'/retry',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({taskNo:taskNo})}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('重试成功','success');loadTasks();closeDrawer();}else showToast(res.message||'重试失败','error');}); }
function executeNow(taskNo) { if(!confirm('确定要立即执行该任务吗？'))return; fetch(BASE+'/execute-now',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({taskNo:taskNo})}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('已提交执行','success');loadTasks();closeDrawer();}else showToast(res.message||'操作失败','error');}); }

loadApis();
loadTasks();
ResizableColumns.init(document.querySelector('.data-table'), {pageKey: '/task'});
</script>
</body>
</html>
