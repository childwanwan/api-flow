<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>分组管理</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <script src="${request.contextPath}/static/js/pagination.js"></script>
    <style>
        .sortable {
            cursor: pointer;
            user-select: none;
        }
        .sortable:hover {
            background-color: #f5f5f5;
        }
        .sort-icon {
            margin-left: 4px;
            opacity: 0.3;
        }
        .sort-icon.active {
            opacity: 1;
        }
    </style>
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
                <label>分组编码:</label>
                <input type="text" id="searchGroupCode" class="input" placeholder="请输入分组编码">
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
                <th class="sortable" onclick="sortBy('GROUP_CODE', 0)">
                    分组编码
                    <span id="sort-GROUP_CODE" class="sort-icon">↕</span>
                </th>
                <th class="sortable" onclick="sortBy('GROUP_NAME', 1)">
                    分组名称
                    <span id="sort-GROUP_NAME" class="sort-icon">↕</span>
                </th>
                <th>API数量</th>
                <th class="sortable" onclick="sortBy('CREATE_TIME_MS', 2)">
                    创建时间
                    <span id="sort-CREATE_TIME_MS" class="sort-icon active">↓</span>
                </th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="groupTableBody">
            <tr><td colspan="6" class="empty-row">暂无数据</td></tr>
            </tbody>
        </table>
        <div style="padding:0 16px;">
            <div class="pagination-wrap" id="groupPagination"></div>
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
                <label>分组编码 <span class="required">*</span></label>
                <input type="text" id="formGroupCode" class="form-control" placeholder="如：AMAZON_GROUP" required>
            </div>
            <div class="form-group">
                <label>分组名称 <span class="required">*</span></label>
                <input type="text" id="formGroupName" class="form-control" placeholder="如：亚马逊渠道">
            </div>
            <div class="form-group">
                <label>分组描述</label>
                <textarea id="formGroupDesc" class="form-control" placeholder="请输入分组描述"></textarea>
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
var editingGroupNo = null;
var sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 2}];

var groupPagination = new Pagination({
    el: 'groupPagination',
    total: 0,
    current: 1,
    pageSize: 20,
    pageSizes: [10, 20, 30, 50, 100, 200],
    onChange: function(page, size) {
        loadGroups();
    }
});

function toggleAll(cb) { document.querySelectorAll('.row-checkbox').forEach(function(c){c.checked=cb.checked;}); }

function formatTime(ms) {
    if (!ms) return '-';
    var d = new Date(ms);
    return d.getFullYear()+'-'+String(d.getMonth()+1).padStart(2,'0')+'-'+String(d.getDate()).padStart(2,'0')+' '+String(d.getHours()).padStart(2,'0')+':'+String(d.getMinutes()).padStart(2,'0')+':'+String(d.getSeconds()).padStart(2,'0');
}

function updateSortIcons() {
    document.querySelectorAll('.sort-icon').forEach(function(icon) {
        icon.className = 'sort-icon';
        icon.textContent = '↕';
    });
    
    sortOrderList.forEach(function(order) {
        var icon = document.getElementById('sort-' + order.field);
        if (icon) {
            icon.className = 'sort-icon active';
            icon.textContent = order.ascending ? '↑' : '↓';
        }
    });
}

function sortBy(field, order) {
    var existingIndex = sortOrderList.findIndex(function(o) { return o.field === field; });
    
    if (existingIndex >= 0) {
        sortOrderList[existingIndex].ascending = !sortOrderList[existingIndex].ascending;
    } else {
        sortOrderList.push({field: field, ascending: true, order: order});
    }
    
    sortOrderList.sort(function(a, b) {
        return (a.order || 999) - (b.order || 999);
    });
    
    updateSortIcons();
    loadGroups();
}

function loadGroups() {
    var groupCode = document.getElementById('searchGroupCode').value;
    var groupName = document.getElementById('searchGroupName').value;
    var state = groupPagination.getState();
    
    var requestData = {
        groupCodeLike: groupCode || undefined,
        groupNameLike: groupName || undefined,
        current: state.current,
        size: state.pageSize,
        sortOrderList: sortOrderList
    };
    
    fetch(BASE + '/page', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(function(r){return r.json();})
    .then(function(result){
        if (result.success) {
            var pageData = result.data;
            groupPagination.update(pageData.total || 0, pageData.current || state.current);
            renderGroups(pageData.records || []);
        }
    });
}

function renderGroups(groups) {
    var tbody = document.getElementById('groupTableBody');
    if (!groups.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-row">暂无数据</td></tr>';
        return;
    }
    tbody.innerHTML = groups.map(function(g){
        return '<tr>'+
            '<td class="checkbox-col"><input type="checkbox" class="row-checkbox" value="'+g.groupNo+'"></td>'+
            '<td>'+g.groupCode+'</td>'+
            '<td>'+g.groupName+'</td>'+
            '<td>'+(g.apiCount||0)+'</td>'+
            '<td>'+formatTime(g.createTimeMs)+'</td>'+
            '<td class="action-col">'+
                '<button class="btn-text" onclick="editGroup(\''+g.groupNo+'\')">编辑</button>'+
                '<span class="action-divider">|</span>'+
                '<button class="btn-text btn-danger-text" onclick="deleteGroup(\''+g.groupNo+'\')">删除</button>'+
            '</td></tr>';
    }).join('');
}

function searchGroups() { 
    groupPagination.update(0, 1);
    loadGroups(); 
}

function resetSearch() {
    document.getElementById('searchGroupCode').value='';
    document.getElementById('searchGroupName').value='';
    groupPagination.update(0, 1);
    sortOrderList = [{field: 'CREATE_TIME_MS', ascending: false, order: 2}];
    updateSortIcons();
    loadGroups();
}

function showCreateDialog() {
    editingGroupNo = null;
    document.getElementById('modalTitle').textContent = '新建分组';
    document.getElementById('formGroupCode').value = '';
    document.getElementById('formGroupName').value = '';
    document.getElementById('formGroupDesc').value = '';
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
                document.getElementById('formGroupCode').value = g.groupCode;
                document.getElementById('formGroupName').value = g.groupName;
                document.getElementById('formGroupDesc').value = g.groupDescription || '';
                document.getElementById('groupModal').style.display = 'flex';
            }
        });
}

function hideModal() { document.getElementById('groupModal').style.display='none'; }

function saveGroup() {
    var groupCode = document.getElementById('formGroupCode').value;
    var groupName = document.getElementById('formGroupName').value;
    if (!groupCode) { showToast('请输入分组编码','warning'); return; }
    if (!groupName) { showToast('请输入分组名称','warning'); return; }
    var data = {
        groupCode: groupCode,
        groupName: groupName,
        groupDescription: document.getElementById('formGroupDesc').value
    };
    if (editingGroupNo) {
        data.groupNo = editingGroupNo;
    }
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

updateSortIcons();
loadGroups();
</script>
</body>
</html>
