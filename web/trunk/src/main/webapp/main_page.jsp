<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    String error=request.getParameter("error");
    if(error==null || error.equals("null")){
        error="";
    }
%>

<html lang="en">
<head>
<meta charset="utf-8" />
<title>SpeedGame</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />

<!-- jQuery Image Gallery styles -->
<link rel="stylesheet" href="http://blueimp.github.com/jQuery-Image-Gallery/css/jquery.image-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the jQuery UI progress bars -->
<link rel="stylesheet" href="/css/jquery.fileupload-ui.css">
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/themes/start/jquery-ui.css" id="theme">

<link rel="stylesheet" href="/css/main_page.css" />
<link rel="stylesheet" href="/css/common.css" />

<script id="template-upload" type="text/x-tmpl" src="/js/template-upload.js"></script>
<script id="template-download" type="text/x-tmpl" src="/js/template-download.js"></script>

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>

<!-- The Templates plugin is included to render the upload/download listings -->
<script src="http://blueimp.github.com/JavaScript-Templates/tmpl.min.js"></script>

<script src="/js/tmpl_fix.js"></script>

<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="http://blueimp.github.com/JavaScript-Load-Image/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="http://blueimp.github.com/JavaScript-Canvas-to-Blob/canvas-to-blob.min.js"></script>
<!-- jQuery Image Gallery -->
<script src="http://blueimp.github.com/jQuery-Image-Gallery/js/jquery.image-gallery.min.js"></script>
<!-- ajaxForm plugin to jquery -->
<script src="http://malsup.github.io/jquery.form.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="/js/jquery.fileupload.js"></script>
<!-- The File Upload file processing plugin -->
<script src="/js/jquery.fileupload-fp.js"></script>
<!-- The File Upload user interface plugin -->
<script src="/js/jquery.fileupload-ui.js"></script>
<!-- The File Upload jQuery UI plugin -->
<script src="/js/jquery.fileupload-jui.js"></script>

<script src="/js/main_page.js"></script>

<script>


    $(function(){
        $( "#create-user" )
                .button()
                .click(function() {
                    $('.template-download').remove()
                    $('.template-upload').remove()
                    $( "#dialog-form" ).dialog( "open" );
                });

        create_menu("avatar");
        create_menu("ring");

        var name = $( "#name" ),
                email = $( "#email" ),
                password = $( "#password" ),
                avatar = $('#avatar span'),
                ring = $('#ring span'),
                allFields = $( [] ).add( name ).add( email ).add( password).add( avatar).add(ring)
                tips = $( ".validateTips");

        $( "#dialog-form" ).dialog({
            autoOpen: false,
            height: 600,
            width: 700,
            modal: true,
            buttons: {
                "Create an account": function() {
                    var bValid = true;

                    allFields.removeClass( "ui-state-error" );

                    bValid = bValid && checkLength( name, "username", 5, 16 );
                    bValid = bValid && checkLength( email, "email", 6, 80 );
                    bValid = bValid && checkLength( password, "password", 5, 16 );

                    bValid = bValid && checkRegexp( name, /^[a-z]([0-9a-z_])+$/i, "Username may consist of a-z, 0-9, underscores, begin with a letter." );
                    // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
                    bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );
                    bValid = bValid && checkRegexp( password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" );
                    bValid = bValid && checkNonEmptyFile(avatar, "Avatar cannot be empty" );
                    bValid = bValid && checkNonEmptyFile(ring, "Ring cannot be empty" );

                    if ( bValid ) {
                        that = this;
                        $.ajax("/register", {
                            error:function(jqXHR, textStatus, errorThrown) {
                                updateTips(textStatus + ": " + errorThrown)
                            },
                            success: function() {
                                window.location.replace("/")
                            },
                            data: {
                                login: name.val(),
                                password : password.val(),
                                email : email.val(),
                                avatar : avatar.text(),
                                ring : ring.text()
                            },
                            type : "POST"
                        })
                    }
                },
                Cancel: function() {
                    $( this ).dialog( "close" );
                }
            },
            close: function() {
                allFields.val( "" ).removeClass( "ui-state-error" );
                tips.removeClass( "ui-state-highlight");
                ring.text("Select ring");
                updateTips("")
                avatar.text("Select avatar");
            }
        });

    });

</script>

</head>
<body>

<%
    if(error != "") {
        out.println("<p id=\"login_error\" style=\"text-align: center; margin-left: 35%; margin-right: 35%\" class=\"ui-state-error\">" + error + "</p>");
    }
%>

<div id="dialog-form" title="Create new user">
    <p class="validateTips">All form fields are required.</p>

    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="/upload/" method="POST" enctype="multipart/form-data">

        <fieldset>

            <label for="name">Login</label>
            <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
            <label for="password">Password</label>
            <input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />
            <label for="email">Email</label>
            <input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />
            <label for="avatar">Avatar</label>
            <div>
                <button id="avatar">Select avatar</button>
            </div>
            <ul id="avatar_list">
            </ul>
            <label for="ring">Ring</label>
            <div>
                <button id="ring">Select ring</button>
            </div>
            <ul id="ring_list">
            </ul>

            <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
            <div class="row fileupload-buttonbar">
                <div class="span7">
                    <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button" style="margin:  5px;">
                    <i class="icon-plus icon-white"></i>
                    <span>Add files...</span>
                    <input type="file" name="files[]" multiple>
                </span>
                </div>
                <!-- The global progress information -->
                <div class="span5 fileupload-progress fade">
                    <!-- The global progress bar -->
                    <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                        <div class="bar" style="width:0%;"></div>
                    </div>
                    <!-- The extended global progress information -->
                    <div class="progress-extended">&nbsp;</div>
                </div>
            </div>

            <!-- The loading indicator is shown during file processing -->
            <div class="fileupload-loading"></div>
            <br>
            <!-- The table listing the files available for upload/download -->
            <table role="presentation" class="table table-striped"><tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody></table>


        </fieldset>
    </form>


</div>

<button id="create-user" style="float: right;margin-right: 30px">Create new user</button>

<form class="login_form" id="login_form" action="/login/" method="POST">
    <p class="field">
        <input type="text" name="login" placeholder="Login">
        <i class="icon-user icon-large"></i>
    </p>
    <p class="field">
        <input type="password" name="password" placeholder="Password">
        <i class="icon-lock icon-large"></i>
    </p>
    <p class="submit">
        <button type="submit" name="submit"><i class="icon-arrow-right icon-large"></i></button>
    </p>
</form>

</body>
</html>
