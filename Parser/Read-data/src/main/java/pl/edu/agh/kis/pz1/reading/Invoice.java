package pl.edu.agh.kis.pz1.reading;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing real invoice. Invoices are created during data extraction.
 */
@Setter @Getter
@XmlRootElement(name = "Faktura")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice {

    @XmlElement(name = "KodFaktury")
    private static final String CODE = "PLN";

    @XmlElement(name = "NazwaOdbiorcy")
    private String recipientName;

    @XmlElement(name = "AdresOdbiorcy")
    private String recipientAddress;

    @XmlElement(name = "NIPOdbiorcy")
    private String recipientNIP;

    @XmlElement(name = "DataWystawienia")
    private String dateOfInvoicing;

    @XmlElement(name = "DataSprzedazy")
    private String saleDate;

    @XmlElement(name = "NrFaktury")
    private String invoiceNumber;

    @XmlElement(name = "TytulPozycji")
    private String positionName;

    @XmlElement(name = "LiczbaSztuk")
    private String numberOfItems;

    @XmlElement(name = "CenaJednostkowa")
    private String unitPrice;

    @XmlElement(name = "StawkaPodatku")
    private String taxRate;

    @XmlElement(name = "KwotaPodatku")
    private String taxValue;

    @XmlElement(name = "CenaNettoPozycji")
    private String positionNetPrice;

    @XmlElement(name = "CenaBruttoPozycji")
    private String positionGrossPrice;

    @XmlElement(name = "CenaNettoFakturyLacznie")
    private String invoiceNetPrice;

    @XmlElement(name = "CenaBruttoFakturyLacznie")
    private String invoiceGrossPrice;

    @XmlElement(name = "RodzajFaktury")
    private static final String TYPE = "VAT";
    /**
     * JAXB requires default constructor.
     */
    public Invoice(){}

    /**
     * Invoice constructor. For each passed row of data constructor partitions data
     * into individual strings and assigns them to corresponding fields.
     * @param data a table of Strings, data extracted from a file
     */
    public Invoice(String [] data) {

        int i = 0;
        for (String cell: data) {

            switch(i) {
                case 0:
                    setRecipientName(cell);
                    break;

                case 1:
                    setRecipientAddress(cell);
                    break;

                case 2:
                    setRecipientNIP(cell);
                    break;

                case 3:
                    setDateOfInvoicing(cell);
                    break;

                case 4:
                    setSaleDate(cell);
                    break;

                case 5:
                    setInvoiceNumber(cell);
                    break;

                case 6:
                    setPositionName(cell);
                    break;

                case 7:
                    setNumberOfItems(cell);
                    break;

                case 8:
                    setUnitPrice(cell);
                    break;

                case 9:
                    setTaxRate(cell);
                    break;

                case 10:
                    setTaxValue(cell);
                    break;

                case 11:
                    setPositionNetPrice(cell);
                    break;

                case 12:
                    setPositionGrossPrice(cell);
                    break;

                case 13:
                    setInvoiceNetPrice(cell);
                    break;

                case 14:
                    setInvoiceGrossPrice(cell);
                    break;

                default:
                    break;
            }
            ++i;
        }
    }
}
