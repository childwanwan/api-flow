<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>调度日志</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header"><h2>调度日志</h2></div>
    <div class="tabs">
        <div class="tab-item active" onclick="switchMainTab(this,'exec-log')">执行日志</div>
        <div class="tab-item" onclick="switchMainTab(this,'receipt-log')">回执日志</div>
        <div class="tab-item" onclick="switchMainTab(this,'change-log')">变更日志</div>
    </div>

    <div id="exec-log">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>任务号:</label><input type="text" id="execTaskNo" class="input" placeholder="请输入任务号"></div>
                <div class="search-item"><label>API:</label><select id="execApiCode" class="select"><option value="">全部</option></select></div>
                <div class="search-item"><label>状态:</label><select id="execStatus" class="select"><option value="">全部</option><option value="PENDING">待执行</option><option value="RUNNING">执行中</option><option value="SUCCESS">已完成</option><option value="FAILED">失败</option><option value="CANCELED">已取消</option></select></div>
                <div class="search-actions"><button class="btn" onclick="resetExecSearch()">重置</button><button class="btn btn-primary" onclick="searchExecLogs()">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>任务编号</th><th>API名称</th><th>状态</th><th>耗时</th><th>执行时间</th><th>操作</th></tr></thead>
            <tbody id="execLogBody"><tr><td colspan="6" class="empty-row">暂无数据</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination-wrap" id="execPagination"></div></div>
        </div>
    </div>

    <div id="receipt-log" style="display:none;">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>任务号:</label><input type="text" id="receiptTaskNo" class="input" placeholder="请输入任务号"></div>
                <div class="search-item"><label>状态:</label><select id="receiptStatus" class="select"><option value="">全部</option><option value="SUCCESS">已完成</option><option value="FAILED">失败</option><option value="PENDING">待执行</option><option value="RUNNING">执行中</option></select></div>
                <div class="search-actions"><button class="btn" onclick="resetReceiptSearch()">重置</button><button class="btn btn-primary" onclick="searchReceiptLogs()">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>任务编号</th><th>API名称</th><th>回执类型</th><th>任务状态</th><th>执行时间</th><th>操作</th></tr></thead>
            <tbody id="receiptLogBody"><tr><td colspan="6" class="empty-row">暂无数据</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination-wrap" id="receiptPagination"></div></div>
        </div>
    </div>

    <div id="change-log" style="display:none;">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>API编码:</label><input type="text" id="changeApiCode" class="input" placeholder="请输入API编码"></div>
                <div class="search-item"><label>变更类型:</label><select id="changeType" class="select"><option value="">全部</option><option value="CREATE">创建</option><option value="UPDATE">更新</option><option value="DELETE">删除</option><option value="ENABLE">启用</option><option value="DISABLE">禁用</option></select></div>
                <div class="search-actions"><button class="btn" onclick="resetChangeSearch()">重置</button><button class="btn btn-primary" onclick="searchChangeLogs()">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>变更时间</th><th>API编码</th><th>变更类型</th><th>操作人</th><th>操作</th></tr></thead>
            <tbody id="changeLogBody"><tr><td colspan="5" class="empty-row">暂无数据</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination-wrap" id="changePagination"></div></div>
        </div>
    </div>
</div>

<div id="detailDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3 id="drawerTitle">详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="drawerContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=task-log';}
var TASK_BASE = contextPath + '/api/v1/task';
var CONFIG_LOG_BASE = contextPath + '/api/v1/config-log';

