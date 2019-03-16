package mypackage;

import java.util.Random;

class Generator {
    // Генератор чисед
    static int generateNum(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max-min+1)+min;
    }

    // Генератор ИНН
    static String generateInn() {
        Random rand = new Random();
        String inn = "77"+String.format("%02d",(rand.nextInt(52)))+String.format("%06d",(rand.nextInt(999999)+1));
        char[] aInn = inn.toCharArray();
        //Считаем 11 цифру
        int a = Character.getNumericValue(aInn[0])*7 + Character.getNumericValue(aInn[1])*2 + Character.getNumericValue(aInn[2])*4 +
                Character.getNumericValue(aInn[3])*10 + Character.getNumericValue(aInn[4])*3 + Character.getNumericValue(aInn[5])*5 +
                Character.getNumericValue(aInn[6])*9 + Character.getNumericValue(aInn[7])*4 + Character.getNumericValue(aInn[8])*6 +
                Character.getNumericValue(aInn[9])*8;
        int b = a-(a/11)*11;
        if (b==10) {
            inn=inn+"0";
        }
        else inn = inn+b;
        //Считаем 12 цифру
        aInn = inn.toCharArray();
        a = Character.getNumericValue(aInn[0])*3 + Character.getNumericValue(aInn[1])*7 + Character.getNumericValue(aInn[2])*2 +
                Character.getNumericValue(aInn[3])*4 + Character.getNumericValue(aInn[4])*10 + Character.getNumericValue(aInn[5])*3 +
                Character.getNumericValue(aInn[6])*5 + Character.getNumericValue(aInn[7])*9 + Character.getNumericValue(aInn[8])*4 +
                Character.getNumericValue(aInn[9])*6 + Character.getNumericValue(aInn[10])*8;

        b = a-(a/11)*11;
        if (b==10) {
            inn=inn+"0";
        }
        else inn = inn+b;
        return inn;
    }

    // Определяем пол из API
    static String getGenderApi(String gender) {
        if (gender.equals("female")) return "Ж";
        else return "М";
    }

    // Проверяем почтовый индекс число?
    private static boolean isPostcodeNumber(String postcode){
        try {
            Integer.parseInt(postcode);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static Integer getPostcodeApi(String postcode) {
        if (isPostcodeNumber(postcode)) {
            int pcode = Integer.parseInt(postcode);
            if (100000 <= pcode && pcode <= 200000)
                return pcode;
            else return generateNum(100000,200000);
        } else {
            return generateNum(100000,200000);
        }
    }

    // Определяем страну из API
    static String getCountry(String code) {
        String country = null;
        switch (code){
            case "AU": country= "Австралия";break;
            case "BR": country= "Бразилия";break;
            case "CA": country= "Канада";break;
            case "CH": country= "Швейцария";break;
            case "DE": country= "Германия";break;
            case "DK": country= "Дания";break;
            case "ES": country= "Испания";break;
            case "FI": country= "Финляндия";break;
            case "FR": country= "Франция";break;
            case "GB": country= "Великобритания";break;
            case "IE": country= "Ирландия";break;
            case "IR": country= "Иран";break;
            case "NO": country= "Норвегия";break;
            case "NL": country= "Нидерланды";break;
            case "NZ": country= "Новая Зеландия";break;
            case "TR": country= "Турция";break;
            case "US": country= "США";break;
        }
        return country;
    }
}
