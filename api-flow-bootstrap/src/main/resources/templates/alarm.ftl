<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>告警管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header"><h2>告警管理</h2></div>
    <div class="tabs">
        <div class="tab-item active" onclick="switchTab(this,'alarm-rules')">告警规则</div>
        <div class="tab-item" onclick="switchTab(this,'alarm-records')">告警记录</div>
    </div>
    <div id="alarm-rules">
        <div style="margin-bottom:12px;"><button class="btn btn-primary" onclick="showCreateRuleDialog()">+ 新建规则</button></div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>规则名称</th><th>告警类型</th><th>触发条件</th><th>级别</th><th>状态</th><th>操作</th></tr></thead>
            <tbody id="ruleTableBody"><tr><td colspan="6" class="empty-row">暂无数据</td></tr></tbody></table>
        </div>
    </div>
    <div id="alarm-records" style="display:none;">
        <div class="search-card">
            <div class="search-row">
                <div class="search-item"><label>告警类型:</label><select id="searchEventType" class="select"><option value="">全部</option><option value="TASK_FAILED">任务异常</option><option value="EXECUTE_TIMEOUT">执行超时</option><option value="COMPENSATE_FAILED">补偿失败</option><option value="RATE_LIMIT_TRIGGERED">限流触发</option><option value="SYSTEM_ERROR">系统异常</option><option value="CONFIG_CHANGED">配置变更</option></select></div>
                <div class="search-item"><label>告警级别:</label><select id="searchLevel" class="select"><option value="">全部</option><option value="INFO">INFO</option><option value="WARNING">WARNING</option><option value="ERROR">ERROR</option><option value="CRITICAL">CRITICAL</option></select></div>
                <div class="search-actions"><button class="btn" onclick="resetRecordSearch()">重置</button><button class="btn btn-primary" onclick="searchRecords()">搜索</button></div>
            </div>
        </div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>告警时间</th><th>告警类型</th><th>告警级别</th><th>消息</th><th>任务编号</th><th>操作</th></tr></thead>
            <tbody id="recordTableBody"><tr><td colspan="6" class="empty-row">暂无告警记录</td></tr></tbody></table>
            <div style="padding:0 16px;"><div class="pagination-wrap" id="recordPagination"></div></div>
        </div>
    </div>
</div>

<div id="ruleModal" class="modal-overlay" style="display:none;">
    <div class="modal">
        <div class="modal-header"><h3 id="ruleModalTitle">新建规则</h3><button class="modal-close" onclick="hideRuleModal()">&times;</button></div>
        <div class="modal-body">
            <input type="hidden" id="ruleId">
            <div class="form-group"><label>规则名称 <span class="required">*</span></label><input type="text" id="formRuleName" class="form-control" placeholder="如：任务失败告警"></div>
            <div class="form-group"><label>告警类型 <span class="required">*</span></label><select id="formAlarmType" class="form-control"><option value="TASK_FAILED">任务异常</option><option value="EXECUTE_TIMEOUT">执行超时</option><option value="COMPENSATE_FAILED">补偿失败</option><option value="RATE_LIMIT_TRIGGERED">限流触发</option><option value="SYSTEM_ERROR">系统异常</option><option value="CONFIG_CHANGED">配置变更</option></select></div>
            <div class="form-group"><label>触发条件</label><input type="text" id="formTriggerCondition" class="form-control" placeholder="如：retry_count>=64"></div>
            <div class="form-group"><label>告警级别</label><select id="formLevel" class="form-control"><option value="INFO">INFO</option><option value="WARNING" selected>WARNING</option><option value="ERROR">ERROR</option><option value="CRITICAL">CRITICAL</option></select></div>
            <div class="form-group"><label>状态</label><div class="radio-group"><label><input type="radio" name="ruleEnabled" value="true" checked> 启用</label><label><input type="radio" name="ruleEnabled" value="false"> 禁用</label></div></div>
        </div>
        <div class="modal-footer"><button class="btn" onclick="hideRuleModal()">取消</button><button class="btn btn-primary" onclick="saveRule()">保存</button></div>
    </div>
</div>

