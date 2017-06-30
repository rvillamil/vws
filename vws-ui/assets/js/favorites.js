function onSuccessGetFavorites(request) {
    try {
        // console.log("request.responseText: " + request.responseText);

        var newHTML = "";
        var favorites = JSON.parse(request.responseText);
        var totalFavorites = favorites.length;
        console.log("Total favorites recovered': " + totalFavorites);
        document.getElementById("number-tvshows-following").innerHTML = totalFavorites;
        if (totalFavorites == 0) {
            newHTML = null;
        }
        var title = '';
        for (var i = 0; i < favorites.length; i++) {
            title = favorites[i]['title'];
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

// -------------------- 
function onSuccessGetFavorite(request) {
    console.log("onSuccessGetFavorite: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");

    var newHTML = "OK"; //FIXME...!!! -- if request.status == 200 .. por ejemplo, puede ser la solucion
    return newHTML;
}

function onErrorGetFavorite(request) {
    console.log("onErrorGetFavorite: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");

}

function onFavoriteFound(resourcePath, htmlFragment) {
    console.log("onFavoriteFound - " + resourcePath);
}

function onFavoriteNotFound(resourcePath, htmlFragment) {
    console.log("onFavoriteNotFound - " + resourcePath);
    doRequest(
        'GET',
        "/tvshows?name=" + resourcePath.split('/')[2],
        onSuccessGetTVShow,
        onErrorGetTVShow,
        onTVShowFound,
        onTVShowNotFound);
}