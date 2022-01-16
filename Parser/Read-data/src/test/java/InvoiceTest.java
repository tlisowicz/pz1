import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.kis.pz1.reading.Invoice;

public class InvoiceTest {

    @Test
    public void constructorTest() {
        String [] s = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14" };
        Invoice invoice = new Invoice(s);
        Assert.assertNotNull(invoice);
    }
}
