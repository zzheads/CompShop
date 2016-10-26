var allProducts = [];
var productsList = $("#productsList");
var dollar_rate = getDollarRate();
var amazon_percent = getAmazonPercent();
console.log("index.js");

function getAllProducts() {
    $.ajax({
        url: "/product",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (products) {
            allProducts = products;
            productList.children().remove();
            if (allProducts!=null) {
                for (var i = 0; i < allProducts.length; i++) {
                    var product = $("<div id='productCard #"+allProducts[i].id+"' class='col s6 no-margin'></div>");
                    var ahref = $("<a href='/details/'"+allProducts[i].id+"'></a>").append(getHtmlProduct(allProducts[i]));
                    product.append(ahref);
                    productList.append(product);
                }
            }
        },
        error: getErrorMsg
    });
}

function updateShoppingCart(newTotal) {
    var $divShoppingCart = $("#shoppingCart");
    var newTotalString = formatDecimal(newTotal,',','.') + " руб";
    var newHtml = "<a class='waves-effect waves-teal btn-flat no-margin white-text right'><i class='material-icons right'>shopping_cart</i>"+newTotalString+"</a>";
    $divShoppingCart.html(newHtml);
}

function addProductToCart (buttonId) {
    console.log(buttonId);
    var id = getIdFromElementId(buttonId);
    var product ={
        id: id
    };
    var purchase = {
        product: product,
        quantity: 1
    };
    $.ajax({
        url: "/add_purchase",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(purchase, null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (purchases) {
            updateShoppingCart(getTotal(purchases) * getAmazonPercent() * getDollarRate());
        },
        error: getErrorMsg
    });
}

function getTotal (purchases) {
    var total = 0;
    for (var i=0;i<purchases.length;i++) {
        total += purchases[i].product.retail_price * purchases[i].quantity;
    }
    return total;
}

function getIdFromElementId (elementId) {
    return elementId.split('#').pop();
}

function getHtmlProduct (product) {
    var productCard = $("<div id='productCard #" + product.id + "' class='card horizontal'></div>");
    var cardImage = $("<div class='card-image'></div>");
    cardImage.append("<div style='float: left; width: 150px; height: 150px; overflow: hidden; background: url(\"" + product.medium_image + "\") center center no-repeat; margin: 20px;'></div>");
    var cardStacked = $("<div class='card-stacked'></div>");
    cardStacked.append("<div class='card-content'><h6>" + product.name + "</h6></div>");
    var cardAction = $("<div class='card-action'></div>");
    cardAction.append("<span class='red-text text-accent-4 left'>" + formatDecimal(product.retail_price * getDollarRate() * getAmazonPercent(),',','.') + "</span>");
    cardAction.append("<span class='left'>. руб</span>");
    cardAction.append("<a href='#' class='btn-flat orange darken-3 white-text right'>Купить</a>");
    cardStacked.append(cardAction);
    productCard.append(cardImage);
    productCard.append(cardStacked);
    return productCard;
}

function getHtmlProductDetails (product) {
    var $divider = $("<div class='divider'></div>");
    var $br = $("<br/>");
    var $div = $("<div class='row'></div>");
    $div.append("<div class='row col s10 offset-s1 white z-depth-3'></div>");
    $div.append("<h5 class='card-title black-text bold'>"+product.name+"</h5>");
    $div.append($divider);
    $div.append($br);
    var $div_image = $("<div class='col s4 offset-s1 valign-wrapper'><img src='"+product.large_image+"' class='materialboxed valign' width='100%'/></div>");
    var $div_desc = $("<div class='col s5 offset-s1'></div>");
    var $row_1 = $("<div class='row'><div class='chip teal lighten-3 left'>asin: "+product.asin+"</div></div>");
    var $row_2 = $("<div class='row no-margin'><blockquote>"+getDescriptionAsList(product.description)+"</blockquote></div>");
    var $row_3 = $("<div class='row no-margin'><div class='chip'>"+'Размеры: '+product.length+'x'+product.width+'x'+product.height+' '+product.units_l+"</div></div>");
    var $row_4 = $("<div class='row no-margin'><div class='chip'>"+'Вес: '+product.weight+' '+product.units_w+"</div></div>");
    var $row_5 = $("<div class='row no-margin'><div class='chip'>"+'Категория: '+product.category.name+"</div></div>");
    var $row_6 = $("<div class='row no-margin'><div class='chip'>"+'Продавец: '+product.supplier.name+"</div></div>");
    var $row_7 = $("<div class='row no-margin'><div class='right'><span>Цена: </span><span class='red-text bold'>"+formatDecimal(product.retail_price*dollar_rate*amazon_percent,',','.')+' '+"</span><span>руб</span></div></div>");
    var $row_8 = $("<div class='row no-margin'><div class='right'><span>Цена с доставкой: </span><span class='red-text'>"+formatDecimal(product.retail_price*dollar_rate*amazon_percent+product.delivery_msk,',','.')+' '+"</span><span>руб</span></div></div>");
    var $row_9 = $("<div class='row'><div class='divider'></div></div>");
    var $row_10 = $("<div class='row'></div>");
    $row_10.append("<div class='input-field col s2'><input value='"+purchase.quantity+"' id='"+'quantityItems #'+stat.index+"' type='text' class='validate' oninput='updateCart(this.id)'/>" +
        "<label class='active' for='"+'quantityItems #'+stat.index+"'>Количество</label></div>");
    $row_10.append("<div class='col s4'><a id='"+'deleteButton #'+stat.index+"' href='#' class='btn-flat orange darken-3 white-text right' onclick='deleteProductFromCart(this.id)'>Удалить</a></div>");

    $div_desc.append($row_1);
    $div_desc.append($row_2);
    $div_desc.append($row_3);
    $div_desc.append($row_4);
    $div_desc.append($row_5);
    $div_desc.append($row_6);
    $div_desc.append($row_7);
    $div_desc.append($row_8);
    $div_desc.append($row_9);
    $div_desc.append($row_10);
    $div.append($div_image);
    $div.append($div_desc);
    return $div;
}

function getThumbnail (product, detailed) {
    var src_small = product.small_image, src_medium = product.medium_image, src_large = product.large_image;
    var small_res = "width: 80px; height: 80px;", large_res = "width: 180px; height: 180px;";
    var htmlString = "";

    if (detailed) {
        htmlString = "<div class='zoomable' style='float: left; "+large_res+" overflow: hidden; background: url(\""+src_medium+"\") center center no-repeat; margin-right: 10px;' onclick='zoom(\""+src_large+"\")'></div>";
    } else {
        htmlString = "<div style='float: left; "+small_res+" overflow: hidden; background: url(\""+src_small+"\") center center no-repeat; margin-right: 10px;'></div>";
    }

    console.log("Thumbnail is here");
    return htmlString;
}

function showAStore () {
    var aStore = "<iframe src='http://astore.amazon.com/zzheads-20' width='90%' height='4000' frameborder='0' scrolling='no'></iframe>";
    var root = $("#root");
    root.children().remove();
    root.append(aStore);
}

function deletePurchaseCard (indexId) {
    var index = getIdFromElementId(indexId);
    var card = document.getElementById('purchaseCard #'+index);
    card.remove();
}

function updateCart (inputId) {
    var index = getIdFromElementId(inputId);
    var updatedQuantity = document.getElementById(inputId).value;
    var data = {
        index: index,
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

function getSelectedCategory (categoryId) {
    console.log(categoryId);
    var root = $("#root");
    $.ajax({
        url: "/product_by_category/"+categoryId,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (allProducts) {
            root.children().remove();
            if (allProducts!=null)
                for (var i=0;i<allProducts.length;i++) {
                    root.append(getHtmlProduct(allProducts[i]));
                }
        },
        error: getErrorMsg
    });
}

function searchProducts (pattern) {
    if (pattern == "") {
        getAllProducts();
        return;
    }
    console.log(pattern);
    var root = $("#root");
    $.ajax({
        url: "/product_by_search/"+pattern,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (allProducts) {
            root.children().remove();
            if (allProducts!=null)
                for (var i=0;i<allProducts.length;i++) {
                    root.append(getHtmlProduct(allProducts[i]));
                }
        },
        error: getErrorMsg
    });
}

function getDollarRate() {
    console.log("DollarRate = "+$("meta[name='dollar_rate']").attr("content"));
    return $("meta[name='dollar_rate']").attr("content");
}

function getAmazonPercent() {
    console.log("AmazonPercent = "+$("meta[name='amazon_percent']").attr("content"));
    return $("meta[name='amazon_percent']").attr("content");
}

function getSelectedCity (city) {
    console.log(city);
    $.ajax({
        url: "/costpickup_bycityname/"+city,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (count) {
            printFlashMessage("Стоимость доставки пересчитана для всех "+count+"продуктов в базе.", "success");
            document.getElementById("deliveryCity").innerHTML=city;
            getAllProducts();
        },
        error: getErrorMsg
    });
}

function getDescription (stringDescription) {
    var description = stringDescription.split('%n');
    return description;
}

function changeSortingOrder (order) {
    console.log(order);
    $.ajax({
        url: "/sorting/"+order,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (newOrder) {
            getAllProducts();
        },
        error: getErrorMsg
    });
}

function checkOut() {

}