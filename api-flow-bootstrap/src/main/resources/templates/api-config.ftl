<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API配置</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>API配置</h2>
        <div class="page-actions"><button class="btn btn-primary" onclick="showAddDialog()">+ 新建API</button></div>
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
                <th class="checkbox-col"><input type="checkbox" onclick="toggleAll(this)"></th>
                <th>API编码</th>
                <th>API名称</th>
                <th>分组</th>
                <th>限流值</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="configTableBody">
            <tr><td colspan="7" class="empty-row">暂无数据</td></tr>
            </tbody>
        </table>
        <div style="padding:0 16px;">
            <div class="pagination">
                <span class="pagination-total" id="paginationTotal">共 0 条</span>
                <div class="pagination-controls" id="paginationControls"></div>
                <div class="pagination-size">每页<select id="pageSize" onchange="loadConfigs()"><option value="20" selected>20</option><option value="50">50</option><option value="100">100</option></select>条</div>
            </div>
        </div>
    </div>
</div>

<div id="configModal" class="modal-overlay" style="display:none;">
    <div class="modal modal-lg">
        <div class="modal-header">
            <h3 id="modalTitle">新建API</h3>
            <button class="modal-close" onclick="hideModal()">&times;</button>
        </div>
        <div class="modal-body">
            <div class="form-tabs">
                <div class="form-tab active" onclick="switchTab(this,'tab-basic')">基本信息</div>
                <div class="form-tab" onclick="switchTab(this,'tab-rate-limit')">限流配置</div>
                <div class="form-tab" onclick="switchTab(this,'tab-filter')">过滤规则</div>
                <div class="form-tab" onclick="switchTab(this,'tab-plugin')">插件配置</div>
                <div class="form-tab" onclick="switchTab(this,'tab-receipt')">回执配置</div>
            </div>
            <div id="tab-basic" class="tab-panel">
                <div class="form-section">
                    <div class="form-section-title">基本信息</div>
                    <div class="form-row">
                        <div class="form-group"><label>所属分组 <span class="required">*</span></label><select id="formGroupNo" class="form-control"><option value="">请选择分组</option></select></div>
                    </div>
                    <div class="form-row">
                        <div class="form-group"><label>API编码 <span class="required">*</span></label><input type="text" id="formApiCode" class="form-control" placeholder="如：AMAZON_ORDER_QUERY"></div>
                        <div class="form-group"><label>API名称 <span class="required">*</span></label><input type="text" id="formApiName" class="form-control" placeholder="如：亚马逊订单查询"></div>
                    </div>
                    <div class="form-group"><label>API描述</label><textarea id="formApiDescription" class="form-control" placeholder="请输入API描述"></textarea></div>
                    <div class="form-group"><label>状态</label><div class="radio-group"><label><input type="radio" name="formStatus" value="ENABLED" checked> 启用</label><label><input type="radio" name="formStatus" value="DISABLED"> 禁用</label></div></div>
                </div>
                <div class="form-section">
                    <div class="form-section-title">执行配置</div>
                    <div class="form-row">
                        <div class="form-group"><label>请求超时(ms)</label><input type="number" id="formRequestTimeoutMs" class="form-control" value="30000"><span class="form-hint">默认30000ms</span></div>
                        <div class="form-group"><label>最大重试次数</label><input type="number" id="formAutoRetryCount" class="form-control" value="64"><span class="form-hint">默认64次</span></div>
                        <div class="form-group"><label>重试间隔(ms)</label><input type="number" id="formRetryIntervalMs" class="form-control" value="5000"><span class="form-hint">默认5000ms，指数退避</span></div>
                    </div>
                </div>
            </div>
            <div id="tab-rate-limit" class="tab-panel" style="display:none;">
                <div class="form-section">
                    <div class="form-section-title">限流配置</div>
                    <div class="form-group"><label><input type="checkbox" id="formRateLimitEnabled" checked> 启用限流</label></div>
                    <div class="form-row">
                        <div class="form-group"><label>限流类型</label><div class="radio-group"><label><input type="radio" name="rateLimitType" value="CONCURRENT" checked> 并发数</label><label><input type="radio" name="rateLimitType" value="QPS"> QPS</label></div></div>
                        <div class="form-group"><label>限流维度</label><div class="radio-group"><label><input type="radio" name="rateLimitDimension" value="API" checked> API级</label><label><input type="radio" name="rateLimitDimension" value="CUSTOM"> 自定义</label></div></div>
                    </div>
                    <div class="form-group"><label>Key模板</label><input type="text" id="formRateLimitKey" class="form-control" value="rate:limit:{apiCode}"></div>
                    <div class="form-row">
                        <div class="form-group"><label>限流阈值</label><input type="number" id="formRateLimitThreshold" class="form-control" value="100"></div>
                        <div class="form-group"><label>限流窗口(秒)</label><input type="number" id="formRateLimitWindow" class="form-control" value="1"></div>
                    </div>
                </div>
            </div>
            <div id="tab-filter" class="tab-panel" style="display:none;">
                <div class="form-section">
                    <div class="form-section-title">过滤规则</div>
                    <p style="color:#909399;margin-bottom:12px;">支持按字段条件过滤请求参数</p>
                    <table class="data-table" id="filterRuleTable">
                        <thead><tr><th>字段(Field)</th><th>操作符(Operator)</th><th>值(Value)</th><th style="width:80px;">操作</th></tr></thead>
                        <tbody id="filterRuleBody"></tbody>
                    </table>
                    <button class="btn" style="margin-top:8px;" onclick="addFilterRule()">+ 添加规则</button>
                </div>
            </div>
            <div id="tab-plugin" class="tab-panel" style="display:none;">
                <div class="form-section">
                    <div class="form-section-title">插件配置</div>
                    <table class="data-table" id="pluginConfigTable">
                        <thead><tr><th>插件编码</th><th>插件名称</th><th>执行顺序</th><th>启用</th><th style="width:80px;">操作</th></tr></thead>
                        <tbody id="pluginConfigBody"></tbody>
                    </table>
                    <button class="btn" style="margin-top:8px;" onclick="addPluginConfig()">+ 添加插件</button>
                </div>
            </div>
            <div id="tab-receipt" class="tab-panel" style="display:none;">
                <div class="form-section">
                    <div class="form-section-title">回执配置</div>
                    <div class="form-group"><label>回执类型</label><div class="checkbox-group"><label><input type="checkbox" id="receiptHttp" value="HTTP"> HTTP</label><label><input type="checkbox" id="receiptMq" value="MQ"> MQ</label><label><input type="checkbox" id="receiptNone" value="NONE" checked> 无</label></div></div>
                    <div id="receiptHttpConfig" style="display:none;">
                        <div class="form-row"><div class="form-group"><label>URL</label><input type="text" class="form-control" placeholder="https://callback.example.com/api"></div><div class="form-group"><label>方法</label><select class="form-control"><option>POST</option><option>GET</option><option>PUT</option></select></div></div>
                        <div class="form-row"><div class="form-group"><label>超时(ms)</label><input type="number" class="form-control" value="5000"></div><div class="form-group"><label>重试次数</label><input type="number" class="form-control" value="64"></div></div>
                        <div class="form-group"><label>消息体模板</label><textarea class="form-control" placeholder='{"taskNo":"${taskNo}","status":"${status}"}'></textarea></div>
                    </div>
                    <div id="receiptMqConfig" style="display:none;">
                        <div class="form-row"><div class="form-group"><label>Topic</label><input type="text" class="form-control" placeholder="api-callback-topic"></div><div class="form-group"><label>Key模板</label><input type="text" class="form-control" placeholder="${taskNo}"></div></div>
                        <div class="form-group"><label>消息体模板</label><textarea class="form-control" placeholder='{"taskNo":"${taskNo}","status":"${status}"}'></textarea></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="hideModal()">取消</button>
            <button class="btn btn-primary" onclick="saveConfig()">保存</button>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-config';}
