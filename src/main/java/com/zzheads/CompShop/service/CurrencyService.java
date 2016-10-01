package com.zzheads.CompShop.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface CurrencyService {
    double getDollarRate() throws UnirestException, IOException, SAXException, ParserConfigurationException;
    double getTodayDollarRate() throws SAXException, UnirestException, ParserConfigurationException, IOException;
}
