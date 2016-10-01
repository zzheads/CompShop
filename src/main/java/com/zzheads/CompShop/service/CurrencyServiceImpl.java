package com.zzheads.CompShop.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.model.AwsRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import static com.zzheads.CompShop.service.AwsServiceImpl.findChild;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final DollarRateService dollarRateService;
    private static final String USD_NODE_ID = "R01235";

    @Autowired
    public CurrencyServiceImpl(DollarRateService dollarRateService) {
        this.dollarRateService = dollarRateService;
    }

    @Override
    public double getDollarRate() throws UnirestException, IOException, SAXException, ParserConfigurationException {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
        HttpResponse xmlResponse = Unirest.get(url).asString();
        Document doc = stringToDom(xmlResponse.getBody().toString());
        NodeList nodeList = doc.getElementsByTagName("Valute");
        for (int i=0;i<nodeList.getLength();i++) {
            NamedNodeMap attributes = nodeList.item(i).getAttributes();
            String nodeId = attributes.item(0).getTextContent();
            if (nodeId.equals(USD_NODE_ID)) {
                Double rate = Double.parseDouble(findChild(nodeList.item(i), "Value").getTextContent().replaceAll(",","."));
                return rate;
            }
        }
        return Double.NaN;
    }

    @Override
    public double getTodayDollarRate() throws SAXException, UnirestException, ParserConfigurationException, IOException {
        Date today = new Date();
        Double todayRate = dollarRateService.findByDate(today);
        if (todayRate.isNaN()) {
            todayRate = getDollarRate();
            dollarRateService.save(todayRate);
            return todayRate;
        }
        return todayRate;
    }

    private static Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }
}
