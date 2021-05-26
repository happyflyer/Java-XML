# [Java-XML](https://github.com/happyflyer/Java-XML)

- [Java 眼中的 XML——文件读取](https://www.imooc.com/learn/171)
- [Java 眼中的 XML——文件写入](https://www.imooc.com/learn/251)

## 1. XML

- 不同应用程序之间的通信
- 不同平台间的通信
- 不同平台间数据的共享

```xml
<?xml version="1.0" encoding="UTF-8"?>
<bookstore>
  <book id="1">
    <name>冰与火之歌</name>
    <author>乔治马丁</author>
    <year>2014</year>
    <price>89</price>
  </book>
  <book id="2">
    <name>安徒生童话</name>
    <year>2004</year>
    <price>77</price>
    <language>English</language>
  </book>
</bookstore>
```

## 2. XML 解析

- DOM
- SAX
- DOM4J
- JDOM

### 2.1. 应用 DOM 方式解析 XML

```java
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
try {
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse("src/main/resources/books.xml");
    // ...
} catch (ParserConfigurationException | SAXException | IOException e) {
    e.printStackTrace();
}
```

```java
System.out.println("DOM解析开始");
NodeList bookList = document.getElementsByTagName("book");
for (int i = 0; i < bookList.getLength(); i++) {
    Node bookNode = bookList.item(i);
    NamedNodeMap attrs = bookNode.getAttributes();
    System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
    // ...
    NodeList childList = bookNode.getChildNodes();
    System.out.println("第" + (i + 1) + "本书共有" + childList.getLength() + "个子节点");
    // ...
}
System.out.println("DOM解析结束");
```

- Element
  - nodeName -> element name
  - nodeValue -> null
- Attribute
  - nodeName -> 属性名称
  - nodeValue -> 属性值
- Text
  - nodeName -> #text
  - nodeValue -> 节点内容

```java
public interface Node {
    public static final short ELEMENT_NODE              = 1;
    public static final short ATTRIBUTE_NODE            = 2;
    public static final short TEXT_NODE                 = 3;
    // ...
}
```

```java
for (int j = 0; j < attrs.getLength(); j++) {
    Node attrNode = attrs.item(j);
    System.out.println("第" + (j + 1) + "个属性的" +
            "属性名：" + attrNode.getNodeName()
            + "，属性值：" + attrNode.getNodeValue());
}
```

```java
Element bookElement = (Element) bookList.item(i);
System.out.println("属性名：id，属性值：" + bookElement.getAttribute("id"));
```

```java
for (int j = 0; j < childList.getLength(); j++) {
    Node childNode = childList.item(j);
    if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
        System.out.println("第" + (j + 1) + "个子节点的" +
                "节点名：" + childNode.getNodeName()
                + "，子节点值：" + childNode.getFirstChild().getNodeValue());
    }
}
```

```java
DOM解析开始
一共有2本书
第1本书共有1个属性
第1个属性的属性名：id，属性值：1
属性名：id，属性值：1
第1本书共有9个子节点
第2个子节点的节点名：name，子节点值：冰与火之歌
第4个子节点的节点名：author，子节点值：乔治马丁
第6个子节点的节点名：year，子节点值：2014
第8个子节点的节点名：price，子节点值：89
第2本书共有1个属性
第1个属性的属性名：id，属性值：2
属性名：id，属性值：2
第2本书共有9个子节点
第2个子节点的节点名：name，子节点值：安徒生童话
第4个子节点的节点名：year，子节点值：2004
第6个子节点的节点名：price，子节点值：77
第8个子节点的节点名：language，子节点值：English
DOM解析结束
```

### 2.2. 应用 SAX 方式解析 XML

#### 2.2.1. 输出解析内容

```java
SAXParserFactory spf = SAXParserFactory.newInstance();
try {
    SAXParser saxParser = spf.newSAXParser();
    SaxParserHandler handler = new SaxParserHandler();
    saxParser.parse("src/main/resources/books.xml", handler);
} catch (ParserConfigurationException | SAXException | IOException e) {
    e.printStackTrace();
}
```

```java
static class SaxParserHandler extends DefaultHandler {
    int bookstoreIndex = 0;
    int bookIndex = 0;
    int childIndex = 0;
    // ...
}
```

```java
@Override
public void startDocument() throws SAXException {
    super.startDocument();
    System.out.println("SAX解析开始");
}
@Override
public void endDocument() throws SAXException {
    super.endDocument();
    System.out.println("SAX解析结束");
}
```

```java
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
        System.out.println("第" + bookIndex + "本书共有" + attributes.getLength() + "个属性");
        for (int i = 0; i < attributes.getLength(); i++) {
            System.out.println("第" + (i + 1) + "个属性的" +
                    "属性名：" + attributes.getQName(i)
                    + "，属性值：" + attributes.getValue(i));
        }
        System.out.println("属性名：id，属性值：" + attributes.getValue("id"));
    } else {
        childIndex++;
        System.out.print("第" + childIndex + "个子节点名：" + qName);
    }
}
```

```java
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
```

```java
@Override
public void characters(char[] ch, int start, int length) throws SAXException {
    super.characters(ch, start, length);
    String value = new String(ch, start, length);
    if (!"".equals(value.trim())) {
        System.out.println("，节点值：" + value);
    }
}
```

```java
SAX解析开始
第1本书共有1个属性
第1个属性的属性名：id，属性值：1
属性名：id，属性值：1
第1个子节点的节点名：name，节点值：冰与火之歌
第2个子节点的节点名：author，节点值：乔治马丁
第3个子节点的节点名：year，节点值：2014
第4个子节点的节点名：price，节点值：89
第1本书共有4个子节点
第2本书共有1个属性
第1个属性的属性名：id，属性值：2
属性名：id，属性值：2
第1个子节点的节点名：name，节点值：安徒生童话
第2个子节点的节点名：year，节点值：2004
第3个子节点的节点名：price，节点值：77
第4个子节点的节点名：language，节点值：English
第2本书共有4个子节点
一共有2本书
SAX解析结束
```

#### 2.2.2. 使用 Java 对象保存

```java
public class Book {
    private String id;
    private String name;
    private String author;
    private String year;
    private String price;
    private String language;
    // ...
}
```

```java
static class SaxParserHandler extends DefaultHandler {
    List<Book> bookList = new ArrayList<>();
    int bookIndex = 0;
    Book book = null;
    String currentValue = null;
    // ...
}
```

```java
@Override
public void startElement(String uri, String localName, String qName,
                          Attributes attributes)
        throws SAXException {
    super.startElement(uri, localName, qName, attributes);
    if ("book".equals(qName)) {
        bookIndex++;
        book = new Book();
        for (int i = 0; i < attributes.getLength(); i++) {
            if ("id".equals(attributes.getQName(i))) {
                book.setId(attributes.getValue(i));
            }
        }
    }
}
```

```java
@Override
public void endElement(String uri, String localName, String qName)
        throws SAXException {
    super.endElement(uri, localName, qName);
    if ("book".equals(qName)) {
        bookList.add(book);
        book = null;
    } else if ("name".equals(qName)) {
        book.setName(currentValue);
    } else if ("author".equals(qName)) {
        book.setAuthor(currentValue);
    } else if ("year".equals(qName)) {
        book.setYear(currentValue);
    } else if ("price".equals(qName)) {
        book.setPrice(currentValue);
    } else if ("language".equals(qName)) {
        book.setLanguage(currentValue);
    }
}
```

```java
@Override
public void characters(char[] ch, int start, int length) throws SAXException {
    super.characters(ch, start, length);
    currentValue = new String(ch, start, length);
}
```

```java
Book{id='1', name='冰与火之歌', author='乔治马丁', year='2014', price='89', language='null'}
Book{id='2', name='安徒生童话', author='null', year='2004', price='77', language='English'}
```

### 2.3. 应用 DOM4J 及 JDOM 方式解析 XML

#### 2.3.1. 应用 JDOM 方式解析

```java
SAXBuilder saxBuilder = new SAXBuilder();
InputStream in;
try {
    in = new FileInputStream("src/main/resources/books.xml");
    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
    Document document = saxBuilder.build(isr);
    Element rootElement = document.getRootElement();
    List<Element> bookList = rootElement.getChildren();
    // ...
} catch (JDOMException | IOException e) {
    e.printStackTrace();
}
```

```java
System.out.println("JDOM解析开始");
System.out.println("一共有" + bookList.size() + "本书");
for (Element book : bookList) {
    List<Attribute> attributeList = book.getAttributes();
    System.out.println("第" + (bookList.indexOf(book) + 1) + "本书共有"
            + attributeList.size() + "个属性");
    // ...
    List<Element> childList = book.getChildren();
    System.out.println("第" + (bookList.indexOf(book) + 1) + "本书共有"
            + childList.size() + "个子节点");
    // ...
}
System.out.println("JDOM解析结束");
```

```java
List<Attribute> attributeList = book.getAttributes();
System.out.println("第" + (bookList.indexOf(book) + 1) + "本书共有"
        + attributeList.size() + "个属性");
for (Attribute attr : attributeList) {
    System.out.println("第" + (attributeList.indexOf(attr) + 1) + "个属性的" +
            "属性名：" + attr.getName()
            + "，属性值：" + attr.getValue());
}
```

```java
System.out.println("属性名：id，属性值：" + book.getAttributeValue("id"));
```

```java
for (Element child : childList) {
    System.out.println("第" + (childList.indexOf(child) + 1) + "个子节点的" +
            "节点名：" + child.getName()
            + "，子节点值：" + child.getValue());
}
```

```java
JDOM解析开始
一共有2本书
第1本书共有1个属性
第1个属性的属性名：id，属性值：1
属性名：id，属性值：1
第1本书共有4个子节点
第1个子节点的节点名：name，子节点值：冰与火之歌
第2个子节点的节点名：author，子节点值：乔治马丁
第3个子节点的节点名：year，子节点值：2014
第4个子节点的节点名：price，子节点值：89
第2本书共有1个属性
第1个属性的属性名：id，属性值：2
属性名：id，属性值：2
第2本书共有4个子节点
第1个子节点的节点名：name，子节点值：安徒生童话
第2个子节点的节点名：year，子节点值：2004
第3个子节点的节点名：price，子节点值：77
第4个子节点的节点名：language，子节点值：English
JDOM解析结束
```

#### 2.3.2. 使用 Java 对象保存

```java
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
```

#### 2.3.3. 乱码问题解决

```xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<bookstore>
  <book id="1">
    <name>冰与火之歌</name>
    <author>乔治马丁</author>
    <year>2014</year>
    <price>89</price>
  </book>
  <book id="2">
    <name>安徒生童话</name>
    <year>2004</year>
    <price>77</price>
    <language>English</language>
  </book>
</bookstore>
```

```java
in = new FileInputStream("src/main/resources/books2.xml");
InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
Document document = saxBuilder.build(isr);
```

#### 2.3.4. 应用 DOM4J 方式解析

```java
SAXReader reader = new SAXReader();
int bookIndex = 0;
int attributeIndex;
int childIndex;
try {
    Document document = reader.read(new File("src/main/resources/books.xml"));
    Element bookStore = document.getRootElement();
    Iterator iterator = bookStore.elementIterator();
    // ...
} catch (DocumentException e) {
    e.printStackTrace();
}
```

```java
System.out.println("DOM4J解析开始");
while (iterator.hasNext()) {
    bookIndex++;
    attributeIndex = 0;
    childIndex = 0;
    Element book = (Element) iterator.next();
    List<Attribute> bookAttrs = book.attributes();
    // ...
    System.out.println("第" + bookIndex + "本书共有" + attributeIndex + "个属性");
    Iterator iterator2 = book.elementIterator();
    // ...
    System.out.println("第" + bookIndex + "本书共有" + childIndex + "个子节点");
}
System.out.println("一共有" + bookIndex + "本书");
System.out.println("DOM4J解析结束");
```

```java
for (Attribute attr : bookAttrs) {
    attributeIndex++;
    System.out.println("属性名：" + attr.getName()
            + "，属性值：" + attr.getValue());
}
```

```java
while (iterator2.hasNext()) {
    childIndex++;
    Element bookChild = (Element) iterator2.next();
    System.out.println("节点名：" + bookChild.getName()
            + "，节点值：" + bookChild.getStringValue());
}
```

```java
DOM4J解析开始
属性名：id，属性值：1
第1本书共有1个属性
节点名：name，节点值：冰与火之歌
节点名：author，节点值：乔治马丁
节点名：year，节点值：2014
节点名：price，节点值：89
第1本书共有4个子节点
属性名：id，属性值：2
第2本书共有1个属性
节点名：name，节点值：安徒生童话
节点名：year，节点值：2004
节点名：price，节点值：77
节点名：language，节点值：English
第2本书共有4个子节点
一共有2本书
DOM4J解析结束
```

### 2.4. 四种 XML 解析方式比较

![4种XML解析方式比较](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/4种XML解析方式比较.adl6khly9oo.jpg)

#### 2.4.1. DOM

- 优点
  - 形成了树结构，只管好理解，代码更易编写
  - 解析过程中树结构保留在内存中，方便修改
- 缺点
  - 当 XML 文件较大时，对内存耗费比较大
  - 容易影响解析性能，造成内存溢出

#### 2.4.2. SAX

- 优点
  - 采用时间驱动模式，对内存耗费比较小
  - 适用于只需要处理 XML 中数据时
- 缺点
  - 不易编码
  - 很难同时访问同一个 XML 中多处不同数据

#### 2.4.3. JDOM

- 仅使用具体类而不使用接口
- API 大量使用了 Collection 类

#### 2.4.4. DOM4J

- JDOM 的一种智能分支，它合并了许多超出基本 XML 文档表示的功能
- DOM4J 使用接口和抽象基本类方法，是一个优秀的 Java XML API
- 具有性能优异、灵活性好、功能强大、极端易用的特点
- 是一个开放源代码的软件

## 3. XML 生成

### 3.1. 通过 DOM 方式生成 XML 文档

```java
DocumentBuilder getDocumentBuilder() {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
        db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    }
    return db;
}
```

```java
Transformer getTransformer() {
    TransformerFactory tff = TransformerFactory.newInstance();
    Transformer tf = null;
    try {
        tf = tff.newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
    } catch (TransformerConfigurationException e) {
        e.printStackTrace();
    }
    return tf;
}
```

```java
public void createXml() {
    Document document = getDocumentBuilder().newDocument();
    document.setXmlStandalone(true);
    Element bookstore = document.createElement("bookstore");
    Element book = document.createElement("book");
    book.setAttribute("id", "1");
    Element name = document.createElement("name");
    name.setTextContent("小王子");
    book.appendChild(name);
    bookstore.appendChild(book);
    document.appendChild(bookstore);
    Transformer tf = getTransformer();
    try {
        tf.transform(new DOMSource(document), new StreamResult(
                new File("src/main/resources/books3.xml")
        ));
    } catch (TransformerException e) {
        e.printStackTrace();
    }
}
```

### 3.2. 通过 SAX 方式生成 XML 文档

```java
TransformerHandler getTransformerHandler() {
    SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    TransformerHandler handler = null;
    try {
        handler = tff.newTransformerHandler();
    } catch (TransformerConfigurationException e) {
        e.printStackTrace();
    }
    return handler;
}
```

```java
List<Book> bookList = parseXml();
TransformerHandler handler = getTransformerHandler();
Transformer tf = handler.getTransformer();
tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
tf.setOutputProperty(OutputKeys.INDENT, "yes");
try {
    File f = new File("src/main/resources/books4.xml");
    if (!f.exists()) {
        f.createNewFile();
    }
    Result result = new StreamResult(new FileOutputStream(f));
    handler.setResult(result);
    handler.startDocument();
    AttributesImpl attr = new AttributesImpl();
    handler.startElement("", "", "bookstore", attr);
    for (Book book : bookList) {
        attr.clear();
        attr.addAttribute("", "", "id", "", "1");
        handler.startElement("", "", "book", attr);
        // ...
        handler.endElement("", "", "book");
    }
    handler.endElement("", "", "bookstore");
    handler.endDocument();
} catch (SAXException | IOException e) {
    e.printStackTrace();
}
```

```java
if (book.getAuthor() != null && !"".equals(book.getAuthor().trim())) {
    attr.clear();
    handler.startElement("", "", "author", attr);
    handler.characters(book.getAuthor().toCharArray(), 0, book.getAuthor().length());
    handler.endElement("", "", "author");
}
```

### 3.3. 通过 JDOM 方式生成 XML 文档

```java
Element rss = new Element("rss");
rss.setAttribute("version", "2.0");
Document document = new Document(rss);
Element channel = new Element("channel");
rss.addContent(channel);
Element title = new Element("title");
title.setText("国内最新新闻");
channel.addContent(title);
Element title2 = new Element("title2");
title2.setText("<尖括号会自动转义>");
channel.addContent(title2);
Format format = Format.getCompactFormat();
format.setIndent("");
format.setEncoding("GBK");
XMLOutputter output = new XMLOutputter(format);
try {
    output.output(document, new FileOutputStream("src/main/resources/rssNews1.xml"));
} catch (IOException e) {
    e.printStackTrace();
}
```

### 3.4. 通过 DOM4J 方式生成 XML 文档

```java
Document document = DocumentHelper.createDocument();
Element rss = document.addElement("rss");
rss.addAttribute("version", "2.0");
Element channel = rss.addElement("channel");
Element title = channel.addElement("title");
title.setText("国内最新新闻");
Element title2 = channel.addElement("title2");
title2.setText("<尖括号不会自动转义>");
OutputFormat format = OutputFormat.createPrettyPrint();
format.setEncoding("GBK");
File file = new File("src/main/resources/rssNews2.xml");
try {
    XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
    writer.setEscapeText(false);
    writer.write(document);
    writer.close();
} catch (IOException e) {
    e.printStackTrace();
}
```

### 3.5. 四种 XML 生成方式比较

- DOM：基于 tree
- SAX：基于事件
- JDOM 和 DOM4J：基于底层 API

![DOM和SAX的比较](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/DOM和SAX的比较.5r9pst3oiv40.jpg)
