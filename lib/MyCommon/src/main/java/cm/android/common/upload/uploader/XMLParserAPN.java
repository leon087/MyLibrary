package cm.android.common.upload.uploader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cm.android.util.MyLog;

public class XMLParserAPN {

    private Element root;

    /**
     * Construct the parser from InputStream.
     *
     * @param is The XML content InputStream.
     */
    public XMLParserAPN(InputStream is) {
        if (is == null) {
            return;
        }

        DocumentBuilder docBuilder;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            root = doc.getDocumentElement();
        } catch (Exception e) {
            MyLog.e(e);
            return;
        }
    }

    /**
     * Construct the parser from data bytes
     *
     * @param data The XML data.
     */
    public XMLParserAPN(byte[] data) {
        if (data == null) {
            return;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DocumentBuilder docBuilder;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(bais);
            root = doc.getDocumentElement();
            root.normalize();
        } catch (Exception e) {
            MyLog.e(e);
            return;
        }
    }

    // <item>value</item>
    public String getValueByTag(String tagName) {
        if (root == null) {
            return null;
        }
        NodeList list = root.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) {
            return null;
        }

        Node node = list.item(0).getFirstChild();
        if (node == null) {
            return null;
        } else {
            return node.getNodeValue();
        }
    }

    public Map<String, String> getDataByTag(String tag) {
        if (root == null) {
            return null;
        }
        Map<String, String> dataMap = new HashMap<String, String>();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.hasChildNodes()) {
                dataMap.put(node.getNodeName(), node.getFirstChild()
                        .getNodeValue());
            }
        }
        return dataMap;
    }
}
