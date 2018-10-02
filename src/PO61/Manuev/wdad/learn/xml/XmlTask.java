package PO61.Manuev.wdad.learn.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.function.Predicate;

public class XmlTask {
    private final String path;
    private final Path xmlFile;
    private Document document;

    public XmlTask(String path) throws SAXException, IOException, ParserConfigurationException {
        this.path = path;
        this.xmlFile = Paths.get(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(true);
        this.document = builder.parse(xmlFile.toFile());
    }

    public int earningsTotal(String officiantSecondName, Calendar calendar) throws TransformerException, DateNotFoundException, OrderNotFoundException {
        return calculateItems(findOrder(officiantSecondName, findDate(calendar)));
    }

    private Element find(NodeList nodeList, Predicate<Element> predicate) {
        Element element;
        for (int i = 0; i < nodeList.getLength(); i++) {
            element = (Element) nodeList.item(i);
            if (predicate.test(element)) return element;
        }
        return null;
    }

    private Element findDate(Calendar calendar) throws DateNotFoundException {
        Element element = find(document.getElementsByTagName("date"), s -> (s.getAttribute("day").equals(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))
                && s.getAttribute("month").equals(String.valueOf(calendar.get(Calendar.MONTH)))
                && s.getAttribute("year").equals(String.valueOf(calendar.get(Calendar.YEAR)))));
        if (element == null)
            throw new DateNotFoundException();
        else
            return element;
    }

    private Element findOrder(String officiantSecondName, Element date) throws OrderNotFoundException {
        Element element = find(date.getElementsByTagName("order"), s -> (((Element) (s.getElementsByTagName("officiant").item(0))).getAttribute("secondname").equals(officiantSecondName)));
        if (element == null)
            throw new OrderNotFoundException();
        else
            return element;
    }

    private int calculateItems(Element order) throws TransformerException {
        NodeList nodelist = order.getElementsByTagName("item");
        Element element;
        int counter = 0;
        for (int i = 0; i < nodelist.getLength(); i++) {
            element = (Element) nodelist.item(i);
            counter += Integer.valueOf(element.getAttribute("cost"));
        }
        try {
            if (counter != Integer.valueOf(order.getElementsByTagName("totalcost").item(0).getTextContent())) {
                setTotalCost(order, counter);
            }
        } catch (NullPointerException e) {
            setTotalCost(order, counter);
        }
        return counter;
    }

    private void setTotalCost(Element order, int totalCost) throws TransformerException {
        try {
            order.getElementsByTagName("totalcost").item(0).setTextContent(String.valueOf(totalCost));
        } catch (NullPointerException e) {
            Element element = document.createElement("totalcost");
            element.setTextContent(String.valueOf(totalCost));
            order.appendChild(element);
        }
        saveXML();

    }

    public void removeDay(Calendar calendar) throws DateNotFoundException {
        Element element = findDate(calendar);
        element.getParentNode().removeChild(element);
    }

    public void changeOfficiantName(String oldFirstName, String oldSecondName, String newFirstName, String newSecondName) throws TransformerException {
        NodeList nodeList = document.getElementsByTagName("officiant");
        Element element;
        for (int i = 0; i < nodeList.getLength(); i++) {
            element = (Element) nodeList.item(i);
            if (element.getAttribute("firstname").equals(oldFirstName) &&
                    element.getAttribute("secondName").equals(oldSecondName)) {
                element.setAttribute("firstname", newFirstName);
                element.setAttribute("secondName", newSecondName);
            }
        }
        saveXML();
    }

    private void saveXML() throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(this.path));
        transformer.transform(source, result);
    }
}