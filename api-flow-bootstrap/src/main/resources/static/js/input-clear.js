/**
 * 输入框清空按钮组件 (参考 Element UI 实现)
 * 核心思路：用 wrapper 只包裹 input，不包含 label
 * 这样 top:50% 就是输入框的中心，不会受 label 影响
 */
(function(window, document) {
    'use strict';

    var supportedTypes = {'text':1, 'search':1, 'password':1, 'number':1, 'email':1, 'url':1, 'tel':1};

    function wrapInput(input) {
        if (input.parentNode && input.parentNode.className && input.parentNode.className.indexOf('input-clear-wrap') !== -1) return input.parentNode;

        var wrapper = document.createElement('span');
        wrapper.className = 'input-clear-wrap';

        var parent = input.parentNode;
        parent.insertBefore(wrapper, input);
        wrapper.appendChild(input);

        return wrapper;
    }

    function createClearButton(input, wrapper) {
        if (input._clearBtn) return;

        var suffix = document.createElement('span');
        suffix.className = 'input-clear-suffix';

        var icon = document.createElement('span');
        icon.className = 'input-clear-icon';
        icon.innerHTML = '\u00D7';

        suffix.appendChild(icon);
        wrapper.appendChild(suffix);

        input._clearBtn = icon;
        input._clearSuffix = suffix;

        function updateBtn() {
            if (input.value && input.value.length > 0) {
                suffix.style.display = 'flex';
            } else {
                suffix.style.display = 'none';
            }
        }

        icon.onclick = function(e) {
            e = e || window.event;
            if (e.stopPropagation) e.stopPropagation();
            if (e.preventDefault) e.preventDefault();
            input.value = '';
            input.focus();
            suffix.style.display = 'none';
            triggerEvent(input, 'input');
            triggerEvent(input, 'change');
        };

        if (input.addEventListener) {
            input.addEventListener('input', updateBtn);
            input.addEventListener('focus', updateBtn);
            input.addEventListener('blur', function() {
                setTimeout(updateBtn, 150);
            });
        } else if (input.attachEvent) {
            input.attachEvent('onpropertychange', updateBtn);
            input.attachEvent('onfocus', updateBtn);
            input.attachEvent('onblur', function() {
                setTimeout(updateBtn, 150);
            });
        }

        updateBtn();
    }

    function triggerEvent(el, name) {
        try {
            if (typeof Event === 'function') {
                var evt = new Event(name, { bubbles: true });
            } else {
                var evt = document.createEvent('HTMLEvents');
                evt.initEvent(name, true, true);
            }
            el.dispatchEvent(evt);
        } catch(e) {}
    }

    function init(root) {
        root = root || document;
        var inputs = root.getElementsByTagName('input');
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            var type = (input.type || 'text').toLowerCase();
            if (!supportedTypes[type]) continue;
            if (input.className && input.className.indexOf('no-clear') !== -1) continue;
            if (input.readOnly) continue;
            if (input._clearBtn) continue;
            var wrapper = wrapInput(input);
            createClearButton(input, wrapper);
        }
    }

    if (document.readyState === 'loading') {
        if (document.addEventListener) {
            document.addEventListener('DOMContentLoaded', function() {
                init();
                setTimeout(init, 500);
            });
        } else if (document.attachEvent) {
            document.attachEvent('onreadystatechange', function() {
                if (document.readyState === 'complete') {
                    init();
                    setTimeout(init, 500);
                }
            });
        }
    } else {
        init();
        setTimeout(init, 500);
    }

    window.InputClear = { init: init };

})(window, document);
