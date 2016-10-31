console.log("shopcart.js");

var shoppingCart;

$.ajax({
    url: "/shoppingcart",
    type: "GET",
    dataType: "json",
    contentType: "application/json",
    headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
    success: function (s) {
        shoppingCart = s;
        console.log("shoppingCart initialized: "+s);
    },
    error: getErrorMsg
});

function updateShoppingCart(newTotal) {
    var $divShoppingCart = $("#shoppingCart");
    var newTotalString = formatDecimal(newTotal,',','.') + " руб";
    var newHtml = "<a class='waves-effect waves-teal btn-flat no-margin white-text right'><i class='material-icons right'>shopping_cart</i>"+newTotalString+"</a>";
    $divShoppingCart.html(newHtml);
}

function updateMakePurchaseButton(purchases) {
    var $button = $("#makePuchaseButton");
    var newString = "Оплатить "+purchases.length+" на сумму "+formatDecimal(getTotal(purchases)*dollar_rate*amazon_percent, ',','.')+" руб";
    $button.html(newString);
}

function deletePurchaseCard (indexId) {
    var index = getIdFromElementId(indexId);
    var card = document.getElementById('purchaseCard #'+index);
    card.remove();
}

function updateCart (inputId) {
    var index = getIdFromElementId(inputId);
    var updatedQuantity = parseInt(document.getElementById(inputId).value);
    if (isNaN(updatedQuantity)) return;
    var data = {
        productid: index,
        quantity: updatedQuantity
    };

    $.ajax({
        url: "/update_purchase",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        data: JSON.stringify(data, null, "\t"),
        success: function (purchases) {
            if (updatedQuantity == 0) {
                console.log("Удалим "+index+"карточку продукта");
                deletePurchaseCard(index);
            }
            updateShoppingCart(getTotal(purchases)*dollar_rate*amazon_percent);
            updateMakePurchaseButton(purchases);
        },
        error: getErrorMsg
    });
}

function deleteProductFromCart (indexId) {
    var index = getIdFromElementId(indexId);
    var inputId = 'quantityItems #'+index;
    document.getElementById(inputId).value = 0;
    updateCart(inputId);
}