var execPagination = new Pagination({el:'execPagination',total:0,pageSize:100,mode:'simple',showSizeChanger:false});
var receiptPagination = new Pagination({el:'receiptPagination',total:0,pageSize:100,mode:'simple',showSizeChanger:false});
var changePagination = new Pagination({el:'changePagination',total:0,pageSize:100,mode:'simple',showSizeChanger:false});

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function formatDuration(ms) { if(!ms) return '-'; return ms<1000 ? ms+' ms' : (ms/1000).toFixed(2)+' s'; }
function taskStatusTag(s) { var m={PENDING:'pending',RUNNING:'running',SUCCESS:'success',FAILED:'failed',CANCELED:'canceled'}; var t={PENDING:'待执行',RUNNING:'执行中',SUCCESS:'已完成',FAILED:'失败',CANCELED:'已取消'}; return '<span class="status-tag status-'+(m[s]||'pending')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }
function changeTypeTag(s) { var m={CREATE:'success',UPDATE:'running',DELETE:'failed',ENABLE:'success',DISABLE:'disabled'}; var t={CREATE:'创建',UPDATE:'更新',DELETE:'删除',ENABLE:'启用',DISABLE:'禁用'}; return '<span class="status-tag status-'+(m[s]||'info')+'">'+(t[s]||s)+'</span>'; }

function switchMainTab(el, panelId) {
    document.querySelectorAll('.tab-item').forEach(function(t){t.classList.remove('active');});
    el.classList.add('active');
    document.getElementById('exec-log').style.display = panelId==='exec-log'?'block':'none';
    document.getElementById('receipt-log').style.display = panelId==='receipt-log'?'block':'none';
    document.getElementById('change-log').style.display = panelId==='change-log'?'block':'none';
}

function loadApis() {
    fetch(contextPath+'/api/v1/config/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var opts='<option value="">全部</option>';
            (res.data||[]).forEach(function(c){opts+='<option value="'+c.apiCode+'">'+c.apiName+'</option>';});
            document.getElementById('execApiCode').innerHTML=opts;
        }
    });
}

function searchExecLogs() {
    var taskNo=document.getElementById('execTaskNo').value;
    var apiCode=document.getElementById('execApiCode').value;
    var status=document.getElementById('execStatus').value;
    var params=new URLSearchParams();
    if(taskNo) params.append('taskNo',taskNo);
    if(apiCode) params.append('apiCode',apiCode);
    if(status) params.append('status',status);
    params.append('limit','100');
    fetch(TASK_BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderExecLogs(res.data||[]);
    });
}
function resetExecSearch() { document.getElementById('execTaskNo').value=''; document.getElementById('execApiCode').value=''; document.getElementById('execStatus').value=''; searchExecLogs(); }

function renderExecLogs(tasks) {
    var tbody=document.getElementById('execLogBody');
    if(!tasks||!tasks.length){tbody.innerHTML='<tr><td colspan="6" class="empty-row">暂无数据</td></tr>';execPagination.update(0);return;}
    tbody.innerHTML=tasks.map(function(t){
        return '<tr>'+
            '<td style="font-family:monospace;font-size:12px;">'+(t.taskNo||'')+'</td>'+
            '<td>'+(t.apiName||t.apiCode||'-')+'</td>'+
            '<td>'+taskStatusTag(t.status)+'</td>'+
            '<td>'+formatDuration(t.executeDurationMs)+'</td>'+
            '<td>'+formatTime(t.startExecuteTimeMs||t.createTimeMs)+'</td>'+
            '<td class="action-col"><button class="btn-text" onclick="showExecDetail(\''+t.taskNo+'\')">详情</button></td>'+
            '</tr>';
    }).join('');
    execPagination.update(tasks.length);
}

