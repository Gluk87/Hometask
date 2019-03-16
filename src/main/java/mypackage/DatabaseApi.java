package mypackage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class DatabaseApi {
    private static Statement stmt;
    private static ResultSet resSet;
    private static Connection conn;

    static void connDB() {
        Properties prop = new Properties();
        String path=Variables.userDir+"\\database.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(path);
            prop.load(fileInputStream);
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String pass = prop.getProperty("pass");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            System.out.println("БД MySQL подключена успешно.");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("БД MySQL не подключена...");
            e.printStackTrace();
        }
    }

    static void insertTables(String postcode,
                              String country,
                              String region,
                              String city,
                              String street,
                              int house,
                              int flat,
                              String surname,
                              String name,
                              String middlename,
                              String birthdate,
                              String gender,
                              String inn){
        String query1 = "select count(*) from PERSONS where surname='"+surname+"' and name='"+name+"' and middlename='"+middlename+"'" ;
        String query2 = "update PERSONS set birthdate='"+birthdate+"', gender='"+gender+"', inn='"+inn+"' " +
                " where surname='"+surname+"' and name='"+name+"' and middlename='"+middlename+"'" ;
        String query3 = "update ADDRESS set postcode='"+postcode+"', country='"+country+"', region='"+region+"', city='"+city+"', " +
                " street='"+street+"', house='"+house+"', flat='"+flat+"'" +
                " where id in (select address_id from PERSONS where surname='"+surname+"' and name='"+name+"' and middlename='"+middlename+"')";
        String query4 = "INSERT INTO ADDRESS (postcode, country, region, city, street, house, flat) \n" +
                " VALUES ('" + postcode +"', '" + country +"', '" + region +"', '" + city +"', '" + street +"', '" + house +"', '" + flat +"'); ";
        String query5 = "INSERT INTO PERSONS (surname, name, middlename, birthdate, gender, inn, address_id) \n" +
                " VALUES ('"+ surname +"', '" + name +"', '" + middlename +"', '" + birthdate +"', '" + gender +"', '" + inn +"',  LAST_INSERT_ID()); ";

        int count;
        try {
            resSet = stmt.executeQuery(query1);
            resSet.next();
            count = resSet.getInt(1);
            // Если есть повторяющиеся записи
            if (count>0) {
                stmt.executeUpdate(query2); // апдейтим остальные поля
                stmt.executeUpdate(query3);
            }
            else {
                stmt.executeUpdate(query4); // иначе инсертим
                stmt.executeUpdate(query5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<String> readTable(int cellnum) {

        List<String> nameList = new ArrayList<>();
        try {
            resSet = stmt.executeQuery("select p.id, p.name, p.surname, p.middlename, floor(datediff(current_date,birthdate)/365) age," +
                    " p.gender, p.birthdate, p.inn, a.postcode, a.country, a.region, a.city, a.street, a.house, a.flat" +
                    " from persons p, address a" +
                    " where a.id = p.address_id order by p.id desc limit "+Variables.genNum);
            while(resSet.next())
            {
                String  name = resSet.getString(cellnum);
                nameList.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameList;
    }

    static int countDB(){
        int count = 0;
        try {
            resSet = stmt.executeQuery("select count(*) from PERSONS");
            resSet.next();
            count = resSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    static void closeDB() throws SQLException
    {
        conn.close();
        stmt.close();
        resSet.close();
    }
}