var BASE = contextPath + '/api/v1/config';
var editingApiCode = null;

function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function statusTag(s) { var m={ENABLED:'enabled',DISABLED:'disabled'}; var t={ENABLED:'启用',DISABLED:'禁用'}; return '<span class="status-tag status-'+(m[s]||'disabled')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }

function switchTab(el, panelId) {
    document.querySelectorAll('.form-tab').forEach(function(t){t.classList.remove('active');});
    document.querySelectorAll('.tab-panel').forEach(function(p){p.style.display='none';});
    el.classList.add('active');
    document.getElementById(panelId).style.display='block';
}

function loadGroups() {
    fetch(contextPath+'/api/v1/group/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var opts = '<option value="">全部</option>';
            (res.data||[]).forEach(function(g){ opts+='<option value="'+g.groupNo+'">'+g.groupName+'</option>'; });
            document.getElementById('searchGroupNo').innerHTML = opts;
            var formOpts = '<option value="">请选择分组</option>';
            (res.data||[]).forEach(function(g){ formOpts+='<option value="'+g.groupNo+'">'+g.groupName+'</option>'; });
            document.getElementById('formGroupNo').innerHTML = formOpts;
        }
    });
}

function loadConfigs() {
    var apiCode = document.getElementById('searchApiCode').value;
    var apiName = document.getElementById('searchApiName').value;
    var groupNo = document.getElementById('searchGroupNo').value;
    var status = document.getElementById('searchStatus').value;
    var params = new URLSearchParams();
    if(apiCode) params.append('apiCode',apiCode);
    if(apiName) params.append('apiName',apiName);
    if(groupNo) params.append('groupNo',groupNo);
    if(status) params.append('status',status);
    fetch(BASE+'/list?'+params.toString()).then(function(r){return r.json();}).then(function(res){
        if(res.success) renderConfigs(res.data||[]);
    });
}

