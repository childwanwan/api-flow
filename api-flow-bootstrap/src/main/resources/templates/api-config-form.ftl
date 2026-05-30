<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title id="pageTitle">新建API</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
    <style>
        .step-progress{display:flex;align-items:flex-start;justify-content:center;padding:0 40px;}
        .step-item{display:flex;flex-direction:column;align-items:center;gap:8px;cursor:pointer;position:relative;z-index:1;}
        .step-node{width:32px;height:32px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:700;border:1.5px solid var(--border-base);background:#fff;color:var(--text-placeholder);transition:all 0.3s ease;}
        .step-item.active .step-node{border-color:var(--primary);background:var(--primary);color:#fff;box-shadow:0 2px 8px rgba(64,158,255,0.35);animation:step-pulse 2s ease-in-out infinite;}
        .step-item.completed .step-node{border-color:var(--success);background:var(--success);color:#fff;}
        .step-node svg{width:14px;height:14px;fill:currentColor;}
        .step-label{font-size:12px;color:var(--text-secondary);transition:all 0.3s ease;white-space:nowrap;}
        .step-item.active .step-label{color:var(--primary);font-weight:600;}
        .step-item.completed .step-label{color:var(--success);}
        .step-connector{width:60px;height:3px;background:var(--border-lighter);margin-top:14px;transition:background 0.3s ease;flex-shrink:0;}
        .step-connector.completed{background:linear-gradient(90deg, var(--primary,#409eff), var(--primary-light,#66b1ff));}

        .config-card{background:#fff;border:1px solid var(--border-lighter);border-radius:var(--radius-lg);padding:20px;margin-bottom:20px;box-shadow:0 1px 2px rgba(0,0,0,0.04);transition:box-shadow 0.2s ease;}
        .config-card:hover{box-shadow:0 2px 8px rgba(0,0,0,0.06);}
        .config-card .form-section-title{margin-bottom:16px;border-left:3px solid var(--primary,#409eff);padding-left:10px;}

        .edit-table{width:100%;border-collapse:collapse;}
        .edit-table thead{border-bottom:1px solid var(--border-lighter);}
        .edit-table th{padding:8px 12px;font-size:11px;font-weight:600;color:var(--text-secondary);text-align:left;text-transform:uppercase;letter-spacing:0.5px;white-space:nowrap;}
        .edit-table td{padding:4px 6px;border-bottom:1px solid var(--border-extra-light);vertical-align:middle;}
        .edit-table tbody tr:last-child td{border-bottom:none;}
        .edit-table tbody tr{transition:background 0.15s ease;}
        .edit-table tbody tr:hover{background:var(--primary-bg);}
        .edit-table td .form-control,.edit-table td select.form-control{padding:6px 10px;font-size:13px;width:100%;border:1px solid transparent;border-radius:var(--radius-sm);background:var(--border-extra-light);transition:all 0.2s ease;}
        .edit-table td .form-control:hover,.edit-table td select.form-control:hover{border-color:var(--border-lighter);background:#fff;}
        .edit-table td .form-control:focus,.edit-table td select.form-control:focus{border-color:var(--primary);background:#fff;box-shadow:0 0 0 3px rgba(64,158,255,0.1);outline:none;}
        .edit-table .col-action{width:36px;text-align:center;}
        .edit-table .col-check{text-align:center;width:50px;}
        .edit-table .col-check label{display:inline-flex;align-items:center;gap:3px;cursor:pointer;font-size:12px;white-space:nowrap;}
        .edit-table .col-sm{width:80px;}
        .edit-table .col-md{width:100px;}

        .btn-del-circle{border:none;background:transparent;color:var(--text-placeholder);cursor:pointer;width:24px;height:24px;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;font-size:14px;line-height:1;transition:all 0.15s ease;opacity:0;font-family:inherit;}
        .edit-table tbody tr:hover .btn-del-circle{opacity:1;}
        .btn-del-circle:hover{background:var(--danger-bg);color:var(--danger);}

        .add-zone{border:1.5px dashed var(--border-light);border-radius:var(--radius-md);padding:10px;text-align:center;cursor:pointer;color:var(--text-secondary);font-size:13px;font-weight:500;transition:all 0.2s ease;margin-top:8px;display:flex;align-items:center;justify-content:center;gap:6px;background:transparent;}
        .add-zone:hover{border-color:var(--primary);border-style:solid;color:var(--primary);background:var(--primary-bg);}
        .add-zone .add-icon{font-size:16px;font-weight:300;line-height:1;}

        .method-segmented{display:inline-flex;background:var(--border-extra-light);border-radius:var(--radius-md);padding:3px;gap:2px;}
        .method-seg-btn{padding:6px 14px;border:none;background:transparent;color:var(--text-secondary);font-size:12px;font-weight:600;cursor:pointer;border-radius:var(--radius-sm);transition:all 0.2s ease;font-family:inherit;letter-spacing:0.3px;}
        .method-seg-btn:hover:not(.active){color:var(--text-primary);background:rgba(255,255,255,0.6);}
        .method-seg-btn.active{background:var(--primary);color:#fff;box-shadow:0 1px 3px rgba(64,158,255,0.3);}

        .step-panel{display:none;}
        .step-panel.active{display:block;}

        input[type="number"]::-webkit-inner-spin-button,input[type="number"]::-webkit-outer-spin-button{-webkit-appearance:none;margin:0;}
        input[type="number"]{-moz-appearance:textfield;}
        input[type="checkbox"],input[type="radio"]{-webkit-appearance:none;-moz-appearance:none;appearance:none;width:16px;height:16px;border:1.5px solid var(--border-base);background:transparent;cursor:pointer;vertical-align:middle;position:relative;transition:all 0.15s ease;flex-shrink:0;}
        input[type="checkbox"]{border-radius:3px;}
        input[type="radio"]{border-radius:50%;}
        input[type="checkbox"]:hover:not(:checked),input[type="radio"]:hover:not(:checked){border-color:var(--primary-light);}
        input[type="checkbox"]:checked{background:var(--primary);border-color:var(--primary);}
        input[type="checkbox"]:checked::after{content:'';position:absolute;left:4.5px;top:1.5px;width:5px;height:9px;border:solid #fff;border-width:0 2px 2px 0;transform:rotate(45deg);}
        input[type="radio"]:checked{border-color:var(--primary);background:transparent;}
        input[type="radio"]:checked::after{content:'';position:absolute;left:50%;top:50%;transform:translate(-50%,-50%);width:6px;height:6px;border-radius:50%;background:var(--primary);}
        label:has(input[type="checkbox"]),label:has(input[type="radio"]){display:inline-flex;align-items:center;gap:6px;cursor:pointer;font-size:13px;}
        .radio-group{display:flex;align-items:center;gap:16px;}
        .checkbox-group{display:flex;align-items:center;gap:16px;}

        .toast{border-radius:10px;padding:12px 20px;}
        .toast svg{width:18px;height:18px;fill:currentColor;flex-shrink:0;margin-right:8px;}

        @keyframes step-pulse{0%,100%{box-shadow:0 0 0 0 rgba(64,158,255,0.4);}50%{box-shadow:0 0 0 8px rgba(64,158,255,0);}}

        @keyframes modalOverlayIn{from{opacity:0;}to{opacity:1;}}
        @keyframes modalContentIn{from{opacity:0;transform:scale(0.95) translateY(10px);}to{opacity:1;transform:scale(1) translateY(0);}}

        .form-page-step-nav .step-progress{padding:0 60px;}

        .readonly-field{background:var(--border-extra-light,#f0f2f5)!important;color:var(--text-secondary,#909399)!important;cursor:not-allowed!important;pointer-events:none!important;}

        .form-page-footer{border-top:1px solid var(--border-lighter,#e4e7ed);}
        .form-page-footer .btn-primary{min-width:100px;}
    </style>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="breadcrumb">
        <a class="back-link" href="javascript:void(0)" onclick="goBackToList()">
            <svg viewBox="0 0 24 24"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>
            返回列表
        </a>
        <a href="javascript:void(0)" onclick="goBackToList()">API配置</a>
        <span class="sep">/</span>
        <span class="current" id="breadcrumbTitle">新建API</span>
    </div>

    <div class="form-page-step-nav">
        <div class="step-progress" id="stepNav">
            <div class="step-item active" onclick="goToStep(0)" data-step="0">
                <div class="step-node">1</div>
                <div class="step-label">基本信息</div>
            </div>
            <div class="step-connector" data-connector="0"></div>
            <div class="step-item" onclick="goToStep(1)" data-step="1">
                <div class="step-node">2</div>
                <div class="step-label">接口与规则</div>
            </div>
            <div class="step-connector" data-connector="1"></div>
            <div class="step-item" onclick="goToStep(2)" data-step="2">
                <div class="step-node">3</div>
                <div class="step-label">回执配置</div>
            </div>
        </div>
    </div>

    <div id="step-0" class="step-panel active">
        <div class="config-card">
            <div class="form-section-title">基本信息</div>
            <div class="form-row">
                <div class="form-group" style="flex:2;"><label>所属分组</label><select id="formGroupNo" class="form-control"><option value="">请选择分组</option></select></div>
                <div class="form-group" style="flex:1;align-self:flex-end;"><label>状态</label><div class="radio-group" style="display:flex;align-items:center;height:100%;"><label><input type="radio" name="formStatus" value="ENABLED" checked> 启用</label><label><input type="radio" name="formStatus" value="DISABLED"> 禁用</label></div></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label>API编码 <span class="required">*</span></label><input type="text" id="formApiCode" class="form-control" placeholder="如：AMAZON_ORDER_QUERY"></div>
                <div class="form-group"><label>API名称 <span class="required">*</span></label><input type="text" id="formApiName" class="form-control" placeholder="如：亚马逊订单查询"></div>
            </div>
            <div class="form-group"><label>API描述</label><textarea id="formApiDescription" class="form-control" placeholder="请输入API描述"></textarea></div>
        </div>
        <div class="config-card">
            <div class="form-section-title">执行配置</div>
            <div class="form-row">
                <div class="form-group"><label>最大重试次数</label><input type="number" id="formAutoRetryCount" class="form-control" value="64"></div>
                <div class="form-group"><label>重试间隔(ms)</label><input type="number" id="formRetryIntervalMs" class="form-control" value="5000"></div>
                <div class="form-group"><label>最大队列大小</label><input type="number" id="formMaxQueueSize" class="form-control" value="100000"></div>
            </div>
        </div>
    </div>

    <div id="step-1" class="step-panel">
        <div class="config-card">
            <div class="form-section-title">外部接口请求配置</div>
            <p style="color:var(--text-secondary);margin-bottom:12px;font-size:13px;">配置业务执行插件发起的真实HTTP请求。支持模板变量：${r"${params.xxx}"}、${r"${customData.xxx}"}、${r"${taskNo}"}、${r"${apiCode}"}</p>
            <div class="form-group"><label>请求地址</label><input type="text" id="formTargetUrl" class="form-control" placeholder="https://api.example.com/orders/${r"${params.orderId}"}"></div>
            <div class="form-row">
                <div class="form-group"><label>请求方法</label>
                    <div class="method-segmented" id="methodBtnGroup">
                        <button type="button" class="method-seg-btn active" data-method="POST" onclick="selectMethod('POST')">POST</button>
                        <button type="button" class="method-seg-btn" data-method="GET" onclick="selectMethod('GET')">GET</button>
                        <button type="button" class="method-seg-btn" data-method="PUT" onclick="selectMethod('PUT')">PUT</button>
                        <button type="button" class="method-seg-btn" data-method="DELETE" onclick="selectMethod('DELETE')">DELETE</button>
                        <button type="button" class="method-seg-btn" data-method="PATCH" onclick="selectMethod('PATCH')">PATCH</button>
                    </div>
                    <select id="formTargetMethod" class="form-control" style="display:none;">
                        <option value="POST">POST</option>
                        <option value="GET">GET</option>
                        <option value="PUT">PUT</option>
                        <option value="DELETE">DELETE</option>
                        <option value="PATCH">PATCH</option>
                    </select>
                </div>
                <div class="form-group"><label>请求超时(ms)</label><input type="number" id="formRequestTimeoutMs" class="form-control" value="30000"></div>
            </div>
            <div class="form-section-title" style="margin-top:16px;">请求头</div>
            <table class="edit-table" id="headerTable"><thead><tr><th>Key</th><th>Value</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
            <div class="add-zone" onclick="addHeaderRow()"><span class="add-icon">+</span> 添加请求头</div>
            <div class="form-section-title" style="margin-top:16px;">请求体模板</div>
            <div class="form-group"><label>Body模板 (JSON)</label><textarea id="formTargetBodyTemplate" class="form-control" style="min-height:120px;font-family:monospace;" placeholder='{"orderId":"${r"${params.orderId}"}","taskNo":"${r"${taskNo}"}"}'></textarea></div>
        </div>

        <div class="config-card">
            <div class="form-section-title">限流配置</div>
            <div class="form-group"><label><input type="checkbox" id="formRateLimitEnabled" checked> 启用限流</label></div>
            <table class="edit-table" id="rateLimitTable"><thead><tr><th>名称</th><th class="col-md">类型</th><th class="col-md">维度</th><th class="col-sm">阈值</th><th class="col-sm">窗口(秒)</th><th>Key模板</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
            <div class="add-zone" onclick="addRateLimitRule()"><span class="add-icon">+</span> 添加限流规则</div>
        </div>

        <div class="config-card">
            <div class="form-section-title">过滤规则</div>
            <p style="color:var(--text-secondary);margin-bottom:12px;font-size:13px;">支持按字段条件过滤请求参数</p>
            <table class="edit-table" id="filterTable"><thead><tr><th>字段</th><th class="col-md">操作符</th><th>值</th><th>提示信息</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
            <div class="add-zone" onclick="addFilterRule()"><span class="add-icon">+</span> 添加过滤规则</div>
        </div>

        <div class="config-card">
            <div class="form-section-title">插件配置</div>
            <p style="color:var(--text-secondary);margin-bottom:12px;font-size:13px;">配置API执行的责任链插件</p>
            <div class="form-group"><label><input type="checkbox" id="formPluginEnabled" checked> 启用插件链</label></div>
            <table class="edit-table" id="pluginTable"><thead><tr><th>插件编码</th><th class="col-check">启用</th><th class="col-sm">顺序</th><th>配置(JSON)</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
            <div class="add-zone" onclick="addPluginConfig()"><span class="add-icon">+</span> 添加插件</div>
        </div>
    </div>

    <div id="step-2" class="step-panel">
        <div class="config-card">
            <div class="form-section-title">回执配置</div>
            <div class="form-group"><label>回执类型</label><div class="radio-group"><label><input type="radio" name="receiptType" value="NONE" checked> 无回执</label><label><input type="radio" name="receiptType" value="HTTP"> HTTP回执</label><label><input type="radio" name="receiptType" value="MQ"> MQ回执</label></div></div>

            <div id="receiptHttpConfig" style="display:none;">
                <div class="form-row" style="margin-top:16px;"><div class="form-group"><label>URL</label><input type="text" id="receiptHttpUrl" class="form-control" placeholder="https://callback.example.com/api"></div><div class="form-group"><label>方法</label><select id="receiptHttpMethod" class="form-control"><option>POST</option><option>GET</option><option>PUT</option></select></div></div>
                <div class="form-row"><div class="form-group"><label>超时(ms)</label><input type="number" id="receiptHttpTimeout" class="form-control" value="5000"></div><div class="form-group"><label>重试次数</label><input type="number" id="receiptHttpRetries" class="form-control" value="64"></div></div>
                <div class="form-section-title" style="margin-top:12px;">请求头</div>
                <table class="edit-table" id="receiptHttpHeaderTable"><thead><tr><th>Key</th><th>Value</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
                <div class="add-zone" onclick="addReceiptHttpHeaderRow()"><span class="add-icon">+</span> 添加回执请求头</div>
                <div class="form-group" style="margin-top:12px;"><label>消息体模板</label><textarea id="receiptHttpBody" class="form-control" style="min-height:100px;font-family:monospace;" placeholder='{"taskNo":"${r"${taskNo}"}","status":"${r"${status}"}"}'></textarea></div>
            </div>

            <div id="receiptMqConfig" style="display:none;">
                <div class="form-group" style="margin-top:16px;"><label>Topic</label><input type="text" id="receiptMqTopic" class="form-control" placeholder="api-callback-topic"></div>
                <div class="form-section-title" style="margin-top:12px;">消息头</div>
                <table class="edit-table" id="receiptMqHeaderTable"><thead><tr><th>Key</th><th>Value</th><th class="col-action"></th></tr></thead><tbody></tbody></table>
                <div class="add-zone" onclick="addReceiptMqHeaderRow()"><span class="add-icon">+</span> 添加消息头</div>
                <div class="form-group" style="margin-top:12px;"><label>消息体模板</label><textarea id="receiptMqBody" class="form-control" style="min-height:100px;font-family:monospace;" placeholder='{"taskNo":"${r"${taskNo}"}","status":"${r"${status}"}"}'></textarea></div>
            </div>
        </div>
    </div>

    <div class="form-page-footer">
        <div class="footer-left">
            <button class="btn" id="btnPrev" onclick="prevStep()" style="display:none;"><svg style="width:14px;height:14px;fill:currentColor;" viewBox="0 0 24 24"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>上一步</button>
        </div>
        <div class="footer-right">
            <button class="btn" onclick="cancelForm()">取消</button>
            <button class="btn btn-primary" id="btnNext" onclick="nextStep()">下一步 <svg style="width:14px;height:14px;fill:currentColor;" viewBox="0 0 24 24"><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"/></svg></button>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-config';}
var BASE = contextPath + '/api/v1/config';
var editingApiCode = null;
var currentStep = 0;
var totalSteps = 3;
var isEditMode = '${apiCode}' !== '';
var formDirty = false;

function goToStep(step) {
    if (step < 0 || step >= totalSteps) return;
    currentStep = step;
    document.querySelectorAll('.step-panel').forEach(function(p) {
        p.classList.remove('active');
    });
    document.getElementById('step-' + step).classList.add('active');
    document.querySelectorAll('.step-item').forEach(function(item, idx) {
        item.classList.remove('active', 'completed', 'disabled');
        var node = item.querySelector('.step-node');
        if (idx < step) {
            item.classList.add('completed');
            node.innerHTML = '<svg viewBox="0 0 24 24"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>';
        } else if (idx === step) {
            item.classList.add('active');
            node.textContent = idx + 1;
        } else {
            item.classList.add('disabled');
            node.textContent = idx + 1;
        }
    });
    document.querySelectorAll('.step-connector').forEach(function(conn, idx) {
        if (idx < step) {
            conn.classList.add('completed');
        } else {
            conn.classList.remove('completed');
        }
    });
    document.getElementById('btnPrev').style.display = step === 0 ? 'none' : '';
    var btnNext = document.getElementById('btnNext');
    if (step === totalSteps - 1) {
        btnNext.innerHTML = '提交 <svg style="width:14px;height:14px;fill:currentColor;" viewBox="0 0 24 24"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>';
        btnNext.onclick = saveConfig;
    } else {
        btnNext.innerHTML = '下一步 <svg style="width:14px;height:14px;fill:currentColor;" viewBox="0 0 24 24"><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"/></svg>';
        btnNext.onclick = nextStep;
    }
    window.scrollTo(0, 0);
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

function selectMethod(method) {
    document.querySelectorAll('#methodBtnGroup .method-seg-btn').forEach(function(btn) {
        btn.classList.toggle('active', btn.getAttribute('data-method') === method);
    });
    document.getElementById('formTargetMethod').value = method;
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g,'&amp;').replace(/"/g,'&quot;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function tryParseJson(str) {
    if (!str || !str.trim()) return null;
    try { return JSON.parse(str); } catch(e) { return str; }
}

function addHeaderRow(key, value) {
    var tbody = document.querySelector('#headerTable tbody');
    var k = key || '';
    var v = value || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control header-key" value="'+escapeHtml(k)+'" placeholder="Key"></td><td><input type="text" class="form-control header-value" value="'+escapeHtml(v)+'" placeholder="Value"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
    tr.querySelector('.header-key').focus();
}

function addRateLimitRule(name, limit, windowSeconds, type, dimension, keyTemplate) {
    var tbody = document.querySelector('#rateLimitTable tbody');
    var n = name || '';
    var lm = limit || '';
    var ws = windowSeconds || '';
    var tp = type || 'QPS';
    var dm = dimension || 'API';
    var kt = keyTemplate || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control rl-name" value="'+escapeHtml(n)+'" placeholder="名称"></td><td><select class="form-control rl-type"><option value="QPS"'+(tp==='QPS'?' selected':'')+'>QPS</option><option value="CONCURRENCY"'+(tp==='CONCURRENCY'?' selected':'')+'>并发数</option></select></td><td><select class="form-control rl-dimension"><option value="API"'+(dm==='API'?' selected':'')+'>API级</option><option value="ACCOUNT"'+(dm==='ACCOUNT'?' selected':'')+'>账号维度</option><option value="CUSTOM"'+(dm==='CUSTOM'?' selected':'')+'>自定义</option></select></td><td><input type="number" class="form-control rl-limit" value="'+escapeHtml(String(lm))+'" placeholder="阈值"></td><td><input type="number" class="form-control rl-window" value="'+escapeHtml(String(ws))+'" placeholder="秒"></td><td><input type="text" class="form-control rl-key" value="'+escapeHtml(kt)+'" placeholder="Key模板"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
}

function addFilterRule(field, operator, value, message) {
    var tbody = document.querySelector('#filterTable tbody');
    var f = field || '';
    var op = operator || 'EQ';
    var v = value || '';
    var msg = message || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control fr-field" value="'+escapeHtml(f)+'" placeholder="字段"></td><td><select class="form-control fr-op"><option value="EQ"'+(op==='EQ'?' selected':'')+'>等于</option><option value="NE"'+(op==='NE'?' selected':'')+'>不等于</option><option value="GT"'+(op==='GT'?' selected':'')+'>大于</option><option value="GTE"'+(op==='GTE'?' selected':'')+'>大于等于</option><option value="LT"'+(op==='LT'?' selected':'')+'>小于</option><option value="LTE"'+(op==='LTE'?' selected':'')+'>小于等于</option><option value="IN"'+(op==='IN'?' selected':'')+'>包含于</option><option value="NOT_IN"'+(op==='NOT_IN'?' selected':'')+'>不包含于</option><option value="MATCHES"'+(op==='MATCHES'?' selected':'')+'>正则匹配</option><option value="NOT_MATCHES"'+(op==='NOT_MATCHES'?' selected':'')+'>不匹配</option><option value="NOT_EMPTY"'+(op==='NOT_EMPTY'?' selected':'')+'>非空</option><option value="EMPTY"'+(op==='EMPTY'?' selected':'')+'>为空</option></select></td><td><input type="text" class="form-control fr-value" value="'+escapeHtml(v)+'" placeholder="值"></td><td><input type="text" class="form-control fr-message" value="'+escapeHtml(msg)+'" placeholder="提示信息"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
}

function addPluginConfig(pluginCode, enabled, order, config) {
    var tbody = document.querySelector('#pluginTable tbody');
    var pc = pluginCode || '';
    var en = enabled !== false;
    var o = order || 0;
    var c = config || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control pc-code" value="'+escapeHtml(pc)+'" placeholder="插件编码"></td><td class="col-check"><label><input type="checkbox" class="pc-enabled"'+(en?' checked':'')+'> 启用</label></td><td><input type="number" class="form-control pc-order" value="'+o+'" placeholder="顺序"></td><td><input type="text" class="form-control pc-config" value="'+escapeHtml(c)+'" placeholder="配置(JSON)"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
}

function addReceiptHttpHeaderRow(key, value) {
    var tbody = document.querySelector('#receiptHttpHeaderTable tbody');
    var k = key || '';
    var v = value || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control rh-key" value="'+escapeHtml(k)+'" placeholder="Key"></td><td><input type="text" class="form-control rh-value" value="'+escapeHtml(v)+'" placeholder="Value"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
}

function addReceiptMqHeaderRow(key, value) {
    var tbody = document.querySelector('#receiptMqHeaderTable tbody');
    var k = key || '';
    var v = value || '';
    var tr = document.createElement('tr');
    tr.innerHTML = '<td><input type="text" class="form-control rmh-key" value="'+escapeHtml(k)+'" placeholder="Key"></td><td><input type="text" class="form-control rmh-value" value="'+escapeHtml(v)+'" placeholder="Value"></td><td class="col-action"><button class="btn-del-circle" onclick="this.closest(\'tr\').remove()" title="删除">×</button></td>';
    tbody.appendChild(tr);
}

function collectKvRows(tableId, keyClass, valueClass) {
    var result = {};
    document.querySelectorAll('#'+tableId+' tbody tr').forEach(function(tr) {
        var key = tr.querySelector('.'+keyClass);
        var val = tr.querySelector('.'+valueClass);
        if (key && val && key.value.trim()) {
            result[key.value.trim()] = val.value;
        }
    });
    return Object.keys(result).length > 0 ? result : null;
}

function collectHeaders() { return collectKvRows('headerTable','header-key','header-value'); }

function collectRateLimitConfig() {
    var enabled = document.getElementById('formRateLimitEnabled').checked;
    var rules = [];
    document.querySelectorAll('#rateLimitTable tbody tr').forEach(function(tr) {
        var name = tr.querySelector('.rl-name');
        var type = tr.querySelector('.rl-type');
        var dimension = tr.querySelector('.rl-dimension');
        var limit = tr.querySelector('.rl-limit');
        var window = tr.querySelector('.rl-window');
        var key = tr.querySelector('.rl-key');
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
    document.querySelectorAll('#filterTable tbody tr').forEach(function(tr) {
        var field = tr.querySelector('.fr-field');
        var op = tr.querySelector('.fr-op');
        var val = tr.querySelector('.fr-value');
        var msg = tr.querySelector('.fr-message');
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
    document.querySelectorAll('#pluginTable tbody tr').forEach(function(tr) {
        var code = tr.querySelector('.pc-code');
        var en = tr.querySelector('.pc-enabled');
        var order = tr.querySelector('.pc-order');
        var config = tr.querySelector('.pc-config');
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
    var receiptType = document.querySelector('input[name="receiptType"]:checked').value;
    var types = [receiptType];
    var isHttp = receiptType === 'HTTP';
    var isMq = receiptType === 'MQ';
    var result = { receiptTypes: types };
    if (isHttp) {
        var httpHeaders = collectKvRows('receiptHttpHeaderTable','rh-key','rh-value');
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
        var mqHeaders = collectKvRows('receiptMqHeaderTable','rmh-key','rmh-value');
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
    selectMethod('POST');
    document.getElementById('formTargetBodyTemplate').value = '';
    document.querySelector('#headerTable tbody').innerHTML = '';

    document.getElementById('formRateLimitEnabled').checked = true;
    document.querySelector('#rateLimitTable tbody').innerHTML = '';

    document.querySelector('#filterTable tbody').innerHTML = '';

    document.getElementById('formPluginEnabled').checked = true;
    document.querySelector('#pluginTable tbody').innerHTML = '';

    var noneRadio = document.querySelector('input[name="receiptType"][value="NONE"]');
    if (noneRadio) noneRadio.checked = true;
    document.getElementById('receiptHttpConfig').style.display = 'none';
    document.getElementById('receiptMqConfig').style.display = 'none';
    document.getElementById('receiptHttpUrl').value = '';
    document.getElementById('receiptHttpMethod').value = 'POST';
    document.getElementById('receiptHttpTimeout').value = '5000';
    document.getElementById('receiptHttpRetries').value = '64';
    document.querySelector('#receiptHttpHeaderTable tbody').innerHTML = '';
    document.getElementById('receiptHttpBody').value = '';
    document.getElementById('receiptMqTopic').value = '';
    document.querySelector('#receiptMqHeaderTable tbody').innerHTML = '';
    document.getElementById('receiptMqBody').value = '';
}

function loadGroups() {
    fetch(contextPath+'/api/v1/group/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) {
            var formOpts = '<option value="">请选择分组</option>';
            (res.data||[]).forEach(function(g){ formOpts+='<option value="'+g.groupNo+'">'+g.groupName+'('+g.groupCode+')</option>'; });
            document.getElementById('formGroupNo').innerHTML = formOpts;
            if (isEditMode) {
                loadEditData();
            }
        }
    }).catch(function(e){
        console.error('Failed to load groups:', e);
    });
}

function loadEditData() {
    var apiCode = '${apiCode}';
    fetch(BASE+'/'+apiCode).then(function(r){return r.json();}).then(function(res){
        if(res.success && res.data) {
            editingApiCode = apiCode;
            var c = res.data;
            document.getElementById('formApiCode').value = c.apiCode;
            document.getElementById('formApiCode').readOnly = true;
            document.getElementById('formApiCode').classList.add('readonly-field');
            document.getElementById('formApiName').value = c.apiName;
            document.getElementById('formApiDescription').value = c.apiDescription||'';
            document.getElementById('formAutoRetryCount').value = c.autoRetryCount||64;
            document.getElementById('formRetryIntervalMs').value = c.retryIntervalMs||5000;
            document.getElementById('formMaxQueueSize').value = c.maxQueueSize||100000;
            document.getElementById('formRequestTimeoutMs').value = c.requestTimeoutMs||30000;
            var radio = document.querySelector('input[name=formStatus][value='+(c.status||'ENABLED')+']');
            if(radio) radio.checked = true;
            // 确保分组下拉框已经加载完成后再设置值
            var groupSelect = document.getElementById('formGroupNo');
            var setGroupValue = function() {
                if (groupSelect.options.length > 1 || c.groupNo) {
                    groupSelect.value = c.groupNo||'';
                } else {
                    // 如果选项还没加载，延迟重试
                    setTimeout(setGroupValue, 100);
                }
            };
            setGroupValue();

            var ec = c.extraConfig || {};
            document.getElementById('formTargetUrl').value = ec.targetUrl || '';
            document.getElementById('formTargetMethod').value = ec.targetMethod || 'POST';
            selectMethod(ec.targetMethod || 'POST');
            document.getElementById('formTargetBodyTemplate').value = ec.targetBodyTemplate || '';
            document.querySelector('#headerTable tbody').innerHTML = '';
            if (ec.targetHeaders) {
                Object.keys(ec.targetHeaders).forEach(function(k) { addHeaderRow(k, ec.targetHeaders[k]); });
            }

            var rl = c.rateLimitConfig || {};
            document.getElementById('formRateLimitEnabled').checked = rl.enabled !== false;
            document.querySelector('#rateLimitTable tbody').innerHTML = '';
            if (rl.rules && rl.rules.length) {
                rl.rules.forEach(function(r) { addRateLimitRule(r.name, r.limit, r.windowSeconds, r.type, r.dimension, r.keyTemplate); });
            }

            var fr = c.filterRules || {};
            document.querySelector('#filterTable tbody').innerHTML = '';
            if (fr.rules && fr.rules.length) {
                fr.rules.forEach(function(r) { addFilterRule(r.field, r.operator, r.value, r.message); });
            }

            var pc = c.pluginConfig || {};
            document.getElementById('formPluginEnabled').checked = pc.enabled !== false;
            document.querySelector('#pluginTable tbody').innerHTML = '';
            if (pc.pluginChain && pc.pluginChain.length) {
                pc.pluginChain.forEach(function(p) {
                    addPluginConfig(p.pluginCode, p.enabled, p.order, typeof p.config === 'string' ? p.config : JSON.stringify(p.config || ''));
                });
            }

            var rc = c.receiptConfig || {};
            var rTypes = rc.receiptTypes || [];
            var receiptTypeVal = 'NONE';
            if (rTypes.indexOf('HTTP') >= 0) receiptTypeVal = 'HTTP';
            if (rTypes.indexOf('MQ') >= 0) receiptTypeVal = 'MQ';
            var receiptRadio = document.querySelector('input[name="receiptType"][value="' + receiptTypeVal + '"]');
            if (receiptRadio) receiptRadio.checked = true;
            document.getElementById('receiptHttpConfig').style.display = receiptTypeVal === 'HTTP' ? 'block' : 'none';
            document.getElementById('receiptMqConfig').style.display = receiptTypeVal === 'MQ' ? 'block' : 'none';
            if (receiptTypeVal === 'HTTP' && rc.http) {
                document.getElementById('receiptHttpUrl').value = rc.http.url || '';
                document.getElementById('receiptHttpMethod').value = rc.http.method || 'POST';
                document.getElementById('receiptHttpTimeout').value = rc.http.timeoutMs || 5000;
                document.getElementById('receiptHttpRetries').value = (rc.http.retryPolicy && rc.http.retryPolicy.maxRetries) || 64;
                document.querySelector('#receiptHttpHeaderTable tbody').innerHTML = '';
                if (rc.http.headers) { Object.keys(rc.http.headers).forEach(function(k) { addReceiptHttpHeaderRow(k, rc.http.headers[k]); }); }
                document.getElementById('receiptHttpBody').value = typeof rc.http.bodyTemplate === 'string' ? rc.http.bodyTemplate : JSON.stringify(rc.http.bodyTemplate || '');
            }
            if (receiptTypeVal === 'MQ' && rc.mq) {
                document.getElementById('receiptMqTopic').value = rc.mq.topic || '';
                document.querySelector('#receiptMqHeaderTable tbody').innerHTML = '';
                if (rc.mq.headers) { Object.keys(rc.mq.headers).forEach(function(k) { addReceiptMqHeaderRow(k, rc.mq.headers[k]); }); }
                document.getElementById('receiptMqBody').value = typeof rc.mq.bodyTemplate === 'string' ? rc.mq.bodyTemplate : JSON.stringify(rc.mq.bodyTemplate || '');
            }

            goToStep(0);
        }
    });
}

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
        envConfig: null
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
        if(res.success){
            showToast(editingApiCode?'保存成功':'创建成功','success');
            formDirty = false;
            var from = new URLSearchParams(window.location.search).get('from');
            if (from === 'list') {
                window.location.href = contextPath + '/api-config';
            } else {
                window.location.href = contextPath + '/api-config/' + apiCode;
            }
        }
        else showToast(res.error&&res.error.message||'操作失败','error');
    });
}

function cancelForm() {
    if (formDirty) {
        showConfirm('您有未保存的修改，确定要离开吗？', function() {
            window.location.href = contextPath + '/api-config';
        }, {title:'确认离开', iconType:'info'});
    } else {
        window.location.href = contextPath + '/api-config';
    }
}

function goBackToList() {
    if (formDirty) {
        showConfirm('您有未保存的修改，确定要离开吗？', function() {
            window.location.href = contextPath + '/api-config';
        }, {title:'确认离开', iconType:'info'});
    } else {
        window.location.href = contextPath + '/api-config';
    }
}

function showConfirm(msg, onConfirm, options) {
    var opts = options || {};
    var title = opts.title || '确认操作';
    var iconType = opts.iconType || 'warning';
    var iconClass = iconType === 'danger' ? 'confirm-icon confirm-icon-danger' : (iconType === 'info' ? 'confirm-icon confirm-icon-info' : 'confirm-icon');
    var iconSvg = iconType === 'danger' ? '<svg viewBox="0 0 24 24"><path d="M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"/></svg>' : (iconType === 'info' ? '<svg viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 17h-2v-2h2v2zm2.07-7.75l-.9.92C13.45 12.9 13 13.5 13 15h-2v-.5c0-1.1.45-2.1 1.17-2.83l1.24-1.26c.37-.36.59-.86.59-1.41 0-1.1-.9-2-2-2s-2 .9-2 2H8c0-2.21 1.79-4 4-4s4 1.79 4 4c0 .88-.36 1.68-.93 2.25z"/></svg>' : '<svg viewBox="0 0 24 24"><path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/></svg>');
    var overlay = document.createElement('div');
    overlay.className = 'confirm-overlay';
    overlay.innerHTML = '<div class="confirm-box"><div class="'+iconClass+'">'+iconSvg+'</div><h4>'+title+'</h4><p>'+msg+'</p><div class="confirm-actions"><button class="btn" onclick="this.closest(\'.confirm-overlay\').remove()">取消</button><button class="btn '+(iconType==='danger'?'btn-danger':'btn-primary')+'" id="confirmOk">确定</button></div></div>';
    document.body.appendChild(overlay);
    document.getElementById('confirmOk').onclick = function() { overlay.remove(); onConfirm(); };
    overlay.addEventListener('click', function(e) { if(e.target===overlay) overlay.remove(); });
}

document.querySelectorAll('input[name="receiptType"]').forEach(function(radio) {
    radio.addEventListener('change', function() {
        var val = this.value;
        document.getElementById('receiptHttpConfig').style.display = val === 'HTTP' ? 'block' : 'none';
        document.getElementById('receiptMqConfig').style.display = val === 'MQ' ? 'block' : 'none';
    });
});

if (isEditMode) {
    document.getElementById('pageTitle').textContent = '编辑API';
    document.getElementById('breadcrumbTitle').textContent = '编辑API';
} else {
    document.getElementById('pageTitle').textContent = '新建API';
    document.getElementById('breadcrumbTitle').textContent = '新建API';
    resetAllForms();
}

// 先初始化第一步，确保DOM元素存在
goToStep(0);
// 然后加载分组数据
loadGroups();

document.querySelectorAll('.form-control, input[type="checkbox"], input[type="radio"]').forEach(function(el) {
    el.addEventListener('change', function() { formDirty = true; });
    el.addEventListener('input', function() { formDirty = true; });
});
</script>
</body>
</html>
