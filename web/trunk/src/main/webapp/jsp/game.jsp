<%@ page import="pl.edu.agh.speedgame.dto.User" %>
<%@ page language="java"%>
<%
    User user = (User) request.getSession().getAttribute("user");
    String login = user.getLogin();
    String email = user.getEmail();
    String avatar = user.getAvatar();
    String ring = user.getRing();
    String time = request.getParameter("time");
    String game_mode = request.getParameter("game_mode");
    String game_end = request.getParameter("game_end");
    int count_end = game_end.equals("npl") ? 0 : 1;
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
        Array.prototype.remove = function(from, to) {
            var rest = this.slice((to || from) + 1 || this.length);
            this.length = from < 0 ? this.length + from : from;
            return this.push.apply(this, rest);
        };

        window.time = <%= time %>;
        window.players = {'<%= login %>' : {login: '<%= login %>', email: '<%= email %>', avatar: '<%= avatar %>', ring: '<%= ring %>', finished: false}}
        window.lost_players_list = []
        window.game_mode =  '<%= game_mode %>'
        window.count_end = <%= count_end %>


        $(function () {


            $("#dialog").modal({
                show: false
            })

            $("#summary").modal({
                show: false
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
                            data.finished = false;
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

                if(counter >= 0) {

                    stopClock(players_list[counter].login)
                    stopRing(players_list[counter].login)
                }

                var stillInGame = players_list.filter(function (obj) {
                    return !obj.finished;
                });

                if(stillInGame.length <= window.count_end) {
                    var text = ""
                    switch(window.count_end)
                    {
                        case 0:
                            text = ""
                            break;
                        case 1:
                            var winner = players_list.filter(function (obj) {
                                return !obj.finished;
                            })[0]
                            text = "The winner is: <i>" + winner.login + "</i> with: " + getLeftTime(winner.login) + " seconds left<br>"
                            break;
                        default:
                            text = "Unexpected ending"
                    }

                    var jres = $("#results")
                    jres.empty()

                    jres.append("<h5>" + text + "</h5>")

                    var lost_player_summary = ""

                    if(game_mode == "eot") {

                        for(var i = 0; i < window.lost_players_list.length; i++) {
                            lost_player_summary += "<p><i>" + lost_players_list[i].login + "</i> was defeated </p>"
                        }

                    } else {
                        for(var i = 0; i < window.lost_players_list.length; i++) {
                            var player = lost_players_list[i]
                            lost_player_summary += "<p><i>" + player.login + "</i> exceeded time by " + (player.time / 1000) + " seconds</p>"
                        }
                    }

                    jres.append(lost_player_summary)
                    $("#summary").modal("show")

                } else {
                    counter = (counter + 1) % players_list.length;

                    player = players_list[counter % players_list.length]

                    startClock(player.login)
                    startRing(player.login)

                    $('#right-arrow').text(players_list[(counter + 1) % players_list.length].login + ">")

                    var jplayer_list = $('#player_list')

                    $('#player_list p').remove()

                    for(var i = 0; i < players_list.length; i++) {
                        jplayer_list.append("<p style='font-size: 21px;font-weight: 200;'>" + players_list[(i + counter + 1) % players_list.length].login + "</p>")
                    }
                }
            }

            window.first = true

            $('#right-arrow').click(function() {
                if(window.first) {
                    window.first = false
                    $('#right-arrow').text(">")
                    $("#errors").html("");
                    $('#intro').text("Game")
                    $('#players').fadeOut(200)
                    $('#add_player_form').fadeOut(200)
                    $('#carousel_to_display').fadeIn()
                    window.players_list = []
                    window.counter = -1

                    var players = $(".player")
                    for(var i = 0 ; i < players.length; i++) {
                        var player = window.players[$(players[i]).text()]
                        players_list.push(player)
                        insert_new_player(player, time);
                    }

                    if(players.length == 1) {
                        $('#right-arrow').css("font-size", "100px")
                    }

                    $('.item').last().addClass("active")
                    $('#player_list').show()
                }

                update_ui_to_next_player();

            })

        });

        $(function(){
            $('#myCarousel').carousel().carousel('pause')
        });

        $(function() {
            $( "#player_list" ).resizable({
                animate: true
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

<div id="dialog" title="Player lost" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>Time is over</h3>
    </div>
    <div class="modal-body" id="over">
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>

<div id="summary" title="Summary" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>Summary</h3>
    </div>
    <div class="modal-body" id="results">
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>

<div id="myCarousel" class="carousel slide" >
    <div class="carousel-inner" id="carousel_to_display"></div>
    <div id="player_list" class="ui-widget-content" style="width: 200px;">
        <h3 class="ui-widget-header">Next Players</h3>
    </div>
    <a class="right carousel-control" href="#myCarousel" data-slide="next" id="right-arrow" style="left: auto; right: 20%;color: rgb(12, 12, 12); font-size: 50px;">Start&#62;</a>
</div>

<script>
</script>
</body>
</html>

