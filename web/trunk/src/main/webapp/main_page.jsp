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
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="http://blueimp.github.com/JavaScript-Templates/tmpl.min.js"></script>
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
<!-- The main application script -->
<script src="/js/main.js"></script>
<!-- jQuery Image Gallery styles -->
<link rel="stylesheet" href="http://blueimp.github.com/jQuery-Image-Gallery/css/jquery.image-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the jQuery UI progress bars -->
<link rel="stylesheet" href="/css/jquery.fileupload-ui.css">
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/themes/base/jquery-ui.css" id="theme">

<link rel="stylesheet" href="/css/login.css" />

<script>

    $(function(){

        var name = $( "#name" ),
                email = $( "#email" ),
                password = $( "#password" ),
                avatar = $('#select_avatar'),
                ring = $('#select_ring'),
                allFields = $( [] ).add( name ).add( email ).add( password).add( avatar).add(ring),
                tips = $( ".validateTips");


        function updateTips( t ) {
            tips
                    .text( t )
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

        function checkNonEmptyFile(t, o, n) {
            if(t.val() == "") {
                o.addClass( "ui-state-error" );
                updateTips( n );
                return false;
            } else {
                return true;
            }
        }

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
                    bValid = bValid && checkNonEmptyFile( $("#avatar"), avatar, "Avatar cannot be empty" );
                    bValid = bValid && checkNonEmptyFile( $("#ring"), ring, "Ring cannot be empty" );

                    if ( bValid ) {
                        /*
                         $( "#users tbody" ).append( "<tr>" +
                         "<td>" + name.val() + "</td>" +
                         "<td>" + email.val() + "</td>" +
                         "<td>" + password.val() + "</td>" +
                         "</tr>" );
                         */
                        that = this;
                        $.ajax("/register", {
                            error:function(jqXHR, textStatus, errorThrown) {
                                updateTips(textStatus + ": " + errorThrown)
                            },
                            success: function(data, textStatus, jqXHR) {
                                $(that).dialog( "close" );
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

        $( "#create-user" )
                .button()
                .click(function() {
                    $( "#dialog-form" ).dialog( "open" );
                });

        $( "#select_avatar" )
                .button()
                .click(function() {
                    function updateList() {
                        var avatars = $("#avatar_list").empty()
                        $.each($('.files .name a'), function () {
                            avatars.append("<li>" + $(this).text() + "</li>");
                        })
                    };

                    updateList();

                    if($( this ).parent().next().children().size() > 0) {
                        var menu = $( this ).parent().next().show().position({
                            my: "left top",
                            at: "left bottom",
                            of: this
                        });
                        $( menu ).one( "click", function(event) {
                            var selected_value = event.target.childNodes[0].data;
                            $('#avatar').val(selected_value)
                            $('#select_avatar').text(selected_value)
                            menu.hide();
                        });
                    }
                    return false;
                })
                .parent()
                .buttonset()
                .next()
                .hide()
                .menu();

        $( "#select_ring" )
                .button()
                .click(function() {
                    function updateList() {
                        var rings = $("#ring_list").empty()
                        $.each($('.files .name a'), function () {
                            rings.append("<li>" + $(this).text() + "</li>");
                        })
                    };

                    updateList();

                    if($( this ).parent().next().children().size() > 0) {
                        var menu = $( this ).parent().next().show().position({
                            my: "left top",
                            at: "left bottom",
                            of: this
                        });
                        $( menu ).one( "click", function(event) {
                            var selected_value = event.target.childNodes[0].data;
                            $('#ring').val(selected_value)
                            $('#select_ring').text(selected_value)
                            menu.hide();
                        });
                    }
                    return false;
                })
                .parent()
                .buttonset()
                .next()
                .hide()
                .menu()
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
                <button id="select_avatar">Select avatar</button>
            </div>
            <ul id="avatar_list">
            </ul>
            <input type="hidden" name="avatar" id="avatar" />
            <label for="avatar">Ring</label>
            <div>
                <button id="select_ring">Select ring</button>
            </div>
            <ul id="ring_list">
            </ul>
            <input type="hidden" name="ring" id="ring" />


            <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
            <div class="row fileupload-buttonbar">
                <div class="span7">
                    <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button" style="margin:  5px;">
                    <i class="icon-plus icon-white"></i>
                    <span>Add files...</span>
                    <input type="file" name="files[]" multiple>
                </span>
                    <!-- <button type="submit" class="btn btn-primary start">
                         <i class="icon-upload icon-white"></i>
                         <span>Start upload</span>
                     </button>-->
                    <!--                <button type="reset" class="btn btn-warning cancel">
                                        <i class="icon-ban-circle icon-white"></i>
                                        <span>Cancel upload</span>
                                    </button>-->
                    <!--<button type="button" class="btn btn-danger delete">
                        <i class="icon-trash icon-white"></i>
                        <span>Delete</span>
                    </button>-->
                    <!--         <input type="checkbox" class="toggle"> -->
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


            <!-- The template to display files available for upload -->
            <script id="template-upload" type="text/x-tmpl">
                {% for (var i=0, file; file=o.files[i]; i++) { %}
                <tr class="template-upload fade">
                    <td class="preview"><span class="fade"></span></td>
                    <td class="name"><span>{%=file.name%}</span></td>
                    <!-- <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td> -->
                    {% if (file.error) { %}
                    <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
                    {% } else if (o.files.valid && !i) { %}
                    <td>
                        <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>
                    </td>
                    <td>{% if (!o.options.autoUpload) { %}
                        <button class="btn btn-primary start">
                            <i class="icon-upload icon-white"></i>
                            <span>Start</span>
                        </button>
                        {% } %}</td>
                    {% } else { %}
                    <td colspan="2"></td>
                    {% } %}
                    <td>{% if (!i) { %}
                        <button class="btn btn-warning cancel">
                            <i class="icon-ban-circle icon-white"></i>
                            <span>Cancel</span>
                        </button>
                        {% } %}</td>
                </tr>
                {% } %}
            </script>
            <!-- The template to display files available for download -->
            <script id="template-download" type="text/x-tmpl">
                {% for (var i=0, file; file=o.files[i]; i++) { %}
                <tr class="template-download fade">
                    {% if (file.error) { %}
                    <td></td>
                    <td class="name"><span>{%=file.name%}</span></td>
                    <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
                    <td class="error" colspan="2"><span class="label label-important">Error</span> {%=file.error%}</td>
                    {% } else { %}
                    <td class="preview">{% if (file.thumbnail_url) { %}
                        <a href="{%=file.url%}" title="{%=file.name%}" data-gallery="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}"></a>
                        {% } %}</td>
                    <td class="name">
                        <a href="{%=file.url%}" title="{%=file.name%}" data-gallery="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name%}</a>
                    </td>
                    <!--  <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td> -->
                    <td colspan="2"></td>
                    {% } %}
                    <td>
                        <button class="btn btn-danger delete" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}"{% if (file.delete_with_credentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                        <i class="icon-trash icon-white"></i>
                        <span>Delete</span>
                        </button>
                        <!--  <input type="checkbox" name="delete" value="1" class="toggle"> -->
                    </td>
                </tr>
                {% } %}
            </script>
        </fieldset>
    </form>


</div>

<button id="create-user" style="float: right;">Create new user</button>

<form class="form-1" id="form-1" action="/login/" method="POST">
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

<!-- <div id="gallery-loader" style="display: none;"></div> -->


<select id="theme-switcher" class="pull-right" style="position: absolute; bottom: 0; right: 0;">
    <option value="base" selected>Base</option>
    <option value="black-tie">Black Tie</option>
    <option value="blitzer">Blitzer</option>
    <option value="cupertino">Cupertino</option>
    <option value="dark-hive">Dark Hive</option>
    <option value="dot-luv">Dot Luv</option>
    <option value="eggplant">Eggplant</option>
    <option value="excite-bike">Excite Bike</option>
    <option value="flick">Flick</option>
    <option value="hot-sneaks">Hot sneaks</option>
    <option value="humanity">Humanity</option>
    <option value="le-frog">Le Frog</option>
    <option value="mint-choc">Mint Choc</option>
    <option value="overcast">Overcast</option>
    <option value="pepper-grinder">Pepper Grinder</option>
    <option value="redmond">Redmond</option>
    <option value="smoothness">Smoothness</option>
    <option value="south-street">South Street</option>
    <option value="start">Start</option>
    <option value="sunny">Sunny</option>
    <option value="swanky-purse">Swanky Purse</option>
    <option value="trontastic">Trontastic</option>
    <option value="ui-darkness">UI Darkness</option>
    <option value="ui-lightness">UI Lightness</option>
    <option value="vader">Vader</option>
</select>
</body>
</html>