function renderConfigs(configs) {
    var tbody = document.getElementById('configTableBody');
    if(!configs.length) { tbody.innerHTML='<tr><td colspan="7" class="empty-row">暂无数据</td></tr>'; document.getElementById('paginationTotal').textContent='共 0 条'; return; }
    tbody.innerHTML = configs.map(function(c){
        return '<tr>'+
            '<td class="checkbox-col"><input type="checkbox" class="row-checkbox"></td>'+
            '<td>'+c.apiCode+'</td>'+
            '<td>'+(c.apiName||'')+'</td>'+
            '<td>'+(c.groupNo||'-')+'</td>'+
            '<td>'+(c.rateLimitThreshold||'-')+'</td>'+
            '<td>'+statusTag(c.status||'ENABLED')+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editConfig(\''+c.apiCode+'\')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteConfig(\''+c.apiCode+'\')">删除</button>'+
                '<span class="action-divider">|</span>'+
                '<span class="dropdown" id="more_'+c.apiCode+'">'+
                    '<button class="btn-text" onclick="toggleDropdown(\''+c.apiCode+'\')">更多▼</button>'+
                    '<div class="dropdown-menu">'+
                        '<button class="dropdown-item" onclick="copyConfig(\''+c.apiCode+'\')">复制</button>'+
                        '<button class="dropdown-item" onclick="toggleConfigStatus(\''+c.apiCode+'\',\''+c.status+'\')">'+(c.status==='ENABLED'?'禁用':'启用')+'</button>'+
                        '<button class="dropdown-item" onclick="viewChangeLog(\''+c.apiCode+'\')">变更日志</button>'+
                    '</div>'+
                '</span>'+
            '</td></tr>';
    }).join('');
    document.getElementById('paginationTotal').textContent = '共 '+configs.length+' 条';
}

function toggleDropdown(code) {
    var el = document.getElementById('more_'+code);
    el.classList.toggle('open');
    setTimeout(function(){ document.addEventListener('click',function close(ev){ if(!el.contains(ev.target)){el.classList.remove('open');document.removeEventListener('click',close);} }); },0);
}

function searchConfigs() { loadConfigs(); }
function resetSearch() { document.getElementById('searchApiCode').value=''; document.getElementById('searchApiName').value=''; document.getElementById('searchGroupNo').value=''; document.getElementById('searchStatus').value=''; loadConfigs(); }

function showAddDialog() {
    editingApiCode = null;
    document.getElementById('modalTitle').textContent = '新建API';
    document.getElementById('formApiCode').value = '';
    document.getElementById('formApiCode').readOnly = false;
    document.getElementById('formApiName').value = '';
    document.getElementById('formApiDescription').value = '';
    document.getElementById('formRequestTimeoutMs').value = '30000';
    document.getElementById('formAutoRetryCount').value = '64';
    document.getElementById('formRetryIntervalMs').value = '5000';
    document.querySelector('input[name=formStatus][value=ENABLED]').checked = true;
    document.getElementById('configModal').style.display = 'flex';
}

function editConfig(apiCode) {
    fetch(BASE+'/'+apiCode).then(function(r){return r.json();}).then(function(res){
        if(res.success && res.data) {
            editingApiCode = apiCode;
            var c = res.data;
            document.getElementById('modalTitle').textContent = '编辑API';
            document.getElementById('formApiCode').value = c.apiCode;
            document.getElementById('formApiCode').readOnly = true;
            document.getElementById('formApiName').value = c.apiName;
            document.getElementById('formGroupNo').value = c.groupNo||'';
            document.getElementById('formApiDescription').value = c.apiDescription||'';
            document.getElementById('formRequestTimeoutMs').value = c.requestTimeoutMs||30000;
            document.getElementById('formAutoRetryCount').value = c.autoRetryCount||64;
            document.getElementById('formRetryIntervalMs').value = c.retryIntervalMs||5000;
            var radio = document.querySelector('input[name=formStatus][value='+(c.status||'ENABLED')+']');
            if(radio) radio.checked = true;
            document.getElementById('configModal').style.display = 'flex';
        }
    });
}

function hideModal() { document.getElementById('configModal').style.display='none'; }

function saveConfig() {
    var apiCode = document.getElementById('formApiCode').value;
    var apiName = document.getElementById('formApiName').value;
    if(!apiCode){showToast('请输入API编码','warning');return;}
    if(!apiName){showToast('请输入API名称','warning');return;}
    var data = {
        apiCode: apiCode,
        apiName: apiName,
        groupNo: document.getElementById('formGroupNo').value||null,
        apiDescription: document.getElementById('formApiDescription').value||null,
        requestTimeoutMs: parseInt(document.getElementById('formRequestTimeoutMs').value)||30000,
        autoRetryCount: parseInt(document.getElementById('formAutoRetryCount').value)||64,
        retryIntervalMs: parseInt(document.getElementById('formRetryIntervalMs').value)||5000,
        status: document.querySelector('input[name=formStatus]:checked').value
    };
    var url = editingApiCode ? BASE+'/update' : BASE+'/create';
    var method = editingApiCode ? 'PUT' : 'POST';
    fetch(url,{method:method,headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast(editingApiCode?'保存成功':'创建成功','success');hideModal();loadConfigs();}
        else showToast(res.message||'操作失败','error');
    });
}

