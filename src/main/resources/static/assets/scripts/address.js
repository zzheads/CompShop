console.log("address.js");

var firstName = document.getElementById("firstName");
var lastName = document.getElementById("lastName");
var country = document.getElementById("country");
var city = document.getElementById("city");
var zipCode = document.getElementById("zipCode");
var street = document.getElementById("street");
var house = document.getElementById("house");
var room = document.getElementById("room");
var phone = document.getElementById("phone");

var address;

function getAddress() {
    address = {
        firstName: firstName.value,
        lastName: lastName.value,
        country: country.value,
        city: city.value,
        zipCode: zipCode.value,
        street: street.value,
        house: house.value,
        room: room.value,
        phone: phone.value
    };
    return address;
}

function postAddress() {
    $.ajax({
        url: "/address",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(getAddress(), null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (result) {
            console.log(result);
        },
        error: getErrorMsg
    });
}
