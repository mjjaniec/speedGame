


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
    item.append(container)
    container.append(caption)
    caption.append(avatar)
    caption.append(login)
    caption.append(email)

    avatar.attr("src","/upload?getfile=" + player.avatar)
    $("#carousel_to_display").append(item)
    add_new_clock(caption, player.login, time)
}

function startClock(login) {
    console.log("starting " + login)
    $('#countdown_dashboard_' + login).startCountDown();
}

function stopClock(login) {
    console.log("stopping " + login)
    $('#countdown_dashboard_' + login).stopCountDown();
}

$(function() {
    $( "#players" ).sortable();
    $( "#players" ).disableSelection();
});
