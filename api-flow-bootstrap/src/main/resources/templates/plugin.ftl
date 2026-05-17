<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>插件管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>插件管理</h2>
        <div class="page-actions"><button class="btn btn-primary" onclick="showCreateDialog()">+ 加载插件</button></div>
    </div>
    <div class="tabs">
        <div class="tab-item active" onclick="switchTab(this,'loaded-plugins')">已加载插件</div>
        <div class="tab-item" onclick="switchTab(this,'plugin-market')">插件市场</div>
    </div>
    <div id="loaded-plugins">
        <div class="data-card">
            <table class="data-table"><thead><tr><th>插件编码</th><th>插件名称</th><th>版本</th><th>状态</th><th>加载时间</th><th>操作</th></tr></thead>
            <tbody id="pluginTableBody"></tbody></table>
        </div>
    </div>
    <div id="plugin-market" style="display:none;">
        <div style="margin-bottom:12px;"><button class="btn btn-primary" onclick="showToast('上传插件包功能开发中','info')">+ 上传插件包</button></div>
        <div class="data-card">
            <table class="data-table"><thead><tr><th>插件包名称</th><th>版本</th><th>类型</th><th>上传时间</th><th>状态</th><th>操作</th></tr></thead>
            <tbody id="marketTableBody"><tr><td colspan="6" class="empty-row">暂无插件包</td></tr></tbody></table>
        </div>
    </div>
</div>

<div id="pluginModal" class="modal-overlay" style="display:none;">
    <div class="modal">
        <div class="modal-header"><h3>加载插件</h3><button class="modal-close" onclick="hideModal()">&times;</button></div>
        <div class="modal-body">
            <div class="form-group"><label>插件编码 <span class="required">*</span></label><input type="text" id="formPluginCode" class="form-control" placeholder="如：PARAM_VALIDATOR"></div>
            <div class="form-group"><label>插件名称 <span class="required">*</span></label><input type="text" id="formPluginName" class="form-control" placeholder="如：参数校验插件"></div>
            <div class="form-group"><label>插件包</label><input type="file" id="formPluginJar" class="form-control" accept=".jar"></div>
            <div class="form-group"><label>插件类型</label><select id="formPluginType" class="form-control"><option value="PARAM_VALIDATOR">参数校验</option><option value="RATE_LIMIT">限流检查</option><option value="API_ADAPTER">API适配</option><option value="CUSTOM">自定义</option></select></div>
            <div class="form-group"><label>类名 <span class="required">*</span></label><input type="text" id="formPluginClass" class="form-control" placeholder="完整类名"></div>
            <div class="form-group"><label>描述</label><textarea id="formPluginDesc" class="form-control" placeholder="请输入插件描述"></textarea></div>
        </div>
        <div class="modal-footer"><button class="btn" onclick="hideModal()">取消</button><button class="btn btn-primary" onclick="createPlugin()">加载</button></div>
    </div>
</div>

<div id="pluginDrawer" class="drawer-overlay" style="display:none;" onclick="if(event.target===this)closeDrawer()">
    <div class="drawer"><div class="drawer-header"><h3>插件详情</h3><button class="drawer-close" onclick="closeDrawer()">&times;</button></div><div class="drawer-body" id="pluginDetailContent"></div></div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=plugin';}
var BASE = contextPath + '/api/v1/plugin';

function switchTab(el, panelId) {
    document.querySelectorAll('.tab-item').forEach(function(t){t.classList.remove('active');});
    el.classList.add('active');
    document.getElementById('loaded-plugins').style.display=panelId==='loaded-plugins'?'block':'none';
    document.getElementById('plugin-market').style.display=panelId==='plugin-market'?'block':'none';
}
function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }

