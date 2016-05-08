package net.linvx.java.libs.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlTest {
	public static String getXmlTestString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>  \n");
		sb.append("    <university name=\"pku\">  \n");
		sb.append("        <college name=\"c1\">  \n");
		sb.append("            <class name=\"class1\">  \n");
		sb.append("                <student name=\"stu1\" sex='male' age=\"21\" />  \n");
		sb.append("                <student name=\"stu2\" sex='female' age=\"20\" />  \n");
		sb.append("                <student name=\"stu3\" sex='female' age=\"20\" />  \n");
		sb.append("            </class>  \n");
		sb.append("            <class name=\"class2\">  \n");
		sb.append("                <student name=\"stu4\" sex='male' age=\"19\" />  \n");
		sb.append("                <student name=\"stu5\" sex='female' age=\"20\" />  \n");
		sb.append("                <student name=\"stu6\" sex='female' age=\"21\" />  \n");
		sb.append("            </class>  \n");
		sb.append("        </college>  \n");
		sb.append("        <college name=\"c2\">  \n");
		sb.append("            <class name=\"class3\">  \n");
		sb.append("                <student name=\"stu7\" sex='male' age=\"20\" />  \n");
		sb.append("            </class>  \n");
		sb.append("        </college>  \n");
		sb.append("        <college name=\"c3\">  \n");
		sb.append("        </college>  \n");
		sb.append("    </university> \n");
		return sb.toString();
	}
	
	public static void readDom() {  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try {  
            DocumentBuilder builder = dbf.newDocumentBuilder();  
            Document doc = builder.parse(new InputSource(new StringReader(XmlTest.getXmlTestString())));
            // root <university>   
            Element root = doc.getDocumentElement();  
            if (root == null) return;  
            System.err.println(root.getAttribute("name"));  
            // all college node   
            NodeList collegeNodes = root.getChildNodes();  
            if (collegeNodes == null) return;  
            for(int i = 0; i < collegeNodes.getLength(); i++) {  
                Node college = collegeNodes.item(i);  
                if (college != null && college.getNodeType() == Node.ELEMENT_NODE) {  
                    System.err.println("\t" + college.getAttributes().getNamedItem("name").getNodeValue());  
                    // all class node   
                    NodeList classNodes = college.getChildNodes();  
                    if (classNodes == null) continue;  
                    for (int j = 0; j < classNodes.getLength(); j++) {  
                        Node clazz = classNodes.item(j);  
                        if (clazz != null && clazz.getNodeType() == Node.ELEMENT_NODE) {  
                            System.err.println("\t\t" + clazz.getAttributes().getNamedItem("name").getNodeValue());  
                            // all student node   
                            NodeList studentNodes = clazz.getChildNodes();  
                            if (studentNodes == null) continue;  
                            for (int k = 0; k < studentNodes.getLength(); k++) {  
                                Node student = studentNodes.item(k);  
                                if (student != null && student.getNodeType() == Node.ELEMENT_NODE) {  
                                    System.err.print("\t\t\t" + student.getAttributes().getNamedItem("name").getNodeValue());  
                                    System.err.print(" " + student.getAttributes().getNamedItem("sex").getNodeValue());  
                                    System.err.println(" " + student.getAttributes().getNamedItem("age").getNodeValue());  
                                }  
                            }  
                        }  
                    }  
                }  
            }  
        } catch (ParserConfigurationException e) {  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (SAXException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
    }  
      
    public static void writeDom() {  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try {  
            DocumentBuilder builder = dbf.newDocumentBuilder();  
            Document doc = builder.parse(new InputSource(new StringReader(XmlTest.getXmlTestString())));
            // root <university>   
            Element root = doc.getDocumentElement();  
            if (root == null) return;  
            // 修改属性   
            root.setAttribute("name", "tsu");  
            NodeList collegeNodes = root.getChildNodes();  
            if (collegeNodes != null) {  
                for (int i = 0; i <collegeNodes.getLength() - 1; i++) {  
                    // 删除节点   
                    Node college = collegeNodes.item(i);  
                    if (college.getNodeType() == Node.ELEMENT_NODE) {  
                        String collegeName = college.getAttributes().getNamedItem("name").getNodeValue();  
                        if ("c1".equals(collegeName) || "c2".equals(collegeName)) {  
                            root.removeChild(college);  
                        } else if ("c3".equals(collegeName)) {  
                            Element newChild = doc.createElement("class");  
                            newChild.setAttribute("name", "c4");  
                            college.appendChild(newChild);  
                        }  
                    }  
                }  
            }  
            // 新增节点   
            Element addCollege = doc.createElement("college");  
            addCollege.setAttribute("name", "c5");  
            root.appendChild(addCollege);  
            Text text = doc.createTextNode("text");  
            addCollege.appendChild(text);  
              
            // 将修改后的文档保存到文件   
            DOMSource domSource = new DOMSource(doc);  
            
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(domSource, result);
            System.out.println(writer.getBuffer().toString());  
            
        } catch (ParserConfigurationException e) {  
            e.printStackTrace();  
        } catch (SAXException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (TransformerConfigurationException e) {  
            e.printStackTrace();  
        } catch (TransformerException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static void main(String[] args){
    	XmlTest.readDom();
    	XmlTest.writeDom();
    }
}
