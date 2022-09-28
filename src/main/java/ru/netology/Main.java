package ru.netology;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Basket basket;
    public static boolean loadBasket;
    public static boolean savingBasket;
    public static String loadFileName;
    public static String saveFileName;
    public static String loadFileFormat;
    public static String saveFileFormat;
    public static boolean savingLog;
    public static String savingLogFileName;


    public static void main(String[] args) throws IOException {
        basket = new Basket(new String[]{"Мясо", "Сыр", "Помидоры", "Хлеб", "Бананы"}, new int[]{500, 300, 110, 30, 90});
        ClientLog clientLog = new ClientLog();
//        File textFile = new File("basket.txt");
//        if (textFile.exists()) {
//            basket = Basket.loadFromTxtFile(textFile);
//        } else {
//            basket = new Basket(new String[]{"Мясо", "Сыр", "Помидоры", "Хлеб", "Бананы"}, new int[]{500, 300, 110, 30, 90});
//        }

        File fileLoadName = new File(loadFileName);
        File fileSaveName = new File(saveFileName);
        File fileLog = new File(savingLogFileName);

        if (loadBasket) {
            if (fileLoadName.exists()) {
                if (loadFileFormat.equals("json")) {
                    basket = Basket.uploadJson(fileLoadName);
                } else if (loadFileFormat.equals("txt")) {
                    basket = Basket.loadFromTxtFile(fileLoadName);
                }
                System.out.println("Корзина восстановлена");
                basket.printCart();
            } else {
                System.out.println("Корзина не восстановлена!");
            }
        } else {
            System.out.println("Корзина не восстановлена!");
        }


        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < basket.products.length; i++) {
            System.out.println((i + 1) + ". " + basket.products[i] + " " + basket.prices[i] + " руб/шт");
            //вывод на экран списка продуктов
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите товар и количество или введите 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1; //номер продукта
            int amount = Integer.parseInt(parts[1]); //количество
            if (productNum != 0 && amount != 0) {
                basket.addToCart(productNum, amount);
            }
            if (savingLog) {
                clientLog.log(productNum, amount);
            }
        }
        if (savingBasket) {
            if (saveFileFormat.equals("json")) {
                Basket.savingInJson(fileSaveName, basket);
            } else if (saveFileFormat.equals("txt")) {
                basket.saveTxt(fileSaveName);
            }
        }
        if (savingLog) {
            clientLog.exportAsCSV(fileLog);
        }
        basket.printCart();
        scanner.close();

    }
}
