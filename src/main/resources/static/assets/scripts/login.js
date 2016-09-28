function getUsername () {
    return document.getElementById("username").value;
}

function getPassword () {
    return document.getElementById("password").value;
}

function getRememberMe () {
    return document.getElementById("remember").checked;
}

function submitLogin () {
    console.log(getUsername());
    console.log(getPassword());
    console.log(getRememberMe());
}
