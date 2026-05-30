var ResizableColumns = (function() {
    var MIN_WIDTH = 40;
    var STORAGE_PREFIX = 'table-col-width:';

    function init(table, options) {
        if (!table || table.tagName !== 'TABLE') return;
        options = options || {};
        var pageKey = options.pageKey || window.location.pathname;

        table.classList.add('resizable');

        var indicator = createIndicator(table);

        restoreWidths(table, pageKey);
        createHandles(table, indicator, pageKey);
    }

    function createIndicator(table) {
        var indicator = document.createElement('div');
        indicator.className = 'resize-indicator';
        var card = table.closest('.data-card');
        if (card) {
            card.style.position = card.style.position || 'relative';
            card.appendChild(indicator);
        } else {
            table.parentElement.style.position = table.parentElement.style.position || 'relative';
            table.parentElement.appendChild(indicator);
        }
        return indicator;
    }

    function restoreWidths(table, pageKey) {
        var saved = loadWidths(pageKey);
        if (!saved) return;

        var ths = getResizableThs(table);
        ensureColgroup(table, ths.length);

        var colgroup = table.querySelector('colgroup');
        var cols = colgroup.querySelectorAll('col');
        ths.forEach(function(th, i) {
            var key = getThKey(th, i);
            if (saved[key] !== undefined) {
                cols[i].style.width = saved[key] + 'px';
            }
        });
    }

    function ensureColgroup(table, colCount) {
        var colgroup = table.querySelector('colgroup');
        if (!colgroup) {
            colgroup = document.createElement('colgroup');
            table.insertBefore(colgroup, table.firstChild);
        }
        var existing = colgroup.querySelectorAll('col');
        if (existing.length >= colCount) return;

        colgroup.innerHTML = '';
        for (var i = 0; i < colCount; i++) {
            var col = document.createElement('col');
            colgroup.appendChild(col);
        }
    }

    function getResizableThs(table) {
        return Array.prototype.slice.call(table.querySelectorAll('thead th'));
    }

    function getThKey(th, index) {
        var text = (th.textContent || '').trim().replace(/\s+/g, '_');
        return text || ('col_' + index);
    }

    function createHandles(table, indicator, pageKey) {
        var ths = getResizableThs(table);
        var lastIdx = ths.length - 1;

        ths.forEach(function(th, idx) {
            if (th.classList.contains('checkbox-col')) return;
            if (idx === lastIdx && isActionCol(th)) return;

            var handle = document.createElement('div');
            handle.className = 'resize-handle';
            th.appendChild(handle);

            handle.addEventListener('mousedown', function(e) {
                e.preventDefault();
                e.stopPropagation();
                startResize(e, table, th, idx, indicator, pageKey, handle);
            });

            handle.addEventListener('dblclick', function(e) {
                e.preventDefault();
                e.stopPropagation();
                autoFitColumn(table, th, idx, pageKey);
            });
        });
    }

    function isActionCol(th) {
        return th.classList.contains('action-col') ||
               (th.textContent || '').trim() === '操作';
    }

    function addColumnHighlight(table, colIdx) {
        var th = table.querySelectorAll('thead th')[colIdx];
        if (th) th.classList.add('resizing');
        var tds = table.querySelectorAll('tbody tr td:nth-child(' + (colIdx + 1) + ')');
        for (var i = 0; i < tds.length; i++) {
            tds[i].classList.add('resizing');
        }
    }

    function removeColumnHighlight(table, colIdx) {
        var th = table.querySelectorAll('thead th')[colIdx];
        if (th) th.classList.remove('resizing');
        var tds = table.querySelectorAll('tbody tr td:nth-child(' + (colIdx + 1) + ')');
        for (var i = 0; i < tds.length; i++) {
            tds[i].classList.remove('resizing');
        }
    }

    function startResize(e, table, th, colIdx, indicator, pageKey, handle) {
        var startX = e.pageX;
        var startWidth = th.offsetWidth;
        var tableRect = table.getBoundingClientRect();
        var card = table.closest('.data-card') || table.parentElement;
        var cardRect = card.getBoundingClientRect();

        indicator.style.height = table.offsetHeight + 'px';
        indicator.style.top = (tableRect.top - cardRect.top) + 'px';
        updateIndicatorPosition(e.pageX, tableRect, cardRect, indicator);
        indicator.classList.add('visible');
        handle.classList.add('active');
        document.body.classList.add('resizing');
        addColumnHighlight(table, colIdx);

        function onMouseMove(ev) {
            var diff = ev.pageX - startX;
            var newWidth = Math.max(MIN_WIDTH, startWidth + diff);

            setColumnWidth(table, colIdx, newWidth);
            updateIndicatorPosition(ev.pageX, tableRect, cardRect, indicator);
        }

        function onMouseUp(ev) {
            document.removeEventListener('mousemove', onMouseMove);
            document.removeEventListener('mouseup', onMouseUp);

            indicator.classList.remove('visible');
            handle.classList.remove('active');
            document.body.classList.remove('resizing');
            removeColumnHighlight(table, colIdx);

            var diff = ev.pageX - startX;
            var newWidth = Math.max(MIN_WIDTH, startWidth + diff);
            setColumnWidth(table, colIdx, newWidth);
            saveWidths(table, pageKey);
        }

        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
    }

    function updateIndicatorPosition(pageX, tableRect, cardRect, indicator) {
        var x = pageX - cardRect.left;
        indicator.style.left = x + 'px';
    }

    function setColumnWidth(table, colIdx, width) {
        ensureColgroup(table, getResizableThs(table).length);
        var colgroup = table.querySelector('colgroup');
        var cols = colgroup.querySelectorAll('col');
        if (cols[colIdx]) {
            cols[colIdx].style.width = width + 'px';
        }
    }

    function autoFitColumn(table, th, colIdx, pageKey) {
        var maxW = th.scrollWidth;
        var tempStyle = null;

        var tds = table.querySelectorAll('tbody tr td:nth-child(' + (colIdx + 1) + ')');
        tds.forEach(function(td) {
            if (tempStyle) {
                td.style.removeProperty(tempStyle);
            }
            var origOverflow = td.style.overflow;
            var origTextOverflow = td.style.textOverflow;
            var origWhiteSpace = td.style.whiteSpace;
            var origMaxWidth = td.style.maxWidth;

            td.style.overflow = 'visible';
            td.style.textOverflow = 'clip';
            td.style.whiteSpace = 'nowrap';
            td.style.maxWidth = 'none';

            var w = td.scrollWidth;
            if (w > maxW) maxW = w;

            td.style.overflow = origOverflow;
            td.style.textOverflow = origTextOverflow;
            td.style.whiteSpace = origWhiteSpace;
            td.style.maxWidth = origMaxWidth;
        });

        var finalWidth = Math.max(MIN_WIDTH, maxW + 24);
        setColumnWidth(table, colIdx, finalWidth);
        saveWidths(table, pageKey);
    }

    function saveWidths(table, pageKey) {
        var ths = getResizableThs(table);
        var colgroup = table.querySelector('colgroup');
        if (!colgroup) return;

        var cols = colgroup.querySelectorAll('col');
        var data = {};
        ths.forEach(function(th, i) {
            var key = getThKey(th, i);
            if (cols[i] && cols[i].style.width) {
                data[key] = parseInt(cols[i].style.width, 10);
            }
        });

        try {
            localStorage.setItem(STORAGE_PREFIX + pageKey, JSON.stringify(data));
        } catch (e) {}
    }

    function loadWidths(pageKey) {
        try {
            var raw = localStorage.getItem(STORAGE_PREFIX + pageKey);
            return raw ? JSON.parse(raw) : null;
        } catch (e) {
            return null;
        }
    }

    return { init: init };
})();
