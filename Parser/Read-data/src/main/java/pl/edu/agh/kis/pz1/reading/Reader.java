package pl.edu.agh.kis.pz1.reading;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class handles extracting data from a file.
 * CSV and XLSX files are supported.
 */
public class Reader {
    /**
     * file extension specification
     */
    @Getter private enum fileTypes {CSV, XLSX}

    /**
     * path to a file.
     */
    private final String path;
    private fileTypes fileType;
    /**
     * List of all created invoices.
     * @see Invoice
     */
    @Getter
    private final List<Invoice> invoices = new ArrayList<>();
    /**
     * Logger for information purposes
     */
    private final Logger logger = Logger.getLogger("Reader");

    /**
     * Reader constructor. During instantiation file type is recognized and
     * stored in fileType field.
     * @param path path to a file.
     */
    public Reader(String path) {

        this.path = path;
        if (path.contains(".csv")) {
            fileType = fileTypes.CSV;

        } else if (path.contains(".xlsx")) {
            fileType = fileTypes.XLSX;
        }
    }

    /**
     * method for extracting data depending on file extension.
     */
    public void read() {

        if (fileType == fileTypes.CSV) {
            readCSV();

        } else if (fileType == fileTypes.XLSX) {
            readXLSX();
        }
    }

    /**
     * method to extract data from a csv file. Data are read row by row
     * and then corresponding number of invoices are created.
     */
    public void readCSV() {

        BufferedReader csvReader;
        int lineCounter = 0;
        try {
            csvReader = new BufferedReader(new FileReader(path));
            String row;
            while ((row = csvReader.readLine()) != null) {

                String [] data = row.split("\t");
                if (lineCounter != 0) {

                    invoices.add(new Invoice(data));
                }
                lineCounter++;
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO,"File not found");
            System.exit(1);

        } catch (IOException e ) {
            logger.log(Level.INFO, "An error has occurred");
            System.exit(1);
        }
    }
    /**
     * method to extract data from a xlsx file. Data are read row by row
     * and cell by cell then corresponding number of invoices are created.
     */
    public void readXLSX() {
        try {
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
                Row row = sheet.getRow(i);
                ArrayList<String> values = new ArrayList<>();

                for (int j = 0; j < row.getPhysicalNumberOfCells(); ++j) {
                    final Cell cell = row.getCell(j);
                    String value = cell.toString();
                    values.add(value);
                }
                String [] data = values.toArray(new String[0]);
                invoices.add(new Invoice(data));
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO,"File not found");
            System.exit(1);

        } catch (IOException e) {
            logger.log(Level.INFO, "An error has occurred");
            System.exit(1);
        }
    }
}
