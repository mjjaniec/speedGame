


function add_new_clock(caption, login, time) {
    var jc = $("#countdown_dashboard").clone(true)
    jc.css("display", "block")
    jc.addClass("clock")
    jc.attr("id", "countdown_dashboard_" + login)
    caption.append(jc)

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
            var player = window.players[login]

            if(!player.finished) {
                player.time = 0
                stopClock(player.login)
                stopRing(player.login)

                if(window.game_mode == "eot") {

                    window.lost_players_list.push(window.players_list[counter]);
                    window.players_list.remove(window.counter);
                    window.counter--;
                    $("#item_" + login).remove()
                    $('#right-arrow').text(">")

                } else {
                    window.lost_players_list.push(window.players_list[counter]);
                }

                var jplayer_list = $('#player_list')

                $('#player_list p').remove()

                for(var i = 0; i < players_list.length; i++) {
                    jplayer_list.append("<p style='font-size: 21px;font-weight: 200;'>" + players_list[(i + counter + 1) % players_list.length].login + "</p>")
                }

                var jo = $('#over')
                jo.text("Time is over for player " + login)
                $("#dialog").modal("show")
                player.finished = true
            }
        }
    })
}

function insert_new_player(player, time) {
    var item = $("<div class=\"item\" id=\"item_" + player.login + "\"></div>")
    var container = $("<div class=\"container\"><div>")
    var caption = $("<div class=\"carousel-caption\"></div>")
    var avatar = $("<img class=\"img-polaroid\"></img>")
    var login = $("<h2></h2>")
    login.text(player.login)
    var email = $("<p class='lead email'></p>")
    email.html("<i>" + player.email + "</i>")
    var ring = $("<audio controls height=\"100\" width=\"100\"></audio>")
    var source = $("<source type=\"audio/mpeg\">")
    var embed = $("<embed height=\"50\" width=\"100\">")

    ring.append(source).append(embed)
    item.append(container)
    container.append(caption)
    caption.append(avatar)
    caption.append(login)
    caption.append(email)

    avatar.attr("src","/upload?getfile=" + player.avatar)
    source.attr("src","/upload?getfile=" + player.ring)
    embed.attr("src","/upload?getfile=" + player.ring)
    ring.attr("id", "ring_" + player.login)
    $("#carousel_to_display").append(item)
    add_new_clock(caption, player.login, time)
    caption.append(ring)
}

function startClock(login) {
    console.log("starting clock " + login)

    var player = window.players[login]

    if(player.finished) {
        player.last_run = new Date()
    } else {
        $('#countdown_dashboard_' + login).startCountDown();
    }
}

function stopClock(login) {
    console.log("stopping clock " + login)

    var player = window.players[login]

    if(player.finished && player.last_run != undefined) {
        var diff = (new Date().getTime()- player.last_run.getTime())
        player.time += diff
    } else {
        $('#countdown_dashboard_' + login).stopCountDown();
    }


}

function getLeftTime(login) {
    return $('#countdown_dashboard_' + login).leftTime();
}

function startRing(login) {
    console.log("playing " + login)
    $('#ring_' + login)[0].load();
    $('#ring_' + login)[0].play();
}

function stopRing(login) {
    console.log("stopping ring " + login)
    $('#ring_' + login)[0].pause();
}


$(function() {
    $( "#players" ).sortable();
    $( "#players" ).disableSelection();
});
