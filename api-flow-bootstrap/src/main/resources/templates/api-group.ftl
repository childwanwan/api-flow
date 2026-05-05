<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>分组管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>分组管理</h2>
        <div class="page-actions">
            <button class="btn btn-primary" onclick="showCreateDialog()">+ 新建分组</button>
        </div>
    </div>
    <div class="search-card">
        <div class="search-row">
            <div class="search-item">
                <label>分组编号:</label>
                <input type="text" id="searchGroupNo" class="input" placeholder="请输入分组编号">
            </div>
            <div class="search-item">
                <label>分组名称:</label>
                <input type="text" id="searchGroupName" class="input" placeholder="请输入分组名称">
            </div>
            <div class="search-actions">
                <button class="btn" onclick="resetSearch()">重置</button>
                <button class="btn btn-primary" onclick="searchGroups()">搜索</button>
            </div>
        </div>
    </div>
    <div class="data-card">
        <table class="data-table">
            <thead>
            <tr>
                <th class="checkbox-col"><input type="checkbox" onclick="toggleAll(this)"></th>
                <th>分组编号</th>
                <th>分组名称</th>
                <th>API数量</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="groupTableBody">
            <tr><td colspan="7" class="empty-row">暂无数据</td></tr>
            </tbody>
        </table>
        <div style="padding:0 16px;">
            <div class="pagination">
                <span class="pagination-total" id="paginationTotal">共 0 条</span>
                <div class="pagination-controls" id="paginationControls"></div>
                <div class="pagination-size">
                    每页
                    <select id="pageSize" onchange="loadGroups()">
                        <option value="20" selected>20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                    条
                </div>
            </div>
        </div>
    </div>
</div>

<div id="groupModal" class="modal-overlay" style="display:none;">
    <div class="modal">
        <div class="modal-header">
            <h3 id="modalTitle">新建分组</h3>
            <button class="modal-close" onclick="hideModal()">&times;</button>
        </div>
        <div class="modal-body">
            <div class="form-group">
                <label>分组编号 <span class="required">*</span></label>
                <input type="text" id="formGroupNo" class="form-control" placeholder="如：AMAZON_GROUP" required>
            </div>
            <div class="form-group">
                <label>分组名称 <span class="required">*</span></label>
                <input type="text" id="formGroupName" class="form-control" placeholder="如：亚马逊渠道">
            </div>
            <div class="form-group">
                <label>分组描述</label>
                <textarea id="formGroupDesc" class="form-control" placeholder="请输入分组描述"></textarea>
            </div>
            <div class="form-group">
                <label>状态</label>
                <div class="radio-group">
                    <label><input type="radio" name="formStatus" value="ENABLED" checked> 启用</label>
                    <label><input type="radio" name="formStatus" value="DISABLED"> 禁用</label>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="hideModal()">取消</button>
            <button class="btn btn-primary" onclick="saveGroup()">保存</button>
        </div>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=api-group';}
var BASE = contextPath + '/api/v1/group';
var currentPage = 1;
var editingGroupNo = null;

function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }

function formatTime(ms) {
    if (!ms) return '-';
    var d = new Date(ms);
    return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0');
}

function statusTag(status) {
    var map = {ENABLED:'enabled',DISABLED:'disabled'};
    var textMap = {ENABLED:'启用',DISABLED:'禁用'};
    return '<span class="status-tag status-'+(map[status]||'disabled')+'"><span class="status-dot"></span>'+textMap[status]+'</span>';
}

function loadGroups() {
    var groupNo = document.getElementById('searchGroupNo').value;
    var groupName = document.getElementById('searchGroupName').value;
    var params = new URLSearchParams();
    if (groupNo) params.append('groupNo', groupNo);
    if (groupName) params.append('groupName', groupName);
    params.append('page', currentPage);
    params.append('size', document.getElementById('pageSize').value);
    fetch(BASE+'/list?'+params.toString())
        .then(function(r){return r.json();})
        .then(function(result){
            if (result.success) {
                renderGroups(result.data || []);
            }
        });
}

