package org.example.java_xml.parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * SAX 解析
 *
 * @author lifei
 */
public class SaxParse {
    public static void main(String[] args) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = spf.newSAXParser();
            SaxParserHandler handler = new SaxParserHandler();
            saxParser.parse("src/main/resources/books.xml", handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    static class SaxParserHandler extends DefaultHandler {
        int bookstoreIndex = 0;
        int bookIndex = 0;
        int childIndex = 0;

        /**
         * 用来标识解析开始
         *
         * @throws SAXException SAXException
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            System.out.println("SAX解析开始");
        }

        /**
         * 用来标识解析结束
         *
         * @throws SAXException SAXException
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            System.out.println("SAX解析结束");
        }

        /**
         * 用来遍历xml文件的开始标签
         *
         * @param uri        uri
         * @param localName  localName
         * @param qName      qName
         * @param attributes attributes
         * @throws SAXException SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("bookstore".equals(qName)) {
                bookstoreIndex++;
                bookIndex = 0;
            } else if ("book".equals(qName)) {
                bookIndex++;
                childIndex = 0;
                /*
                  解析属性
                 */
                System.out.println("第" + bookIndex + "本书共有" + attributes.getLength() + "个属性");
                for (int i = 0; i < attributes.getLength(); i++) {
                    System.out.println("第" + (i + 1) + "个属性的" +
                            "属性名：" + attributes.getQName(i)
                            + "，属性值：" + attributes.getValue(i));
                }
                /*
                  解析属性，通过属性名获取属性值
                 */
                System.out.println("属性名：id，属性值：" + attributes.getValue("id"));
            } else {
                childIndex++;
                System.out.print("第" + childIndex + "个子节点的节点名：" + qName);
            }
        }

        /**
         * 用来遍历xml文件的结束标签
         *
         * @param uri       uri
         * @param localName localName
         * @param qName     qName
         * @throws SAXException SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            if ("bookstore".equals(qName)) {
                System.out.println("一共有" + bookIndex + "本书");
            } else if ("book".equals(qName)) {
                System.out.println("第" + bookIndex + "本书共有" + childIndex + "个子节点");
            }
        }

        /**
         * 解析节点值
         *
         * @param ch     ch
         * @param start  start
         * @param length length
         * @throws SAXException SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length);
            if (!"".equals(value.trim())) {
                System.out.println("，节点值：" + value);
            }
        }
    }
}
