

$(function () {
    'use strict';
    $('#fileupload').fileupload({
        url: '/upload',
        maxFileSize: 5000000,
        //acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        acceptFileTypes: /(\w*)$/i,
        process: [
            {
                action: 'load',
                //fileTypes: /^image\/(gif|jpeg|png)$/,
                fileTypes: /(\w*)$/,
                maxFileSize: 20000000 // 20MB
            },
            {
                action: 'resize',
                maxWidth: 1440,
                maxHeight: 900
            },
            {
                action: 'save'
            }
        ]
    });

    $('#fileupload .files').imagegallery();

});

function create_list(that) {
    return $( that ).parent().next().show().position({
        my: "left top",
        at: "left bottom",
        of: that
    });
}

function update_list(id) {
    var rings = $("#" + id).empty()
    $.each($('.files .name a'), function () {
        rings.append("<li>" + $(this).attr("download") + "</li>");
    })
};

function set_responsive(that, id) {
    if($( that ).parent().next().children().size() > 0) {
        var menu = create_list(that);
        menu.one( "click", function(event) {
            var selected_value = event.target.childNodes[0].data;
            $('#' + id).val(selected_value)
            $('#' + id + ' span').text(selected_value)
            menu.hide();
        });
    }
}

function create_menu(id) {
    $( "#" + id)
        .button()
        .click(function() {

            update_list(id + "_list");
            set_responsive(this, id)

            return false;
        })
        .parent()
        .buttonset()
        .next()
        .hide()
        .menu();
}

function updateTips( t ) {
    tips.text( t )
        .addClass( "ui-state-highlight" );
    setTimeout(function() {
        tips.removeClass( "ui-state-highlight", 1500 );
    }, 500 );
}

function checkLength( o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min ) {
        o.addClass( "ui-state-error" );
        updateTips( "Length of " + n + " must be between " +
            min + " and " + max + "." );
        return false;
    } else {
        return true;
    }
}

function checkRegexp( o, regexp, n ) {
    if ( !( regexp.test( o.val() ) ) ) {
        o.addClass( "ui-state-error" );
        updateTips( n );
        return false;
    } else {
        return true;
    }
}

function checkNonEmptyFile(o, n) {
    if(o.text().indexOf("Select ") == 0) {
        o.addClass( "ui-state-error" );
        updateTips( n );
        return false;
    } else {
        return true;
    }
}