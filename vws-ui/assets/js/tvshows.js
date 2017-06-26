function onSuccessGetTVShow(request) {
    try {
        // Ojo! El servidor retorna por cada episodio un objeto Show. Nosotros lo pintamos como 
        // si fuese uno solo
        var shows = JSON.parse(request.responseText);
        var newHTML = " <div class='showtv-episodes-container'>";
        for (var i = 0; i < shows.length; i++) {
            console.log("Processing TV show '" + shows[i]['title'] + " T-" +
                shows[i]['session'] + " E-" + shows[i]['episode'] + "'");
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
}

function onTVShowFound(resourcePath, htmlFragment) {
    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
    console.log("TV Show found, the add to favorites list.. " + resourcePath);

    // No podemos aniadir, por lo que hacemos este truqui
    // ¨ER: https://developer.tizen.org/community/tip-tech/html5-local-storage
    //  https://www.kirupa.com/html5/storing_and_retrieving_an_array_from_local_storage.htm
    var strFavorite = resourcePath.split("=")[1];
    var jsonFavorite = JSON.stringify(strFavorite);
    var strStoragedFavorites = localStorage.getItem("favorites");
    var jsonArrayFavorites = null;
    console.log('strStoragedFavorites: ' + strStoragedFavorites);

    //ESTO NO VA BIEN
    // Si tenemos datos en el almacen...
    /*
    if (strStoragedFavorites) {
        jsonArrayFavorites = JSON.parse("[" + strStoragedFavorites + "]");
        console.log('jsonArrayFavorites: ' + jsonArrayFavorites);

        if (include(jsonArrayFavorites, jsonFavorite)) {
            console.log('El favorito existe: ' + jsonFavorite);
        } else {
            console.log('El favorito NO existe: ' + jsonFavorite);
            localStorage.setItem('favorites', jsonFavorite);
            doPost(
                "/favorites", { "title": strFavorite });
        }
    } else {
        localStorage.setItem('favorites', jsonFavorite);
        doPost(
            "/favorites", { "title": strFavorite });
    }
    */
    // Opcion nueva
    // retrieving our data and converting it back into an array
    var retrievedData = localStorage.getItem("favorites2");
    var tvShowsArray = [];
    if (retrievedData != null) {
        tvShowsArray = JSON.parse(retrievedData);
    }
    //    if (tvShowsArray.length > 0) {
    // storing our array as a string
    tvShowsArray.push(strFavorite);
    localStorage.setItem("favorites2", JSON.stringify(tvShowsArray));
    //  }
    /*
        var tvShows = ["Reservoir Dogs",
            "Pulp Fiction", "Jackie Brown", "Kill Bill", "Death Proof", "Inglourious Basterds"
        ];

        // retrieving our data and converting it back into an array
        var retrievedData = localStorage.getItem("favorites2");
        var tvShows2 = JSON.parse(retrievedData);
    */
    /*
        if (include(storedFavorites, jsonFavorite)) {
            console.log('El favorito existe: ' + jsonFavorite);
        } else {
            console.log('El favorito NO existe: ' + jsonFavorite);
        }
    */
    //if include (storedFavorites, )
    // Run request ..

}

function onTVShowNotFound(resourcePath, htmlFragment) {
    console.log("Request problem! - onTVShowNotFound: - resourcePath: " + resourcePath);
    showAlertWindow("Serie no encontrada : " + resourcePath);
}

function include(arr, obj) {
    for (i = 0; i < arr.length; ++i) {
        console.log("array: " + arr[i]);
    }
    //return (arr.indexOf(obj) != -1);
}