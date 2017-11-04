function showMainPage() {
    if (isAuthenticated()) {
        console.log("showMainPage: User is autenticated. Redirecting to 'home.html' ...");
        window.location.assign("/app/home/home.html");
    } else {
        console.log("showMainPage: User is NOT autenticated. Redirecting to 'login.html'");
        window.location.assign("/app/login/login.html");
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

function getFormUsername() {
    return document.getElementById("form-login-uname").value
}

function getFormPassword() {
    return document.getElementById("form-login-passwd").value
}

function doLogin() {
    doPost(
        '/login',
        newJsonWithLoginDataForm(),
        null,
        function(request) {
            setAuthToken(request.getResponseHeader('Authorization'));
            setCurrentUsername(getFormUsername());
            showMainPage();
        },
        function(request) {
            deleteSession();
        });
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