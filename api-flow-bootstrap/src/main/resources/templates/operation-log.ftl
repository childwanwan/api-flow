<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>操作日志</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header"><h2>操作日志</h2></div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item"><label>操作人:</label><input type="text" id="searchOperator" class="input" placeholder="请输入操作人"></div>
            <div class="search-item"><label>操作类型:</label><select id="searchAction" class="select"><option value="">全部</option><option value="LOGIN">登录</option><option value="CREATE">创建</option><option value="UPDATE">更新</option><option value="DELETE">删除</option><option value="EXECUTE">执行</option><option value="CANCEL">取消</option></select></div>
            <div class="search-item"><label>操作对象:</label><input type="text" id="searchTarget" class="input" placeholder="请输入操作对象"></div>
            <div class="search-actions"><button class="btn" onclick="resetSearch()">重置</button><button class="btn btn-primary" onclick="searchLogs()">搜索</button></div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table"><thead><tr>
            <th>操作时间</th><th>操作人</th><th>操作类型</th><th>操作对象</th><th>操作内容</th><th>IP</th><th>操作</th>
        </tr></thead>
        <tbody id="logTableBody"><tr><td colspan="7" class="empty-row">暂无数据</td></tr></tbody></table>
        <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total" id="paginationTotal">共 0 条</span><div class="pagination-controls" id="paginationControls"></div><div class="pagination-size">每页<select id="pageSize" onchange="loadLogs()"><option value="20" selected>20</option><option value="50">50</option><option value="100">100</option></select>条</div></div></div>
    </div>
</div>

<div id="logDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3>操作详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="logDetailContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=operation-log';}
var BASE = contextPath + '/api/v1/operation-log';
var currentPage = 1;

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }

function actionTag(s) {
    var m={LOGIN:'running',CREATE:'success',UPDATE:'warning',DELETE:'failed',EXECUTE:'running',CANCEL:'canceled'};
    var t={LOGIN:'登录',CREATE:'创建',UPDATE:'更新',DELETE:'删除',EXECUTE:'执行',CANCEL:'取消'};
    return '<span class="status-tag status-'+(m[s]||'info')+'">'+(t[s]||s)+'</span>';
}

function loadLogs() {
    var operator=document.getElementById('searchOperator').value;
    var action=document.getElementById('searchAction').value;
    var target=document.getElementById('searchTarget').value;
    var params=new URLSearchParams();
    if(operator) params.append('operator',operator);
    if(action) params.append('action',action);
    if(target) params.append('target',target);
    params.append('page',currentPage);
    params.append('size',document.getElementById('pageSize').value);
    fetch(BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderLogs(res.data||[]);
    });
}

function renderLogs(logs) {
    var tbody=document.getElementById('logTableBody');
    if(!logs||!logs.length){tbody.innerHTML='<tr><td colspan="7" class="empty-row">暂无数据</td></tr>';document.getElementById('paginationTotal').textContent='共 0 条';return;}
    tbody.innerHTML=logs.map(function(l){
        return '<tr>'+
            '<td>'+formatTime(l.operateTimeMs)+'</td>'+
            '<td>'+(l.operator||'-')+'</td>'+
            '<td>'+actionTag(l.action)+'</td>'+
            '<td>'+(l.target||'-')+'</td>'+
            '<td style="max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(l.content||'')+'">'+(l.content||'-')+'</td>'+
            '<td>'+(l.ip||'-')+'</td>'+
            '<td class="action-col"><button class="btn-text" onclick="showDetail('+l.id+')">详情</button></td>'+
            '</tr>';
    }).join('');
    document.getElementById('paginationTotal').textContent='共 '+logs.length+' 条';
}

function searchLogs() { currentPage=1; loadLogs(); }
function resetSearch() { document.getElementById('searchOperator').value=''; document.getElementById('searchAction').value=''; document.getElementById('searchTarget').value=''; currentPage=1; loadLogs(); }

function showDetail(id) {
    fetch(BASE+'/'+id).then(function(r){return r.json();}).then(function(res){
        if(res.success&&res.data) {
            var l=res.data;
            document.getElementById('logDetailContent').innerHTML='<div class="detail-section"><div class="detail-section-title">操作信息</div><table class="detail-table">'+
                '<tr><td>操作时间</td><td>'+formatTime(l.operateTimeMs)+'</td></tr>'+
                '<tr><td>操作人</td><td>'+(l.operator||'-')+'</td></tr>'+
                '<tr><td>操作类型</td><td>'+(l.action||'-')+'</td></tr>'+
                '<tr><td>操作对象</td><td>'+(l.target||'-')+'</td></tr>'+
                '<tr><td>IP地址</td><td>'+(l.ip||'-')+'</td></tr>'+
                '</table></div>'+
                '<div class="detail-section"><div class="detail-section-title">操作内容</div><div class="json-viewer">'+(l.content||'-')+'</div></div>'+
                (l.beforeData?'<div class="detail-section"><div class="detail-section-title">变更前</div><div class="json-viewer">'+(typeof l.beforeData==='string'?l.beforeData:JSON.stringify(l.beforeData,null,2))+'</div></div>':'')+
                (l.afterData?'<div class="detail-section"><div class="detail-section-title">变更后</div><div class="json-viewer">'+(typeof l.afterData==='string'?l.afterData:JSON.stringify(l.afterData,null,2))+'</div></div>':'');
            document.getElementById('logDrawer').style.display='block';
        }
    });
}
function closeDrawer() { document.getElementById('logDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

loadLogs();
</script>
</body>
</html>
