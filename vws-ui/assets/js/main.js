/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */

/**
 * Use: loadShows("url");
 */
function loadShows(urlPath, cFunction) {
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
		if (this.readyState == 0 ) {
			console.log ("WTF! Server not running?")
		}

		/*
		 * if (this.readyState == 3 ) { console.log ("Processing ..") }
		 */

		if (this.readyState == 4 && this.status == 200) {
			onSuccess(this);
		}
	};

	request.onerror = function() {
		console.log("Error");
	};

	request.open("GET", "http://localhost:8080" + urlPath, true);
	request.send();
}


/*
SHOW:
"title": "string", "urltodownload": "string",  "urlwithCover": "string"
"session": "string", "episode": "string",
"releaseDate": "string", "quality": "string", "fileSize": "string"
"filmaffinityPoints": 0,
"description": "string",
"sinopsis": "string",
*/
function onSuccess(request) {
	var htmlFragment = document.getElementById("id-shows-section");
	try {
		var shows = JSON.parse(request.responseText);

		var newHtml  ="";
		for (var i = 0; i < shows.length; i++) {
			console.log("Processing show " + shows[i]['title'])

			newHtml += "<div class='show-box'>";
			newHtml += 	 "<div class='show-box-element'>" + " Filmaffinity "+ shows[i]["filmaffinityPoints"] + "</div>";
			newHtml += 	 "<a class='show-box-element' href='"+ shows[i]["urltodownload"] + "'>";
			newHtml += 	    "<img src='" + shows[i]["urlwithCover"] + "'" + " alt='cover' " + " style='width: 104px; height: 142px;'" + "/>";
			newHtml +=    "</a>";
			newHtml += 	  "<div class='show-box-title'>" + shows[i]["title"];
			newHtml +=	     "<span class='tooltiptext'>" + shows[i]["title"] + "</span>";
			newHtml +=    "</div>";
			// Solo las series tiene estos valores
			if (shows[i]["session"] != null){
				newHtml +=    "<div class='show-box-element'>" + "Temporada " + shows[i]["session"] + "-" + "Episodio " + shows[i]["episode"] + "</div>";
			}
			newHtml += 	  "<div class='show-box-element'>" + shows[i]["quality"] + "</div>";
			newHtml += 	  "<div class='show-box-element'>" + shows[i]["releaseDate"] + " - " + shows[i]["fileSize"] + "</div>";
			newHtml += "</div>";
		}

		console.log (newHtml)

		htmlFragment.innerHTML = newHtml;
	} catch (err) {
		console.log(err.message + " in " + request.responseText);
		return;
	}
}

function loadBillboardfilms() {
	loadShows('/billboardfilms', onSuccess);
}
