const Utilities = {
    debounce: function(func, wait) {
        if (typeof func !== 'function') {
            throw new TypeError('Expected a function');
        }
        let timeout;
        return function(...args) {
            const context = this;
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(context, args), wait);
        };
    },

    sanitizeText: function(text) {
        if (text === null || text === undefined) return '';
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    },

    fetchWithTimeout: function(url, options = {}, timeout = 10000) {
        return Promise.race([
            fetch(url, options),
            new Promise((_, reject) => setTimeout(() => reject(new Error("Request timed out")), timeout))
        ]);
    }
};