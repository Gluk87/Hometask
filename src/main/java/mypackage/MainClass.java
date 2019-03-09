package mypackage;

/**
 * Created by Azat Sultangulov on 28.02.2019.
 */
public class MainClass {

    public static void main(String[] args) {

        System.out.println("Количество записей="+Variables.genNum);
        System.out.println("Проверка состояния сети...");

        if (Network.isNetworkActiv()) {
            System.out.println("Сеть активна.");
            Run.initFromApi();
        } else {
            System.out.println("Сеть неактивна.");
            Run.initFromFile();
        }
    }
}