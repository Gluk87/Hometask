package mypackage;

import java.sql.SQLException;

class Run {
    static void initFromFile(){
        try {
            DB.connect();
            DB.dropTables();
            DB.createTables();
            DB.createMainTable();
            ParseFiles.parseFiles();
            DB.createPeople();
            ExportExcel.writeIntoExcel("File");
            ExportPDF.writeIntoPdf("File");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                DB.closeDB();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static void initFromApi() {
        try {
            DB.connect();
            DB.dropTables();
            DB.createMainTable();
            ParseJSON.parseJSON();
            ExportExcel.writeIntoExcel("Api");
            ExportPDF.writeIntoPdf("Api");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                DB.closeDB();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
