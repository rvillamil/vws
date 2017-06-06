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
        setMainContent("Getting billboard films async mode ...")
        doRequest('GET', '/billboardfilms', newHTMLShowList, htmlElementID);

    } else if (htmlElementID == "videopremieres-content") {
        setMainContent("Getting video premieres async mode ...")
        doRequest('GET', '/videopremieres', newHTMLShowList, htmlElementID);
    } else if (htmlElementID == "tvshows-content") {
        //document.getElementById("favorites-tvshows-content").innerHTML = "MOLA";
        // setMainContent("Getting tvshows async mode ...")
        //doRequest('POST', '/tvshows', onSuccessGetShows, htmlElementID);

        //doRequest('GET', '/videopremieres', onSuccessGetShows, htmlElementID);
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
 */
function doRequest(operation, resourcePath, onSuccessCFunction, htmlElementID) {
    console.log("doRequest to: " + resourcePath + " ( Method: " + operation + " )" +
        " the result replace the html element: " + htmlElementID);

    var request = new XMLHttpRequest();
    // onreadystatechange: Defines a function to be called when the readyState
    // property changes
    request.onreadystatechange = function() {
        // readyState:
        // - 0: request not initialized
        // - 1: server connection established
        // - 2: request received
        // - 3: processing request
        // - 4: request finished and response is ready
        //
        // status:
        // - 200 : OK ... https://www.w3schools.com/tags/ref_httpmessages.asp
        //
        // statusText:
        // - "OK", "not Found" ..Returns the status-text
        //console.log("doRequest: [readyState: " + this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "' ]");
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById(htmlElementID).innerHTML = onSuccessCFunction(this);
        } else if (this.readyState == 4) {
            setMainContent("Ha sucedido algun problema al obtener el recurso '" + resourcePath + "'");
            showAlertWindow("doRequest: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        }
    };

    request.onloadstart = function() {
        setMainContent("Obteniendo '" + resourcePath + "' ...");
    }

    request.open(operation, server + resourcePath, true);
    request.send();
}


function getTVShow() {
    resourcePath = '/tvshows';
    doRequest('GET', resourcePath, newHTMLTVShow, "tvshows-content");
}

/**
 * Create HTML text with TV show
 * @param response: String with JSON format with the tvshow list
 * @returns html text with TV show
 */
function newHTMLTVShow(response) {
    try {
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
        newHTML = newHTMLShow(shows[0], newHTML);
        // console.log("newHTMLTVShow - newHTML:" + newHTML);
    } catch (err) {
        showAlertWindow("newHTMLTVShow method error: " + err.message + " in " + response.responseText);
        return;
    }
    return newHTML;
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
 * Create HTML text with show
 * 
 * @param jsonShow: JSON Object, with the show
 * @param htmlWithEpisodes: HTML text with episodes or null show is not a TV Show
 * @return html text with the show
 */
function newHTMLShow(jsonShow, htmlWithEpisodes) {
    var newHtml = "";
    newHtml += "<div class='show-container'" +
        " onmouseover='setAboutShow(" + '"' + jsonShow["title"] + '"' +
        "," + '"' + jsonShow["description"] + '"' +
        "," + '"' + jsonShow["sinopsis"] + '"' + ")'" +
        ">"
        // Filmaffinity Points
    if (jsonShow["filmaffinityPoints"] != null) {
        newHtml += "<div class='show-box-text'>" + " Filmaffinity " +
            jsonShow["filmaffinityPoints"] + "</div>";
    }
    // Cover            
    newHtml += "<div class='show-box-img'>"
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
    newHtml += "<div class='show-box-text'>" + quality + "</div>";

    // Releasedate and filesize
    newHtml += "<div class='show-box-text'>" + jsonShow["releaseDate"] +
        " - " + jsonShow["fileSize"] + "</div>";

    // Add html with episode list
    if (htmlWithEpisodes != null) {
        newHtml += htmlWithEpisodes;
    }
    newHtml += "</div>";

    return newHtml;
}

/**
 * 
 * @param {*} title 
 * @param {*} description 
 * @param {*} sinopsis 
 */
function setAboutShow(title, description, sinopsis) {
    document.getElementById("about-show-title").innerHTML = title;
    document.getElementById("about-show-description").innerHTML = description;
    document.getElementById("about-show-sinopsis").innerHTML = sinopsis;
}

/**
 * 
 * @param {*} htmlFragment 
 */
function setMainContent(htmlFragment) {
    //console.log("setHTMLAllTabContainers:" + htmlFragment);
    var tabcontent = document.getElementsByClassName("main-content");
    for (var i = 0; i < tabcontent.length; i++) {
        tabcontent[i].innerHTML = htmlFragment;
    }
}