package filedeleteftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FilerDeleteFTP {

    static String dst = "/Design/Supershift S&L/PRODUCTS/";
    static String excelSource = "C:\\Users\\AMaslowiec\\Desktop\\Book1.xlsx";

    public static void main(String[] args) throws IOException {
        FTPClient ftpClient = new FTPClient();
        Utils con = new Utils();
        if (con.connect(ftpClient)) {
            System.out.println("Connected");

            FileInputStream fis_excel = null;
            try {
                fis_excel = new FileInputStream(excelSource);
                XSSFWorkbook wb_excel = new XSSFWorkbook(fis_excel);
                XSSFSheet sheet_excel = wb_excel.getSheetAt(0);
                Iterator rows = sheet_excel.rowIterator();
                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    if (row.getCell(1) != null) {
                        System.out.println(row.getCell(0).toString() + " , " + row.getCell(1).toString());
                        FileUploadFTP(ftpClient, row.getCell(0).toString(), row.getCell(1).toString());
                        //FileRename(row.getCell(0).toString(), row.getCell(1).toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis_excel != null) {
                    fis_excel.close();
                }
            }

            if (con.disconnect(ftpClient)) {
                System.out.println("\nDisconnected");
            }
        }
    }

    static void FileUploadFTP(FTPClient ftpClient, String folderName, String fileName) throws IOException {

        boolean existed = ftpClient.changeWorkingDirectory(dst + folderName);
        if (existed) {
            boolean delete = ftpClient.deleteFile(fileName);
            if (delete == true) {
                System.out.println("\t" + fileName + " deleted");
            } else {
                System.out.println("\t" + fileName + " not deleted !!!");
            }
        } else {
            System.out.println(dst + folderName + " not exists !!!");
        }
    }

    private static void FileRename(String folderName, String fileName) {
        String productContent = "G:/Product Content/PRODUCTS";
        boolean existed = new File(productContent + "/" + folderName + "/" + fileName).exists();
        if (existed) {
            System.out.println(fileName + " exists");
            boolean rename = new File(productContent + "/" + folderName + "/" + fileName).renameTo(new File(productContent + "/" + folderName + "/test" + fileName));
            if (rename == true) {
                System.out.println("\t" + fileName + " renamed");
            } else {
                System.out.println("\t" + fileName + " not renamed !!!");
            }
        } else {
            System.out.println(fileName + " not exists !!!");
        }
    }
}
