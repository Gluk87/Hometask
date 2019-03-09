package mypackage;

import com.google.gson.Gson;
import mypackage.postmodel.POJO;
import java.sql.SQLException;

import static mypackage.Generator.getCountry;

class ParseJSON {

    //Получаем строку JSON из API
    private static String getLineJson() {
        String str = Connect.getFromApi(Variables.genNum);
        //Убираем все, что слева и справа от квадратных скобок
        str = str.substring(str.indexOf("["), str.indexOf("]")+1);
        //Убираем одинарную кавычку, с ней парсинг неуспешен
        str = str.replace("'"," ");
        return str;
    }

    //Парсим JSON и инсертим в БД
    static void parseJSON() {
        Gson gson = new Gson();
        POJO[] pojo = gson.fromJson(getLineJson(), POJO[].class);
        for (POJO aPojo : pojo) {
            try {
                DB.insertToMainTable(
                        aPojo.getName().getFirst(),
                        aPojo.getName().getLast(),
                        aPojo.getDob().getAge(),
                        Generator.getGenderApi(aPojo.getGender()),
                        aPojo.getDob().getDate(),
                        Generator.getInnApi(aPojo.getId().getName(),aPojo.getId().getValue()),
                        Generator.getPostcodeApi(aPojo.getLocation().getPostcode()),
                        getCountry(aPojo.getNat()),
                        aPojo.getLocation().getState(),
                        aPojo.getLocation().getCity(),
                        aPojo.getLocation().getStreet(),
                        Generator.generateNum(1,200),
                        Generator.generateNum(1,300)
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
