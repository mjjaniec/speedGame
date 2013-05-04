


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
            var jo = $('#over')
            jo.text("Time is over for player " + login)

            $("#dialog").dialog("open") }
    })
}

function insert_new_player(player, time) {
    var item = $("<div class=\"item\"></div>")
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
    $('#countdown_dashboard_' + login).startCountDown();
}

function stopClock(login) {
    console.log("stopping clock " + login)
    $('#countdown_dashboard_' + login).stopCountDown();
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
