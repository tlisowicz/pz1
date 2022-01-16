import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.reading.Invoice;
import pl.edu.agh.kis.pz1.reading.Reader;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReaderTest {

     String[] header = {"Nazwa odbiorcy", "Adres odbiorcy",	"NIP odbiorcy",	"Data wystawienia",
            "Data sprzedaży",	"Nr faktury",	"Tytuł pozycji",	"Liczba sztuk",	"Cena jednostkowa",
            "Stawka podatku", "Kwota Podatku",	"Cena netto pozycji",	"Cena brutto pozycji",
            "Cena netto faktury łącznie", "Cena brutto faktury łącznie"};


    String[] data = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14" };

    @Before
    public void makeTestCSVFile() {
        String newFile = "test.csv";
        generateTestCSV(newFile);
    }

    @Before
    public void makeXLSXTestFile() {
        String newFile = "test.xlsx";
        generateTestXLSX(newFile);
    }

    private void generateTestXLSX(String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("1");
        XSSFRow row;
        Map<String, String[]> fileData = new TreeMap<>();
        fileData.put("1", header);
        fileData.put("2", data);
        Set<String> keyid = fileData.keySet();
        int rowid = 0;

        for (String key: keyid) {
            row = spreadsheet.createRow(rowid++);
            String [] s = fileData.get(key);
            int cellid = 0;

            for (String str: s) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(str);
            }
        }

        try {
            FileOutputStream output;
            output = new FileOutputStream(fileName);
            workbook.write(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void generateTestCSV(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output, '\t', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(header);
            writer.writeNext(data);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readerConstructorTest() {
        Reader reader = new Reader("test.csv");
        Assert.assertNotNull(reader);

    }

    @Test
    public void readCSVTest() {
        Reader reader = new Reader("test.csv");
        reader.readCSV();
        Invoice invoice = reader.getInvoices().get(0);
        String firstField = invoice.getRecipientAddress();
        String eighthField = invoice.getUnitPrice();
        String twelfthField =  invoice.getPositionGrossPrice();

        Assert.assertEquals("1",firstField);
        Assert.assertEquals("8",eighthField);
        Assert.assertEquals("12",twelfthField);

    }

    @Test
    public void readXLSXTest() {
        Reader reader = new Reader("test.xlsx");
        reader.readXLSX();
        Invoice invoice = reader.getInvoices().get(0);
        String firstField = invoice.getRecipientAddress();
        String eighthField = invoice.getUnitPrice();
        String twelfthField =  invoice.getPositionGrossPrice();

        Assert.assertEquals("1",firstField);
        Assert.assertEquals("8",eighthField);
        Assert.assertEquals("12",twelfthField);

    }

    @Test
    public void readTest() {
        Reader reader = new Reader("test.xlsx");
        reader.read();
        Invoice invoice = reader.getInvoices().get(0);
        String firstField = invoice.getRecipientAddress();
        Assert.assertEquals("1",firstField);

        Reader reader2 = new Reader("test.csv");
        reader2.read();
        Invoice invoice2 = reader.getInvoices().get(0);
        String firstField2 = invoice2.getRecipientAddress();
        Assert.assertEquals("1",firstField2);
    }

    @After
    public void deleteTestFile() {
        File testFile = new File("test.csv");
        File testFile2 = new File("test.xlsx");
        assert testFile.delete();
        assert testFile2.delete();
    }
}
