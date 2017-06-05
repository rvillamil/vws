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
 * Replace de tabcontent with name 'showType' with HTML show list
 *
 * @param evt : 
 * @param showType: tabcontent_billboardfilms, tabcontent_videopremieres,..
 */
function openShows(evt, showType) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("main-content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(showType).style.display = "block";
    evt.currentTarget.className += " active";

    if (showType == "billboardfilms-content") {
        setMainContent("Getting billboard films async mode ...")
        doRequest('GET', '/billboardfilms', onSuccessGetShows, showType);

    } else if (showType == "videopremieres-content") {
        setMainContent("Getting video premieres async mode ...")
        doRequest('GET', '/videopremieres', onSuccessGetShows, showType);
    } else if (showType == "tvshows-content") {
        //document.getElementById("favorites-tvshows-content").innerHTML = "MOLA";
        // setMainContent("Getting tvshows async mode ...")
        //doRequest('POST', '/tvshows', onSuccessGetShows, showType);

        //doRequest('GET', '/videopremieres', onSuccessGetShows, showType);
    } else {
        showAlertWindow("ERROR!! 'main-content' not exists " + showType)
    }
}

function getTVShow() {
    resourcePath = '/tvshows';
    doRequest('GET', resourcePath, onSuccessGetShows, showType);
}

/**
 * Make a request aginst AWS shows API
 * 
 * @param operation: HTTP method. (e.g. GET, POST..)
 * @param resourcePath: Resource path (e.g. /billboardfilms)
 * @param onSuccessCFunction: on success request, callback function
 * @param showType: tabcontent_billboardfilms, tabcontent_videopremieres,..
 */
function doRequest(operation, resourcePath, onSuccessCFunction, showType) {
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
            onSuccessCFunction(this, showType);
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

/**
 * On request success event, then create new show list in the 'show-container'
 * div tag
 *
 * @param response: JSON with the show list
 * @param showType: tabcontent_billboardfilms, tabcontent_videopremieres or tabcontent_tvshows
 * @returns nothing
 */
function onSuccessGetShows(response, showType) {
    try {
        var newHtml = "";
        var shows = JSON.parse(response.responseText);

        for (var i = 0; i < shows.length; i++) {
            console.log("Processing show '" + shows[i]['title'] + "'")
            newHtml += newHTMLShow(shows[i]);
        } // End for
        document.getElementById(showType).innerHTML = newHtml;
        //console.log("newHtml:" + newHtml);
    } // End try
    catch (err) {
        showAlertWindow("onSuccess method error: " + err.message + " in " + response.responseText);
        return;
    }
}

/**
 * 
 * @param {*} jsonShow 
 */
function newHTMLShow(jsonShow) {
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
    // console.log ("session:'" + jsonShow["session"] + "'");
    // Session and Episode
    if (jsonShow["session"] != null) {
        newHtml += "<div class='show-box-text'>" + "Temporada " +
            jsonShow["session"] + "-" + "Episodio " +
            jsonShow["episode"] + "</div>";
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