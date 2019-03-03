import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class DB {
    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;
    static String[] fileTitle = {"NameMen", "NameWomen", "SurnameMen", "SurnameWomen", "ScndnameMen",
                                   "ScndnameWomen", "Country", "Region", "City", "Street"};

    //Подключение к БД
    static void connect() throws SQLException, ClassNotFoundException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db_people.s3db");
    }

    // Удаление таблиц
    static void dropTable() throws SQLException {
        statmt = conn.createStatement();
        for (String sTitle : fileTitle) {
            statmt.execute("DROP TABLE IF EXISTS '" + sTitle + "';");
        }
        statmt.execute("DROP TABLE IF EXISTS 'People';");
    }

    // Создание таблиц
    static void createTable() throws SQLException  {
        for (String sTitle : fileTitle) {
            statmt.execute("CREATE TABLE if not exists '" + sTitle + "' ('column' text);");
        }
        statmt.execute(
                "CREATE TABLE if not exists 'People' (" +
                        "'id' INTEGER PRIMARY KEY, " +
                        "'name' text, " +
                        "'surname' text, " +
                        "'scndname' text, " +
                        "'age' integer, " +
                        "'gender' text, " +
                        "'birthdate' date, " +
                        "'inn' integer, " +
                        "'postcode' integer, " +
                        "'country' text, " +
                        "'region' text, " +
                        "'city' text, " +
                        "'street' text, " +
                        "'house' integer, " +
                        "'flat' integer);");
    }

    // Заполнение таблиц
    static void insertToTable(String tableTitle, String txt) throws SQLException {
        statmt.execute("INSERT INTO '"+ tableTitle +"' ('column') VALUES ('"+ txt +"'); ");
    }

    // Заполняем таблицу People данными
    static void createPeople() throws SQLException {
        statmt.execute(
                "insert into 'People' ('name', 'surname', 'scndname', 'gender') " +
                     "select * from " +
                     "(select nameMen.column, surnameMen.column, scndnameMen.column, 'М' from nameMen, surnameMen, scndnameMen " +
                     "union " +
                     "select nameWomen.column, surnameWomen.column, scndnameWomen.column, 'Ж' from nameWomen, surnameWomen, scndnameWomen) " +
                     "order by random() limit " + MainClass.genNum + ";");

        for (int i = 1; i<= MainClass.genNum; i++) {
            statmt.execute("update 'people' set 'postcode'=(select abs(random())%(200000-100000)+100000) where id="+i+"; ");
            statmt.execute("update 'people' set 'inn'=77||(select abs(random())%(9999999999-1000000000)+1000000000) where id="+i+"; ");
            statmt.execute("update 'people' set 'birthdate'=(" +
                    " select date(strftime('%s', '1970-01-01') + abs(random() % (strftime('%s', '2019-01-01') - strftime('%s', '1970-01-01'))), 'unixepoch')) where id="+i+"; ");
            statmt.execute("update 'people' set 'country'=(select column from Country order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'region'=(select column from Region order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'city'=(select column from City order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'street'=(select column from Street order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'house'=(select abs(random())%(200)+1) where id="+i+"; ");
            statmt.execute("update 'people' set 'flat'=(select abs(random())%(300)+1) where id="+i+"; ");
        }
        statmt.execute("update 'people' set 'age'=(strftime('%s', date('now')) - strftime('%s', birthdate))/31536000; ");
    }

    // Количество столбцов в таблице
    static Integer countColumns() throws SQLException {
        resSet = statmt.executeQuery("SELECT COUNT(*) cnt FROM pragma_table_info('People')");
        return resSet.getInt("cnt");
    }

    // Вывод таблицы
    static List<String> readTable(int cellnum) throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM people");
        List<String> nameList = new ArrayList<>();
        while(resSet.next())
        {
            String  name = resSet.getString(cellnum);
            nameList.add(name);
        }
        return nameList;
    }

    // Закрытие
    static void closeDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
    }

}