<div id="alarmDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3>告警详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="alarmDetailContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=alarm';}
var BASE = contextPath + '/api/v1/alarm';

var recordPagination = new Pagination({el:'recordPagination',total:0,pageSize:100,mode:'simple',showSizeChanger:false});

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }

function alarmTypeText(s) { var m={TASK_FAILED:'任务异常',EXECUTE_TIMEOUT:'执行超时',COMPENSATE_FAILED:'补偿失败',RATE_LIMIT_TRIGGERED:'限流触发',SYSTEM_ERROR:'系统异常',CONFIG_CHANGED:'配置变更'}; return m[s]||s; }
function levelTag(s) { var m={INFO:'success',WARNING:'warning',ERROR:'error',CRITICAL:'critical'}; return '<span class="status-tag status-'+(m[s]||'info')+'">'+(s||'-')+'</span>'; }

function switchTab(el, panelId) {
    document.querySelectorAll('.tab-item').forEach(function(t){t.classList.remove('active');});
    el.classList.add('active');
    document.getElementById('alarm-rules').style.display=panelId==='alarm-rules'?'block':'none';
    document.getElementById('alarm-records').style.display=panelId==='alarm-records'?'block':'none';
}

function loadRules() {
    fetch(BASE+'/rule/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) renderRules(res.data||[]);
    });
}

function renderRules(rules) {
    var tbody=document.getElementById('ruleTableBody');
    if(!rules||!rules.length){tbody.innerHTML='<tr><td colspan="6" class="empty-row">暂无数据</td></tr>';return;}
    tbody.innerHTML=rules.map(function(r){
        return '<tr>'+
            '<td>'+r.ruleName+'</td>'+
            '<td>'+alarmTypeText(r.alarmType)+'</td>'+
            '<td>'+(r.triggerCondition||'-')+'</td>'+
            '<td>'+levelTag(r.level)+'</td>'+
            '<td><span class="status-tag '+(r.enabled?'status-enabled':'status-disabled')+'"><span class="status-dot"></span>'+(r.enabled?'启用':'禁用')+'</span></td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editRule('+r.id+')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text" onclick="toggleRule('+r.id+','+r.enabled+')">'+(r.enabled?'禁用':'启用')+'</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteRule('+r.id+')">删除</button>'+
            '</td></tr>';
    }).join('');
}

function showCreateRuleDialog() {
    document.getElementById('ruleModalTitle').textContent='新建规则';
    document.getElementById('ruleId').value='';
    document.getElementById('formRuleName').value='';
    document.getElementById('formAlarmType').value='TASK_FAILED';
    document.getElementById('formTriggerCondition').value='';
    document.getElementById('formLevel').value='WARNING';
    document.querySelector('input[name=ruleEnabled][value=true]').checked=true;
    document.getElementById('ruleModal').style.display='flex';
    setTimeout(function(){ if(window.InputClear) InputClear.init(document.getElementById('ruleModal')); }, 100);
}

function editRule(id) {
    fetch(BASE+'/rule/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var rule=(res.data||[]).find(function(r){return r.id===id;});
            if(rule) {
                document.getElementById('ruleModalTitle').textContent='编辑规则';
                document.getElementById('ruleId').value=rule.id;
                document.getElementById('formRuleName').value=rule.ruleName;
                document.getElementById('formAlarmType').value=rule.alarmType;
                document.getElementById('formTriggerCondition').value=rule.triggerCondition||'';
                document.getElementById('formLevel').value=rule.level||'WARNING';
                var radio=document.querySelector('input[name=ruleEnabled][value='+rule.enabled+']');
                if(radio) radio.checked=true;
                document.getElementById('ruleModal').style.display='flex';
                setTimeout(function(){ if(window.InputClear) InputClear.init(document.getElementById('ruleModal')); }, 100);
            }
        }
    });
}

function hideRuleModal() { document.getElementById('ruleModal').style.display='none'; }

