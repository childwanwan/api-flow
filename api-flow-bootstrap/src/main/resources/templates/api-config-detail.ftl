<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API详情</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <style>
        .detail-card .detail-kv .kv-item:nth-last-child(-n+2){border-bottom:none;}
        .detail-card .detail-kv.full-width .kv-item{border-bottom:1px solid var(--border-extra-light);}
        .detail-card .detail-kv.full-width .kv-item:last-child{border-bottom:none;}
        .confirm-overlay{position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.4);display:flex;align-items:center;justify-content:center;z-index:10000;animation:modalOverlayIn 0.2s ease-out;}
        .confirm-box{background:#fff;border-radius:var(--radius-lg);padding:28px;max-width:400px;width:90%;box-shadow:0 12px 40px rgba(0,0,0,0.18);animation:modalContentIn 0.25s ease-out;}
        .confirm-box .confirm-icon{width:48px;height:48px;border-radius:50%;background:var(--warning-bg);display:flex;align-items:center;justify-content:center;margin-bottom:16px;}
        .confirm-box .confirm-icon svg{width:24px;height:24px;fill:var(--warning);}
        .confirm-box h4{margin:0 0 8px;font-size:16px;font-weight:600;color:var(--text-primary);}
        .confirm-box p{margin:0 0 24px;font-size:14px;color:var(--text-secondary);line-height:1.6;}
        .confirm-box .confirm-actions{display:flex;justify-content:flex-end;gap:10px;}
        .toast svg{width:18px;height:18px;fill:currentColor;flex-shrink:0;}
        @keyframes modalOverlayIn{from{opacity:0;}to{opacity:1;}}
        @keyframes modalContentIn{from{opacity:0;transform:scale(0.95) translateY(10px);}to{opacity:1;transform:scale(1) translateY(0);}}
    </style>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="breadcrumb">
        <a class="back-link" href="${request.contextPath}/api-config">
            <svg viewBox="0 0 24 24"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>
            返回列表
        </a>
        <a href="${request.contextPath}/api-config">API配置</a>
        <span class="sep">/</span>
        <span class="current">API详情</span>
    </div>
    <div id="overviewArea"><div class="skeleton-card"><div class="skeleton skeleton-title"></div><div class="skeleton skeleton-text" style="width:60%;"></div><div class="skeleton skeleton-text" style="width:40%;"></div></div></div>
    <div id="detailArea"><div class="skeleton-card"><div class="skeleton skeleton-title"></div><div class="skeleton skeleton-text"></div><div class="skeleton skeleton-text" style="width:80%;"></div><div class="skeleton skeleton-text" style="width:50%;"></div></div><div class="skeleton-card"><div class="skeleton skeleton-title"></div><div class="skeleton skeleton-text"></div><div class="skeleton skeleton-text" style="width:70%;"></div></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-config';}
var BASE = contextPath + '/api/v1/config';
var apiCode = '${apiCode}';
var groupsMap = {};

function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }
function statusTag(s) { var m={ENABLED:'enabled',DISABLED:'disabled'}; var t={ENABLED:'启用',DISABLED:'禁用'}; return '<span class="status-tag status-'+(m[s]||'disabled')+'"><span class="status-dot"></span>'+(t[s]||s)+'</span>'; }
function escapeHtml(str) { if (!str) return ''; return String(str).replace(/&/g,'&amp;').replace(/"/g,'&quot;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }

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

function kvItem(label, value) {
    return '<div class="kv-item"><span class="kv-label">'+label+'</span><span class="kv-value">'+value+'</span></div>';
}

function renderOverview(data) {
    var groupDisplay = data.groupName && data.groupCode ? data.groupName+'('+data.groupCode+')' : '-';
    var toggleLabel = data.status === 'ENABLED' ? '禁用' : '启用';
    document.getElementById('overviewArea').innerHTML =
        '<div class="overview-card">' +
            '<div class="overview-header">' +
                '<div class="overview-title">' +
                    '<div class="overview-code">'+escapeHtml(data.apiCode)+'</div>' +
                    '<div class="overview-sub">' +
                        '<span>'+escapeHtml(data.apiName||'')+'</span>' +
                        '<span>'+escapeHtml(groupDisplay)+'</span>' +
                    '</div>' +
                '</div>' +
                '<div class="overview-actions">' +
                    '<button class="btn btn-primary" onclick="goEdit()">编辑</button>' +
                    '<button class="btn" onclick="toggleStatus()">'+toggleLabel+'</button>' +
                    '<button class="btn-text btn-danger-text" onclick="deleteConfig()">删除</button>' +
                '</div>' +
            '</div>' +
        '</div>';
}

function renderDetail(data) {
    var ec = data.extraConfig || {};
    var rl = data.rateLimitConfig || {};
    var fr = data.filterRules || {};
    var pc = data.pluginConfig || {};
    var rc = data.receiptConfig || {};

    var html = '';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">基本信息</div>' +
        '<div class="detail-kv">' +
            kvItem('API编码', escapeHtml(data.apiCode)) +
            kvItem('API名称', escapeHtml(data.apiName||'-')) +
            kvItem('所属分组', escapeHtml(data.groupName && data.groupCode ? data.groupName+'('+data.groupCode+')' : '-')) +
            kvItem('状态', statusTag(data.status||'ENABLED')) +
        '</div>' +
        '<div class="detail-kv full-width" style="margin-top:0;">' +
            kvItem('API描述', escapeHtml(data.apiDescription||'-')) +
        '</div>' +
    '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">执行配置</div>' +
        '<div class="detail-kv">' +
            kvItem('最大重试次数', data.autoRetryCount!=null ? data.autoRetryCount : '-') +
            kvItem('重试间隔(ms)', data.retryIntervalMs!=null ? data.retryIntervalMs : '-') +
            kvItem('最大队列大小', data.maxQueueSize!=null ? data.maxQueueSize : '-') +
            kvItem('请求超时(ms)', data.requestTimeoutMs!=null ? data.requestTimeoutMs : '-') +
        '</div>' +
    '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">外部接口请求配置</div>' +
        '<div class="detail-kv">' +
            kvItem('请求地址', escapeHtml(ec.targetUrl||'-')) +
            kvItem('请求方法', escapeHtml(ec.targetMethod||'-')) +
            kvItem('请求超时(ms)', ec.targetTimeoutMs!=null ? ec.targetTimeoutMs : '-') +
        '</div>';

    if (ec.targetHeaders && Object.keys(ec.targetHeaders).length > 0) {
        html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">请求头</div>' +
            '<table class="detail-table-readonly"><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>';
        Object.keys(ec.targetHeaders).forEach(function(k) {
            html += '<tr><td>'+escapeHtml(k)+'</td><td>'+escapeHtml(ec.targetHeaders[k])+'</td></tr>';
        });
        html += '</tbody></table></div>';
    }

    if (ec.targetBodyTemplate) {
        var bodyStr = typeof ec.targetBodyTemplate === 'string' ? ec.targetBodyTemplate : JSON.stringify(ec.targetBodyTemplate, null, 2);
        html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">请求体模板</div>' +
            '<div class="json-viewer">'+escapeHtml(bodyStr)+'</div></div>';
    }
    html += '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">限流配置</div>' +
        '<div class="detail-kv">' +
            kvItem('启用状态', rl.enabled ? '<span class="status-tag status-enabled"><span class="status-dot"></span>已启用</span>' : '<span class="status-tag status-disabled"><span class="status-dot"></span>未启用</span>') +
            kvItem('规则数量', rl.rules ? rl.rules.length+'条' : '0条') +
        '</div>';
    if (rl.rules && rl.rules.length > 0) {
        html += '<table class="detail-table-readonly" style="margin-top:12px;"><thead><tr><th>名称</th><th>类型</th><th>维度</th><th>阈值</th><th>窗口(秒)</th><th>Key模板</th></tr></thead><tbody>';
        rl.rules.forEach(function(r) {
            html += '<tr>' +
                '<td>'+escapeHtml(r.name||'-')+'</td>' +
                '<td>'+escapeHtml(r.type||'-')+'</td>' +
                '<td>'+escapeHtml(r.dimension||'-')+'</td>' +
                '<td>'+escapeHtml(r.limit!=null?String(r.limit):'-')+'</td>' +
                '<td>'+escapeHtml(r.windowSeconds!=null?String(r.windowSeconds):'-')+'</td>' +
                '<td>'+escapeHtml(r.keyTemplate||'-')+'</td>' +
            '</tr>';
        });
        html += '</tbody></table>';
    }
    html += '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">过滤规则</div>';
    if (fr.rules && fr.rules.length > 0) {
        html += '<table class="detail-table-readonly"><thead><tr><th>字段</th><th>操作符</th><th>值</th><th>提示信息</th></tr></thead><tbody>';
        fr.rules.forEach(function(r) {
            html += '<tr>' +
                '<td>'+escapeHtml(r.field||'-')+'</td>' +
                '<td>'+escapeHtml(r.operator||'-')+'</td>' +
                '<td>'+escapeHtml(r.value||'-')+'</td>' +
                '<td>'+escapeHtml(r.message||'-')+'</td>' +
            '</tr>';
        });
        html += '</tbody></table>';
    } else {
        html += '<div class="empty-state">暂无过滤规则</div>';
    }
    html += '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">插件配置</div>' +
        '<div class="detail-kv">' +
            kvItem('启用状态', pc.enabled ? '<span class="status-tag status-enabled"><span class="status-dot"></span>已启用</span>' : '<span class="status-tag status-disabled"><span class="status-dot"></span>未启用</span>') +
            kvItem('插件数量', pc.pluginChain ? pc.pluginChain.length+'个' : '0个') +
        '</div>';
    if (pc.pluginChain && pc.pluginChain.length > 0) {
        html += '<table class="detail-table-readonly" style="margin-top:12px;"><thead><tr><th>插件编码</th><th>启用</th><th>顺序</th><th>配置</th></tr></thead><tbody>';
        pc.pluginChain.forEach(function(p) {
            var configStr = typeof p.config === 'string' ? p.config : JSON.stringify(p.config||'');
            html += '<tr>' +
                '<td>'+escapeHtml(p.pluginCode||'-')+'</td>' +
                '<td>'+(p.enabled ? '<span class="status-tag status-enabled"><span class="status-dot"></span>启用</span>' : '<span class="status-tag status-disabled"><span class="status-dot"></span>禁用</span>')+'</td>' +
                '<td>'+escapeHtml(p.order!=null?String(p.order):'-')+'</td>' +
                '<td><div class="json-viewer" style="max-height:80px;padding:6px 10px;font-size:12px;">'+escapeHtml(configStr)+'</div></td>' +
            '</tr>';
        });
        html += '</tbody></table>';
    }
    html += '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">回执配置</div>';
    var rTypes = rc.receiptTypes || [];
    if (rTypes.length === 0 || (rTypes.length === 1 && rTypes[0] === 'NONE')) {
        html += '<div class="detail-kv">' +
            kvItem('回执类型', '无回执') +
        '</div>';
    } else if (rTypes.indexOf('HTTP') >= 0 && rc.http) {
        var h = rc.http;
        html += '<div class="detail-kv">' +
            kvItem('回执类型', 'HTTP回执') +
            kvItem('URL', escapeHtml(h.url||'-')) +
            kvItem('方法', escapeHtml(h.method||'-')) +
            kvItem('超时(ms)', h.timeoutMs!=null?h.timeoutMs:'-') +
        '</div>';
        if (h.retryPolicy && h.retryPolicy.maxRetries != null) {
            html += '<div class="detail-kv">' +
                kvItem('重试次数', h.retryPolicy.maxRetries) +
            '</div>';
        }
        if (h.headers && Object.keys(h.headers).length > 0) {
            html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">回执请求头</div>' +
                '<table class="detail-table-readonly"><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>';
            Object.keys(h.headers).forEach(function(k) {
                html += '<tr><td>'+escapeHtml(k)+'</td><td>'+escapeHtml(h.headers[k])+'</td></tr>';
            });
            html += '</tbody></table></div>';
        }
        if (h.bodyTemplate) {
            var btStr = typeof h.bodyTemplate === 'string' ? h.bodyTemplate : JSON.stringify(h.bodyTemplate, null, 2);
            html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">消息体模板</div>' +
                '<div class="json-viewer">'+escapeHtml(btStr)+'</div></div>';
        }
    } else if (rTypes.indexOf('MQ') >= 0 && rc.mq) {
        var mq = rc.mq;
        html += '<div class="detail-kv">' +
            kvItem('回执类型', 'MQ回执') +
            kvItem('Topic', escapeHtml(mq.topic||'-')) +
        '</div>';
        if (mq.headers && Object.keys(mq.headers).length > 0) {
            html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">消息头</div>' +
                '<table class="detail-table-readonly"><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>';
            Object.keys(mq.headers).forEach(function(k) {
                html += '<tr><td>'+escapeHtml(k)+'</td><td>'+escapeHtml(mq.headers[k])+'</td></tr>';
            });
            html += '</tbody></table></div>';
        }
        if (mq.bodyTemplate) {
            var mqBtStr = typeof mq.bodyTemplate === 'string' ? mq.bodyTemplate : JSON.stringify(mq.bodyTemplate, null, 2);
            html += '<div style="margin-top:12px;"><div class="detail-section-title" style="font-size:13px;">消息体模板</div>' +
                '<div class="json-viewer">'+escapeHtml(mqBtStr)+'</div></div>';
        }
    }
    html += '</div>';

    html += '<div class="detail-card">' +
        '<div class="detail-section-title">系统信息</div>' +
        '<div class="detail-kv">' +
            kvItem('创建时间', formatTime(data.createTimeMs)) +
            kvItem('更新时间', formatTime(data.updateTimeMs)) +
        '</div>' +
    '</div>';

    document.getElementById('detailArea').innerHTML = html;
}

function goEdit() {
    window.location.href = contextPath + '/api-config/' + apiCode + '/edit?from=detail';
}

function deleteConfig() {
    showConfirm('确定要删除该API配置吗？此操作不可恢复。', function() {
        fetch(BASE + '/' + apiCode, {method:'DELETE'}).then(function(r){return r.json();}).then(function(res){
            if(res.success){showToast('删除成功','success');window.location.href=contextPath+'/api-config';}
            else showToast(res.error&&res.error.message||'删除失败','error');
        });
    }, {title:'确认删除', iconType:'danger'});
}

function toggleStatus() {
    fetch(BASE+'/'+apiCode).then(function(r){return r.json();}).then(function(res){
        if(res.success && res.data) {
            var currentStatus = res.data.status;
            var newStatus = currentStatus === 'ENABLED' ? 'DISABLED' : 'ENABLED';
            var label = newStatus === 'ENABLED' ? '启用' : '禁用';
            showConfirm('确定要'+label+'该API配置吗？', function() {
                fetch(BASE+'/'+apiCode+'/'+(newStatus==='ENABLED'?'enable':'disable'),{method:'POST'}).then(function(r){return r.json();}).then(function(res2){
                    if(res2.success){showToast('操作成功','success');loadData();}
                    else showToast(res2.error&&res2.error.message||'操作失败','error');
                });
            }, {title:'确认操作', iconType:'info'});
        }
    });
}

function loadData() {
    fetch(BASE+'/'+apiCode).then(function(r){return r.json();}).then(function(res){
        if(res.success && res.data) {
            renderOverview(res.data);
            renderDetail(res.data);
        } else {
            showToast(res.error&&res.error.message||'加载失败','error');
        }
    });
}

loadData();
</script>
</body>
</html>
