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
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(showType).style.display = "block";
    evt.currentTarget.className += " active";

    if (showType == "tabcontent-billboardfilms") {
        setHTMLAllTabContents("Getting billboard films async mode ...")
        doRequest('GET', '/billboardfilms', onSuccessGetShows, showType);

    } else if (showType == "tabcontent-videopremieres") {
        setHTMLAllTabContents("Getting video premieres async mode ...")
        doRequest('GET', '/videopremieres', onSuccessGetShows, showType);
    } else {
        showAlertWindow("ERROR!! 'tabcontent' not exists " + showType)
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
            setHTMLAllTabContents("Ha sucedido algun problema al obtener el recurso '" + resourcePath + "'");
            showAlertWindow("doRequest: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
        }
    };

    request.onloadstart = function() {
        setHTMLAllTabContents("Obteniendo '" + resourcePath + "' ...");
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
            newHtml += "<div class='show-container'>";

            // Filmaffinity Points
            if (shows[i]["filmaffinityPoints"] != null) {
                newHtml += "<div class='show-box-text'>" + " Filmaffinity " +
                    shows[i]["filmaffinityPoints"] + "</div>";
            }
            // Cover
            newHtml += "<div class='show-box-img'>";
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
        setHTMLTabContentByName(showType, newHtml);
    } // End try
    catch (err) {
        showAlertWindow("onSuccess method error: " + err.message + " in " + response.responseText);
        return;
    }
}


function setHTMLAllTabContents(htmlFragment) {
    //console.log("setHTMLAllTabContainers:" + htmlFragment);
    var tabcontent = document.getElementsByClassName("tabcontent");
    for (var i = 0; i < tabcontent.length; i++) {
        tabcontent[i].innerHTML = htmlFragment;
    }
}

function setHTMLTabContentByName(tabContent, htmlFragment) {
    document.getElementById(tabContent).innerHTML = htmlFragment;
}

// ------------ Ventanas modales -----------
function showAlertWindow(text) {
    console.log("showAlertWindow:" + text);
    alert(text);
}

function showModalWindow(text) {
    // Get the modal
    var modal = document.getElementById('modalWindow');
    modal.style.display = "block";
    document.getElementById('modal-text').innerHTML = text;

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}