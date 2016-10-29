console.log("register.js");

var $email = document.getElementById("email");
var $password1 = document.getElementById("password1");
var $password2 = document.getElementById("password2");
var $registrationForm = $("#registrationForm");

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

var emailProviders = [];
count = 0;
while (true) {
    var idSuffix = "emailProviderSuffix #"+count;
    var idName = "emailProviderName #"+count;
    var idHost = "emailProviderHost #"+count;
    var emailProvider ={
        suffix : $("meta[name='"+idSuffix+"']").attr("content"),
        name : $("meta[name='"+idName+"']").attr("content"),
        host : $("meta[name='"+idHost+"']").attr("content")
    }
    if (emailProvider.suffix == null) {
        break;
    }
    emailProviders.push(emailProvider);
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
            $registrationForm.children().remove();
            $registrationForm.append(showConfirm(email));
        },
        error: getErrorMsg
    });
}

function showConfirm (email) {
    var emailProvider = findEmailProvider(email);
    var $div = "<div class='container'><div class='row col s12'><div class='row col s8 offset-s2 white z-depth-3'><div class='row col s11 offset-s1 teal-text text-darken-2'>"+
        "<br/><h5>Регистрация пользователя: "+email+"</h5>"+
        "<p><h6 class='black-text'>На указаный вами адрес было отправлено письмо, для окончания регистрации перейдите по ссылке, указанной в письме.</h6>"+
        "<h6 class='black-text'>К своему почтовому ящику вы можете перейти по ссылке ниже:</h6><a href='"+emailProvider.host+"'>"+emailProvider.name+"</a>"+
        "</p></div><div class='row'><div class='col s4 offset-s2'>"+
        "<button class='waves-effect waves-light btn teal lighten-3' type='reset' onclick='location=\"/\"'><i class='material-icons right'>not_interested</i>Отменить</button>"+
        "</div><div class='col s4'>"+
        "<button class='waves-effect waves-light btn teal darken-2' type='submit' onclick='location=\""+emailProvider.host+"\"'><i class='material-icons right'>email</i>Почта</button>"+
        "</div><br/></div> </div></div></div>";
    return $div;
}

function findEmailProvider (email) {
    var emailSuffix = email.split('@')[1];
    for (var i=0;i<emailProviders.length;i++) {
        var emailProvider = emailProviders[i];
        if (emailProvider.suffix == emailSuffix)
            return emailProvider;
    }
    return null;
}