function showExecDetail(taskNo) {
    fetch(TASK_BASE+'/'+taskNo).then(function(r){return r.json();}).then(function(res){
        if(res.success&&res.data) {
            var t=res.data;
            document.getElementById('drawerTitle').textContent='执行详情';
            var html='<div class="detail-section"><div class="detail-section-title">任务信息</div><table class="detail-table">'+
                '<tr><td>任务编号</td><td style="font-family:monospace;">'+t.taskNo+'</td></tr>'+
                '<tr><td>API编码</td><td>'+(t.apiCode||'-')+'</td></tr>'+
                '<tr><td>API名称</td><td>'+(t.apiName||'-')+'</td></tr>'+
                '<tr><td>来源</td><td>'+(t.source||'-')+'</td></tr>'+
                '<tr><td>状态</td><td>'+taskStatusTag(t.status)+'</td></tr>'+
                '<tr><td>优先级</td><td>'+(t.priority||'-')+'</td></tr>'+
                '</table></div>'+
                '<div class="detail-section"><div class="detail-section-title">执行信息</div><table class="detail-table">'+
                '<tr><td>创建时间</td><td>'+formatTime(t.createTimeMs)+'</td></tr>'+
                '<tr><td>开始执行</td><td>'+formatTime(t.startExecuteTimeMs)+'</td></tr>'+
                '<tr><td>结束时间</td><td>'+formatTime(t.endExecuteTimeMs)+'</td></tr>'+
                '<tr><td>执行耗时</td><td>'+formatDuration(t.executeDurationMs)+'</td></tr>'+
                '<tr><td>重试次数</td><td>'+(t.retryCount||0)+' / '+(t.maxRetryCount||64)+'</td></tr>'+
                '</table></div>';
            if(t.requestContext) {
                html+='<div class="detail-section"><div class="detail-section-title">请求参数</div><div class="json-viewer">'+(t.requestContext.params?JSON.stringify(t.requestContext.params,null,2):'-')+'</div></div>';
            }
            if(t.responseData) {
                html+='<div class="detail-section"><div class="detail-section-title">响应数据</div><div class="json-viewer">'+(typeof t.responseData==='string'?t.responseData:JSON.stringify(t.responseData,null,2))+'</div></div>';
            }
            document.getElementById('drawerContent').innerHTML=html;
            document.getElementById('detailDrawer').style.display='block';
        }
    });
}

function searchReceiptLogs() {
    var taskNo=document.getElementById('receiptTaskNo').value;
    var status=document.getElementById('receiptStatus').value;
    var params=new URLSearchParams();
    if(taskNo) params.append('taskNo',taskNo);
    if(status) params.append('status',status);
    params.append('limit','100');
    fetch(TASK_BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var tasks=(res.data||[]).filter(function(t){return t.receiptConfig||t.status==='SUCCESS'||t.status==='FAILED';});
            renderReceiptLogs(tasks);
        }
    });
}
function resetReceiptSearch() { document.getElementById('receiptTaskNo').value=''; document.getElementById('receiptStatus').value=''; searchReceiptLogs(); }

function renderReceiptLogs(tasks) {
    var tbody=document.getElementById('receiptLogBody');
    if(!tasks||!tasks.length){tbody.innerHTML='<tr><td colspan="6" class="empty-row">暂无数据</td></tr>';receiptPagination.update(0);return;}
    tbody.innerHTML=tasks.map(function(t){
        var receiptType='-';
        if(t.receiptConfig&&t.receiptConfig.receiptTypes) receiptType=t.receiptConfig.receiptTypes.join(', ');
        return '<tr>'+
            '<td style="font-family:monospace;font-size:12px;">'+(t.taskNo||'')+'</td>'+
            '<td>'+(t.apiName||t.apiCode||'-')+'</td>'+
            '<td>'+receiptType+'</td>'+
            '<td>'+taskStatusTag(t.status)+'</td>'+
            '<td>'+formatTime(t.endExecuteTimeMs||t.updateTimeMs)+'</td>'+
            '<td class="action-col"><button class="btn-text" onclick="showReceiptDetail(\''+t.taskNo+'\')">详情</button></td>'+
            '</tr>';
    }).join('');
    receiptPagination.update(tasks.length);
}

function showReceiptDetail(taskNo) {
    fetch(TASK_BASE+'/'+taskNo).then(function(r){return r.json();}).then(function(res){
        if(res.success&&res.data) {
            var t=res.data;
            document.getElementById('drawerTitle').textContent='回执详情';
            var html='<div class="detail-section"><div class="detail-section-title">任务信息</div><table class="detail-table">'+
                '<tr><td>任务编号</td><td style="font-family:monospace;">'+t.taskNo+'</td></tr>'+
                '<tr><td>API名称</td><td>'+(t.apiName||'-')+'</td></tr>'+
                '<tr><td>状态</td><td>'+taskStatusTag(t.status)+'</td></tr>'+
                '</table></div>';
            if(t.receiptConfig) {
                html+='<div class="detail-section"><div class="detail-section-title">回执配置</div><div class="json-viewer">'+JSON.stringify(t.receiptConfig,null,2)+'</div></div>';
            }
            if(t.receiptInfo) {
                html+='<div class="detail-section"><div class="detail-section-title">回执执行记录</div><div class="json-viewer">'+JSON.stringify(t.receiptInfo,null,2)+'</div></div>';
            }
            document.getElementById('drawerContent').innerHTML=html;
            document.getElementById('detailDrawer').style.display='block';
        }
    });
}

