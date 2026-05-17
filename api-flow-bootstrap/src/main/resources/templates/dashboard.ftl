<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>运行报表</title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/common.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/input-clear.css">
    <script src="${request.contextPath}/static/js/input-clear.js"></script>
    <script src="${request.contextPath}/static/js/common.js"></script>
</head>
<body class="iframe-body">
<div class="iframe-content">
    <div class="page-header">
        <h2>运行报表</h2>
    </div>
    <div class="stat-cards">
        <div class="stat-card">
            <div class="stat-title">今日任务量</div>
            <div class="stat-value" id="todayTotal">-</div>
        </div>
        <div class="stat-card">
            <div class="stat-title">今日成功率</div>
            <div class="stat-value" id="successRate">-</div>
        </div>
        <div class="stat-card">
            <div class="stat-title">今日失败数</div>
            <div class="stat-value" id="todayFailed">-</div>
        </div>
        <div class="stat-card">
            <div class="stat-title">待执行任务</div>
            <div class="stat-value" id="pendingTasks">-</div>
        </div>
    </div>
    <div class="charts-row">
        <div class="chart-card" style="flex:3;">
            <div class="chart-header">
                <h3>任务趋势</h3>
                <div class="radio-group">
                    <label><input type="radio" name="trendType" value="day" checked> 按天</label>
                    <label><input type="radio" name="trendType" value="hour"> 按小时</label>
                </div>
            </div>
            <div class="chart-body" id="trendChart">
                <div style="display:flex;align-items:center;justify-content:center;height:100%;color:#909399;">暂无趋势数据</div>
            </div>
        </div>
        <div class="chart-card" style="flex:2;">
            <div class="chart-header">
                <h3>任务状态分布</h3>
            </div>
            <div class="chart-body" id="statusChart">
                <div style="display:flex;flex-direction:column;align-items:center;justify-content:center;height:100%;gap:8px;" id="statusDistribution">
                    <div style="display:flex;align-items:center;gap:8px;"><span style="width:10px;height:10px;border-radius:50%;background:#E6A23C;display:inline-block;"></span><span style="color:#606266;font-size:13px;">待执行: <strong id="distPending">0</strong> (0%)</span></div>
                    <div style="display:flex;align-items:center;gap:8px;"><span style="width:10px;height:10px;border-radius:50%;background:#409EFF;display:inline-block;"></span><span style="color:#606266;font-size:13px;">执行中: <strong id="distRunning">0</strong> (0%)</span></div>
                    <div style="display:flex;align-items:center;gap:8px;"><span style="width:10px;height:10px;border-radius:50%;background:#67C23A;display:inline-block;"></span><span style="color:#606266;font-size:13px;">已完成: <strong id="distSuccess">0</strong> (0%)</span></div>
                    <div style="display:flex;align-items:center;gap:8px;"><span style="width:10px;height:10px;border-radius:50%;background:#F56C6C;display:inline-block;"></span><span style="color:#606266;font-size:13px;">失败: <strong id="distFailed">0</strong> (0%)</span></div>
                    <div style="display:flex;align-items:center;gap:8px;"><span style="width:10px;height:10px;border-radius:50%;background:#909399;display:inline-block;"></span><span style="color:#606266;font-size:13px;">已取消: <strong id="distCanceled">0</strong> (0%)</span></div>
                </div>
            </div>
        </div>
    </div>
    <div class="rank-table">
        <div class="rank-header"><h3>Top API 调用排行</h3></div>
        <table class="data-table">
            <thead>
            <tr>
                <th style="width:50px;">#</th>
                <th>API编码</th>
                <th>API名称</th>
                <th style="width:120px;">调用次数</th>
                <th style="width:100px;">成功率</th>
            </tr>
            </thead>
            <tbody id="topApiBody">
            <tr><td colspan="5" class="empty-row">暂无数据</td></tr>
            </tbody>
        </table>
    </div>
</div>

<script>
var contextPath = '${request.contextPath}';
if(window.top!==window.self){}else{window.top.location.href=contextPath+'/index?page=dashboard';}

fetch(contextPath + '/api/v1/statistics/dashboard', {credentials: 'include'})
    .then(function(res) { return res.json(); })
    .then(function(res) {
        if (res.success && res.data) {
            var d = res.data;
            document.getElementById('todayTotal').textContent = d.todayTotal || 0;
            document.getElementById('todayFailed').textContent = d.todayFailed || 0;
            document.getElementById('pendingTasks').textContent = d.pendingTasks || 0;
            document.getElementById('successRate').textContent = (d.successRate || 0) + '%';
            document.getElementById('distPending').textContent = d.pendingTasks || 0;
            document.getElementById('distRunning').textContent = d.runningTasks || 0;
            document.getElementById('distSuccess').textContent = d.successTasks || 0;
            document.getElementById('distFailed').textContent = d.failedTasks || 0;
            var total = (d.todayTotal || 0);
            if (total > 0) {
                document.getElementById('distPending').parentElement.querySelector('strong').nextSibling.textContent = ' (' + ((d.pendingTasks||0)/total*100).toFixed(1) + '%)';
                document.getElementById('distRunning').parentElement.querySelector('strong').nextSibling.textContent = ' (' + ((d.runningTasks||0)/total*100).toFixed(1) + '%)';
                document.getElementById('distSuccess').parentElement.querySelector('strong').nextSibling.textContent = ' (' + ((d.successTasks||0)/total*100).toFixed(1) + '%)';
                document.getElementById('distFailed').parentElement.querySelector('strong').nextSibling.textContent = ' (' + ((d.failedTasks||0)/total*100).toFixed(1) + '%)';
            }
        }
    })
    .catch(function(err) { console.error('Failed to load dashboard data:', err); });
</script>
</body>
</html>
