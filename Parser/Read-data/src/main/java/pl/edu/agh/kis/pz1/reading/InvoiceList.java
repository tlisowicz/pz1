package pl.edu.agh.kis.pz1.reading;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Auxiliary class containing a list of invoices. Made just for JAXB marshall.
 */
@XmlRootElement(name ="Faktury")
public class InvoiceList {
    /**
     * List of invoices
     */
    @XmlElement(name = "Faktura")
    List<Invoice> invoices;

    /**
     * JAXB requires default constructor.
     */
    public InvoiceList() {}

    /**
     * Class constructor.
     * @param invoices list of invoices
     */
    public InvoiceList(List<Invoice> invoices) {
        this.invoices = invoices;
    }

}