function saveRule() {
    var id=document.getElementById('ruleId').value;
    var ruleName=document.getElementById('formRuleName').value;
    var alarmType=document.getElementById('formAlarmType').value;
    if(!ruleName){showToast('请输入规则名称','warning');return;}
    var data={
        ruleName:ruleName,
        alarmType:alarmType,
        triggerCondition:document.getElementById('formTriggerCondition').value,
        level:document.getElementById('formLevel').value,
        enabled:document.querySelector('input[name=ruleEnabled]:checked').value==='true'
    };
    if(id) data.id=parseInt(id);
    var url=id?BASE+'/rule/update':BASE+'/rule/create';
    var method=id?'PUT':'POST';
    fetch(url,{method:method,headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast(id?'保存成功':'创建成功','success');hideRuleModal();loadRules();}
        else showToast((res.error&&res.error.message)||'操作失败','error');
    });
}

function toggleRule(id,currentEnabled) {
    var data={id:id,enabled:!currentEnabled};
    fetch(BASE+'/rule/update',{method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast('操作成功','success');loadRules();}
        else showToast((res.error&&res.error.message)||'操作失败','error');
    });
}

function deleteRule(id) {
    if(!confirm('确定要删除该告警规则吗？'))return;
    fetch(BASE+'/rule/'+id,{method:'DELETE'}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast('删除成功','success');loadRules();}
        else showToast((res.error&&res.error.message)||'删除失败','error');
    });
}

function searchRecords() {
    var eventType=document.getElementById('searchEventType').value;
    var level=document.getElementById('searchLevel').value;
    var params=new URLSearchParams();
    if(eventType) params.append('eventType',eventType);
    if(level) params.append('level',level);
    params.append('limit','100');
    fetch(BASE+'/record/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderRecords(res.data||[]);
    });
}
function resetRecordSearch() { document.getElementById('searchEventType').value=''; document.getElementById('searchLevel').value=''; searchRecords(); }

var recordCache=[];
function renderRecords(records) {
    recordCache=records||[];
    var tbody=document.getElementById('recordTableBody');
    if(!records||!records.length){tbody.innerHTML='<tr><td colspan="6" class="empty-row">暂无告警记录</td></tr>';recordPagination.update(0);return;}
    tbody.innerHTML=records.map(function(r){
        return '<tr>'+
            '<td>'+formatTime(r.createTimeMs)+'</td>'+
            '<td>'+alarmTypeText(r.eventType)+'</td>'+
            '<td>'+levelTag(r.level)+'</td>'+
            '<td style="max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(r.message||'')+'">'+(r.message||'-')+'</td>'+
            '<td style="font-family:monospace;font-size:12px;">'+(r.taskNo||'-')+'</td>'+
            '<td class="action-col"><button class="btn-text" onclick="showRecordDetail('+r.id+')">详情</button></td>'+
            '</tr>';
    }).join('');
    recordPagination.update(records.length);
}

function showRecordDetail(id) {
    var record=recordCache.find(function(r){return r.id===id;});
    if(!record) return;
    var html='<div class="detail-section"><div class="detail-section-title">告警信息</div><table class="detail-table">'+
        '<tr><td>告警时间</td><td>'+formatTime(record.createTimeMs)+'</td></tr>'+
        '<tr><td>告警类型</td><td>'+alarmTypeText(record.eventType)+'</td></tr>'+
        '<tr><td>告警级别</td><td>'+levelTag(record.level)+'</td></tr>'+
        '<tr><td>消息</td><td>'+(record.message||'-')+'</td></tr>'+
        '<tr><td>任务编号</td><td style="font-family:monospace;">'+(record.taskNo||'-')+'</td></tr>'+
        '<tr><td>API编码</td><td>'+(record.apiCode||'-')+'</td></tr>'+
        '</table></div>';
    if(record.detail) {
        var detailStr=record.detail;
        try{detailStr=JSON.stringify(JSON.parse(record.detail),null,2);}catch(e){}
        html+='<div class="detail-section"><div class="detail-section-title">详情数据</div><div class="json-viewer">'+detailStr+'</div></div>';
    }
    document.getElementById('alarmDetailContent').innerHTML=html;
    document.getElementById('alarmDrawer').style.display='block';
}

function closeDrawer() { document.getElementById('alarmDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

document.getElementById('ruleModal').addEventListener('click',function(e){if(e.target===this)hideRuleModal();});

loadRules();
searchRecords();
</script>
</body>
</html>