function loadPlugins() {
    fetch(BASE+'/list').then(function(r){return r.json();}).then(function(res){
        if(res.success) renderPlugins(res.data||[]);
    });
}
function renderPlugins(plugins) {
    var tbody=document.getElementById('pluginTableBody');
    if(!plugins.length){tbody.innerHTML='<tr><td colspan="6" class="empty-row">暂无已加载插件</td></tr>';return;}
    tbody.innerHTML=plugins.map(function(p){
        return '<tr><td>'+p.pluginCode+'</td><td>'+p.pluginName+'</td><td>'+(p.version||'v1.0.0')+'</td><td><span class="status-tag '+(p.enabled?'status-enabled':'status-disabled')+'"><span class="status-dot"></span>'+(p.enabled?'运行':'停用')+'</span></td><td>'+formatTime(p.createTimeMs)+'</td><td class="action-col"><button class="btn-text" onclick="showPluginDetail(\''+p.pluginCode+'\')">配置</button><span class="action-divider">|</span><button class="btn-text btn-danger-text" onclick="uninstallPlugin(\''+p.pluginCode+'\')">卸载</button></td></tr>';
    }).join('');
}

function showCreateDialog() {
    document.getElementById('formPluginCode').value='';
    document.getElementById('formPluginName').value='';
    document.getElementById('formPluginClass').value='';
    document.getElementById('formPluginDesc').value='';
    document.getElementById('pluginModal').style.display='flex';
    setTimeout(function(){ if(window.InputClear) InputClear.init(document.getElementById('pluginModal')); }, 100);
}
function hideModal() { document.getElementById('pluginModal').style.display='none'; }

function createPlugin() {
    var data={
        pluginCode:document.getElementById('formPluginCode').value,
        pluginName:document.getElementById('formPluginName').value,
        pluginClass:document.getElementById('formPluginClass').value,
        description:document.getElementById('formPluginDesc').value,
        orderNum:0,enabled:true
    };
    if(!data.pluginCode||!data.pluginName){showToast('请填写必填项','warning');return;}
    fetch(BASE+'/create',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}).then(function(r){return r.json();}).then(function(res){
        if(res.success){showToast('加载成功','success');hideModal();loadPlugins();}
        else showToast(res.error?.message||'加载失败','error');
    });
}

function uninstallPlugin(code) { if(!confirm('确定要卸载该插件吗？'))return; fetch(BASE+'/'+code,{method:'DELETE'}).then(function(r){return r.json();}).then(function(res){if(res.success){showToast('卸载成功','success');loadPlugins();}else showToast(res.error?.message||'卸载失败','error');}); }

function showPluginDetail(code) {
    fetch(BASE+'/'+code).then(function(r){return r.json();}).then(function(res){
        if(res.success&&res.data) {
            var p=res.data;
            document.getElementById('pluginDetailContent').innerHTML='<div class="detail-section"><div class="detail-section-title">基本信息</div><table class="detail-table">'+
                '<tr><td>插件编码</td><td>'+p.pluginCode+'</td></tr>'+
                '<tr><td>插件名称</td><td>'+p.pluginName+'</td></tr>'+
                '<tr><td>类名</td><td>'+(p.pluginClass||'-')+'</td></tr>'+
                '<tr><td>版本</td><td>'+(p.version||'v1.0.0')+'</td></tr>'+
                '<tr><td>状态</td><td>'+(p.enabled?'运行中':'已停用')+'</td></tr>'+
                '<tr><td>加载时间</td><td>'+formatTime(p.createTimeMs)+'</td></tr>'+
                '</table></div>'+
                '<div class="detail-section"><div class="detail-section-title">配置Schema</div><div class="json-viewer">'+(p.configSchema?JSON.stringify(p.configSchema,null,2):'暂无配置')+'</div></div>';
            document.getElementById('pluginDrawer').style.display='block';
        }
    });
}
function closeDrawer() { document.getElementById('pluginDrawer').style.display='none'; }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }

document.getElementById('pluginModal').addEventListener('click',function(e){if(e.target===this)hideModal();});
loadPlugins();
</script>
</body>
</html>
