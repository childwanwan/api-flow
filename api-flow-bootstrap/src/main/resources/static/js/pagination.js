function Pagination(options) {
    this.el = typeof options.el === 'string' ? document.getElementById(options.el) : options.el;
    if (!this.el) return;

    this.total = options.total || 0;
    this.current = options.current || 1;
    this.pageSize = options.pageSize || 20;
    this.pageSizes = options.pageSizes || [10, 20, 30, 50, 100, 200];
    this.showJumper = options.showJumper !== false;
    this.showSizeChanger = options.showSizeChanger !== false;
    this.showTotal = options.showTotal !== false;
    this.mode = options.mode || 'full';
    this.onChange = options.onChange || function () {};
    this._build();
    this._render();
}

Pagination.prototype._build = function () {
    var self = this;
    this.el.className = 'pagination-wrap';
    this.el.innerHTML = '';

    if (this.showTotal) {
        this.elTotal = document.createElement('div');
        this.elTotal.className = 'pagination-info';
        this.el.appendChild(this.elTotal);
    }

    this.elList = document.createElement('div');
    this.elList.className = 'pagination-list';
    this.el.appendChild(this.elList);

    this.elPrev = document.createElement('button');
    this.elPrev.className = 'pagination-item pagination-nav-btn';
    this.elPrev.title = '上一页';
    this.elPrev.innerHTML = '<svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor"><path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/></svg>';
    this.elPrev.addEventListener('click', function () {
        if (self.current > 1) self._go(self.current - 1);
    });

    this.elNext = document.createElement('button');
    this.elNext.className = 'pagination-item pagination-nav-btn';
    this.elNext.title = '下一页';
    this.elNext.innerHTML = '<svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor"><path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"/></svg>';
    this.elNext.addEventListener('click', function () {
        if (self.current < self._totalPages()) self._go(self.current + 1);
    });

    if (this.showSizeChanger) {
        this.elSize = document.createElement('div');
        this.elSize.className = 'pagination-size';
        this.elSize.innerHTML = '每页 ';
        this.elSizeSelect = document.createElement('select');
        this.elSizeSelect.addEventListener('change', function () {
            self.pageSize = parseInt(this.value);
            self.current = 1;
            self._render();
            self.onChange(self.current, self.pageSize);
        });
        this.elSize.appendChild(this.elSizeSelect);
        this.elSize.appendChild(document.createTextNode(' 条'));
        this.el.appendChild(this.elSize);
    }

    if (this.showJumper) {
        this.elJumper = document.createElement('div');
        this.elJumper.className = 'pagination-jumper';
        this.elJumper.innerHTML = '跳至 ';
        this.elJumperInput = document.createElement('input');
        this.elJumperInput.type = 'text';
        this.elJumperInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                var page = parseInt(self.elJumperInput.value);
                var tp = self._totalPages();
                if (page >= 1 && page <= tp) {
                    self._go(page);
                }
                self.elJumperInput.value = '';
            }
        });
        this.elJumper.appendChild(this.elJumperInput);
        this.elJumper.appendChild(document.createTextNode(' 页'));
        this.el.appendChild(this.elJumper);
    }
};

Pagination.prototype._totalPages = function () {
    return Math.max(1, Math.ceil(this.total / this.pageSize));
};

Pagination.prototype._render = function () {
    var self = this;
    var totalPages = this._totalPages();

    if (this.elTotal) {
        this.elTotal.innerHTML = '共 <strong>' + this.total + '</strong> 条';
    }

    if (this.elSizeSelect) {
        var html = '';
        for (var i = 0; i < this.pageSizes.length; i++) {
            var s = this.pageSizes[i];
            html += '<option value="' + s + '"' + (s === this.pageSize ? ' selected' : '') + '>' + s + '</option>';
        }
        this.elSizeSelect.innerHTML = html;
    }

    if (this.mode === 'simple') {
        this.elList.innerHTML = '';
        return;
    }

    this.elList.innerHTML = '';

    this.elList.appendChild(this.elPrev);
    this.elPrev.classList.toggle('disabled', this.current <= 1);

    if (totalPages <= 9) {
        for (var i = 1; i <= totalPages; i++) {
            this.elList.appendChild(this._createPageBtn(i));
        }
    } else {
        this.elList.appendChild(this._createPageBtn(1));
        if (this.current > 4) {
            this.elList.appendChild(this._createEllipsis());
        }
        var start = Math.max(2, this.current - 2);
        var end = Math.min(totalPages - 1, this.current + 2);
        if (this.current <= 4) {
            start = 2;
            end = Math.min(6, totalPages - 1);
        }
        if (this.current >= totalPages - 3) {
            start = Math.max(2, totalPages - 5);
            end = totalPages - 1;
        }
        for (var i = start; i <= end; i++) {
            this.elList.appendChild(this._createPageBtn(i));
        }
        if (this.current < totalPages - 3) {
            this.elList.appendChild(this._createEllipsis());
        }
        this.elList.appendChild(this._createPageBtn(totalPages));
    }

    this.elList.appendChild(this.elNext);
    this.elNext.classList.toggle('disabled', this.current >= totalPages);
};

Pagination.prototype._createPageBtn = function (page) {
    var self = this;
    var btn = document.createElement('button');
    btn.className = 'pagination-item';
    btn.textContent = page;
    if (page === this.current) btn.classList.add('active');
    btn.addEventListener('click', function () {
        if (page !== self.current) self._go(page);
    });
    return btn;
};

Pagination.prototype._createEllipsis = function () {
    var span = document.createElement('span');
    span.className = 'pagination-ellipsis';
    span.innerHTML = '<svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor"><path d="M6 10c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm12 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-6 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z"/></svg>';
    return span;
};

Pagination.prototype._go = function (page) {
    this.current = page;
    this._render();
    this.onChange(this.current, this.pageSize);
};

Pagination.prototype.update = function (total, current) {
    this.total = total || 0;
    if (typeof current !== 'undefined') this.current = current;
    if (this.current > this._totalPages()) this.current = this._totalPages();
    this._render();
};

Pagination.prototype.getState = function () {
    return {
        current: this.current,
        pageSize: this.pageSize,
        total: this.total
    };
};