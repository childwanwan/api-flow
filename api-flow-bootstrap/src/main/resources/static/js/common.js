(function() {
    if (window.InputClear) {
        window.InputClear.init();
    }
})();

(function() {
    var popup = null;
    var currentTarget = null;
    var hideTimer = null;

    function ensurePopup() {
        if (popup) return;
        popup = document.createElement('div');
        popup.className = 'cell-tooltip-popup';
        document.body.appendChild(popup);
    }

    function show(el) {
        if (hideTimer) {
            clearTimeout(hideTimer);
            hideTimer = null;
        }
        ensurePopup();
        var tip = el.getAttribute('data-tip');
        if (!tip) return;

        if (el.scrollWidth <= el.clientWidth) return;

        popup.textContent = tip;
        popup.className = 'cell-tooltip-popup arrow-top';
        var rect = el.getBoundingClientRect();
        var popupLeft = rect.left;
        var popupTop = rect.bottom + 6;
        popup.style.left = popupLeft + 'px';
        popup.style.top = popupTop + 'px';
        popup.classList.add('visible');
        requestAnimationFrame(function() {
            var popupRect = popup.getBoundingClientRect();
            if (popupRect.right > window.innerWidth - 8) {
                popup.style.left = Math.max(8, window.innerWidth - popupRect.width - 8) + 'px';
            }
            if (popupRect.bottom > window.innerHeight - 8) {
                popup.style.top = Math.max(8, rect.top - popupRect.height - 6) + 'px';
                popup.classList.remove('arrow-top');
                popup.classList.add('arrow-bottom');
            }
        });
        currentTarget = el;
    }

    function hide() {
        if (!popup) return;
        popup.classList.remove('visible');
        popup.classList.remove('arrow-top');
        popup.classList.remove('arrow-bottom');
        currentTarget = null;
    }

    function scheduleHide() {
        hideTimer = setTimeout(function() {
            hideTimer = null;
            hide();
        }, 80);
    }

    document.addEventListener('mouseover', function(e) {
        var el = e.target.closest('.cell-tooltip');
        if (!el) return;
        if (el === currentTarget) return;
        show(el);
    });

    document.addEventListener('mouseout', function(e) {
        var el = e.target.closest('.cell-tooltip');
        if (!el) return;
        var related = e.relatedTarget;
        if (related && el.contains(related)) return;
        scheduleHide();
    });

    document.addEventListener('scroll', function() {
        if (currentTarget) hide();
    }, true);
})();

(function() {
    var icons = {
        success: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12l5 5L20 7"/></svg>',
        warning: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 9v4"/><path d="M12 17h.01"/><path d="M18 21H6a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2Z"/></svg>',
        error: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>',
        info: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/></svg>'
    };

    window.showToast = function(msg, type) {
        type = type || 'info';
        
        var toastContainer = document.getElementById('toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toast-container';
            document.body.appendChild(toastContainer);
        }

        var toast = document.createElement('div');
        toast.className = 'toast toast-' + type;
        toast.innerHTML = '<div class="toast-icon">' + (icons[type] || icons.info) + '</div><div class="toast-content"><span class="toast-message">' + msg + '</span></div><button class="toast-close" onclick="this.parentElement.remove()"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg></button>';
        
        toastContainer.appendChild(toast);

        setTimeout(function() {
            toast.classList.add('toast-leave');
            setTimeout(function() {
                if (toast.parentElement) {
                    toast.parentElement.removeChild(toast);
                }
            }, 300);
        }, 3500);
    };
})();