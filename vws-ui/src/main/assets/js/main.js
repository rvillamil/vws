/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */


/**
 * Redirect to login.html page if user is not autenticated
 */
function validateAutentication() {
    if (!isAuthenticated()) {
        console.log("validateAutentication: User is NOT autenticated. Redirecting to login.html ..");
        window.location.assign("login.html");
    } else {
        // Cargamos los favoritos
        getShows(event, 'tvshows-content');
    }
}

/**
 * Replace de tabcontent with name 'htmlElementID' with HTML show list
 *
 * @param evt : MouseEvent
 * @param htmlElementID: billboardfilms-content, videopremieres-content,... HTML element to replace
 */
function getShows(evt, htmlElementID) {
    console.log("Loading content for replacing html element: '" + htmlElementID + "'");

    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("main-content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(htmlElementID).style.display = "block";
    evt.currentTarget.className += " active";

    if (htmlElementID == "billboardfilms-content") {
        doRequest(
            'GET',
            url_base_billboard_films,
            onSuccessGetShows,
            onErrorGetShows,
            onBillBoardFilmsFound,
            onBillBoardFilmsNotFound);

    } else if (htmlElementID == "videopremieres-content") {
        doRequest(
            'GET',
            url_base_video_premieres,
            onSuccessGetShows,
            onErrorGetShows,
            onVideoPremieresFound,
            onVideoPremieresNotFound);

    } else if (htmlElementID == "tvshows-content") {
        doRequest(
            'GET',
            url_base_favorites,
            onSuccessGetFavorites,
            onErrorGetFavorites,
            onFavoritesTVShowsFound,
            onFavoritesTVShowsNotFound);
    } else {
        showAlertWindow("ERROR!! 'main-content' not exists " + htmlElementID)
    }
    setCurrentUserInTopBar();

}


function doRequest(operation, resourcePath, onSuccess, onError, onElementsFound, onElementsNotFound, async) {
    // por defecto asincronas
    async_request = async;
    if (async == null) {
        async_request = true;
    }

    console.log("doRequest to: " + resourcePath + " ( operation: " + operation + " )" + " - Async: " + async_request);
    var modal = null;
    var request = new XMLHttpRequest();

    request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var htmlFragment = onSuccess(this);
            if (htmlFragment != null) {
                onElementsFound(resourcePath, htmlFragment)
            } else {
                onElementsNotFound(resourcePath, htmlFragment);
            }
        } else if (this.readyState == 4 && request.status === 0) {
            showAlertWindow("ERROR!!! Backend down?");
        } else if (this.readyState == 4) {
            onError(this);
        }
    };
    request.onloadstart = function() {
        modal = showModalWindow("Espere por favor..", "Obteniendo las peliculas del servidor..", "");
    };
    request.onloadend = function() {
        modal.style.display = "none";
    };

    request.open(operation, server + resourcePath, async_request);
    request.setRequestHeader("Authorization", getAuthToken());
    request.send();
}

/**
 * 
 * @param  resourcePath 
 * @param  stringData  
 */
function doPost(resourcePath, body) {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 201) {
            console.log("doPost OK!: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        } else if (this.readyState == 4 && request.status === 0) {
            showAlertWindow("ERROR!!! Backend down?");
        } else if (this.readyState == 4) {
            showAlertWindow("doPost: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        }
    };

    request.open("POST", server + resourcePath, true);
    request.setRequestHeader("Content-type", "application/json");
    request.setRequestHeader("Authorization", getAuthToken());
    request.send(body);
}

/**
 * Request to get tvshow from a 'Form' called 'form-tvshows-name'
 * @param event: form event 
 */
function sendFormWithTVShowFollow(event) {
    //console.log("sendTVShowFollowForm...");
    // to stop the form from submitting
    if (event.preventDefault) {
        event.preventDefault();
    }
    // The value of input text
    var name = document.getElementById("form-tvshows-name").value;
    // Para añadir favoritos hacemos los siguiente: buscamos con la lista 
    // de favoritos para ver si ya lo tenemos . Si no lo tenemos , 
    // buscamos en el portal de torrents si existe el tvshow. Si existe lo añadimos a favoritos
    doRequest(
        'GET',
        "/api/favorites/" + name,
        onSuccessGetFavorite,
        onErrorGetFavorite,
        onFavoriteAlreadyExists,
        onFavoriteNotExists);

    // You must return false to prevent the default form behavior
    return false;
}

/**
 * Create HTML text with show
 * 
 * @param jsonShow: JSON Object, with the show
 * @param htmlWithEpisodeLinks: HTML text with episode links or null show is not a TV Show
 * @return html text with the show
 */
function newHTMLShow(jsonShow, htmlWithEpisodeLinks) {
    var newHtml = "";
    newHtml += "<div class='show-container'" +
        " onmouseover='setAboutShow(" + '"' + jsonShow["title"] + '"' +
        "," + '"' + jsonShow["description"] + '"' +
        "," + '"' + jsonShow["sinopsis"] + '"' + ")'" +
        ">";
    // console.log("Titulo: " + jsonShow["title"] + "- descr" + jsonShow["description"] + "- sinopsis: " + jsonShow["sinopsis"]);


    // Filmaffinity Points
    if (jsonShow["filmaffinityPoints"] != null) {
        newHtml += "<div class='show-box-text'>" + " Filmaffinity " +
            jsonShow["filmaffinityPoints"] + "</div>";
    }
    // Cover            
    newHtml += "<div class='show-box-img'>";
    newHtml += "<a href='" + jsonShow["urltodownload"] + "'>";
    newHtml += "<img src='" + jsonShow["urlwithCover"] + "'" +
        " alt='cover' " + "/>";
    newHtml += "</a>";
    newHtml += "<span class='tooltiptext'>" + jsonShow["title"] +
        "</span>";
    newHtml += "</div>";

    // Title
    newHtml += "<div class='show-box-title'>" + jsonShow["title"] +
        "</div>";

    // Session
    if (jsonShow["session"] != null) {
        newHtml += "<div class='show-box-session'>" + "Temporada " +
            jsonShow["session"] + "</div>";
    }
    // Quality
    var quality = jsonShow["quality"];
    //console.log ("Quality:'" + quality + "'");
    if (quality == null) {
        quality = "Undetermined";
    }
    newHtml += "<div class='show-box-quality'>" + quality + "</div>";

    // Releasedate and filesize
    newHtml += "<div class='show-box-text'>" + jsonShow["releaseDate"] +
        " - " + jsonShow["fileSize"] + "</div>";

    // Add html with episode list
    if (htmlWithEpisodeLinks != null) {
        newHtml += htmlWithEpisodeLinks;
    }
    newHtml += "</div>";

    return newHtml;
}

/**
 * Fill 'about show' section
 * 
 * @param title: The show title 
 * @param description The show description (actor, length..)
 * @param sinopsis The show sinopsis
 */
function setAboutShow(title, description, sinopsis) {
    document.getElementById("about-show-title").innerHTML = "<p>Titulo</p>" + title;
    document.getElementById("about-show-description").innerHTML = "<p>Descripcion</p>" + description;
    document.getElementById("about-show-sinopsis").innerHTML = "<p>Sinopsis</p>" + sinopsis;
}

function setCurrentUserInTopBar() {
    document.getElementById("username-txt").innerHTML = getCurrentUsername();
}