import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

class ExportPDF {
    private static String fileNamePdf = System.getProperty("user.dir")+"\\peoples.pdf";

    static void writeIntoPdf() {
        try {
            Document document = new Document(PageSize.A3, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(fileNamePdf));
            document.open();
            document.newPage();
            createTablePdf(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTablePdf(Document document) throws DocumentException, IOException {

        BaseFont arial = BaseFont.createFont(System.getProperty("user.dir")+"\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font normalFont = new Font(arial, 6, Font.NORMAL);
        Font boldFont = new Font(arial, 7, Font.BOLD);

        float[] columnWidths = new float[]{4f, 10f, 12f, 15f, 7f, 5f, 12f, 15f, 10f, 12f, 20f, 15f, 15f, 5f, 10f };

        try {
            PdfPTable table = new PdfPTable(DB.countColumns());
            table.setWidths(columnWidths);
            PdfPCell pdfcell;
            for (int i = 0; i < ExportExcel.headline.length; i++) {
                pdfcell = new PdfPCell(new Phrase(ExportExcel.headline[i], boldFont));
                pdfcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(pdfcell);
            }

            for (int rownum = 0; rownum < Hometask2.genNum; rownum++) {
                for (int cellnum = 1; cellnum <= DB.countColumns(); cellnum++) {
                    table.addCell(new PdfPCell(new Phrase(DB.readTable(cellnum).get(rownum), normalFont)));
                }
            }
            document.add(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
