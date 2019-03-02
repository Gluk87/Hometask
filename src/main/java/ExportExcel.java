import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

class ExportExcel {

    private static HSSFWorkbook book;
    private static HSSFSheet sheet;

    static void writeIntoExcel() throws IOException, SQLException {
        book = new HSSFWorkbook();
        sheet = book.createSheet("Лист 1");
        createHeadline();
        createRows();
        book.write(new FileOutputStream("people.xls"));
        book.close();
    }

    //Создаем заголовок
    private static void createHeadline(){
        // Заголовки помещаем в массив
        String[] headline = {"№", "Имя", "Фамилия", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН",
                "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};
        HSSFRow row = sheet.createRow(0);
        // Создаем стиль
        HSSFFont font = book.createFont();
        font.setBold(true);
        HSSFCellStyle style = book.createCellStyle();
        style.setFont(font);
        // Заполняем строку с заголовками
        for (int i = 0; i < headline.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headline[i]);
            cell.setCellStyle(style);
        }
    }

    // Заполняем строки данными
    private static void createRows() throws SQLException {
        for(int rownum = 1; rownum<= Hometask2.genNum; rownum++) {
            HSSFRow row = sheet.createRow(rownum);
            for (int cellnum=0; cellnum<DB.countColumns(); cellnum++ ) {
                HSSFCell cell = row.createCell(cellnum, CellType.STRING);
                cell.setCellValue(DB.readTable(cellnum+1).get(rownum - 1));
                if (cellnum==6) {
                    String birthDate = DB.readTable(cellnum+1).get(rownum - 1).substring(8,10)+'-'+
                            DB.readTable(cellnum+1).get(rownum - 1).substring(5,7)+'-'+
                            DB.readTable(cellnum+1).get(rownum - 1).substring(0,4);
                    cell.setCellValue(birthDate);
                }
                sheet.autoSizeColumn(cellnum);
            }
        }
    }
}
