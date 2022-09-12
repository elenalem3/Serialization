import java.io.*;

public class Basket implements Serializable {
    String[] products; //массив названий продуктов
    int[] prices; //массив цен
    int[] cart; //массив количества продуктов

    //конструктор, принимающий массив цен и названий продуктов
    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length];
    }

    //метод добавления amount штук продукта номер productNum в корзину
    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
        try {
            saveTxt(new File("basket.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //метод вывода на экран покупательской корзины
    public void printCart() {
        int sumProducts = 0;//итоговая сумма чека
        System.out.println("Ваша корзина: ");
        for (int i = 0; i < getCart().length; i++) {
            if (getCart()[i] > 0) {
                System.out.println(getProducts()[i] + " " + getCart()[i] + " шт " + getPrices()[i] + " руб/шт "
                        + (getCart()[i] * getPrices()[i]) + " руб в сумме");
            }
            sumProducts += getCart()[i] * getPrices()[i];
        }
        System.out.println("Итого " + sumProducts + " руб");
    }

    //метод сохранения корзины в текстовый файл; использовать встроенные сериализаторы нельзя
    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (String product : getProducts()) {
                out.print(product + " ");
            }
            for (int price : getPrices()) {
                out.print(price + " ");
            }
            for (int count : getCart()) {
                out.print(count + " ");
            }
        } catch (IOException e) {
            System.out.println("Сохранить корзину в файл basket.txt не удалось!");
        }
    }

    //статический(!) метод восстановления объекта корзины из текстового файла, в который ранее была она сохранена
    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String[] recovery = reader.readLine().split(" "); //восстанавливаем массив количества продуктов
            int[] recCart = new int[recovery.length];
            for (int i = 0; i < recovery.length; i++) {
                recCart[i] = Integer.parseInt(recovery[i]);
            }
            String[] recProd = reader.readLine().split(" "); //восстанавливаем массив названий продуктов

            recovery = reader.readLine().split(" "); //восстанавливаем массив цен
            int[] recPrice = new int[recovery.length];
            for (int i = 0; i < recovery.length; i++) {
                recPrice[i] = Integer.parseInt(recovery[i]);
            }
            Basket basket = new Basket(recProd, recPrice); //восстанавливаем корзину
            basket.cart = recCart;
            return basket;
        } catch (Exception e) {
            System.out.println("Не удалось восстановить объект корзины!");
        }
        return null;
    }

    //метод для сохранения в файл в бинарном формате
    public void saveBin(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(Basket.this);
        } catch (IOException e) {
            System.out.println("Сохранить корзину в файл basket.bin не удалось!");
        }
    }

    //метод для загрузки корзины из бинарного файла
    public static Basket loadFromBinFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Basket) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не удалось восстановить объект корзины!");
        }
        return null;
    }

    //геттеры, которые вы посчитаете нужными
    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getCart() {
        return cart;
    }
}
