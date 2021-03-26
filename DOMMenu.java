import java.io.*;               // import input-output
import java.sql.SQLOutput;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;         // import parsers
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;           // import XPath
import javax.xml.validation.*;      // import validators
import javax.xml.transform.*;       // import DOM source classes

//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import org.w3c.dom.*;               // import DOM
import org.w3c.dom.ls.LSOutput;

/**
  DOM handler to read XML information, to create this, and to print it.

  @author   CSCU9T4, University of Stirling
  @version  11/03/20
*/
public class DOMMenu {

  /** Document builder */
  private static DocumentBuilder builder = null;

  /** XML document */
  private static Document document = null;

  /** XPath expression */
  private static XPath path = null;

  /** XML Schema for validation */
  private static Schema schema = null;

  /*----------------------------- General Methods ----------------------------*/

  /**
    Main program to call DOM parser.

    @param args         command-line arguments
  */
  public static void main(String[] args)  {
    // load XML file into "document"
    loadDocument(args[0]);
    if (validateDocument("small_menu.xsd")) {
      printNodes();

    }

    // print staff.xml using DOM methods and XPath queries

  
   
  }

  /**
    Set global document by reading the given file.

    @param filename     XML file to read
  */
  private static void loadDocument(String filename) {
    try {
      // create a document builder
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();
      builder.isNamespaceAware();
      // create an XPath expression
      XPathFactory xpathFactory = XPathFactory.newInstance();
      path = xpathFactory.newXPath();

      // parse the document for later searching
      document = builder.parse(new File(filename));
    }
    catch (Exception e) {
     System.err.println("could not load:" + e.getMessage());
    }
  }


  /*-------------------------- DOM and XPath Methods -------------------------*/
  /**
   Validate the document given a schema file
   @param filename XSD file to read
  */
  private static Boolean validateDocument(String filename)  {
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language); //loading the xsd
      schema = factory.newSchema(new File(filename));
      Validator validator = schema.newValidator();
      document=builder.parse(new File("small_menu.xml"));
      validator.validate(new DOMSource(document.getDocumentElement())); // validate the xml document with the xsd

      return true;
    } catch (Exception e){

      System.err.println("Could not validate");
      return false;
    }
  }
  /**
    Print nodes using DOM methods and XPath queries.
  */
  private static void printNodes() {

    System.out.println("Root element:"+ document.getDocumentElement().getNodeName());

    NodeList itemsList = document.getElementsByTagName("item");
    System.out.println("\ntype: " + itemsList.item(0).getNodeName());
    for (int i=0 ; i<itemsList.getLength();i++) {
      Node menuItem = itemsList.item(i);


      if (menuItem.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) menuItem;

        System.out.println("\nname: " + element.getElementsByTagName("name").item(0).getTextContent());
        System.out.println("Price: " + element.getElementsByTagName("price").item(0).getTextContent());
        System.out.println("Calories: " + element.getElementsByTagName("description").item(0).getTextContent());

      }
    }
    }







  /**
    Get result of XPath query.

    @param query        XPath query
    @return         result of query
  */
  private static String query(String query) {
    String result = "";
    try {
      result = path.evaluate(query, document);
    }
    catch (Exception exception) {
      System.err.println("could not perform query - " + exception);
    }
    return(result);
  }
}
