package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.dto.EmailProviderDto;

public class EmailProvidersRepository {

    private static final EmailProviderDto[] EMAIL_PROVIDERS = {
            new EmailProviderDto("mail.ru", "Почта Mail.Ru", "https://e.mail.ru/"),
            new EmailProviderDto("bk.ru", "Почта Mail.Ru (bk.ru)", "https://e.mail.ru/"),
            new EmailProviderDto("list.ru", "Почта Mail.Ru (list.ru)", "https://e.mail.ru/"),
            new EmailProviderDto("inbox.ru", "Почта Mail.Ru (inbox.ru)", "https://e.mail.ru/"),
            new EmailProviderDto("yandex.ru", "Яндекс.Почта", "https://mail.yandex.ru/"),
            new EmailProviderDto("ya.ru", "Яндекс.Почта", "https://mail.yandex.ru/"),
            new EmailProviderDto("yandex.ua", "Яндекс.Почта", "https://mail.yandex.ua/"),
            new EmailProviderDto("yandex.by", "Яндекс.Почта", "https://mail.yandex.by/"),
            new EmailProviderDto("yandex.kz", "Яндекс.Почта", "https://mail.yandex.kz/"),
            new EmailProviderDto("yandex.com", "Yandex.Mail", "https://mail.yandex.com/"),
            new EmailProviderDto("gmail.com", "Gmail", "https://mail.google.com/"),
            new EmailProviderDto("googlemail.com", "Gmail", "https://mail.google.com/"),
            new EmailProviderDto("outlook.com", "Outlook.com", "https://mail.live.com/"),
            new EmailProviderDto("hotmail.com", "Outlook.com (Hotmail)", "https://mail.live.com/"),
            new EmailProviderDto("live.ru", "Outlook.com (live.ru)", "https://mail.live.com/"),
            new EmailProviderDto("live.com", "Outlook.com (live.com)", "https://mail.live.com/"),
            new EmailProviderDto("me.com", "iCloud Mail", "https://www.icloud.com/"),
            new EmailProviderDto("icloud.com", "iCloud Mail", "https://www.icloud.com/"),
            new EmailProviderDto("rambler.ru", "Рамблер-Почта", "https://mail.rambler.ru/"),
            new EmailProviderDto("yahoo.com", "Yahoo! Mail", "https://mail.yahoo.com/"),
            new EmailProviderDto("ukr.net", "Почта ukr.net", "https://mail.ukr.net/"),
            new EmailProviderDto("i.ua", "Почта I.UA", "http://mail.i.ua/"),
            new EmailProviderDto("bigmir.net", "Почта Bigmir.net", "http://mail.bigmir.net/"),
            new EmailProviderDto("tut.by", "Почта tut.by", "https://mail.tut.by/"),
            new EmailProviderDto("inbox.lv", "Inbox.lv", "https://www.inbox.lv/"),
            new EmailProviderDto("mail.kz", "Почта mail.kz", "http://mail.kz/")
    };

    public static EmailProviderDto[] getEmailProviders() {
        return EMAIL_PROVIDERS;
    }
}