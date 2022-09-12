import java.io.File;
import java.util.Scanner;

public class Main {
    static Basket basket;
    public static void main(String[] args) {
        File textFile = new File("basket.txt");
        if (textFile.exists()) {
            basket = Basket.loadFromTxtFile(textFile);
        } else {
            basket = new Basket(new String[]{"Мясо", "Сыр", "Помидоры", "Хлеб", "Бананы"}, new int[]{500, 300, 110, 30, 90});
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
        }
        basket.printCart();
        scanner.close();
    }
}
