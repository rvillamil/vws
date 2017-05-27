/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */

/**
 * Replace de tabcontent with name 'showType' with HTML show list
 *
 * @param evt
 * @param showType:
 *            tabcontent_billboardfilms, tabcontent_videopremieres,..
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

	if (showType == "tabcontent_billboardfilms") {
		console.log("Getting billboard films async mode ...")
		loadShows('/billboardfilms', onSuccess, showType);

	} else if (showType == "tabcontent_videopremieres") {
		console.log("Getting video premieres async mode ...")
		loadShows('/videopremieres', onSuccess, showType);
	} else {
		setTextStatusBar("ERROR!! 'tabconten' not exists" + showType)
	}
}

/**
 * Use: loadShows("url", "callback function");
 */
function loadShows(urlPath, cFunction, showType) {
	var request = new XMLHttpRequest();
	// onreadystatechange: Defines a function to be called when the readyState
	// property changes
	request.onreadystatechange = function() {
		// readyState:
		// - 0: request not initialized
		// - 1: server connection established
		// - 2: request received
		// - 3: processing request
		// - 4 request finished and response is ready
		//
		// status:
		// - 200 : OK ... https://www.w3schools.com/tags/ref_httpmessages.asp
		//
		// statusText:
		// - "OK", "not Found" ..Returns the status-text
		if (this.readyState == 0) {
			setTextStatusBar("WTF! Server not running?");
		}

		/*
		 * if (this.readyState == 3 ) { console.log ("Processing ..") }
		 */

		if (this.readyState == 4 && this.status == 200) {
			onSuccess(this, showType);
		}
	};

	request.onerror = function() {
		setTextStatusBar("Error --> readyState: " + this.readyState
				+ " - status: " + this.status + " - statusText "
				+ this.statusText);
	};

	request.open("GET", "http://localhost:8080" + urlPath, true);
	request.send();
}

/**
 * On request success event, then create new show list in the 'show-container'
 * div tag
 *
 * @param response
 *            JSON with the show list
 * @param showType
 *            tabcontent_billboardfilms, tabcontent_videopremieres or
 *            tabcontent_tvshows
 *
 * @returns nothing
 */
function onSuccess(response, showType) {
	try {
		var newHtml = "";
		var shows = JSON.parse(response.responseText);

		for (var i = 0; i < shows.length; i++) {
			console.log("Processing show " + shows[i]['title'])
			newHtml += "<div class='show-container'>";

			// Filmaffinity Points
			if (shows[i]["filmaffinityPoints"] != null) {
				newHtml += "<div class='show-box-text'>" + " Filmaffinity "
						+ shows[i]["filmaffinityPoints"] + "</div>";
			}
			// Cover
			newHtml += "<div class='show-box-img'>";
			newHtml += "<a href='" + shows[i]["urltodownload"] + "'>";
			newHtml += "<img src='" + shows[i]["urlwithCover"] + "'"
					+ " alt='cover' " + "/>";
			newHtml += "</a>";
			newHtml += "<span class='tooltiptext'>" + shows[i]["title"]
					+ "</span>";
			newHtml += "</div>";

			// Title
			newHtml += "<div class='show-box-title'>" + shows[i]["title"]
					+ "</div>";
			// console.log ("session:'" + shows[i]["session"] + "'");
			// Session and Episode
			if (shows[i]["session"] != null) {
				newHtml += "<div class='show-box-text'>" + "Temporada "
						+ shows[i]["session"] + "-" + "Episodio "
						+ shows[i]["episode"] + "</div>";
			}
			// Quality
			var quality = shows[i]["quality"];
			//console.log ("Quality:'" + quality + "'");
			if ( quality == null) {
				quality ="Undetermined";
			}
			newHtml += "<div class='show-box-text'>" + quality + "</div>";
			// Releasedate and filesize
			newHtml += "<div class='show-box-text'>" + shows[i]["releaseDate"]
					+ " - " + shows[i]["fileSize"] + "</div>";

			newHtml += "</div>";

		} // End for
		setHTMLInTabContainer(showType, newHtml);
	} // End try
	catch (err) {
		setTextStatusBar(err.message + " in " + response.responseText);
		return;
	}
	// console.log("newhtml with shows: " + newHtml)
}

function setTextStatusBar(text) {
	console.log("Status Bar text: " + text);
	document.getElementById('statusbar').innerHTML = text;
}

function setHTMLInTabContainer(tabContainer, htmlFragment) {
	document.getElementById(tabContainer).innerHTML = htmlFragment;
}