function renderGroups(groups) {
    var tbody = document.getElementById('groupTableBody');
    if (!groups.length) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-row">暂无数据</td></tr>';
        document.getElementById('paginationTotal').textContent = '共 0 条';
        return;
    }
    tbody.innerHTML = groups.map(function(g){
        return '<tr>'+
            '<td class="checkbox-col"><input type="checkbox" class="row-checkbox" value="'+g.groupNo+'"></td>'+
            '<td>'+g.groupNo+'</td>'+
            '<td>'+g.groupName+'</td>'+
            '<td>'+(g.apiCount||0)+'</td>'+
            '<td>'+statusTag(g.status||'ENABLED')+'</td>'+
            '<td>'+formatTime(g.createTimeMs)+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editGroup(\''+g.groupNo+'\')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteGroup(\''+g.groupNo+'\')">删除</button>'+
            '</td></tr>';
    }).join('');
    document.getElementById('paginationTotal').textContent = '共 '+groups.length+' 条';
}

function searchGroups() { currentPage=1; loadGroups(); }
function resetSearch() {
    document.getElementById('searchGroupNo').value='';
    document.getElementById('searchGroupName').value='';
    currentPage=1; loadGroups();
}

function showCreateDialog() {
    editingGroupNo = null;
    document.getElementById('modalTitle').textContent = '新建分组';
    document.getElementById('formGroupNo').value = '';
    document.getElementById('formGroupNo').readOnly = false;
    document.getElementById('formGroupName').value = '';
    document.getElementById('formGroupDesc').value = '';
    document.querySelector('input[name=formStatus][value=ENABLED]').checked = true;
    document.getElementById('groupModal').style.display = 'flex';
}

function editGroup(groupNo) {
    fetch(BASE+'/'+groupNo)
        .then(function(r){return r.json();})
        .then(function(result){
            if (result.success && result.data) {
                editingGroupNo = groupNo;
                var g = result.data;
                document.getElementById('modalTitle').textContent = '编辑分组';
                document.getElementById('formGroupNo').value = g.groupNo;
                document.getElementById('formGroupNo').readOnly = true;
                document.getElementById('formGroupName').value = g.groupName;
                document.getElementById('formGroupDesc').value = g.groupDescription || '';
                var radio = document.querySelector('input[name=formStatus][value='+(g.status||'ENABLED')+']');
                if (radio) radio.checked = true;
                document.getElementById('groupModal').style.display = 'flex';
            }
        });
}

function hideModal() { document.getElementById('groupModal').style.display='none'; }

function saveGroup() {
    var groupNo = document.getElementById('formGroupNo').value;
    var groupName = document.getElementById('formGroupName').value;
    if (!groupNo) { showToast('请输入分组编号','warning'); return; }
    if (!groupName) { showToast('请输入分组名称','warning'); return; }
    var status = document.querySelector('input[name=formStatus]:checked').value;
    var data = {
        groupNo: groupNo,
        groupName: groupName,
        groupDescription: document.getElementById('formGroupDesc').value,
        status: status
    };
    var url = editingGroupNo ? BASE+'/update' : BASE+'/create';
    var method = editingGroupNo ? 'PUT' : 'POST';
    fetch(url, {method:method, headers:{'Content-Type':'application/json'}, body:JSON.stringify(data)})
        .then(function(r){return r.json();})
        .then(function(result){
            if (result.success) {
                showToast(editingGroupNo?'保存成功':'创建成功','success');
                hideModal();
                loadGroups();
            } else {
                showToast(result.error?.message||'操作失败','error');
            }
        });
}

function deleteGroup(groupNo) {
    if (!confirm('确定要删除该分组吗？')) return;
    fetch(BASE+'/'+groupNo, {method:'DELETE'})
        .then(function(r){return r.json();})
        .then(function(result){
            if (result.success) { showToast('删除成功','success'); loadGroups(); }
            else showToast(result.error?.message||'删除失败','error');
        });
}

function showToast(msg, type) {
    var toast = document.createElement('div');
    toast.className = 'toast toast-'+type;
    toast.textContent = msg;
    document.body.appendChild(toast);
    setTimeout(function(){ toast.remove(); }, 3000);
}

document.getElementById('groupModal').addEventListener('click', function(e){ if(e.target===this) hideModal(); });

loadGroups();
</script>
</body>
</html>
