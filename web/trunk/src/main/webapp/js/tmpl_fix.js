
window.old_tmpl = window.tmpl

window.tmpl = function (id) {
    return function(data) {
        var template;
        $.ajax({
            async: false,
            type: 'GET',
            url: "/js/" + id + ".js",
            success: function(d){
                template = d;
            },
            dataType: "html"
        });

        return window.old_tmpl(template, data)
    }
}