console.log("login.js");

function getUsername () {
    return document.getElementById("username").value;
}

function getPassword () {
    return document.getElementById("password").value;
}

function getRememberMe () {
    return document.getElementById("remember-me").checked;
}

function submitLogin () {
    console.log(getUsername());
    console.log(getPassword());
    console.log(getRememberMe());
}

function emailAgain (email) {
    console.log("emailAgain "+email);

    $.ajax({
        url: "/sendagain",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(email, null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (result) {
            console.log(result);
            // $registrationForm.children().remove();
            // $registrationForm.append(showConfirm(email));
        },
        error: getErrorMsg
    });
}
