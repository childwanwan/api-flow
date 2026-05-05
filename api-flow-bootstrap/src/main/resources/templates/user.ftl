<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>用户管理</h2>
        <div class="page-actions"><button class="btn btn-primary" onclick="showAddDialog()">+ 新建用户</button></div>
    </div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item"><label>用户名:</label><input type="text" id="searchUsername" class="input" placeholder="请输入用户名"></div>
            <div class="search-item"><label>角色:</label><select id="searchRole" class="select"><option value="">全部</option><option value="ADMIN">管理员</option><option value="USER">普通用户</option></select></div>
            <div class="search-item"><label>状态:</label><select id="searchStatus" class="select"><option value="">全部</option><option value="ENABLED">启用</option><option value="DISABLED">禁用</option></select></div>
            <div class="search-actions"><button class="btn" onclick="resetSearch()">重置</button><button class="btn btn-primary" onclick="searchUsers()">搜索</button></div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table">
            <thead><tr>
                <th class="checkbox-col"><input type="checkbox" onclick="toggleAll(this)"></th>
                <th>用户名</th><th>角色</th><th>状态</th><th>创建时间</th><th>最后登录</th><th>操作</th>
            </tr></thead>
            <tbody id="userTableBody">
            <#if userList??>
                <#list userList as user>
                <tr>
                    <td class="checkbox-col"><input type="checkbox" class="row-checkbox"></td>
                    <td>${user.username}</td>
                    <td><span class="status-tag <#if user.role=='ADMIN'>status-running<#else>status-success</#if>"><#if user.role=='ADMIN'>管理员<#else>普通用户</#if></span></td>
                    <td><span class="status-tag <#if user.status=='ENABLED'>status-enabled<#else>status-disabled</#if>"><span class="status-dot"></span><#if user.status=='ENABLED'>启用<#else>禁用</#if></span></td>
                    <td>${formatTime(user.createTimeMs)}</td>
                    <td>${user.lastLoginTimeMs?has_content?then(formatTime(user.lastLoginTimeMs), '-')}</td>
                    <td class="action-col">
                        <button class="btn-text" onclick="showEditDialog(${user.id})">编辑</button>
                        <span class="action-divider">|</span>
                        <button class="btn-text btn-danger-text" onclick="deleteUser(${user.id})">删除</button>
                    </td>
                </tr>
                </#list>
            <#else>
                <tr><td colspan="7" class="empty-row">暂无数据</td></tr>
            </#if>
            </tbody>
        </table>
        <div style="padding:0 16px;"><div class="pagination"><span class="pagination-total">共 ${userList?size!0} 条</span><div class="pagination-controls"></div></div></div>
    </div>
</div>

<div id="userModal" class="modal-overlay" style="display:none;">
    <div class="modal">
        <div class="modal-header"><h3 id="modalTitle">新建用户</h3><button class="modal-close" onclick="hideModal()">&times;</button></div>
        <div class="modal-body">
            <input type="hidden" id="userId">
            <div class="form-group"><label>用户名 <span class="required">*</span></label><input type="text" id="formUsername" class="form-control" placeholder="请输入用户名"></div>
            <div class="form-group"><label>密码 <span class="required">*</span></label><input type="password" id="formPassword" class="form-control" placeholder="请输入密码"><span class="form-hint">编辑时不填则不修改密码</span></div>
            <div class="form-group"><label>确认密码</label><input type="password" id="formPasswordConfirm" class="form-control" placeholder="请确认密码"></div>
            <div class="form-group"><label>角色</label><div class="radio-group"><label><input type="radio" name="formRole" value="ADMIN"> 管理员</label><label><input type="radio" name="formRole" value="USER" checked> 普通用户</label></div></div>
            <div class="form-group"><label>状态</label><div class="radio-group"><label><input type="radio" name="formUserStatus" value="ENABLED" checked> 启用</label><label><input type="radio" name="formUserStatus" value="DISABLED"> 禁用</label></div></div>
        </div>
        <div class="modal-footer"><button class="btn" onclick="hideModal()">取消</button><button class="btn btn-primary" onclick="saveUser()">保存</button></div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=user';}
