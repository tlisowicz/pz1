package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.reading.Invoice;
import pl.edu.agh.kis.pz1.reading.InvoiceList;
import pl.edu.agh.kis.pz1.reading.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main parser class. Parsed data are stored in faktury.xml file
 */
public class Main {

    private static final Logger logger = Logger.getLogger("Main");

    /**
     * Method for creating and using JAXB marshall.
     * @param invoices list of invoices to parse
     */
    public static void generateXML(InvoiceList invoices, String path) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            logger.log(Level.INFO,"Parsing data...");
            marshaller.marshal(invoices,new File(path));
            logger.log(Level.INFO,"Done.");

        } catch (JAXBException e) {
            logger.log(Level.INFO, "JAXB marshaller could not marshall the file.");
            System.exit(1);
        }
    }
    public static void main(String [] args) {

        Reader reader = new Reader(args[0]);
        reader.read();
        List<Invoice> inv = reader.getInvoices();
        InvoiceList invoices = new InvoiceList(inv);
        generateXML(invoices, "..\\Parser\\faktury\\faktury.xml");




    }
}
