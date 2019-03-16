package mypackage;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ExportExcel {

    private static HSSFWorkbook book;
    private static HSSFSheet sheet;

    static void writeIntoExcel(String method) {
        book = new HSSFWorkbook();
        sheet = book.createSheet("Лист 1");
        createHeadline();
        try {
            createRows(method);
            book.write(new FileOutputStream("people.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                book.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Файл Excel создан. Путь: "+Variables.userDir+"\\peoples.xls");
    }

    //Создаем заголовок
    private static void createHeadline(){
        HSSFRow row = sheet.createRow(0);
        // Создаем стиль
        HSSFFont font = book.createFont();
        font.setBold(true);
        HSSFCellStyle style = book.createCellStyle();
        style.setFont(font);
        // Заполняем строку с заголовками
        for (int i = 0; i < Variables.columnCount; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(Variables.headline[i]);
            cell.setCellStyle(style);
        }
    }

    // Заполняем строки данными
    private static void createRows(String method) {
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat inputFormat;
        Date date;
        inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        for(int rownum = 1; rownum<= Variables.genNum; rownum++) {
            HSSFRow row = sheet.createRow(rownum);
            for (int cellnum=0; cellnum<Variables.columnCount; cellnum++ ) {
                HSSFCell cell = row.createCell(cellnum, CellType.STRING);
                try {
                    if (method.equals("Files"))
                        cell.setCellValue(DatabaseFiles.readTable(cellnum+1).get(rownum - 1));
                        else cell.setCellValue(DatabaseApi.readTable(cellnum+1, method).get(rownum - 1));

                    if (cellnum==0) {
                        cell.setCellValue(rownum);
                    }
                    if (cellnum==6) {
                        if (method.equals("Files"))
                            date = inputFormat.parse(DatabaseFiles.readTable(cellnum+1).get(rownum - 1));
                        else date = inputFormat.parse(DatabaseApi.readTable(cellnum+1, method).get(rownum - 1));
                        String birthDate = outputFormat.format(date);
                        cell.setCellValue(birthDate);
                    }
                } catch (SQLException | ParseException e) {
                    e.printStackTrace();
                }
                sheet.autoSizeColumn(cellnum);
            }
        }
    }
}
