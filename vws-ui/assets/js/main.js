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
 */
function doRequest(operation, resourcePath, onSuccessCFunction, htmlElementID, addContent) {
    console.log("doRequest to: " + resourcePath + " ( Method: " + operation + " )" +
        " the result replace the html element: " + htmlElementID + " (addcontent=" + addContent + ")");
    var modal = null;
    var request = new XMLHttpRequest();
    // onreadystatechange: Defines a function to be called when the readyState property changes
    request.onreadystatechange = function() {
        // console.log("doRequest: [readyState: " + this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "' ]");
        if (this.readyState == 4 && this.status == 200) {
            if (addContent == true) {
                document.getElementById(htmlElementID).innerHTML += onSuccessCFunction(this);
            } else  {
                document.getElementById(htmlElementID).innerHTML = onSuccessCFunction(this);
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
        true);
    // You must return false to prevent the default form behavior
    return false;
}


/**
 * Create HTML text with show list
 * @param response: String with JSON format with the show list
 * @returns html text with shows
 */
function newHTMLShowList(response) {
    try {
        var newHTML = "";
        var shows = JSON.parse(response.responseText);
        if (shows.length == 0) {
            showAlertWindow("newHTMLShowList: Server returns 0 shows!");
        }
        for (var i = 0; i < shows.length; i++) {
            console.log("Processing show '" + shows[i]['title'] + "'");
            newHTML += newHTMLShow(shows[i], null);
        }
        // console.log("newHTMLShowList - newHTML:" + newHTML);
    } catch (err) {
        showAlertWindow("newHTMLShowList method error: " + err.message + " in " + response.responseText);
        return;
    }
    return newHTML;
}

/**
 * 
 * @param response:
 */
function newHTMLFavoritesTVShowList(response) {
    try {
        var newHTML = "";
        var shows = JSON.parse(response.responseText);
        if (shows.length == 0) {
            showAlertWindow("newHTMLFavoritesTVShowList: Server returns 0 TV favorite shows!");
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
        showAlertWindow("newHTMLFavoritesTVShowList method error: " + err.message + " in " + response.responseText);
        return;
    }
    return newHTML;
}

/**
 * Create HTML text with TV show info
 * @param response: String with JSON format with the tvshow list
 * @returns html text with TV show
 */
function newHTMLTVShow(response) {
    try {
        // Ojo! El servidor retorna por cada episodio un objeto Show. Nosotros lo pintamos como 
        // si fuese uno solo
        var shows = JSON.parse(response.responseText);
        if (shows.length == 0) {
            showAlertWindow("newHTMLTVShow: Server returns 0 TV Shows");
        }
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
        }
        // console.log("newHTMLTVShow - newHTML:" + newHTML);
    } catch (err) {
        showAlertWindow("newHTMLTVShow method error: " + err.message + " in " + response.responseText);
        return;
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