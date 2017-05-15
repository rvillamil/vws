/**
 * Javascript functions
 */

/**
 * Use: loadShows("url");
 */
function loadShows(urlPath) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			showList = JSON.parse(this.responseText);

			for (show in showList) {
				//document.getElementById("show-list").innerHTML = "<li class=\"show-item\">"
				document.getElementById("films").innerHTML += show.name
			}

		} else {
			document.getElementById("films").innerHTML = "WTF!"
		}
	};
	xhttp.open("GET", "http://localhost:8080"+urlPath, true);
	xhttp.send();
}

function loadBillboardfilms(){
	loadShows('/billboardfilms')
}
