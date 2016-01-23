package org.kairos.components;

import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Felipe on 07/09/2015.
 */
public class SVGFactory {

    static public SVGPath createSVG(File file) {
        SVGPath svg = new SVGPath();
        svg.getStyleClass().add("icon-svg");
        svg.setContent(getSVGContent(file));
        return svg;
    }

    static public SVGPath createSVG(InputStream file) {
        SVGPath svg = new SVGPath();
        svg.getStyleClass().add("icon-svg");
        svg.setContent(getSVGContent(file));
        return svg;
    }

    static public String getSVGContent(InputStream file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = null;
        try {
            dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(file);
            return parseToSVG(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public String getSVGContent(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = null;
        try {
            dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(file);
            return parseToSVG(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private String parseToSVG(Document doc) {

        String pathStr = "";


        doc.normalizeDocument();
        NodeList paths = doc.getElementsByTagName("path");
        for (int i = 0; i < paths.getLength(); i++) {
            Node node = paths.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element path = (Element) node;

                if (!path.getAttribute("fill").equals("none")) {
                    pathStr += path.getAttribute("d");
                }
            }

        }

        return pathStr;
    }
}
