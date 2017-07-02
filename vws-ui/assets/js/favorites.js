function onSuccessGetFavorites(request) {
    try {
        // console.log("request.responseText: " + request.responseText);

        var newHTML = "";
        var favorites = JSON.parse(request.responseText);
        var totalFavorites = favorites.length;
        console.log("Total favorites recovered': " + totalFavorites);
        document.getElementById("number-tvshows-following").innerHTML = totalFavorites;
        document.getElementById("box-with-tvshows-follow").innerHTML = "";
        if (totalFavorites == 0) {
            newHTML = null;
        }
        var title = '';
        for (var i = 0; i < favorites.length; i++) {
            title = favorites[i]['title'];
            // console.log("onSuccessGetFavorites - Processing favorite TV show title '" + title + "'");
            doRequest(
                'GET',
                url_base_tvshows + title,
                onSuccessGetTVShow,
                onErrorGetTVShow,
                onFavoriteTVShowFound,
                onFavoriteTVShowNotFound);

        }
    } catch (err) {
        newHTML = null;
        showAlertWindow("onSuccessGetFavorites exception!: " + err.message + " in " + request.responseText);
    }

    //    modal.style.display = "none";

    return newHTML;
}

function onErrorGetFavorites(request) {
    console.log("onErrorGetFavorites: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
    showAlertWindow("No tienes favoritos en la lista todavia. Busca las series que te interesa seguir ...");
}

function onFavoritesTVShowsFound(resourcePath, htmlFragment) {
    console.log("onFavoritesTVShowsFound - resourcePath:'" + resourcePath + "'");
}

function onFavoritesTVShowsNotFound(resourcePath, htmlFragment) {
    console.log("Request problem! - onFavoritesTVShowsNotFound: - resourcePath: " + resourcePath);
}

// -------------------
function onFavoriteTVShowFound(resourcePath, htmlFragment) {
    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
}

function onFavoriteTVShowNotFound(resourcePath, htmlFragment) {
    console.log("Request problem! - onFavoriteTVShowNotFound: - resourcePath: " + resourcePath);
}

// -------------------- 
function onSuccessGetFavorite(request) {
    console.log("El favorito ya existe! - onSuccessGetFavorite: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
    return "OK"; // --> fuerza a que pase por el handler onFavoriteAlreadyExists
}

function onErrorGetFavorite(request) {
    console.log("El favorito no existe! - onErrorGetFavorite: [readyState: " +
        request.readyState + ", status: " + request.status + ", statusText: '" + request.statusText + "']");
    var name = document.getElementById("form-tvshows-name").value;
    doRequest(
        'GET',
        url_base_tvshows + name,
        onSuccessGetTVShow,
        onErrorGetTVShow,
        onTVShowFound,
        onTVShowNotFound);
}

function onFavoriteAlreadyExists(resourcePath, htmlFragment) {
    console.log("onFavoriteAlreadyExists - " + resourcePath);
}

function onFavoriteNotExists(resourcePath, htmlFragment) {
    console.log("onFavoriteNotExists - " + resourcePath);
}