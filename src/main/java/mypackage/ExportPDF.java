package mypackage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ExportPDF {
    private static String fileNamePdf = Variables.userDir+"\\peoples.pdf";

    static void writeIntoPdf(String method) {
        Document document = new Document(PageSize.A3, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileNamePdf));
            document.open();
            document.newPage();
            createTablePdf(document, method);
            System.out.println("Файл PDF создан. Путь: "+fileNamePdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                document.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void createTablePdf(Document document, String method) {

        BaseFont arial;
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat inputFormat;
        Date date;
        inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        float[] columnWidths = new float[]{4f, 10f, 12f, 15f, 7f, 5f, 12f, 15f, 10f, 12f, 20f, 15f, 15f, 5f, 10f };

        try {
            arial = BaseFont.createFont(System.getProperty("user.dir")+"\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normalFont = new Font(arial, 6, Font.NORMAL);
            Font boldFont = new Font(arial, 7, Font.BOLD);
            PdfPTable table = new PdfPTable(Variables.columnCount);
            table.setWidths(columnWidths);
            PdfPCell pdfcell;
            for (int i = 0; i < Variables.headline.length; i++) {
                pdfcell = new PdfPCell(new Phrase(Variables.headline[i], boldFont));
                pdfcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(pdfcell);
            }
            for (int rownum = 0; rownum < Variables.genNum; rownum++) {
                for (int cellnum = 1; cellnum <= Variables.columnCount; cellnum++) {
                    if (method.equals("Files"))
                        pdfcell = new PdfPCell(new Phrase(DatabaseFiles.readTable(cellnum).get(rownum), normalFont));
                    else pdfcell = new PdfPCell(new Phrase(DatabaseApi.readTable(cellnum, method).get(rownum), normalFont));

                    if (cellnum==1) {
                        pdfcell = new PdfPCell(new Phrase(String.valueOf((rownum+1)), normalFont));
                    }

                    if (cellnum==7) {
                        if (method.equals("Files"))
                            date = inputFormat.parse(DatabaseFiles.readTable(cellnum).get(rownum));
                        else date = inputFormat.parse(DatabaseApi.readTable(cellnum, method).get(rownum));
                        String birthDate = outputFormat.format(date);
                        pdfcell = new PdfPCell(new Phrase((birthDate), normalFont));
                    }
                    table.addCell(pdfcell);
                }
            }
            document.add(table);
        } catch (SQLException | ParseException | DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
