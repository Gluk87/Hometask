package mypackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
    static void dropTables() throws SQLException {
        statmt = conn.createStatement();
        for (String sTitle : fileTitle) {
            statmt.execute("DROP TABLE IF EXISTS '" + sTitle + "';");
        }
        statmt.execute("DROP TABLE IF EXISTS 'People';");
    }

    // Создание таблиц
    static void createTables() {
        for (String sTitle : fileTitle) {
            try {
                statmt.execute("CREATE TABLE if not exists '" + sTitle + "' ('column' text);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Создание таблицы People
    static void createMainTable() {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Заполнение таблиц
    static void insertToTable(String tableTitle, String txt) throws SQLException {
        statmt.execute("INSERT INTO '"+ tableTitle +"' ('column') VALUES ('"+ txt +"'); ");
    }

    // Заполняем главную таблицу из API
    static void insertToMainTable(String name,
                                  String surname,
                                  Integer age,
                                  String gender,
                                  Date birthdate,
                                  String inn,
                                  Integer postcode,
                                  String country,
                                  String region,
                                  String city,
                                  String street,
                                  Integer house,
                                  Integer flat) throws SQLException {
        statmt.execute("INSERT INTO 'People' ('name','surname','age','gender','birthdate','inn','postcode', 'country','region','city','street','house','flat') " +
                "VALUES ('"+ name +"', '" + surname +"', '" + age +"', '" + gender +"', '" + birthdate +"', '" + inn +"', '" + postcode +"', '" + country +"', '" + region +"', '" + city +"', '" + street +"', '" + house +"', '" + flat +"'); ");
    }

    // Заполняем таблицу People данными
    static void createPeople() throws SQLException {
        statmt.execute(
                "insert into 'People' ('name', 'surname', 'scndname', 'gender') " +
                     "select * from " +
                     "(select nameMen.column, surnameMen.column, scndnameMen.column, 'М' from nameMen, surnameMen, scndnameMen " +
                     "union " +
                     "select nameWomen.column, surnameWomen.column, scndnameWomen.column, 'Ж' from nameWomen, surnameWomen, scndnameWomen) " +
                     "order by random() limit " + Variables.genNum + ";");

        for (int i = 1; i<= Variables.genNum; i++) {
            statmt.execute("update 'people' set 'postcode'=(select abs(random())%(200000-100000)+100000) where id="+i+"; ");
            statmt.execute("update 'people' set 'inn'="+Generator.generateInn()+" where id="+i+"; ");
            statmt.execute("update 'people' set 'birthdate'=(" +
                    " select date(strftime('%s', '1970-01-01') + abs(random() % (strftime('%s', '2019-01-01') - strftime('%s', '1970-01-01'))), 'unixepoch')) where id="+i+"; ");
            statmt.execute("update 'people' set 'country'=(select column from Country order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'region'=(select column from Region order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'city'=(select column from City order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'street'=(select column from Street order by random() limit 1) where id="+i+"; ");
            statmt.execute("update 'people' set 'house'="+ Generator.generateNum(1,200)+" where id="+i+"; ");
            statmt.execute("update 'people' set 'flat'="+ Generator.generateNum(1,300)+" where id="+i+"; ");
        }
        statmt.execute("update 'people' set 'age'=(strftime('%s', date('now')) - strftime('%s', birthdate))/31536000; ");
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