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
    console.log("TV Show found: adding to favorites list.. " + resourcePath);
    document.getElementById("box-with-tvshows-follow").innerHTML += htmlFragment;
    
    // FIXME 00 : Solucionar la carga de favotiros: 	
    	// Al entrar en la sección de favoritos vamos al servidor , los cargamos y los mostramos
    	// Para añadir favoritos hacemos los siguiente: buscamos con la lista de favoritos para ver si ya lo tenemos . Si no lo tenemos , buscamos en el portal de torrents si existe el tvshow. Si existe lo añadimos a favoritos
    var strFavorite = resourcePath.split("=")[1];
    var jsonFavorite = JSON.stringify(strFavorite);       
    
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