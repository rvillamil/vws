/**
 * Show javascript 'alert' window and send text to console.log
 * @param {*} text The text to show
 */
function showAlertWindow(text) {
    console.log("showAlertWindow: '" + text + "'");
    alert(text);
}

/**
 * doPost function 
 * 
 * @param {*} url Target url
 * @param {*} body JSon with body
 * @param {*} JWToken Authorization token
 * @param {*} onSuccess HTTP 200
 * @param {*} onError HTTP Error
 */
function doPost(url, body, JWToken, onSuccess, onError) {
    console.log("doPost on '" + server + url + "' whith body '" + body + "'");
    var request = new XMLHttpRequest();
    request.withCredentials = true;
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            console.log("doPost : HTTP200 OK!");
            if (onSuccess != null) {
                onSuccess(this);
            }
        } else if (request.readyState === 4 && request.status === 201) {
            console.log("doPost : HTTP201 Resource created successfully!");
        } else if (this.readyState == 4 && request.status === 0) {
            showAlertWindow("ERROR!!! Backend down?");
        } else if (this.readyState == 4 && request.status === 401) {
            showAlertWindow("HTTP401: Authorization error !!");
        } else if (this.readyState == 4) {
            if (onError != null) {
                onError(this);
            } else {
                showAlertWindow("doPost ERROR: [readyState: " +
                    this.readyState + ", status: " + this.status +
                    ", statusText: '" + this.statusText + "']");
            }
        }
    };

    request.open("POST", server + url, true);
    if (JWToken != null) {
        request.setRequestHeader("Authorization", JWToken);
    }
    request.setRequestHeader("Content-type", "application/json");
    request.send(body);
}