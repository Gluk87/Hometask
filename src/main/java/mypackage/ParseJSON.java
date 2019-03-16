package mypackage;

import com.google.gson.Gson;
import mypackage.postmodel.POJO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    static void parseJson() {
        Gson gson = new Gson();
        POJO[] pojo = gson.fromJson(getLineJson(), POJO[].class);

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        for (POJO aPojo : pojo) {
            try {
                DatabaseApi.insertTables(
                        // Для таблицы Address
                        String.valueOf(Generator.getPostcodeApi(aPojo.getLocation().getPostcode())),
                        getCountry(aPojo.getNat()),
                        aPojo.getLocation().getState(),
                        aPojo.getLocation().getCity(),
                        aPojo.getLocation().getStreet(),
                        Generator.generateNum(1,200),
                        Generator.generateNum(1,300),
                        //Для таблицы Persons
                        aPojo.getName().getLast(),
                        aPojo.getName().getFirst(),
                        DatabaseFiles.getMiddleName(aPojo.getGender()),
                        outputFormat.format(inputFormat.parse(aPojo.getDob().getDate().substring(0,10))),
                        Generator.getGenderApi(aPojo.getGender()),
                        Generator.generateInn()

                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
