package com.zzheads.CompShop.service;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AwsServiceImpl implements AwsService {

    @Override
    public List<Product> itemSearch(String keywords, String searchIndex) {
        List<Product> result = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("Operation", "ItemSearch");
        params.put("ResponseGroup", "ItemAttributes");
        params.put("Keywords", keywords);
        params.put("SearchIndex", searchIndex);
        AwsRequest request = new AwsRequest();
        Document doc = null;
        try {
            doc = request.getRequest(params);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        if (doc == null) return null;

        NodeList nodeList = doc.getElementsByTagName("Item");
        for (int i=0;i<nodeList.getLength();i++) {
            NodeList itemNodeList = nodeList.item(i).getChildNodes();
            Product product = new Product();
            String featureString = "", unitL = "", unitW = "";
            Double height = 0.0, length = 0.0, width = 0.0, weight = 0.0, price = 0.0;
            for (int j = 0; j < itemNodeList.getLength(); j++) {
                Node node = itemNodeList.item(j);
                if (node.getNodeName().equals("ASIN")) product.setAsin(node.getTextContent());
                if (node.getNodeName().equals("ItemAttributes")) {
                    NodeList itemAttributesNodeList = node.getChildNodes();
                    for (int k = 0; k < itemAttributesNodeList.getLength(); k++) {
                        node = itemAttributesNodeList.item(k);
                        switch (node.getNodeName()) {
                            case "Title":
                                product.setName(node.getTextContent());
                                break;
                            case "Feature":
                                featureString += node.getTextContent() + System.lineSeparator();
                                break;
                            case "ItemDimensions" : case "PackageDimensions":
                                for (int c = 0; c < node.getChildNodes().getLength(); c++) {
                                    Node dimNode = node.getChildNodes().item(c);
                                    if (dimNode.getNodeName().equals("Height")) {
                                        height = Double.parseDouble(dimNode.getTextContent());
                                        unitL = dimNode.getAttributes().item(0).getTextContent();
                                    }
                                    if (dimNode.getNodeName().equals("Length")) {
                                        length = Double.parseDouble(dimNode.getTextContent());
                                        unitL = dimNode.getAttributes().item(0).getTextContent();
                                    }
                                    if (dimNode.getNodeName().equals("Width")) {
                                        width = Double.parseDouble(dimNode.getTextContent());
                                        unitL = dimNode.getAttributes().item(0).getTextContent();
                                    }
                                    if (dimNode.getNodeName().equals("Weight")) {
                                        weight = Double.parseDouble(dimNode.getTextContent());
                                        unitW = dimNode.getAttributes().item(0).getTextContent();
                                    }
                                }
                                break;
                            case "ListPrice":
                                for (int c = 0; c < node.getChildNodes().getLength(); c++) {
                                    Node priceNode = node.getChildNodes().item(c);
                                    if (priceNode.getNodeName().equals("FormattedPrice"))
                                        price = Double.parseDouble(priceNode.getTextContent().substring(1).replaceAll(",", ""));
                                }
                                break;
                            default:
                                break;
                        }
                        product.setDescription(featureString);
                        product.setHeight(height);
                        product.setLength(length);
                        product.setWidth(width);
                        product.setWeight(weight);
                        product.setUnitsL(unitL);
                        product.setUnitsW(unitW);
                        product.setPurchasePrice(price);
                        product.setRetailPrice(price * 1.1);
                    }
                }
            }
            result.add(product);
        }
        return result;
    }

    @Override
    public Product itemLookup(String asin, String responseGroup) {
        Product product = new Product();
        product.setAsin(asin);
        Map<String, String> params = new HashMap<>();
        params.put("Operation", "ItemLookup");
        params.put("ResponseGroup", "ItemAttributes");
        params.put("ItemId", asin);
        params.put("ResponseGroup", responseGroup);

        AwsRequest request = new AwsRequest();
        Document doc = null;
        try {
            doc = request.getRequest(params);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        if (doc == null) return null;

        switch (responseGroup) {
            case "Images":
                product.setSmallImage(doc.getElementsByTagName("SmallImage").item(0).getFirstChild().getTextContent());
                product.setMediumImage(doc.getElementsByTagName("MediumImage").item(0).getFirstChild().getTextContent());
                product.setLargeImage(doc.getElementsByTagName("LargeImage").item(0).getFirstChild().getTextContent());
                break;

            case "OfferSummary":
                if (findChild(doc.getElementsByTagName("LowestUsedPrice").item(0), "FormattedPrice")!=null) {
                    Double usedPrice = Double.parseDouble(findChild(doc.getElementsByTagName("LowestUsedPrice").item(0), "FormattedPrice").getTextContent().substring(1).replaceAll(",", ""));
                    product.setPurchasePrice(usedPrice);
                    product.setRetailPrice(usedPrice * Application.PROFIT_PERCENT);
                } else {
                    if (findChild(doc.getElementsByTagName("LowestNewPrice").item(0), "FormattedPrice")!=null) {
                        Double newPrice = Double.parseDouble(findChild(doc.getElementsByTagName("LowestNewPrice").item(0),"FormattedPrice").getTextContent().substring(1).replaceAll(",",""));
                        product.setPurchasePrice(newPrice);
                        product.setRetailPrice(newPrice * Application.PROFIT_PERCENT);
                    } else {
                        product.setPurchasePrice(0.0);
                        product.setRetailPrice(0.0);
                    }
                }
                break;

            default:
                break;
        }

        return product;
    }

    public static Node findNode (NodeList nodeList, String nodeName) {
        for (int i=0;i<nodeList.getLength();i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName))
                return nodeList.item(i);
        }
        return null;
    }

    public static Node findChild (Node node, String childNodeName) {
        if (node == null) return null;
        NodeList nodeList = node.getChildNodes();
        return findNode(nodeList, childNodeName);
    }

}
