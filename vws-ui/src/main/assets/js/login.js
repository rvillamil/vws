function showMainPage() {
    if (isAuthenticated()) {
        console.log("showMainPage: User is autenticated. Redirect to main.html");
        window.location.assign("main.html");
    } else {
        console.log("showMainPage: User is NOT autenticated. Redirect to login.html");
        window.location.assign("login.html");
    }
}

function newJsonWithLoginDataForm() {
    var jsonStrData =
        '{ "userName": ' +
        '"' +
        document.getElementById("form-login-uname").value +
        '"' +
        "," +
        '"password": ' +
        '"' +
        document.getElementById("form-login-passwd").value +
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
            showMainPage();
        } else if (this.readyState == 4) {
            console.log("doLogin Error: [readyState: " +
                this.readyState + ", status: " + this.status + ", statusText: '" + this.statusText + "']");
            deleteAuthToken();
            showAlertWindow("Eeeehhh", "¿Que haces?", "");
        }
    };

    request.open("POST", server + "/login", true);
    request.setRequestHeader("Content-type", "application/json");
    request.send(body);
}

function getAuthToken() {
    return localStorage.getItem('Authorization');
}

function setAuthToken(authToken) {
    return localStorage.setItem('Authorization', authToken);
}

function isAuthenticated() {
    return (getAuthToken() != null) && (getAuthToken().length) > 0;
}

function deleteAuthToken() {
    localStorage.removeItem('Authorization');
}