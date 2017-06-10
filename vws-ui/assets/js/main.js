/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */

/**
 * Global config
 */
var server = "http://localhost:8080";

/**
 * Replace de tabcontent with name 'htmlElementID' with HTML show list
 *
 * @param evt : MouseEvent
 * @param htmlElementID: billboardfilms-content, videopremieres-content,... HTML element to replace
 */
function getShows(evt, htmlElementID) {
    console.log("getShows with event: " + event + " for replacing html element: " + htmlElementID);
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
        doRequest('GET', '/billboardfilms', newHTMLShowList, htmlElementID, false);
    } else if (htmlElementID == "videopremieres-content") {
        doRequest('GET', '/videopremieres', newHTMLShowList, htmlElementID, false);
    } else if (htmlElementID == "tvshows-content") {
        console.log('d');
        doRequest('GET', '/favorites', newHTMLFavoritesTVShowList, "box-with-tvshows-follow", false)
    } else {
        showAlertWindow("ERROR!! 'main-content' not exists " + htmlElementID)
    }
}

/**
 * Make a request aginst AWS shows API
 * 
 * @param operation: HTTP method. (e.g. GET, POST..)
 * @param resourcePath: Resource path (e.g. /billboardfilms)
 * @param onSuccessCFunction: on success request, callback function
 * @param htmlElementID: billboardfilms-content, videopremieres-content,... The HTML ID element to replace
 * @param addContent: true for add content to htmlElementID. Otherwise, replace the content
 * @param onShowsFound: Callback function. if request was succesfully, onShowsFound is called when show or shows has been found
 * @param onShowsNotFound: Callback function. if request was succesfully, onShowsNotFound is called when show or show list is empty
 */
function doRequest(operation, resourcePath, onSuccessCFunction, htmlElementID, addContent, onShowsFound, onShowsNotFound) {
    console.log("doRequest to: " + resourcePath + " ( Method: " + operation + " )" +
        " the result replace the html element: " + htmlElementID + " (addcontent=" + addContent + ")");
    var modal = null;
    var request = new XMLHttpRequest();
    // onreadystatechange: Defines a function to be called when the readyState property changes
    request.onreadystatechange = function() {
        // console.log("doRequest: [readyState: " + this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "' ]");
        if (this.readyState == 4 && this.status == 200) {
            var response = onSuccessCFunction(this);
            if (response != null) {
                if (addContent == true) {
                    document.getElementById(htmlElementID).innerHTML += response;
                } else  {
                    document.getElementById(htmlElementID).innerHTML = response;
                }
                if (onShowsFound != null) {
                    onShowsFound(resourcePath);
                }
            } else {
                if (onShowsNotFound != null) {
                    onShowsNotFound(resourcePath);
                }
            }
        } else if (this.readyState == 4) {
            showAlertWindow("Request problem! - doRequest: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        }
    };

    request.onloadstart = function() {
        modal = showModalWindow("Request to URL", "   " + server + resourcePath, "Wait ...");
    };

    request.onloadend = function() {
        modal.style.display = "none";
    };

    request.open(operation, server + resourcePath, true);
    request.send();
}

/**
 * 
 * @param  resourcePath 
 * @param  stringData  
 */
function doPost(resourcePath, stringData) {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            var json = JSON.parse(request.responseText);
            console.log(json);
        } else if (this.readyState == 4) {
            showAlertWindow("Request problem! - doPost: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        }
    };

    var jSonBody = JSON.stringify(stringData);
    request.open("POST", server + resourcePath, true);
    request.setRequestHeader("Content-type", "application/json");
    request.send(jSonBody);
}


/**
 * Request to get tvshow from a 'Form' called 'form-tvshows-name'
 * @param event: form event 
 */
function sendTVShowFollowForm(event) {
    //console.log("sendTVShowFollowForm...");
    // to stop the form from submitting
    if (event.preventDefault) {
        event.preventDefault();
    }
    // The value of input text
    var name = document.getElementById("form-tvshows-name").value;
    // Run request ..
    doRequest(
        'GET',
        "/tvshows?name=" + name,
        newHTMLTVShow,
        "box-with-tvshows-follow",
        true,
        cbTVShowFound,
        cbTVShowNotFound);
    // You must return false to prevent the default form behavior
    return false;
}

/**
 * callback on tv show not found
 * @param resourcePath 
 */
function cbTVShowNotFound(resourcePath) {
    showAlertWindow("Serie no encontrada : " + resourcePath);
}


/**
 * callback on tv show found
 * @param resourcePath 
 */
function cbTVShowFound(resourcePath) {
    console.log("TV Show found, the add to favorites list.. " + resourcePath);
    // Run request ..
    doPost(
        "/favorites",
        'modern-family');
}


/**
 * Create HTML text with show list
 * @param response: String with JSON format with the show list
 * @returns html text with shows or null if shows list is empty
 */
function newHTMLShowList(response) {
    try {
        var newHTML = "";
        var shows = JSON.parse(response.responseText);
        if (shows.length == 0) {
            newHTML = null;
            showAlertWindow("ERROR!: El servidor ha retornado 0 shows! Revisa el log del servidor..");
        }
        for (var i = 0; i < shows.length; i++) {
            console.log("Processing show '" + shows[i]['title'] + "'");
            newHTML += newHTMLShow(shows[i], null);
        }
        // console.log("newHTMLShowList - newHTML:" + newHTML);
    } catch (err) {
        newHTML = null;
        showAlertWindow("newHTMLShowList method error: " + err.message + " in " + response.responseText);
    }

    return newHTML;
}


/**
 * Create HTML text with favorites tv show list
 * 
 * @param response: String with JSON format with the tv show list
 * @returns html text with tv shows or null if tv shows list is empty
 */
function newHTMLFavoritesTVShowList(response) {
    try {
        var newHTML = "";
        var shows = JSON.parse(response.responseText);
        if (shows.length == 0) {
            newHTML = null;
            showModalWindow("No tienes favoritos en la lista todavia. Busca las series que te interesa seguir ...");
        }
        for (var i = 0; i < shows.length; i++) {
            console.log("Processing favorite TV show '" + shows[i]['title'] + "'");
            doRequest(
                'GET',
                "/tvshows?name=" + shows[i]['title'],
                newHTMLTVShow,
                "box-with-tvshows-follow",
                true);
        }
        // console.log("newHTMLShowList - newHTML:" + newHTML);
    } catch (err) {
        newHTML = null;
        showAlertWindow("newHTMLFavoritesTVShowList method error: " + err.message + " in " + response.responseText);
    }
    return newHTML;
}

/**
 * Create HTML text with TV show info
 * 
 * @param response: String with JSON format with the tvshow list
 * @returns html text with TV show or null if there is no show
 */
function newHTMLTVShow(response) {
    try {
        // Ojo! El servidor retorna por cada episodio un objeto Show. Nosotros lo pintamos como 
        // si fuese uno solo
        var shows = JSON.parse(response.responseText);
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
        console.log("newHTMLTVShow - error: " + err.message + " in " + response.responseText);
        newHTML = null;
    }
    return newHTML;
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
    console.log("Titulo: " + jsonShow["title"] + "- descr" + jsonShow["description"] + "- sinopsis: " + jsonShow["sinopsis"]);


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