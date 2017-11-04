function newJsonStringWithFavorite(title) {
    var jsonStrData =
        '{ "title": ' +
        '"' +
        title +
        '"' +
        "}";

    return jsonStrData;
}

function onSuccessGetFavorites(request) {
    try {
        // console.log("request.responseText: " + request.responseText);
        var newHTML = "";
        var favorites = JSON.parse(request.responseText);
        var totalFavorites = favorites.length;
        console.log("onSuccessGetFavorites - Total favorites found on DDBB: '" + totalFavorites + "'");
        document.getElementById("number-tvshows-following").innerHTML = totalFavorites;
        document.getElementById("box-with-tvshows-follow").innerHTML = "";
        if (totalFavorites == 0) {
            newHTML = null;
        }
        var title = '';
        for (var i = 0; i < favorites.length; i++) {
            title = favorites[i]['title'];
            // console.log("onSuccessGetFavorites - Processing from DDBB favorite TV show title '" + title + "'");
            doRequest(
                'GET',
                url_base_tvshows + title,
                onSuccessGetTVShow,
                onErrorGetTVShow,
                function(resourcePath, htmlFragment) {
                    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
                },
                function(resourcePath, htmlFragment) {
                    console.log("onSuccessGetFavorites - Elements not found! - resourcePath: " + resourcePath);
                });

        }
    } catch (err) {
        newHTML = null;
        showAlertWindow("onSuccessGetFavorites - EXCEPTION!: " + err.message + " in " + request.responseText);
    }
    return newHTML;
}

function onErrorGetFavorite(request) {
    console.log("onErrorGetFavorite - Favorite does not exist!");
    var name = document.getElementById("form-tvshows-name").value;
    doRequest(
        'GET',
        url_base_tvshows + name,
        onSuccessGetTVShow,
        onErrorGetTVShow,
        onTVShowFound,
        function(resourcePath, htmlFragment) {
            showAlertWindow("Serie no encontrada : " + resourcePath);
        });
}

function onTVShowFound(resourcePath, htmlFragment) {
    var jsonStringWithFavorite = newJsonStringWithFavorite(resourcePath.split("/")[3]);
    console.log("onTVShowFound - TV Show found. Sending request to add my favorite list '" + jsonStringWithFavorite + "'");
    doPost(url_base_favorites,
        jsonStringWithFavorite,
        getAuthToken(),
        null,
        null);
    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
}


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
        newHTML = null;
        console.log("onSuccessGetTVShow - EXCEPTION!: " + err.message + " in " + request.responseText);
    }
    return newHTML;
}

function onErrorGetTVShow(request) {
    console.log("onErrorGetTVShow  - Request problem! : [readyState: " +
        request.readyState + ", status: " + request.status + "']");
    var name = document.getElementById("form-tvshows-name").value;
    showAlertWindow("No se ha podido obtener el TVShow '" + name + "'")
}