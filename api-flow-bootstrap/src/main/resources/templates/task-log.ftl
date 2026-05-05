<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>调度日志</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
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
            <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total" id="execPaginationTotal">共 0 条</span><div class="pagination-controls"></div></div></div>
        </div>
    </div>

    <div id="receipt-log" style="display:none;">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>任务号:</label><input type="text" id="receiptTaskNo" class="input" placeholder="请输入任务号"></div>
                <div class="search-item"><label>回执类型:</label><select id="receiptType" class="select"><option value="">全部</option><option value="HTTP">HTTP</option><option value="MQ">MQ</option></select></div>
                <div class="search-item"><label>状态:</label><select id="receiptStatus" class="select"><option value="">全部</option><option value="PENDING">待发送</option><option value="RETRYING">重试中</option><option value="SUCCESS">已发送</option><option value="FAILED">发送失败</option></select></div>
                <div class="search-actions"><button class="btn" onclick="resetReceiptSearch()">重置</button><button class="btn btn-primary" onclick="searchReceiptLogs()">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>任务编号</th><th>回执类型</th><th>回执目标</th><th>状态</th><th>重试</th><th>操作</th></tr></thead>
            <tbody id="receiptLogBody"><tr><td colspan="6" class="empty-row">暂无数据</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total" id="receiptPaginationTotal">共 0 条</span><div class="pagination-controls"></div></div></div>
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
            <table class="data-table"><thead><tr><th>变更时间</th><th>API编码</th><th>变更类型</th><th>变更字段</th><th>操作人</th></tr></thead>
            <tbody id="changeLogBody"><tr><td colspan="5" class="empty-row">暂无数据</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total" id="changePaginationTotal">共 0 条</span><div class="pagination-controls"></div></div></div>
        </div>
    </div>
</div>

<div id="detailDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3 id="drawerTitle">详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="drawerContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=task-log';}
function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function formatDuration(ms) { if(!ms) return '-'; return ms<1000 ? ms+' ms' : (ms/1000).toFixed(2)+' s'; }
function taskStatusTag(s) { var m={PENDING:'pending',RUNNING:'running',SUCCESS:'success',FAILED:'failed',CANCELED:'canceled'}; var t={PENDING:'待执行',RUNNING:'执行中',SUCCESS:'已完成',FAILED:'失败',CANCELED:'已取消'}; return '<span class="status-tag status-'+(m[s]||'pending')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }
function receiptStatusTag(s) { var m={PENDING:'pending',RETRYING:'running',SUCCESS:'success',FAILED:'failed'}; var t={PENDING:'待发送',RETRYING:'重试中',SUCCESS:'已发送',FAILED:'发送失败'}; return '<span class="status-tag status-'+(m[s]||'pending')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }
function changeTypeTag(s) { var m={CREATE:'success',UPDATE:'running',DELETE:'failed',ENABLE:'success',DISABLE:'disabled'}; var t={CREATE:'创建',UPDATE:'更新',DELETE:'删除',ENABLE:'启用',DISABLE:'禁用'}; return '<span class="status-tag status-'+(m[s]||'info')+'">'+(t[s]||s)+'</span>'; }

function switchMainTab(el, panelId) {
    document.querySelectorAll('.tab-item').forEach(function(t){t.classList.remove('active');});
    el.classList.add('active');
    document.getElementById('exec-log').style.display = panelId==='exec-log'?'block':'none';
    document.getElementById('receipt-log').style.display = panelId==='receipt-log'?'block':'none';
    document.getElementById('change-log').style.display = panelId==='change-log'?'block':'none';
}

function searchExecLogs() { showToast('执行日志查询开发中','info'); }
function resetExecSearch() { document.getElementById('execTaskNo').value=''; document.getElementById('execApiCode').value=''; document.getElementById('execStatus').value=''; }
function searchReceiptLogs() { showToast('回执日志查询开发中','info'); }
function resetReceiptSearch() { document.getElementById('receiptTaskNo').value=''; document.getElementById('receiptType').value=''; document.getElementById('receiptStatus').value=''; }
function searchChangeLogs() { showToast('变更日志查询开发中','info'); }
function resetChangeSearch() { document.getElementById('changeApiCode').value=''; document.getElementById('changeType').value=''; }

function showExecDetail(taskNo) { parent.openTab('task',contextPath+'/task','任务管理',null); }
function showReceiptDetail(taskNo) {
    document.getElementById('drawerTitle').textContent='回执详情';
    document.getElementById('drawerContent').innerHTML='<div class="detail-section"><div class="detail-section-title">回执信息</div><table class="detail-table"><tr><td>任务编号</td><td>'+taskNo+'</td></tr><tr><td>回执类型</td><td>-</td></tr><tr><td>回执目标</td><td>-</td></tr><tr><td>回执状态</td><td>-</td></tr><tr><td>重试次数</td><td>-</td></tr></table></div><div class="detail-section"><div class="detail-section-title">请求内容</div><div class="json-viewer">-</div></div><div class="detail-section"><div class="detail-section-title">响应结果</div><div class="json-viewer">-</div></div>';
    document.getElementById('detailDrawer').style.display='block';
}
function showChangeDetail(id) {
    document.getElementById('drawerTitle').textContent='变更详情';
    document.getElementById('drawerContent').innerHTML='<div class="detail-section"><div class="detail-section-title">变更信息</div><table class="detail-table"><tr><td>变更时间</td><td>-</td></tr><tr><td>API编码</td><td>-</td></tr><tr><td>变更类型</td><td>-</td></tr><tr><td>变更字段</td><td>-</td></tr><tr><td>操作人</td><td>-</td></tr><tr><td>操作IP</td><td>-</td></tr></table></div><div class="detail-section"><div class="detail-section-title">变更内容</div><div class="json-viewer">-</div></div>';
    document.getElementById('detailDrawer').style.display='block';
}
function closeDrawer() { document.getElementById('detailDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

var urlParams=new URLSearchParams(window.location.search);
if(urlParams.get('tab')==='change'){switchMainTab(document.querySelectorAll('.tab-item')[2],'change-log');}
if(urlParams.get('apiCode')){document.getElementById('changeApiCode').value=urlParams.get('apiCode');}
</script>
</body>
</html>
