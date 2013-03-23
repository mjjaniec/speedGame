<%@ page language="java"%>
<html>
<head>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
    <script src="lib/jquery.lwtCountdown-1.0.js"></script>
    <script src="lib/misc.js"></script>
    <link rel="stylesheet" href="css/game.css"/>
    <link rel="stylesheet" href="css/countdown.css"/>
</head>

<body>

<script>

    $(document).ready(function () {

        $('button').button();

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

        var players = <%= request.getParameter("players") %>
        var time = <%= request.getParameter("time") %>

        for (i = 0; i < players; i++) {
            insert_new_player(i, players, time);
        }

        $('button').first().toggle("highlight", 500)
    });

    function insert_new_player(id, max, time) {
        document.getElementById("player_list").insertCell(-1).innerHTML = "<h2>Player " + (id + 1) + "</h2>"

        createNewClock(id, time)

        var button = document.createElement('button')
        var jb = $(button).button()
        jb.text("Next player")
        jb.hide()
        jb.click(function () {
            stopClock(id)
            var next = (id + 1) % max + 1
            $('#button_list td:nth-child(' + next + ') button').toggle("highlight", 500)
            startClock(next - 1)
            $(this).toggle("highlight", 500)
        })

        document.getElementById("button_list").insertCell(-1).appendChild(button)

    }

    function createNewClock(id, time) {
        var jc = $("#countdown_dashboard").clone(true)
        jc.css('visibility, "visible')
        jc.attr('id', 'countdown_dashboard_' + id.toString())

        document.getElementById("clock_list").insertCell(-1).appendChild(jc[0])

        jc.countDown({
            targetOffset: {
                'day': 		0,
                'month': 	0,
                'year': 	0,
                'hour': 	0,
                'min': 		time,
                'sec': 		0
            },
            onComplete: function() {
                var jo = $('#over')
                jo.text("Time is over for player " + (id + 1).toString())

                $("#dialog").dialog("open") }
        })

        //jc.stopCountDown();
    }

    function startClock(i) {
        $('#countdown_dashboard_' + i.toString()).startCountDown();
    }

    function stopClock(i) {
        $('#countdown_dashboard_' + i.toString()).stopCountDown();
    }


</script>

<h1>Game</h1>

<div>
    <table>
        <tr id="player_list">

        </tr>
        <tr id="clock_list">
        </tr>
        <tr id="button_list">
        </tr>
    </table>
</div>

<div id="dialog" title="Player lost">
    <p id="over">
        Time is over for player
    </p>
</div>

<div id="countdown_dashboard">

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
</body>
</html>

