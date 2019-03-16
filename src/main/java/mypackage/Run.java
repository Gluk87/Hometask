package mypackage;

import java.sql.SQLException;

class Run {

    static void initFromApi() {
        try {
            DatabaseFiles.connect();
            DatabaseFiles.dropTables();
            DatabaseFiles.createTables();
            ParseFiles.parseFiles();
            DatabaseApi.connDB();
            ParseJSON.parseJson();
            ExportExcel.writeIntoExcel("Api");
            ExportPDF.writeIntoPdf("Api");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                DatabaseFiles.closeDB();
                DatabaseApi.closeDB();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Если кол-во записей в БД не меньше количества Необходимых,
    // то берется из БД, иначе из Файлов
    static void initFromDBorFiles() {
        DatabaseApi.connDB();
        int countDB = DatabaseApi.countDB();

        if (countDB>=Variables.genNum) {
            System.out.println("Количество записей  в БД MySQL: "+countDB+" >= "+Variables.genNum + " необходимых.");
            System.out.println("Записи выгружаем из БД...");
            ExportExcel.writeIntoExcel("Api");
            ExportPDF.writeIntoPdf("Api");
        }
        else {
            System.out.println("Количество записей  в БД MySQL: "+countDB+" < "+Variables.genNum + " необходимых.");
            System.out.println("Записи выгружаем из Файлов...");
            try {
                DatabaseFiles.connect();
                DatabaseFiles.dropTables();
                DatabaseFiles.createTables();
                DatabaseFiles.createMainTable();
                ParseFiles.parseFiles();
                DatabaseFiles.createPeople();
                ExportExcel.writeIntoExcel("Files");
                ExportPDF.writeIntoPdf("Files");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    DatabaseFiles.closeDB();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        try {
            DatabaseApi.closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
