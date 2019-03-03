import java.io.*;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by Azat Sultangulov on 28.02.2019.
 */
public class MainClass {
    static int genNum;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

        DB.connect();
        DB.dropTable();
        DB.createTable();
        genNum = generateCount();
        System.out.println("Количество записей="+genNum);

        String path=System.getProperty("user.dir")+"\\src\\main\\resources";
        File dir = new File(path);

        File[] fList = dir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return true;
            }
        });

        assert fList != null;

        for (File file : fList) {
            if (file.isFile()) {
                String  put = path + "\\" + file.getName();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(put), "windows-1251"));
               // BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getClass().getResourceAsStream(resource)), "windows-1251"));
                String line;
                // Для каждого файла вызываем insert в таблицы
                while ((line = reader.readLine()) != null) {
                    for (String sTitle : DB.fileTitle) {
                        if (file.getName().startsWith(""+ sTitle +""))
                            DB.insertToTable(""+ sTitle +"", line);
                    }
                }
            }
        }
        DB.createPeople();
        ExportExcel.writeIntoExcel();
        System.out.println("Файл Excel создан. Путь: "+System.getProperty("user.dir")+"\\peoples.xls");
        ExportPDF.writeIntoPdf();
        System.out.println("Файл PDF создан. Путь: "+System.getProperty("user.dir")+"\\peoples.pdf");
        DB.closeDB();
    }

    // Генератор случайных чисел
    private static int generateCount() {
        Random rand = new Random();
        return rand.nextInt(29)+1;
    }
}