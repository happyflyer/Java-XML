package org.example.java_xml.parse;

import org.example.java_xml.Book;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * JDOM 解析
 *
 * @author lifei
 */
public class JdomParse2 {
    public static void main(String[] args) {
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        List<Book> bookEntityList = new ArrayList<>();
        Book bookEntity = null;
        try {
            in = new FileInputStream("src/main/resources/books.xml");
            Document document = saxBuilder.build(in);
            Element rootElement = document.getRootElement();
            List<Element> bookList = rootElement.getChildren();
            for (Element book : bookList) {
                bookEntity = new Book();
                List<Attribute> attributeList = book.getAttributes();
                for (Attribute attr : attributeList) {
                    if ("id".equals(attr.getName())) {
                        bookEntity.setId(attr.getValue());
                    }
                }
                List<Element> childList = book.getChildren();
                for (Element child : childList) {
                    if ("name".equals(child.getName())) {
                        bookEntity.setName(child.getValue());
                    } else if ("author".equals(child.getName())) {
                        bookEntity.setAuthor(child.getValue());
                    } else if ("year".equals(child.getName())) {
                        bookEntity.setYear(child.getValue());
                    } else if ("price".equals(child.getName())) {
                        bookEntity.setPrice(child.getValue());
                    } else if ("language".equals(child.getName())) {
                        bookEntity.setLanguage(child.getValue());
                    }
                }
                bookEntityList.add(bookEntity);
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        for (Book book : bookEntityList) {
            System.out.println(book);
        }
    }
}