function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }
function formatTime(ms) { if(!ms) return '-'; var d=new Date(ms); return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0'); }

function showAddDialog() {
    document.getElementById('modalTitle').textContent='新建用户';
    document.getElementById('userId').value='';
    document.getElementById('formUsername').value='';
    document.getElementById('formPassword').value='';
    document.getElementById('formPasswordConfirm').value='';
    document.querySelector('input[name=formRole][value=USER]').checked=true;
    document.querySelector('input[name=formUserStatus][value=ENABLED]').checked=true;
    document.getElementById('userModal').style.display='flex';
}
function showEditDialog(id) {
    fetch(contextPath+'/user/api/'+id).then(function(r){return r.json();}).then(function(data){
        if(data.success){
            document.getElementById('modalTitle').textContent='编辑用户';
            document.getElementById('userId').value=data.data.id;
            document.getElementById('formUsername').value=data.data.username;
            document.getElementById('formPassword').value='';
            document.getElementById('formPasswordConfirm').value='';
            var roleRadio=document.querySelector('input[name=formRole][value='+data.data.role+']');
            if(roleRadio) roleRadio.checked=true;
            var statusRadio=document.querySelector('input[name=formUserStatus][value='+data.data.status+']');
            if(statusRadio) statusRadio.checked=true;
            document.getElementById('userModal').style.display='flex';
        }
    });
}
function hideModal() { document.getElementById('userModal').style.display='none'; }
function saveUser() {
    var id=document.getElementById('userId').value;
    var username=document.getElementById('formUsername').value;
    var password=document.getElementById('formPassword').value;
    if(!username){showToast('请输入用户名','warning');return;}
    if(!id&&!password){showToast('请输入密码','warning');return;}
    if(password&&password!==document.getElementById('formPasswordConfirm').value){showToast('两次密码输入不一致','warning');return;}
    var body={username:username,role:document.querySelector('input[name=formRole]:checked').value,status:document.querySelector('input[name=formUserStatus]:checked').value};
    if(password) body.password=password;
    var url=id?contextPath+'/user/api/'+id:contextPath+'/user/api/create';
    var method=id?'PUT':'POST';
    fetch(url,{method:method,headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}).then(function(r){return r.json();}).then(function(data){
        if(data.success){showToast('保存成功','success');hideModal();location.reload();}
        else showToast(data.message||'操作失败','error');
    });
}
function deleteUser(id) { if(!confirm('确定要删除该用户吗？'))return; fetch(contextPath+'/user/api/'+id,{method:'DELETE'}).then(function(r){return r.json();}).then(function(data){if(data.success){showToast('删除成功','success');location.reload();}else showToast(data.message||'删除失败','error');}); }
function searchUsers() { var username=document.getElementById('searchUsername').value; var role=document.getElementById('searchRole').value; var status=document.getElementById('searchStatus').value; var url=contextPath+'/user/api/list?'+(username?'username='+encodeURIComponent(username):'')+(role?'&role='+role:'')+(status?'&status='+status:''); fetch(url).then(function(r){return r.json();}).then(function(data){if(data.success)renderTable(data.data.list);}); }
function resetSearch() { document.getElementById('searchUsername').value=''; document.getElementById('searchRole').value=''; document.getElementById('searchStatus').value=''; searchUsers(); }
function renderTable(list) { var tbody=document.getElementById('userTableBody'); if(!list||!list.length){tbody.innerHTML='<tr><td colspan="7" class="empty-row">暂无数据</td></tr>';return;} tbody.innerHTML=list.map(function(u){ return '<tr><td class="checkbox-col"><input type="checkbox" class="row-checkbox"></td><td>'+u.username+'</td><td><span class="status-tag '+(u.role==='ADMIN'?'status-running':'status-success')+'">'+(u.role==='ADMIN'?'管理员':'普通用户')+'</span></td><td><span class="status-tag '+(u.status==='ENABLED'?'status-enabled':'status-disabled')+'"><span class="status-dot"></span>'+(u.status==='ENABLED'?'启用':'禁用')+'</span></td><td>'+formatTime(u.createTimeMs)+'</td><td>'+(u.lastLoginTimeMs?formatTime(u.lastLoginTimeMs):'-')+'</td><td class="action-col"><button class="btn-text" onclick="showEditDialog('+u.id+')">编辑</button><span class="action-divider">|</span><button class="btn-text btn-danger-text" onclick="deleteUser('+u.id+')">删除</button></td></tr>'; }).join(''); }
function showToast(msg,type) { var t=document.createElement('div');t.className='toast toast-'+type;t.textContent=msg;document.body.appendChild(t);setTimeout(function(){t.remove();},3000); }
document.getElementById('userModal').addEventListener('click',function(e){if(e.target===this)hideModal();});
</script>
</body>
</html>
