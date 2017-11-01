function showMainPage() {
    if (isAuthenticated()) {
        console.log("showMainPage: User is autenticated. Redirecting to 'home.html' ...");
        window.location.assign("app/home/home.html");
    } else {
        console.log("showMainPage: User is NOT autenticated. Redirecting to 'login.html'");
        window.location.assign("app/login/login.html");
    }
}

function newJsonWithLoginDataForm() {
    var jsonStrData =
        '{ "userName": ' +
        '"' +
        getFormUsername() +
        '"' +
        "," +
        '"password": ' +
        '"' +
        getFormPassword() +
        '"' +
        "}";

    return jsonStrData;
}

function doLogin() {
    var body = newJsonWithLoginDataForm();
    console.log("doLogin on '" + server + "/login'" + " whith body-->" + body);
    var request = new XMLHttpRequest();
    request.withCredentials = true;
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            setAuthToken(request.getResponseHeader('Authorization'));
            setCurrentUsername(getFormUsername());
            showMainPage();
        } else if (this.readyState == 4 && request.status === 0) {
            showAlertWindow("ERROR!!! Backend down?");
        } else if (this.readyState == 4 && request.status === 401) {
            showAlertWindow("Wrong user or invalid password");
        } else if (this.readyState == 4) {
            console.log("doLogin Error: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
            deleteSession();
            showAlertWindow("Post Request error: ", "Status: " + this.status, "");
        }
    };

    request.open("POST", server + "/login", true);
    request.setRequestHeader("Content-type", "application/json");
    request.send(body);
}

function doLogout() {
    console.log("doLogout '" + getCurrentUsername() + "'");
    deleteSession();
    showMainPage()
}

function getAuthToken() {
    return localStorage.getItem('Authorization');
}

function setAuthToken(authToken) {
    localStorage.setItem('Authorization', authToken);
}

function isAuthenticated() {
    return (getAuthToken() != null) && (getAuthToken().length) > 0;
}

function deleteSession() {
    localStorage.clear();
}

function setCurrentUsername(username) {
    localStorage.setItem('username', username);
}

function getCurrentUsername() {
    return localStorage.getItem('username');
}

function getFormUsername() {
    return document.getElementById("form-login-uname").value
}

function getFormPassword() {
    return document.getElementById("form-login-passwd").value
}