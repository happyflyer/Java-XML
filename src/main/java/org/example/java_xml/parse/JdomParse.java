package org.example.java_xml.parse;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JDOM 解析
 *
 * @author lifei
 */
public class JdomParse {
    public static void main(String[] args) {
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        try {
            in = new FileInputStream("src/main/resources/books.xml");
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            Document document = saxBuilder.build(isr);
            Element rootElement = document.getRootElement();
            List<Element> bookList = rootElement.getChildren();
            System.out.println("JDOM解析开始");
            System.out.println("一共有" + bookList.size() + "本书");
            for (Element book : bookList) {
                /*
                  解析属性
                 */
                List<Attribute> attributeList = book.getAttributes();
                System.out.println("第" + (bookList.indexOf(book) + 1) + "本书共有"
                        + attributeList.size() + "个属性");
                for (Attribute attr : attributeList) {
                    System.out.println("第" + (attributeList.indexOf(attr) + 1) + "个属性的" +
                            "属性名：" + attr.getName()
                            + "，属性值：" + attr.getValue());
                }
                /*
                  解析属性，通过属性名获取属性值
                 */
                System.out.println("属性名：id，属性值：" + book.getAttributeValue("id"));
                /*
                  解析子节点
                 */
                List<Element> childList = book.getChildren();
                System.out.println("第" + (bookList.indexOf(book) + 1) + "本书共有"
                        + childList.size() + "个子节点");
                for (Element child : childList) {
                    System.out.println("第" + (childList.indexOf(child) + 1) + "个子节点的" +
                            "节点名：" + child.getName()
                            + "，子节点值：" + child.getValue());
                }
            }
            System.out.println("JDOM解析结束");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
