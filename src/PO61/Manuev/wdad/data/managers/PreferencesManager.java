package PO61.Manuev.wdad.data.managers;

import org.w3c.dom.Document;
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

public class PreferencesManager {
    private static PreferencesManager instance;
    private Path xmlFile;
    private String path;
    private Document document;


    private PreferencesManager(String path) throws IOException, SAXException, ParserConfigurationException {
        this.path = path;
        this.xmlFile = Paths.get(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.document = builder.parse(xmlFile.toFile());
    }

    public static PreferencesManager getInstance(String path) throws IOException, SAXException, ParserConfigurationException {
        if (instance == null) {
            instance = new PreferencesManager(path);
        }
        return instance;
    }

    private void saveXML() throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(this.path));
        transformer.transform(source, result);
    }

    public void setCreateRegistry(String createRegistry) throws TransformerException {
        document.getElementsByTagName("createregistry").item(0).setTextContent(createRegistry);
        saveXML();
    }

    public String getCreateRegistry() {
        return document.getElementsByTagName("createregistry").item(0).getTextContent();
    }

    public void setRegistryAddress(String registryAddress) throws TransformerException {
        document.getElementsByTagName("registryaddress").item(0).setTextContent(registryAddress);
        saveXML();
    }

    public String getRegistryAddress() {
        return document.getElementsByTagName("registryaddress").item(0).getTextContent();
    }

    public void setRegistryPort(String registryPort) throws TransformerException {
        document.getElementsByTagName("registryport").item(0).setTextContent(registryPort);
        saveXML();
    }

    public String getRegistryPort() {
        return document.getElementsByTagName("registryport").item(0).getTextContent();
    }

    public void setPolicyPath(String policyPath) throws TransformerException {
        document.getElementsByTagName("policypath").item(0).setTextContent(policyPath);
        saveXML();
    }

    public String getPolicyPath() {
        return document.getElementsByTagName("policypath").item(0).getTextContent();
    }

    public void setUseCodeBaseOnly(String useCodeBaseOnly) throws TransformerException {
        document.getElementsByTagName("usecodebaseonly").item(0).setTextContent(useCodeBaseOnly);
        saveXML();
    }

    public String getUseCodeBaseOnly() {
        return document.getElementsByTagName("usecodebaseonly").item(0).getTextContent();
    }

    public void setClassProvider(String classProvider) throws TransformerException {
        document.getElementsByTagName("classprovider").item(0).setTextContent(classProvider);
        saveXML();
    }

    public String getClassProvider() {
        return document.getElementsByTagName("classprovider").item(0).getTextContent();
    }
}