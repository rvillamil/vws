/**
 * Show javascrip 'alert' window
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
 * @param {*} onSuccessNewResource  HTTP 201
 * @param {*} onError HTTP Error
 */
function doPost(url, body, JWToken, onSuccess, onSuccessNewResource, onError) {
    console.log("doPost on '" + server + url + "' whith body '" + body + "'");
    var request = new XMLHttpRequest();
    request.withCredentials = true;
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            if (onSuccess != null) {
                onSuccess(this);
            }
        } else if (request.readyState === 4 && request.status === 201) {
            console.log("doPost : 201 Resource created successfully");
            if (onSuccessNewResource != null) {
                onSuccessNewResource(this);
            }
        } else if (this.readyState == 4 && request.status === 0) {
            showAlertWindow("ERROR!!! Backend down?");
        } else if (this.readyState == 4 && request.status === 401) {            
            showAlertWindow("HTTP401: Authorization error !!");
        } else if (this.readyState == 4) {            
            showAlertWindow("doPost ERROR: [readyState: " + 
                            this.readyState + ", status: " + this.status +
                             ", statusText: '" + this.statusText + "']");
            if (onError != null) {
                onError(this);
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