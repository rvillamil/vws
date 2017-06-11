// curl -i -X POST -H "Content-Type:application/json" -d "{  \"title\" : \"Frodo2\" }" http://localhost:8080/favorites

function onSuccessGetFavorites(request) {
    try {
        var newHTML = "";
        var favorites = JSON.parse(request.responseText);
        var totalFavorites = favorites['page']['totalElements'];
        console.log("Total favorites recovered': " + totalFavorites);
        if (totalFavorites == 0) {
            newHTML = null;
        }
        var title = '';
        for (var i = 0; i < totalFavorites; i++) {
            title = favorites['_embedded']['favorites'][i]['title'];
            console.log("onSuccessGetFavorites - Processing favorite TV show title '" + title + "'");
            // doRequest( 'GET',  "/tvshows?name=" + shows[i]['title'], newHTMLTVShow, "box-with-tvshows-follow",true);
            doRequest(
                'GET',
                "/tvshows?name=" + title,
                onSuccessGetTVShow,
                onErrorGetTVShow,
                onFavoriteTVShowFound,
                onFavoriteTVShowNotFound);

        }
    } catch (err) {
        newHTML = null;
        showAlertWindow("onSuccessGetFavorites exception!: " + err.message + " in " + request.responseText);
    }
    return newHTML;
}

function onErrorGetFavorites(request) {
    showAlertWindow("Request problem! - onErrorGetFavorites: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
}

function onFavoritesTVShowsFound(resourcePath, htmlFragment) {
    console.log("onFavoritesTVShowsFound - resourcePath:'" + resourcePath + "'");
}

function onFavoritesTVShowsNotFound(resourcePath, htmlFragment) {
    showModalWindow("No tienes favoritos en la lista todavia. Busca las series que te interesa seguir ...");
}

function onFavoriteTVShowFound(resourcePath, htmlFragment) {
    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
}

function onFavoriteTVShowNotFound(resourcePath, htmlFragment) {
    console.log("Request problem! - onFavoriteTVShowNotFound: - resourcePath: " + resourcePath);
}