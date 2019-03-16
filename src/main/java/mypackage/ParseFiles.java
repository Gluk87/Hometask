package mypackage;

import java.io.*;
import java.sql.SQLException;

class ParseFiles {
    static void parseFiles() {
        String path=Variables.userDir+"\\src\\main\\resources";
        File dir = new File(path);
        File[] fList = dir.listFiles((dir1, name) -> true);

        assert fList != null;
        for (File file : fList) {
            if (file.isFile()) {
                String  put = path + "\\" + file.getName();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(put), "windows-1251"));
                } catch (UnsupportedEncodingException | FileNotFoundException e) {
                    e.printStackTrace();
                }
                String line = null;
                // Для каждого файла вызываем insert в таблицы
                while (true) {
                    try {
                        assert reader != null;
                        if ((line = reader.readLine()) == null) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (String sTitle : DatabaseFiles.fileTitle) {
                        if (file.getName().startsWith(""+ sTitle +"")) {
                            try {
                                DatabaseFiles.insertToTable(""+ sTitle +"", line);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
