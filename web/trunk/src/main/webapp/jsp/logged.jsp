<%@ page import="java.util.Map" %>
<%@ page import="pl.edu.agh.speedgame.dto.User" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    String login = user.getLogin();
    String email = user.getEmail();
    String avatar = user.getAvatar();
    String ring = user.getRing();
%>

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>SpeedGame</title>

    <!-- jQuery Image Gallery styles -->
    <link rel="stylesheet" href="http://blueimp.github.com/jQuery-Image-Gallery/css/jquery.image-gallery.min.css">
    <!-- CSS to style the file input field as button and adjust the jQuery UI progress bars -->
    <link rel="stylesheet" href="/css/jquery.fileupload-ui.css">
    <%--<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/themes/start/jquery-ui.css" id="theme">--%>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

    <link rel="stylesheet" href="/css/login.css"/>
    <link rel="stylesheet" href="/css/common.css"/>

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

        $(function () {
            $("#game_modes").accordion()

            $("#change-profile")
                    .button()
                    .click(function () {
                        $('.template-download').remove()
                        $('.template-upload').remove()
                        $("#dialog-form").dialog("open");
                    });

            create_menu("new_avatar");
            create_menu("new_ring");

            $('#new_ring span').css("padding", "2")

            var email = $("#new_email"),
                    password = $("#new_password"),
                    avatar = $('#new_avatar span'),
                    ring = $('#new_ring span'),
                    allFields = $([]).add(email).add(password).add(avatar).add(ring)
            tips = $(".validateTips");

            $("#dialog-form").dialog({
                autoOpen: false,
                height: 600,
                width: 700,
                modal: true,
                buttons: {
                    "Change Profile": function () {
                        var bValid = true;

                        allFields.removeClass("ui-state-error");

                        bValid = bValid && (password.val().length == 0 || (checkLength(password, "password", 5, 16) && checkRegexp(password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9")));

                        // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
                        bValid = bValid && (email.val().length == 0 || checkRegexp(email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com"));

                        if (bValid) {
                            that = this;
                            $.ajax("/register", {
                                error: function (jqXHR, textStatus, errorThrown) {
                                    updateTips(textStatus + ": " + errorThrown)
                                },
                                success: function () {
                                    $(that).dialog("close");
                                },
                                data: {
                                    update: true,
                                    password: password.val(),
                                    email: email.val(),
                                    avatar: avatar.text() == "Select avatar" ? "" : avatar.text(),
                                    ring: ring.text() == "Select ring" ? "" : ring.text()
                                },
                                type: "POST"
                            })
                        }
                    },
                    Cancel: function () {
                        $(this).dialog("close");
                    }
                },
                close: function () {
                    allFields.val("").removeClass("ui-state-error");
                    tips.removeClass("ui-state-highlight");
                    ring.text("Select ring");
                    updateTips("")
                    avatar.text("Select avatar");
                    tips.text("Change only necessary fields")
                }
            });

        });

    </script>

</head>
<body>


<div id="dialog-form" title="Change Profile">
    <p class="validateTips">Change only necessary fields</p>

    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="/upload/" method="POST" enctype="multipart/form-data">

        <fieldset>
            <div>
                <label>Login</label>
                <label>
                    <%out.print(login);%>
                </label>
            </div>
            <div>
                <label for="new_password">New Password</label>
                <input type="password" name="new_password" id="new_password" value=""
                       class="text ui-widget-content ui-corner-all"/>
            </div>
            <div>
                <label>Current Email</label>
                <label><%out.print(email);%></label>
                <label for="new_email">New Email</label>
                <input type="text" name="email" id="new_email" value="" class="text ui-widget-content ui-corner-all"/>
            </div>
            <div class="files">
                <div>
                    <label>Current Avatar</label>
                    <label for="new_avatar">New Avatar</label>
                </div>
                <div class="name">
                    <a href="/upload?getfile=<%out.print(avatar);%>" title="<%out.print(avatar);%>"
                       data-gallery="gallery" download="<%out.print(avatar);%>"
                       style="position: relative;left: 3%;"><img src="/upload?getthumb=<%out.print(avatar);%>"></a>
                    <span style="position: relative;top: -20;left: 5%;">
                        <button id="new_avatar">Select avatar</button>
                    </span>
                    <ul id="new_avatar_list"></ul>
                </div>
            </div>
            <div>
                <table style="border-spacing: 0;" class="files">
                    <tr class="name">
                        <td><label>Current Ring</label></td>
                        <td><a href="/upload?getfile=<%out.print(ring);%>" title="<%out.print(ring);%>"
                               data-gallery="gallery" download="<%out.print(ring);%>"><%out.print(ring);%></a></td>
                    </tr>
                    <tr>
                        <td><label for="new_ring">New Ring</label></td>
                        <td>
                            <span>
                                <button id="new_ring">Select ring</button>
                            </span>
                            <ul id="new_ring_list"></ul>
                        </td>
                    </tr>
                </table>
            </div>


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
                    <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0"
                         aria-valuemax="100">
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
            <table role="presentation" class="table table-striped">
                <tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
            </table>


        </fieldset>

        <h3 class="ui-state-highlight">You need to relog to see changes</h3>
    </form>


</div>

<button id="change-profile" style="float: right;margin-right: 30px">Change profile</button>

<form class="login_form" id="game" action="/game/" method="GET" onsubmit="return validateTime();">
    <p class="field">

    <h2 style="text-align: center;color: #686868; font-family: ?Palatino Linotype?, ?Book Antiqua?, Palatino, serif;">
        New Game</h2>
    </p>
    <div id="game_modes">
        <h3 class="field"
            style="text-align: center;color: #686868; font-family: ?Palatino Linotype?, ?Book Antiqua?, Palatino, serif;margin-top: 10px;">
            Choose time</h3>
        <div>
            <input type="text" id="input_time" name="time" style="width: 200px;" placeholder="Time of game">
        </div>


        <h3 class="field"
            style="text-align: center;color: #686868; font-family: ?Palatino Linotype?, ?Book Antiqua?, Palatino, serif;margin-top: 10px;">
            Choose game mode</h3>

        <div>
            <table>
                <tr>
                    <td></td>
                    <td><input type="radio"
                               name="game_mode"
                               id="eot"
                               value="eot"
                               checked="true"/>
                        <label for="eot">Till end of time</label></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="radio"
                               name="game_mode"
                               id="pp"
                               value="pp"/>
                        <label for="pp">Penalty points</label></td>
                </tr>
            </table>

        </div>
        <h3 class="field"
            style="text-align: center;color: #686868; font-family: ?Palatino Linotype?, ?Book Antiqua?, Palatino, serif;margin-top: 10px;">
            Game ends when</h3>

        <div>
            <table>
                <tr>
                    <td></td>
                    <td><input type="radio"
                               name="game_end"
                               id="opl"
                               value="opl"
                               checked="true"/>
                        <label for="opl">One player left</label></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="radio"
                               name="game_end"
                               id="npl"
                               value="npl"/>
                        <label for="npl">None player left</label></td>
                </tr>
            </table>


        </div>
    </div>
    <p class="submit" style="top: 21px;">
        <button type="submit"><i class="icon-arrow-right icon-large"></i></button>
    </p>
</form>


</body>
</html>
