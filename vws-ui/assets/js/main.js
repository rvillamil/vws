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
        document.getElementById("favorites-tvshows-content").innerHTML = "MOLA";
        // setMainContent("Getting tvshows async mode ...")

        //doRequest('GET', '/videopremieres', onSuccessGetShows, showType);
    } else {
        showAlertWindow("ERROR!! 'main-content' not exists " + showType)
    }
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
            newHtml += "<div class='show-container'" +
                " onmouseover='setAboutShow(" + '"' + shows[i]["title"] + '"' +
                "," + '"' + shows[i]["description"] + '"' +
                "," + '"' + shows[i]["sinopsis"] + '"' + ")'" +
                ">"
                // Filmaffinity Points
            if (shows[i]["filmaffinityPoints"] != null) {
                newHtml += "<div class='show-box-text'>" + " Filmaffinity " +
                    shows[i]["filmaffinityPoints"] + "</div>";
            }
            // Cover            
            newHtml += "<div class='show-box-img'>"
            newHtml += "<a href='" + shows[i]["urltodownload"] + "'>";
            newHtml += "<img src='" + shows[i]["urlwithCover"] + "'" +
                " alt='cover' " + "/>";
            newHtml += "</a>";
            newHtml += "<span class='tooltiptext'>" + shows[i]["title"] +
                "</span>";
            newHtml += "</div>";

            // Title
            newHtml += "<div class='show-box-title'>" + shows[i]["title"] +
                "</div>";
            // console.log ("session:'" + shows[i]["session"] + "'");
            // Session and Episode
            if (shows[i]["session"] != null) {
                newHtml += "<div class='show-box-text'>" + "Temporada " +
                    shows[i]["session"] + "-" + "Episodio " +
                    shows[i]["episode"] + "</div>";
            }
            // Quality
            var quality = shows[i]["quality"];
            //console.log ("Quality:'" + quality + "'");
            if (quality == null) {
                quality = "Undetermined";
            }
            newHtml += "<div class='show-box-text'>" + quality + "</div>";

            // Releasedate and filesize
            newHtml += "<div class='show-box-text'>" + shows[i]["releaseDate"] +
                " - " + shows[i]["fileSize"] + "</div>";

            newHtml += "</div>";
        } // End for
        document.getElementById(showType).innerHTML = newHtml;
        console.log("newHtml:" + newHtml);
    } // End try
    catch (err) {
        showAlertWindow("onSuccess method error: " + err.message + " in " + response.responseText);
        return;
    }
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