function deleteConfig(apiCode) { if(!confirm('确定要删除该API配置吗？'))return; fetch(BASE+'/'+apiCode,{method:'DELETE'}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('删除成功','success');loadConfigs();}else showToast(res.message||'删除失败','error');}); }
function toggleConfigStatus(apiCode,currentStatus) { var newStatus=currentStatus==='ENABLED'?'DISABLED':'ENABLED'; fetch(BASE+'/'+apiCode+'/'+(newStatus==='ENABLED'?'enable':'disable'),{method:'POST'}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('操作成功','success');loadConfigs();}else showToast(res.message||'操作失败','error');}); }
function copyConfig(apiCode) { showToast('复制功能开发中','info'); }
function viewChangeLog(apiCode) { parent.openTab('task-log',contextPath+'/task-log','调度日志',null); }

function addFilterRule() { var tbody=document.getElementById('filterRuleBody'); tbody.insertAdjacentHTML('beforeend','<tr><td><input type="text" class="form-control" placeholder="field"></td><td><select class="form-control"><option>equals</option><option>not_equals</option><option>contains</option><option>gt</option><option>lt</option></select></td><td><input type="text" class="form-control" placeholder="value"></td><td><button class="btn-text btn-danger-text" onclick="this.closest(\'tr\').remove()">删除</button></td></tr>'); }
function addPluginConfig() { var tbody=document.getElementById('pluginConfigBody'); tbody.insertAdjacentHTML('beforeend','<tr><td><input type="text" class="form-control" placeholder="插件编码"></td><td><input type="text" class="form-control" placeholder="插件名称"></td><td><input type="number" class="form-control" value="0"></td><td><input type="checkbox" checked></td><td><button class="btn-text btn-danger-text" onclick="this.closest(\'tr\').remove()">删除</button></td></tr>'); }

function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

document.getElementById('configModal').addEventListener('click',function(e){if(e.target===this)hideModal();});
document.getElementById('receiptHttp').addEventListener('change',function(){document.getElementById('receiptHttpConfig').style.display=this.checked?'block':'none';if(this.checked)document.getElementById('receiptNone').checked=false;});
document.getElementById('receiptMq').addEventListener('change',function(){document.getElementById('receiptMqConfig').style.display=this.checked?'block':'none';if(this.checked)document.getElementById('receiptNone').checked=false;});
document.getElementById('receiptNone').addEventListener('change',function(){if(this.checked){document.getElementById('receiptHttp').checked=false;document.getElementById('receiptMq').checked=false;document.getElementById('receiptHttpConfig').style.display='none';document.getElementById('receiptMqConfig').style.display='none';}});

loadGroups();
loadConfigs();
</script>
</body>
</html>