function searchChangeLogs() {
    var apiCode=document.getElementById('changeApiCode').value;
    var changeType=document.getElementById('changeType').value;
    var params=new URLSearchParams();
    if(apiCode) params.append('apiCode',apiCode);
    if(changeType) params.append('changeType',changeType);
    params.append('limit','100');
    fetch(CONFIG_LOG_BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderChangeLogs(res.data||[]);
    });
}
function resetChangeSearch() { document.getElementById('changeApiCode').value=''; document.getElementById('changeType').value=''; searchChangeLogs(); }

function renderChangeLogs(logs) {
    var tbody=document.getElementById('changeLogBody');
    if(!logs||!logs.length){tbody.innerHTML='<tr><td colspan="5" class="empty-row">暂无数据</td></tr>';changePagination.update(0);return;}
    tbody.innerHTML=logs.map(function(l){
        return '<tr>'+
            '<td>'+formatTime(l.createTimeMs)+'</td>'+
            '<td>'+(l.apiCode||'-')+'</td>'+
            '<td>'+changeTypeTag(l.changeType)+'</td>'+
            '<td>'+(l.operator||'-')+'</td>'+
            '<td class="action-col"><button class="btn-text" onclick="showChangeDetail('+l.id+')">详情</button></td>'+
            '</tr>';
    }).join('');
    changePagination.update(logs.length);
}

function showChangeDetail(id) {
    var rows=document.querySelectorAll('#changeLogBody tr');
    var log=null;
    for(var i=0;i<changeLogCache.length;i++){if(changeLogCache[i].id===id){log=changeLogCache[i];break;}}
    if(!log) return;
    document.getElementById('drawerTitle').textContent='变更详情';
    var html='<div class="detail-section"><div class="detail-section-title">变更信息</div><table class="detail-table">'+
        '<tr><td>变更时间</td><td>'+formatTime(log.createTimeMs)+'</td></tr>'+
        '<tr><td>API编码</td><td>'+(log.apiCode||'-')+'</td></tr>'+
        '<tr><td>变更类型</td><td>'+changeTypeTag(log.changeType)+'</td></tr>'+
        '<tr><td>操作人</td><td>'+(log.operator||'-')+'</td></tr>'+
        '</table></div>';
    if(log.beforeConfig) {
        var beforeStr=typeof log.beforeConfig==='string'?log.beforeConfig:JSON.stringify(log.beforeConfig,null,2);
        html+='<div class="detail-section"><div class="detail-section-title">变更前</div><div class="json-viewer">'+beforeStr+'</div></div>';
    }
    if(log.afterConfig) {
        var afterStr=typeof log.afterConfig==='string'?log.afterConfig:JSON.stringify(log.afterConfig,null,2);
        html+='<div class="detail-section"><div class="detail-section-title">变更后</div><div class="json-viewer">'+afterStr+'</div></div>';
    }
    document.getElementById('drawerContent').innerHTML=html;
    document.getElementById('detailDrawer').style.display='block';
}

var changeLogCache=[];
var origRenderChangeLogs=renderChangeLogs;
renderChangeLogs=function(logs){changeLogCache=logs;origRenderChangeLogs(logs);};

function closeDrawer() { document.getElementById('detailDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

var urlParams=new URLSearchParams(window.location.search);
if(urlParams.get('tab')==='change'){switchMainTab(document.querySelectorAll('.tab-item')[2],'change-log');}
if(urlParams.get('apiCode')){document.getElementById('changeApiCode').value=urlParams.get('apiCode');}

loadApis();
searchExecLogs();
searchReceiptLogs();
searchChangeLogs();
</script>
</body>
</html>
