<%@ page import="pl.edu.agh.speedgame.dto.User" %>
<%@ page language="java"%>
<%
    User user = (User) request.getSession().getAttribute("user");
    String login = user.getLogin();
    String email = user.getEmail();
    String avatar = user.getAvatar();
    String ring = user.getRing();
    String time = request.getParameter("time");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet" href="/css/countdown.css"/>
    <link rel="stylesheet" href="/css/common.css"/>
    <link rel="stylesheet" href="/css/bootstrap.css"/>
    <link rel="stylesheet" href="/css/game.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
    <script src="/js/jquery.lwtCountdown-1.0.js"></script>
    <script src="/js/misc.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/game.js"></script>

    <script>
        window.time = <%= time %>
                window.players = {'<%= login %>' : {login: '<%= login %>', email: '<%= email %>', avatar: '<%= avatar %>', ring: '<%= ring %>'}}

        $(function () {


            $("#dialog").dialog({
                autoOpen: false,
                resizable: false,
                height: 320,
                modal: true,
                buttons: {
                    "Ok": function() {
                        $(this).dialog( "close" );
                    }
                }
            })

            $("#add_player_form").submit(function(event) {
                var login = $('#login').val()
                var pass = $('#password').val()
                $.ajax("/login", {
                    error:function(jqXHR, textStatus, errorThrown) {
                        $("#errors").html("<div class='alert alert-error'><button type='btn' class='close' data-dismiss='alert'>&times;</button><strong>Wrong login or password</strong></div>")
                    },
                    success: function(data, textStatus, jqXHR) {
                        $("#errors").html("");
                        if (players[data.login] == undefined) {
                            players[data.login] = data;
                            $('#players').append("<li class='ui-state-default player'><span class='ui-icon ui-icon-arrowthick-2-n-s'></span>" + login + "</li>");
                            $('input').val("")
                        }
                    },
                    data: {
                        login: login,
                        password : pass,
                        exists : true
                    },
                    dataType: "json",
                    type : "POST"
                })

                return false;
            });

            function update_ui_to_next_player() {
                var last = counter

                if(counter >= 0) {
                    // first run
                    stopClock(players_list[last].login)
                    stopRing(players_list[last].login)
                }

                counter = (counter + 1) % players_list.length;

                player = players_list[counter % players_list.length]

                startClock(player.login)
                startRing(player.login)

                $('#right-arrow').text(players_list[(counter + 1) % players_list.length].login + ">")

                var jplayer_list = $('#player_list')

                $('#player_list p').remove()

                for(var i = 0; i < players_list.length - 1; i++) {
                    jplayer_list.append("<p style='font-size: 21px;font-weight: 200;'>" + players_list[(i + counter + 1) % players_list.length].login + "</p>")
                }
            }

            window.first = true

            $('#right-arrow').click(function() {
                if(window.first) {
                    window.first = false
                    $("#errors").html("");
                    $('#intro').text("Game")
                    $('#players').fadeOut(200)
                    $('#add_player_form').fadeOut(200)
                    $('#carousel_to_display').fadeIn()
                    $('#right-arrow').css("font-size", "50px")
                    window.players_list = []
                    window.counter = -1

                    var players = $(".player")
                    for(var i = 0 ; i < players.length; i++) {
                        var player = window.players[$(players[i]).text()]
                        players_list.push(player)
                        insert_new_player(player, time);
                    }

                    $('.item').last().addClass("active")
                    $('#player_list').show()
                }

                update_ui_to_next_player();

            })



        });


    </script>
</head>

<body>

<div id="countdown_dashboard" style="margin-left: 15%;display:none;margin-top: 20px;">

    <div class="dash minutes_dash">
        <span class="dash_title">minutes</span>

        <div class="digit">0</div>
        <div class="digit">0</div>
    </div>

    <div class="dash seconds_dash">
        <span class="dash_title">seconds</span>

        <div class="digit">0</div>
        <div class="digit">0</div>
    </div>

</div>

<h1 style="margin: 0; line-height: 1.25; color: #fff; text-shadow: 0 1px 1px rgba(0,0,0,.4); font-size: 400%;" id="intro">Game Setup</h1>

<p id="errors"></p>

<form class="form-horizontal" id="add_player_form" action="/login" method="POST" style="max-width: 400px; position: relative; left: 15%;" >
    <legend style="text-align: center;">Player Setup</legend>
    <div class="control-group">
        <label class="control-label" for="login">Login</label>
        <div class="controls">
            <input type="text" id="login" placeholder="Login">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="password">Password</label>
        <div class="controls">
            <input type="password" id="password" placeholder="Password">
        </div>
    </div>
    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn btn-success">Add Player</button>
        </div>
    </div>
</form>


<ul id="players" style="position: absolute; right: 22%; top: 113px;">
    <li class="ui-state-default player"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span><%out.print(login);%></li>
</ul>

<div id="dialog" title="Player lost">
    <p id="over">
        Time is over for player
    </p>
</div>

<!-- Carousel
================================================== -->
<div id="myCarousel" class="carousel slide" >
    <div class="carousel-inner"  id="carousel_to_display"> </div>
    <%--<a class="left carousel-control" href="#myCarousel" data-slide="prev">&lsaquo;</a>--%>
    <div id="player_list" class="ui-widget-content">
        <h3 class="ui-widget-header">Players</h3>
        <p> ala ma kota</p>
    </div>
    <a class="right carousel-control" href="#myCarousel" data-slide="next" id="right-arrow" style="left: auto; right: 15%;color: rgb(12, 12, 12);">&#62;</a>
</div>

<script>
    !function ($) {
        $(function(){
            $('#myCarousel').carousel().carousel('pause')
        })

        $(function() {
            $( "#player_list" ).resizable({
                animate: true
            })
        })
    }(window.jQuery)
</script>
</body>
</html>

