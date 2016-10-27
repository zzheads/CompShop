console.log("register.js");

var $email = document.getElementById("email");
var $password1 = document.getElementById("password1");
var $password2 = document.getElementById("password2");

var registeredUsers = [];
var count = 0;
while (true) {
    var name = "registeredUsername #"+count;
    var username = $("meta[name='"+name+"']").attr("content");
    if (username == null) {
        break;
    }
    registeredUsers.push(username);
    count++;
}

function isFree (username) {
    for (var i=0;i<registeredUsers.length;i++) {
        if (registeredUsers[i].toLocaleUpperCase() == username.toLocaleUpperCase())
            return false;
    }
    return true;
}

function isOk (password) {
    return true;
}

function registerNewUser () {
    var email = $email.value;
    var password1 = $password1.value;
    var password2 = $password2.value;

    if (!isFree(email)) {
        printFlashMessage("Данный e-mail уже зарегистрирован в базе", "failure");
        return;
    }
    if (password1.toLocaleUpperCase() != password2.toLocaleUpperCase()) {
        printFlashMessage("Пароли не совпадают", "failure");
        return;
    }
    if (!isOk(password1)) {
        printFlashMessage("Недопустимый пароль", "failure");
        return;
    }

    var user = {
        username: email,
        password: password1
    };

    $.ajax({
        url: "/register",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(user, null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (result) {
            console.log(result);
        },
        error: getErrorMsg
    });
}