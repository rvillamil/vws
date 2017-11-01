function onSuccessGetTVShow(request) {
    try {
        // Ojo! El servidor retorna por cada episodio un objeto Show. Nosotros lo pintamos como 
        // si fuese uno solo
        var shows = JSON.parse(request.responseText);
        var newHTML = " <div class='showtv-episodes-container'>";
        for (var i = 0; i < shows.length; i++) {
            //console.log("Processing TV show '" + shows[i]['title'] + " T-" + shows[i]['session'] + " E-" + shows[i]['episode'] + "'");
            newHTML += "<a href='" + shows[i]["urltodownload"] + "'>";
            newHTML += "<p>Episodio " + shows[i]['episode'] + "</p>";
            newHTML += "</a>";
        }
        newHTML += "</div>";
        if (shows.length > 0) {
            newHTML = newHTMLShow(shows[0], newHTML);
        } else {
            newHTML = null;
        }
    } catch (err) {
        console.log("onSuccessGetFavorites exception!: " + err.message + " in " + request.responseText);
        newHTML = null;
    }
    return newHTML;
}

function onErrorGetTVShow(request) {
    console.log("Request problem! - onErrorGetTVShow: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
    var name = document.getElementById("form-tvshows-name").value;
    showAlertWindow("No se ha podido obtener el TVShow '" + name + "'")
}


function onErrorOnTVShowFound(request) {
    showAlertWindow("Post Request error: ", "Status: " + this.status, "");
}

function onTVShowFound(resourcePath, htmlFragment) {
    var jsonStringWithFavorite = newJsonStringWithFavorite(resourcePath.split("/")[3]);

    console.log("TV Show found: adding to favorites list:'" + jsonStringWithFavorite + "'");

    doPost(url_base_favorites,
        jsonStringWithFavorite,
        getAuthToken(),
        null,
        null,
        onErrorOnTVShowFound);

    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
}

function onTVShowNotFound(resourcePath, htmlFragment) {
    console.log("Request problem! - onTVShowNotFound: - resourcePath: " + resourcePath);
    showAlertWindow("Serie no encontrada : " + resourcePath);
}

function newJsonStringWithFavorite(title) {
    var jsonStrData =
        '{ "title": ' +
        '"' +
        title +
        '"' +
        "}";

    return jsonStrData;
}