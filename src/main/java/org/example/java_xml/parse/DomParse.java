package org.example.java_xml.parse;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * DOM 解析
 *
 * @author lifei
 */
public class DomParse {
    public static void main(String[] args) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse("src/main/resources/books.xml");
            NodeList bookList = document.getElementsByTagName("book");
            System.out.println("DOM解析开始");
            System.out.println("一共有" + bookList.getLength() + "本书");
            for (int i = 0; i < bookList.getLength(); i++) {
                Node bookNode = bookList.item(i);
                /*
                  解析属性
                 */
                NamedNodeMap attrs = bookNode.getAttributes();
                System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attrNode = attrs.item(j);
                    System.out.println("第" + (j + 1) + "个属性的" +
                            "属性名：" + attrNode.getNodeName()
                            + "，属性值：" + attrNode.getNodeValue());
                }
                /*
                  解析属性，通过属性名获取属性值
                 */
                Element bookElement = (Element) bookList.item(i);
                System.out.println("属性名：id，属性值：" + bookElement.getAttribute("id"));
                /*
                  解析子节点
                 */
                NodeList childList = bookNode.getChildNodes();
                System.out.println("第" + (i + 1) + "本书共有" + childList.getLength() + "个子节点");
                for (int j = 0; j < childList.getLength(); j++) {
                    /*
                      区分text类型的Node和element类型的Node
                     */
                    Node childNode = childList.item(j);
                    if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println("第" + (j + 1) + "个子节点的" +
                                "节点名：" + childNode.getNodeName()
                                + "，子节点值：" + childNode.getFirstChild().getNodeValue());
                    }
                }
            }
            System.out.println("DOM解析结束");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
