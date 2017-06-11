function onSuccessGetShows(request) {
    try {
        var newHTML = "";
        var shows = JSON.parse(request.responseText);
        if (shows.length == 0) {
            newHTML = null;
        }
        for (var i = 0; i < shows.length; i++) {
            console.log("onSuccessGetShows - Processing show '" + shows[i]['title'] + "'");
            newHTML += newHTMLShow(shows[i], null);
        }
    } catch (err) {
        newHTML = null;
        showAlertWindow("onSuccessGetShows exception!: " + err.message + " in " + request.responseText);
    }

    return newHTML;
}

function onErrorGetShows(request) {
    showAlertWindow("Request problem! - onErrorGetShows: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
}

function onBillBoardFilmsFound(resourcePath, htmlFragment) {
    document.getElementById("billboardfilms-content").innerHTML += htmlFragment;
}

function onBillBoardFilmsNotFound(resourcePath, htmlFragment) {
    showAlertWindow("ERROR!: El servidor ha retornado 0 peliculas de cine! Revisa el log del servidor..");
}

function onVideoPremieresFound(resourcePath, htmlFragment) {
    document.getElementById("videopremieres-content").innerHTML += htmlFragment;
}

function onVideoPremieresNotFound(resourcePath, htmlFragment) {
    showAlertWindow("ERROR!: El servidor ha retornado 0 peliculas de video! Revisa el log del servidor..");
}