package mypackage;

class Variables {
    static int genNum = Generator.generateNum(1,30); // Выбираем случайное кол-во записей от 1 до 30 включительно
    static String userDir = System.getProperty("user.dir");
    static String[] headline = {"№", "Имя", "Фамилия", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН",
            "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};
    static int columnCount = headline.length;
}
