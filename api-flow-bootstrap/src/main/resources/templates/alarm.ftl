<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>告警管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header"><h2>告警管理</h2></div>
    <div class="tabs">
        <div class="tab-item active" onclick="switchTab(this,'alarm-rules')">告警规则</div>
        <div class="tab-item" onclick="switchTab(this,'alarm-records')">告警记录</div>
    </div>
    <div id="alarm-rules">
        <div style="margin-bottom:12px;"><button class="btn btn-primary" onclick="showToast('新建规则功能开发中','info')">+ 新建规则</button></div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>规则名称</th><th>告警类型</th><th>触发条件</th><th>状态</th><th>操作</th></tr></thead>
            <tbody>
            <tr><td>任务失败告警</td><td>任务异常</td><td>retry_count>=64</td><td><span class="status-tag status-enabled"><span class="status-dot"></span>启用</span></td><td class="action-col"><button class="btn-text">编辑</button><span class="action-divider">|</span><button class="btn-text btn-danger-text">删除</button></td></tr>
            <tr><td>执行超时告警</td><td>执行超时</td><td>RUNNING>30分钟</td><td><span class="status-tag status-enabled"><span class="status-dot"></span>启用</span></td><td class="action-col"><button class="btn-text">编辑</button><span class="action-divider">|</span><button class="btn-text btn-danger-text">删除</button></td></tr>
            <tr><td>补偿失败告警</td><td>补偿失败</td><td>compensate=FAILED</td><td><span class="status-tag status-enabled"><span class="status-dot"></span>启用</span></td><td class="action-col"><button class="btn-text">编辑</button><span class="action-divider">|</span><button class="btn-text btn-danger-text">删除</button></td></tr>
            <tr><td>限流触发告警</td><td>限流告警</td><td>rate_limit_reject</td><td><span class="status-tag status-disabled"><span class="status-dot"></span>禁用</span></td><td class="action-col"><button class="btn-text">编辑</button><span class="action-divider">|</span><button class="btn-text btn-danger-text">删除</button></td></tr>
            </tbody></table>
        </div>
    </div>
    <div id="alarm-records" style="display:none;">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>告警类型:</label><select class="select"><option value="">全部</option><option>任务异常</option><option>执行超时</option><option>补偿失败</option><option>限流触发</option><option>系统异常</option><option>配置变更</option></select></div>
                <div class="search-item"><label>告警级别:</label><select class="select"><option value="">全部</option><option value="INFO">INFO</option><option value="WARNING">WARNING</option><option value="ERROR">ERROR</option><option value="CRITICAL">CRITICAL</option></select></div>
                <div class="search-actions"><button class="btn">重置</button><button class="btn btn-primary">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>告警时间</th><th>告警类型</th><th>告警级别</th><th>任务编号</th><th>操作</th></tr></thead>
            <tbody id="alarmRecordBody"><tr><td colspan="5" class="empty-row">暂无告警记录</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total">共 0 条</span><div class="pagination-controls"></div></div></div>
        </div>
    </div>
</div>

<div id="alarmDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3>告警详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="alarmDetailContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=alarm';}
function switchTab(el, panelId) {
    document.querySelectorAll('.tab-item').forEach(function(t){t.classList.remove('active');});
    el.classList.add('active');
    document.getElementById('alarm-rules').style.display=panelId==='alarm-rules'?'block':'none';
    document.getElementById('alarm-records').style.display=panelId==='alarm-records'?'block':'none';
}
function closeDrawer() { document.getElementById('alarmDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }
</script>
</body>
</html>
