package com.zzheads.CompShop.utils;

public class EmailTemplate {

    private final static String HOST_NAME = "http://localhost:8080";

    public static String getHtmlCode (String email, String confirm) {
        return
                "<!DOCTYPE html><html lang='ru-RU'><head><meta charset='UTF-8'><link href='https://fonts.googleapis.com/css?family=Roboto:400,700,900&amp;subset=cyrillic' rel='stylesheet'>" +
                        "<title>Активирование аккаунта</title></head><body style='background: whitesmoke; font-family: 'Roboto', sans-serif; alignment: center'>" +
                        "<table width='500' bgcolor=#f5f5f5 cellspacing='0' cellpadding='5' border='0' align='center'><tr><td style='text-align: left'> <h2 style='color: #0D47A1; font-weight: 900'>" +
                        "ZDelivery" +
                        "</h2></td></tr><tr><td style='width: 100%'><p>Чтобы активировать аккаунт нажмите кнопку ниже:</p></td></tr><tr><td style='text-align: center'><button style='background: #0059bc; padding: 5%'>" +
                        "<a href='" + HOST_NAME + "/confirm?confirmString=" + confirm + "' style='color: white; font-size: medium; margin: 10%; font-weight: 900'>" +
                        "Активировать аккаунт </a></button></td></tr><tr><td  style='width: 100%'><p>" +
                        "Ваш логин - адрес электронной почты - <span style='color: #00b0ff'>" + email + "</span>" +
                        "</p></td></tr></table></body></html>";
    }
}
