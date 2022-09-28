package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Basket {
    protected String[] products; //������ �������� ���������
    protected int[] prices; //������ ���
    protected int[] cart; //������ ���������� ���������

    //�����������, ����������� ������ ��� � �������� ���������
    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length];
    }


    //����� ���������� amount ���� �������� ����� productNum � �������
    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
        try {
            saveTxt(new File("basket.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //����� ������ �� ����� �������������� �������
    public void printCart() {
        int sumProducts = 0;//�������� ����� ����
        System.out.println("���� �������: ");
        for (int i = 0; i < getCart().length; i++) {
            if (getCart()[i] > 0) {
                System.out.println(getProducts()[i] + " " + getCart()[i] + " �� " + getPrices()[i] + " ���/�� "
                        + (getCart()[i] * getPrices()[i]) + " ��� � �����");
            }
            sumProducts += getCart()[i] * getPrices()[i];
        }
        System.out.println("����� " + sumProducts + " ���");
    }

    public static void savingInJson(File fileJson, Basket basket) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter file = new FileWriter(fileJson)) {
            file.write(gson.toJson(basket).toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket uploadJson (File fileJson){
        Basket basket;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileReader reader = new FileReader(fileJson)) {
            basket = gson.fromJson(reader, Basket.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }


    //����� ���������� ������� � ��������� ����; ������������ ���������� ������������� ������;
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
            System.out.println("��������� ������� � ���� basket.txt �� �������!");
        }
    }


    //�����������(!) ����� �������������� ������� ������� �� ���������� �����, � ������� ����� ���� ��� ���������;
    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String[] recovery = reader.readLine().split(" "); //��������������� ������ ���������� ���������
            int[] recCart = new int[recovery.length];
            for (int i = 0; i < recovery.length; i++) {
                recCart[i] = Integer.parseInt(recovery[i]);
            }
            String[] recProd = reader.readLine().split(" "); //��������������� ������ �������� ���������

            recovery = reader.readLine().split(" "); //��������������� ������ ���
            int[] recPrice = new int[recovery.length];
            for (int i = 0; i < recovery.length; i++) {
                recPrice[i] = Integer.parseInt(recovery[i]);
            }
            Basket basket = new Basket(recProd, recPrice); //��������������� �������
            basket.cart = recCart;
            return basket;
        } catch (Exception e) {
            System.out.println("�� ������� ������������ ������ �������!");
        }
        return null;
    }

    //�������, ������� �� ���������� �������
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
