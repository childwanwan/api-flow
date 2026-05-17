<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API配置</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
    <style>
        .step-wizard{display:flex;align-items:center;justify-content:center;padding:0 20px 16px;margin-bottom:4px;}
        .step-item{display:flex;align-items:center;gap:8px;cursor:pointer;padding:6px 12px;border-radius:20px;transition:all var(--transition-fast);font-size:13px;color:var(--text-secondary);}
        .step-item:hover{background:var(--primary-bg);}
        .step-item.active{color:var(--primary);font-weight:600;}
        .step-item.completed{color:var(--success);}
        .step-item .step-num{width:24px;height:24px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:12px;font-weight:600;border:2px solid var(--border-base);color:var(--text-secondary);background:#fff;transition:all var(--transition-fast);}
        .step-item.active .step-num{border-color:var(--primary);color:#fff;background:var(--primary);}
        .step-item.completed .step-num{border-color:var(--success);color:#fff;background:var(--success);}
        .step-connector{width:32px;height:2px;background:var(--border-light);margin:0 4px;transition:background var(--transition-fast);}
        .step-connector.completed{background:var(--success);}

        .dynamic-row{display:flex;align-items:center;gap:8px;padding:8px 12px;background:#fafbfc;border:1px solid var(--border-lighter);border-radius:var(--radius-md);margin-bottom:8px;transition:all var(--transition-fast);}
        .dynamic-row:hover{border-color:var(--border-light);background:#f5f7fa;}
        .dynamic-row .form-control{flex:1;min-width:0;padding:6px 10px;font-size:13px;}
        .dynamic-row .row-label{font-size:12px;color:var(--text-secondary);white-space:nowrap;min-width:fit-content;}
        .dynamic-row .btn-remove{width:28px;height:28px;border-radius:50%;border:none;background:transparent;color:var(--text-secondary);cursor:pointer;display:flex;align-items:center;justify-content:center;font-size:16px;transition:all var(--transition-fast);flex-shrink:0;}
        .dynamic-row .btn-remove:hover{background:var(--danger-bg);color:var(--danger);}
        .btn-add-row{display:inline-flex;align-items:center;gap:4px;padding:6px 14px;border:1px dashed var(--border-base);border-radius:var(--radius-md);background:transparent;color:var(--text-secondary);font-size:13px;cursor:pointer;transition:all var(--transition-fast);}
        .btn-add-row:hover{border-color:var(--primary);color:var(--primary);background:var(--primary-bg);}

        .receipt-section{border:1px solid var(--border-lighter);border-radius:var(--radius-md);padding:16px;margin-bottom:16px;background:#fafbfc;}
        .receipt-section-title{font-size:14px;font-weight:600;color:var(--text-primary);margin-bottom:12px;display:flex;align-items:center;gap:6px;}
        .receipt-section-title::before{content:'';width:3px;height:14px;background:var(--primary);border-radius:2px;display:inline-block;}

        .step-panel{display:none;}
        .step-panel.active{display:block;}

        .step-footer{display:flex;justify-content:space-between;padding:12px 20px;border-top:1px solid var(--border-lighter);background:#fafbfc;border-radius:0 0 var(--radius-lg) var(--radius-lg);}
        .step-footer .step-footer-left{display:flex;gap:8px;}
        .step-footer .step-footer-right{display:flex;gap:8px;}

        .confirm-overlay{position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.4);display:flex;align-items:center;justify-content:center;z-index:10000;}
        .confirm-box{background:#fff;border-radius:var(--radius-lg);padding:24px;max-width:400px;width:90%;box-shadow:0 8px 32px rgba(0,0,0,0.15);}
        .confirm-box h4{margin:0 0 12px;font-size:16px;color:var(--text-primary);}
        .confirm-box p{margin:0 0 20px;font-size:14px;color:var(--text-secondary);}
        .confirm-box .confirm-actions{display:flex;justify-content:flex-end;gap:8px;}
    </style>
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
                <th>目标URL</th>
                <th>状态</th>
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

<div id="configModal" class="modal-overlay" style="display:none;">
    <div class="modal modal-lg">
        <div class="modal-header">
            <h3 id="modalTitle">新建API</h3>
            <button class="modal-close" onclick="hideModal()">&times;</button>
        </div>
        <div class="modal-body">
            <div class="step-wizard" id="stepWizard">
                <div class="step-item active" onclick="goToStep(0)"><span class="step-num">1</span><span>基本信息</span></div>
                <div class="step-connector"></div>
                <div class="step-item" onclick="goToStep(1)"><span class="step-num">2</span><span>外部接口配置</span></div>
                <div class="step-connector"></div>
                <div class="step-item" onclick="goToStep(2)"><span class="step-num">3</span><span>限流配置</span></div>
                <div class="step-connector"></div>
                <div class="step-item" onclick="goToStep(3)"><span class="step-num">4</span><span>过滤规则</span></div>
                <div class="step-connector"></div>
                <div class="step-item" onclick="goToStep(4)"><span class="step-num">5</span><span>插件配置</span></div>
                <div class="step-connector"></div>
                <div class="step-item" onclick="goToStep(5)"><span class="step-num">6</span><span>回执配置</span></div>
            </div>

            <div id="step-0" class="step-panel active">
                <div class="form-section">
                    <div class="form-section-title">基本信息</div>
                    <div class="form-row">
                        <div class="form-group"><label>所属分组</label><select id="formGroupNo" class="form-control"><option value="">请选择分组</option></select></div>
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
                        <div class="form-group"><label>最大重试次数</label><input type="number" id="formAutoRetryCount" class="form-control" value="64"><span class="form-hint">默认64次</span></div>
                        <div class="form-group"><label>重试间隔(ms)</label><input type="number" id="formRetryIntervalMs" class="form-control" value="5000"><span class="form-hint">默认5000ms</span></div>
                    </div>
                    <div class="form-row">
                        <div class="form-group"><label>最大队列大小</label><input type="number" id="formMaxQueueSize" class="form-control" value="100000"><span class="form-hint">默认100000</span></div>
                    </div>
                </div>
            </div>

            <div id="step-1" class="step-panel">
                <div class="form-section">
                    <div class="form-section-title">外部接口请求配置</div>
                    <p style="color:#909399;margin-bottom:12px;font-size:13px;">配置业务执行插件发起的真实HTTP请求。支持模板变量：${r"${params.xxx}"}、${r"${customData.xxx}"}、${r"${taskNo}"}、${r"${apiCode}"}</p>
                    <div class="form-group"><label>目标URL</label><input type="text" id="formTargetUrl" class="form-control" placeholder="https://api.example.com/orders/${r"${params.orderId}"}"></div>
                    <div class="form-row">
                        <div class="form-group"><label>请求方法</label>
                            <select id="formTargetMethod" class="form-control">
                                <option value="POST">POST</option>
                                <option value="GET">GET</option>
                                <option value="PUT">PUT</option>
                                <option value="DELETE">DELETE</option>
                                <option value="PATCH">PATCH</option>
                            </select>
                        </div>
                        <div class="form-group"><label>请求超时(ms)</label><input type="number" id="formRequestTimeoutMs" class="form-control" value="30000"><span class="form-hint">默认30000ms</span></div>
                    </div>
                    <div class="form-section-title" style="margin-top:16px;">请求头</div>
                    <div id="headerRows"></div>
                    <button class="btn-add-row" onclick="addHeaderRow()">+ 添加请求头</button>
                    <div class="form-section-title" style="margin-top:16px;">请求体模板</div>
                    <div class="form-group"><label>Body模板 (JSON)</label><textarea id="formTargetBodyTemplate" class="form-control" style="min-height:120px;font-family:monospace;" placeholder='{"orderId":"${r"${params.orderId}"}","taskNo":"${r"${taskNo}"}"}'></textarea></div>
                </div>
                <div class="form-section">
                    <div class="form-section-title">环境变量配置</div>
                    <p style="color:#909399;margin-bottom:12px;font-size:13px;">自定义环境变量，可在模板中通过 ${r"${env.xxx}"} 引用</p>
                    <div id="envConfigRows"></div>
                    <button class="btn-add-row" onclick="addEnvRow()">+ 添加环境变量</button>
                </div>
            </div>

            <div id="step-2" class="step-panel">
                <div class="form-section">
                    <div class="form-section-title">限流配置</div>
                    <div class="form-group"><label><input type="checkbox" id="formRateLimitEnabled" checked> 启用限流</label></div>
                    <div id="rateLimitRulesContainer"></div>
                    <button class="btn-add-row" onclick="addRateLimitRule()">+ 添加限流规则</button>
                </div>
            </div>

            <div id="step-3" class="step-panel">
                <div class="form-section">
                    <div class="form-section-title">过滤规则</div>
                    <p style="color:#909399;margin-bottom:12px;font-size:13px;">支持按字段条件过滤请求参数</p>
                    <div id="filterRuleRows"></div>
                    <button class="btn-add-row" onclick="addFilterRule()">+ 添加过滤规则</button>
                </div>
            </div>

            <div id="step-4" class="step-panel">
                <div class="form-section">
                    <div class="form-section-title">插件配置</div>
                    <p style="color:#909399;margin-bottom:12px;font-size:13px;">配置API执行的责任链插件</p>
                    <div class="form-group"><label><input type="checkbox" id="formPluginEnabled" checked> 启用插件链</label></div>
                    <div id="pluginConfigRows"></div>
                    <button class="btn-add-row" onclick="addPluginConfig()">+ 添加插件</button>
                </div>
            </div>

            <div id="step-5" class="step-panel">
                <div class="form-section">
                    <div class="form-section-title">回执配置</div>
                    <div class="form-group"><label>回执类型</label><div class="checkbox-group"><label><input type="checkbox" id="receiptHttp" value="HTTP"> HTTP</label><label><input type="checkbox" id="receiptMq" value="MQ"> MQ</label><label><input type="checkbox" id="receiptNone" value="NONE" checked> 无</label></div></div>

                    <div id="receiptHttpConfig" style="display:none;">
                        <div class="receipt-section">
                            <div class="receipt-section-title">HTTP回执</div>
                            <div class="form-row"><div class="form-group"><label>URL</label><input type="text" id="receiptHttpUrl" class="form-control" placeholder="https://callback.example.com/api"></div><div class="form-group"><label>方法</label><select id="receiptHttpMethod" class="form-control"><option>POST</option><option>GET</option><option>PUT</option></select></div></div>
                            <div class="form-row"><div class="form-group"><label>超时(ms)</label><input type="number" id="receiptHttpTimeout" class="form-control" value="5000"></div><div class="form-group"><label>重试次数</label><input type="number" id="receiptHttpRetries" class="form-control" value="64"></div></div>
                            <div class="form-section-title" style="margin-top:12px;">请求头</div>
                            <div id="receiptHttpHeaderRows"></div>
                            <button class="btn-add-row" onclick="addReceiptHttpHeaderRow()">+ 添加回执请求头</button>
                            <div class="form-group" style="margin-top:12px;"><label>消息体模板</label><textarea id="receiptHttpBody" class="form-control" style="min-height:100px;font-family:monospace;" placeholder='{"taskNo":"${r"${taskNo}"}","status":"${r"${status}"}"}'></textarea></div>
                        </div>
                    </div>

                    <div id="receiptMqConfig" style="display:none;">
                        <div class="receipt-section">
                            <div class="receipt-section-title">MQ回执</div>
                            <div class="form-group"><label>Topic</label><input type="text" id="receiptMqTopic" class="form-control" placeholder="api-callback-topic"></div>
                            <div class="form-section-title" style="margin-top:12px;">消息头</div>
                            <div id="receiptMqHeaderRows"></div>
                            <button class="btn-add-row" onclick="addReceiptMqHeaderRow()">+ 添加消息头</button>
                            <div class="form-group" style="margin-top:12px;"><label>消息体模板</label><textarea id="receiptMqBody" class="form-control" style="min-height:100px;font-family:monospace;" placeholder='{"taskNo":"${r"${taskNo}"}","status":"${r"${status}"}"}'></textarea></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="step-footer">
            <div class="step-footer-left">
                <button class="btn" id="btnPrev" onclick="prevStep()" style="display:none;">上一步</button>
            </div>
            <div class="step-footer-right">
                <button class="btn" onclick="hideModal()">取消</button>
                <button class="btn btn-primary" id="btnNext" onclick="nextStep()">下一步</button>
            </div>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-config';}
var BASE = contextPath + '/api/v1/config';
var editingApiCode = null;
var currentStep = 0;
var totalSteps = 6;

var configPagination = new Pagination({el:'configPagination',total:0,mode:'simple',showSizeChanger:false});

function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function statusTag(s) { var m={ENABLED:'enabled',DISABLED:'disabled'}; var t={ENABLED:'启用',DISABLED:'禁用'}; return '<span class="status-tag status-'+(m[s]||'disabled')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }

function goToStep(step) {
    if (step < 0 || step >= totalSteps) return;
    currentStep = step;
    document.querySelectorAll('.step-panel').forEach(function(p){p.classList.remove('active');});
    document.getElementById('step-'+step).classList.add('active');
    document.querySelectorAll('.step-item').forEach(function(item, idx){
        item.classList.remove('active');
        if (idx < step) item.classList.add('completed');
        else item.classList.remove('completed');
        if (idx === step) item.classList.add('active');
    });
    document.querySelectorAll('.step-connector').forEach(function(c, idx){
        if (idx < step) c.classList.add('completed');
        else c.classList.remove('completed');
    });
    document.getElementById('btnPrev').style.display = step === 0 ? 'none' : '';
    var btnNext = document.getElementById('btnNext');
    if (step === totalSteps - 1) {
        btnNext.textContent = '提交';
        btnNext.onclick = saveConfig;
    } else {
        btnNext.textContent = '下一步';
        btnNext.onclick = nextStep;
    }
    document.querySelector('.modal-body').scrollTop = 0;
}

function nextStep() {
    if (!validateStep(currentStep)) return;
    goToStep(currentStep + 1);
}

function prevStep() {
    goToStep(currentStep - 1);
}

function validateStep(step) {
    if (step === 0) {
        var apiCode = document.getElementById('formApiCode').value.trim();
        var apiName = document.getElementById('formApiName').value.trim();
        if (!apiCode) { showToast('请输入API编码','warning'); return false; }
        if (!apiName) { showToast('请输入API名称','warning'); return false; }
    }
    return true;
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
    if(!configs.length) { tbody.innerHTML='<tr><td colspan="7" class="empty-row">暂无数据</td></tr>'; configPagination.update(0); return; }
    tbody.innerHTML = configs.map(function(c){
        var ec = c.extraConfig || {};
        var targetUrlDisplay = ec.targetUrl ? (ec.targetUrl.length > 40 ? ec.targetUrl.substring(0,40)+'...' : ec.targetUrl) : '-';
        return '<tr>'+
            '<td class="checkbox-col"><input type="checkbox" class="row-checkbox"></td>'+
            '<td>'+c.apiCode+'</td>'+
            '<td>'+(c.apiName||'')+'</td>'+
            '<td>'+(c.groupNo||'-')+'</td>'+
            '<td title="'+(ec.targetUrl||'')+'">'+targetUrlDisplay+'</td>'+
            '<td>'+statusTag(c.status||'ENABLED')+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editConfig(\''+c.apiCode+'\')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteConfig(\''+c.apiCode+'\')">删除</button>'+
                '<span class="action-divider">|</span>'+
                '<span class="dropdown" id="more_'+c.apiCode+'">'+
                    '<button class="btn-text" onclick="toggleDropdown(\''+c.apiCode+'\')">更多▼</button>'+
                    '<div class="dropdown-menu">'+
                        '<button class="dropdown-item" onclick="toggleConfigStatus(\''+c.apiCode+'\',\''+c.status+'\')">'+(c.status==='ENABLED'?'禁用':'启用')+'</button>'+
                        '<button class="dropdown-item" onclick="viewChangeLog(\''+c.apiCode+'\')">变更日志</button>'+
                    '</div>'+
                '</span>'+
            '</td></tr>';
    }).join('');
    configPagination.update(configs.length);
}

function toggleDropdown(code) {
    var el = document.getElementById('more_'+code);
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

function searchConfigs() { loadConfigs(); }
function resetSearch() { document.getElementById('searchApiCode').value=''; document.getElementById('searchApiName').value=''; document.getElementById('searchGroupNo').value=''; document.getElementById('searchStatus').value=''; loadConfigs(); }

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g,'&amp;').replace(/"/g,'&quot;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function tryParseJson(str) {
    if (!str || !str.trim()) return null;
    try { return JSON.parse(str); } catch(e) { return str; }
}

function addHeaderRow(key, value) {
    var container = document.getElementById('headerRows');
    var k = key || '';
    var v = value || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.innerHTML = '<span class="row-label">Key</span><input type="text" class="form-control header-key" value="'+escapeHtml(k)+'" placeholder="如：Authorization"><span class="row-label">Value</span><input type="text" class="form-control header-value" value="'+escapeHtml(v)+'" placeholder="如：Bearer token"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
    row.querySelector('.header-key').focus();
}

function addEnvRow(key, value) {
    var container = document.getElementById('envConfigRows');
    var k = key || '';
    var v = value || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.innerHTML = '<span class="row-label">Key</span><input type="text" class="form-control env-key" value="'+escapeHtml(k)+'" placeholder="如：region"><span class="row-label">Value</span><input type="text" class="form-control env-value" value="'+escapeHtml(v)+'" placeholder="如：us-east-1"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
    row.querySelector('.env-key').focus();
}

function addRateLimitRule(name, limit, windowSeconds, type, dimension, keyTemplate) {
    var container = document.getElementById('rateLimitRulesContainer');
    var n = name || '';
    var lm = limit || '';
    var ws = windowSeconds || '';
    var tp = type || 'QPS';
    var dm = dimension || 'API';
    var kt = keyTemplate || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.style.flexWrap = 'wrap';
    row.innerHTML = '<span class="row-label">名称</span><input type="text" class="form-control rl-name" value="'+escapeHtml(n)+'" placeholder="API维度限流" style="flex:2;"><span class="row-label">类型</span><select class="form-control rl-type" style="flex:1;min-width:100px;"><option value="QPS"'+(tp==='QPS'?' selected':'')+'>QPS</option><option value="CONCURRENCY"'+(tp==='CONCURRENCY'?' selected':'')+'>并发数</option></select><span class="row-label">维度</span><select class="form-control rl-dimension" style="flex:1;min-width:100px;"><option value="API"'+(dm==='API'?' selected':'')+'>API级</option><option value="ACCOUNT"'+(dm==='ACCOUNT'?' selected':'')+'>账号维度</option><option value="CUSTOM"'+(dm==='CUSTOM'?' selected':'')+'>自定义</option></select><span class="row-label">阈值</span><input type="number" class="form-control rl-limit" value="'+escapeHtml(String(lm))+'" placeholder="100" style="flex:1;min-width:80px;"><span class="row-label">窗口(秒)</span><input type="number" class="form-control rl-window" value="'+escapeHtml(String(ws))+'" placeholder="1" style="flex:1;min-width:80px;"><span class="row-label">Key</span><input type="text" class="form-control rl-key" value="'+escapeHtml(kt)+'" placeholder="${r"${apiCode}"}" style="flex:2;"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
}

function addFilterRule(field, operator, value, message) {
    var container = document.getElementById('filterRuleRows');
    var f = field || '';
    var op = operator || 'EQ';
    var v = value || '';
    var msg = message || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.style.flexWrap = 'wrap';
    row.innerHTML = '<span class="row-label">字段</span><input type="text" class="form-control fr-field" value="'+escapeHtml(f)+'" placeholder="params.orderId" style="flex:2;"><span class="row-label">操作符</span><select class="form-control fr-op" style="flex:1;min-width:100px;"><option value="EQ"'+(op==='EQ'?' selected':'')+'>等于</option><option value="NE"'+(op==='NE'?' selected':'')+'>不等于</option><option value="GT"'+(op==='GT'?' selected':'')+'>大于</option><option value="GTE"'+(op==='GTE'?' selected':'')+'>大于等于</option><option value="LT"'+(op==='LT'?' selected':'')+'>小于</option><option value="LTE"'+(op==='LTE'?' selected':'')+'>小于等于</option><option value="IN"'+(op==='IN'?' selected':'')+'>包含于</option><option value="NOT_IN"'+(op==='NOT_IN'?' selected':'')+'>不包含于</option><option value="MATCHES"'+(op==='MATCHES'?' selected':'')+'>正则匹配</option><option value="NOT_MATCHES"'+(op==='NOT_MATCHES'?' selected':'')+'>不匹配</option><option value="NOT_EMPTY"'+(op==='NOT_EMPTY'?' selected':'')+'>非空</option><option value="EMPTY"'+(op==='EMPTY'?' selected':'')+'>为空</option></select><span class="row-label">值</span><input type="text" class="form-control fr-value" value="'+escapeHtml(v)+'" placeholder="匹配值" style="flex:2;"><span class="row-label">提示</span><input type="text" class="form-control fr-message" value="'+escapeHtml(msg)+'" placeholder="校验失败提示" style="flex:2;"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
}

function addPluginConfig(pluginCode, enabled, order, config) {
    var container = document.getElementById('pluginConfigRows');
    var pc = pluginCode || '';
    var en = enabled !== false;
    var o = order || 0;
    var c = config || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.innerHTML = '<span class="row-label">编码</span><input type="text" class="form-control pc-code" value="'+escapeHtml(pc)+'" placeholder="插件编码" style="flex:2;"><span class="row-label">启用</span><input type="checkbox" class="pc-enabled"'+(en?' checked':'')+'><span class="row-label">顺序</span><input type="number" class="form-control pc-order" value="'+o+'" style="max-width:80px;"><span class="row-label">配置</span><input type="text" class="form-control pc-config" value="'+escapeHtml(c)+'" placeholder="JSON配置" style="flex:2;"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
}

function addReceiptHttpHeaderRow(key, value) {
    var container = document.getElementById('receiptHttpHeaderRows');
    var k = key || '';
    var v = value || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.innerHTML = '<span class="row-label">Key</span><input type="text" class="form-control rh-key" value="'+escapeHtml(k)+'" placeholder="Header名称"><span class="row-label">Value</span><input type="text" class="form-control rh-value" value="'+escapeHtml(v)+'" placeholder="Header值"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
}

function addReceiptMqHeaderRow(key, value) {
    var container = document.getElementById('receiptMqHeaderRows');
    var k = key || '';
    var v = value || '';
    var row = document.createElement('div');
    row.className = 'dynamic-row';
    row.innerHTML = '<span class="row-label">Key</span><input type="text" class="form-control rmh-key" value="'+escapeHtml(k)+'" placeholder="Header名称"><span class="row-label">Value</span><input type="text" class="form-control rmh-value" value="'+escapeHtml(v)+'" placeholder="Header值"><button class="btn-remove" onclick="this.parentElement.remove()" title="删除">&times;</button>';
    container.appendChild(row);
}

function collectKvRows(containerId, keyClass, valueClass) {
    var result = {};
    document.querySelectorAll('#'+containerId+' .dynamic-row').forEach(function(row) {
        var key = row.querySelector('.'+keyClass);
        var val = row.querySelector('.'+valueClass);
        if (key && val && key.value.trim()) {
            result[key.value.trim()] = val.value;
        }
    });
    return Object.keys(result).length > 0 ? result : null;
}

function collectHeaders() { return collectKvRows('headerRows','header-key','header-value'); }
function collectEnvConfig() { return collectKvRows('envConfigRows','env-key','env-value'); }

function collectRateLimitConfig() {
    var enabled = document.getElementById('formRateLimitEnabled').checked;
    var rules = [];
    document.querySelectorAll('#rateLimitRulesContainer .dynamic-row').forEach(function(row) {
        var name = row.querySelector('.rl-name');
        var type = row.querySelector('.rl-type');
        var dimension = row.querySelector('.rl-dimension');
        var limit = row.querySelector('.rl-limit');
        var window = row.querySelector('.rl-window');
        var key = row.querySelector('.rl-key');
        if (name && name.value.trim()) {
            rules.push({
                name: name.value.trim(),
                type: type ? type.value : 'QPS',
                dimension: dimension ? dimension.value : 'API',
                limit: limit ? parseInt(limit.value) || null : null,
                windowSeconds: window ? parseInt(window.value) || null : null,
                keyTemplate: key ? key.value.trim() || null : null
            });
        }
    });
    return { enabled: enabled, rules: rules.length > 0 ? rules : null };
}

function collectFilterRules() {
    var rules = [];
    document.querySelectorAll('#filterRuleRows .dynamic-row').forEach(function(row) {
        var field = row.querySelector('.fr-field');
        var op = row.querySelector('.fr-op');
        var val = row.querySelector('.fr-value');
        var msg = row.querySelector('.fr-message');
        if (field && field.value.trim()) {
            rules.push({
                field: field.value.trim(),
                operator: op ? op.value : 'EQ',
                value: val ? val.value : '',
                message: msg ? msg.value.trim() || null : null
            });
        }
    });
    return rules.length > 0 ? { rules: rules } : null;
}

function collectPluginConfig() {
    var enabled = document.getElementById('formPluginEnabled').checked;
    var chain = [];
    document.querySelectorAll('#pluginConfigRows .dynamic-row').forEach(function(row) {
        var code = row.querySelector('.pc-code');
        var en = row.querySelector('.pc-enabled');
        var order = row.querySelector('.pc-order');
        var config = row.querySelector('.pc-config');
        if (code && code.value.trim()) {
            chain.push({
                pluginCode: code.value.trim(),
                enabled: en ? en.checked : true,
                order: order ? parseInt(order.value) || 0 : 0,
                config: config ? config.value || null : null
            });
        }
    });
    return { enabled: enabled, pluginChain: chain.length > 0 ? chain : null };
}

function collectReceiptConfig() {
    var types = [];
    var isHttp = document.getElementById('receiptHttp').checked;
    var isMq = document.getElementById('receiptMq').checked;
    if (isHttp) types.push('HTTP');
    if (isMq) types.push('MQ');
    if (!isHttp && !isMq) types.push('NONE');

    var result = { receiptTypes: types };
    if (isHttp) {
        var httpHeaders = collectKvRows('receiptHttpHeaderRows','rh-key','rh-value');
        result.http = {
            url: document.getElementById('receiptHttpUrl').value || null,
            method: document.getElementById('receiptHttpMethod').value || 'POST',
            headers: httpHeaders,
            bodyTemplate: tryParseJson(document.getElementById('receiptHttpBody').value),
            retryPolicy: {
                maxRetries: parseInt(document.getElementById('receiptHttpRetries').value) || 64
            },
            timeoutMs: parseInt(document.getElementById('receiptHttpTimeout').value) || 5000
        };
    }
    if (isMq) {
        var mqHeaders = collectKvRows('receiptMqHeaderRows','rmh-key','rmh-value');
        result.mq = {
            topic: document.getElementById('receiptMqTopic').value || null,
            headers: mqHeaders,
            bodyTemplate: tryParseJson(document.getElementById('receiptMqBody').value)
        };
    }
    return result;
}

function resetAllForms() {
    document.getElementById('formApiCode').value = '';
    document.getElementById('formApiCode').readOnly = false;
    document.getElementById('formApiName').value = '';
    document.getElementById('formApiDescription').value = '';
    document.getElementById('formAutoRetryCount').value = '64';
    document.getElementById('formRetryIntervalMs').value = '5000';
    document.getElementById('formMaxQueueSize').value = '100000';
    document.getElementById('formRequestTimeoutMs').value = '30000';
    document.querySelector('input[name=formStatus][value=ENABLED]').checked = true;
    document.getElementById('formGroupNo').value = '';

    document.getElementById('formTargetUrl').value = '';
    document.getElementById('formTargetMethod').value = 'POST';
    document.getElementById('formTargetBodyTemplate').value = '';
    document.getElementById('headerRows').innerHTML = '';
    document.getElementById('envConfigRows').innerHTML = '';

    document.getElementById('formRateLimitEnabled').checked = true;
    document.getElementById('rateLimitRulesContainer').innerHTML = '';

    document.getElementById('filterRuleRows').innerHTML = '';

    document.getElementById('formPluginEnabled').checked = true;
    document.getElementById('pluginConfigRows').innerHTML = '';

    document.getElementById('receiptHttp').checked = false;
    document.getElementById('receiptMq').checked = false;
    document.getElementById('receiptNone').checked = true;
    document.getElementById('receiptHttpConfig').style.display = 'none';
    document.getElementById('receiptMqConfig').style.display = 'none';
    document.getElementById('receiptHttpUrl').value = '';
    document.getElementById('receiptHttpMethod').value = 'POST';
    document.getElementById('receiptHttpTimeout').value = '5000';
    document.getElementById('receiptHttpRetries').value = '64';
    document.getElementById('receiptHttpHeaderRows').innerHTML = '';
    document.getElementById('receiptHttpBody').value = '';
    document.getElementById('receiptMqTopic').value = '';
    document.getElementById('receiptMqHeaderRows').innerHTML = '';
    document.getElementById('receiptMqBody').value = '';
}

function showAddDialog() {
    editingApiCode = null;
    document.getElementById('modalTitle').textContent = '新建API';
    resetAllForms();
    goToStep(0);
    document.getElementById('configModal').style.display = 'flex';
    setTimeout(function(){ if(window.InputClear) InputClear.init(document.getElementById('configModal')); }, 100);
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
            document.getElementById('formAutoRetryCount').value = c.autoRetryCount||64;
            document.getElementById('formRetryIntervalMs').value = c.retryIntervalMs||5000;
            document.getElementById('formMaxQueueSize').value = c.maxQueueSize||100000;
            document.getElementById('formRequestTimeoutMs').value = c.requestTimeoutMs||30000;
            var radio = document.querySelector('input[name=formStatus][value='+(c.status||'ENABLED')+']');
            if(radio) radio.checked = true;

            var ec = c.extraConfig || {};
            document.getElementById('formTargetUrl').value = ec.targetUrl || '';
            document.getElementById('formTargetMethod').value = ec.targetMethod || 'POST';
            document.getElementById('formTargetBodyTemplate').value = ec.targetBodyTemplate || '';
            document.getElementById('headerRows').innerHTML = '';
            if (ec.targetHeaders) {
                Object.keys(ec.targetHeaders).forEach(function(k) { addHeaderRow(k, ec.targetHeaders[k]); });
            }
            document.getElementById('envConfigRows').innerHTML = '';
            if (ec.envConfig) {
                Object.keys(ec.envConfig).forEach(function(k) { addEnvRow(k, ec.envConfig[k]); });
            }

            var rl = c.rateLimitConfig || {};
            document.getElementById('formRateLimitEnabled').checked = rl.enabled !== false;
            document.getElementById('rateLimitRulesContainer').innerHTML = '';
            if (rl.rules && rl.rules.length) {
                rl.rules.forEach(function(r) { addRateLimitRule(r.name, r.limit, r.windowSeconds, r.type, r.dimension, r.keyTemplate); });
            }

            var fr = c.filterRules || {};
            document.getElementById('filterRuleRows').innerHTML = '';
            if (fr.rules && fr.rules.length) {
                fr.rules.forEach(function(r) { addFilterRule(r.field, r.operator, r.value, r.message); });
            }

            var pc = c.pluginConfig || {};
            document.getElementById('formPluginEnabled').checked = pc.enabled !== false;
            document.getElementById('pluginConfigRows').innerHTML = '';
            if (pc.pluginChain && pc.pluginChain.length) {
                pc.pluginChain.forEach(function(p) {
                    addPluginConfig(p.pluginCode, p.enabled, p.order, typeof p.config === 'string' ? p.config : JSON.stringify(p.config || ''));
                });
            }

            var rc = c.receiptConfig || {};
            var rTypes = rc.receiptTypes || [];
            var isHttp = rTypes.indexOf('HTTP') >= 0;
            var isMq = rTypes.indexOf('MQ') >= 0;
            document.getElementById('receiptHttp').checked = isHttp;
            document.getElementById('receiptMq').checked = isMq;
            document.getElementById('receiptNone').checked = !isHttp && !isMq;
            document.getElementById('receiptHttpConfig').style.display = isHttp ? 'block' : 'none';
            document.getElementById('receiptMqConfig').style.display = isMq ? 'block' : 'none';
            if (isHttp && rc.http) {
                document.getElementById('receiptHttpUrl').value = rc.http.url || '';
                document.getElementById('receiptHttpMethod').value = rc.http.method || 'POST';
                document.getElementById('receiptHttpTimeout').value = rc.http.timeoutMs || 5000;
                document.getElementById('receiptHttpRetries').value = (rc.http.retryPolicy && rc.http.retryPolicy.maxRetries) || 64;
                document.getElementById('receiptHttpHeaderRows').innerHTML = '';
                if (rc.http.headers) { Object.keys(rc.http.headers).forEach(function(k) { addReceiptHttpHeaderRow(k, rc.http.headers[k]); }); }
                document.getElementById('receiptHttpBody').value = typeof rc.http.bodyTemplate === 'string' ? rc.http.bodyTemplate : JSON.stringify(rc.http.bodyTemplate || '');
            }
            if (isMq && rc.mq) {
                document.getElementById('receiptMqTopic').value = rc.mq.topic || '';
                document.getElementById('receiptMqHeaderRows').innerHTML = '';
                if (rc.mq.headers) { Object.keys(rc.mq.headers).forEach(function(k) { addReceiptMqHeaderRow(k, rc.mq.headers[k]); }); }
                document.getElementById('receiptMqBody').value = typeof rc.mq.bodyTemplate === 'string' ? rc.mq.bodyTemplate : JSON.stringify(rc.mq.bodyTemplate || '');
            }

            goToStep(0);
            document.getElementById('configModal').style.display = 'flex';
            setTimeout(function(){ if(window.InputClear) InputClear.init(document.getElementById('configModal')); }, 100);
        }
    });
}

function hideModal() { document.getElementById('configModal').style.display='none'; }

function saveConfig() {
    var apiCode = document.getElementById('formApiCode').value.trim();
    var apiName = document.getElementById('formApiName').value.trim();
    if(!apiCode){showToast('请输入API编码','warning');goToStep(0);return;}
    if(!apiName){showToast('请输入API名称','warning');goToStep(0);return;}

    var extraConfig = {
        targetUrl: document.getElementById('formTargetUrl').value || null,
        targetMethod: document.getElementById('formTargetMethod').value || null,
        targetHeaders: collectHeaders(),
        targetBodyTemplate: tryParseJson(document.getElementById('formTargetBodyTemplate').value),
        targetTimeoutMs: parseInt(document.getElementById('formRequestTimeoutMs').value) || null,
        envConfig: collectEnvConfig()
    };

    var data = {
        apiCode: apiCode,
        apiName: apiName,
        groupNo: document.getElementById('formGroupNo').value||null,
        apiDescription: document.getElementById('formApiDescription').value||null,
        requestTimeoutMs: parseInt(document.getElementById('formRequestTimeoutMs').value)||30000,
        autoRetryCount: parseInt(document.getElementById('formAutoRetryCount').value)||64,
        retryIntervalMs: parseInt(document.getElementById('formRetryIntervalMs').value)||5000,
        maxQueueSize: parseInt(document.getElementById('formMaxQueueSize').value)||100000,
        status: document.querySelector('input[name=formStatus]:checked').value,
        extraConfig: extraConfig,
        rateLimitConfig: collectRateLimitConfig(),
        filterRules: collectFilterRules(),
        pluginConfig: collectPluginConfig(),
        receiptConfig: collectReceiptConfig()
    };

    var url = editingApiCode ? BASE+'/update' : BASE+'/create';
    var method = editingApiCode ? 'PUT' : 'POST';
    fetch(url,{method:method,headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast(editingApiCode?'保存成功':'创建成功','success');hideModal();loadConfigs();}
        else showToast(res.error&&res.error.message||'操作失败','error');
    });
}

function showConfirm(msg, onConfirm) {
    var overlay = document.createElement('div');
    overlay.className = 'confirm-overlay';
    overlay.innerHTML = '<div class="confirm-box"><h4>确认操作</h4><p>'+msg+'</p><div class="confirm-actions"><button class="btn" onclick="this.closest(\'.confirm-overlay\').remove()">取消</button><button class="btn btn-danger" id="confirmOk">确定</button></div></div>';
    document.body.appendChild(overlay);
    document.getElementById('confirmOk').onclick = function() { overlay.remove(); onConfirm(); };
    overlay.addEventListener('click', function(e) { if(e.target===overlay) overlay.remove(); });
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
    var newStatus=currentStatus==='ENABLED'?'DISABLED':'ENABLED';
    var label=newStatus==='ENABLED'?'启用':'禁用';
    showConfirm('确定要'+label+'该API配置吗？', function() {
        fetch(BASE+'/'+apiCode+'/'+(newStatus==='ENABLED'?'enable':'disable'),{method:'POST'}).then(function(r){return r.json();}).then(function(res){
            if(res.success){showToast('操作成功','success');loadConfigs();}
            else showToast(res.error&&res.error.message||'操作失败','error');
        });
    });
}

function viewChangeLog(apiCode) { parent.openTab('config-log',contextPath+'/config-log?apiCode='+apiCode,'配置变更日志',null); }

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
