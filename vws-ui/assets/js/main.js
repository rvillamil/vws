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
		if (this.readyState == 3 ) {
			console.log ("Processing ..")
		}
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

function onSuccess(request) {
	var htmlFragment = document.getElementById("films");
	try {
		var shows = JSON.parse(request.responseText);
		var html  = "<ul>";
		for (var i = 0; i < shows.length; i++) {
			html += '<li><a href="' + shows[i]["urltodownload"] + '">'
					+ shows[i]["title"] + "</a></li>";
		}
		html += "</ul>";
		htmlFragment.innerHTML = html

	} catch (err) {
		console.log(err.message + " in " + request.responseText);
		return;
	}
}

function loadBillboardfilms() {
	loadShows('/billboardfilms', onSuccess);
}
