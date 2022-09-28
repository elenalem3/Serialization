package ru.netology;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        Basket basket = new Basket(new String[]{"����", "���", "��������", "����", "������"}, new int[]{500, 300, 110, 30, 90});
        ClientLog clientLog = new ClientLog();
//        File textFile = new File("basket.txt");
//        if (textFile.exists()) {
//            basket = Basket.loadFromTxtFile(textFile);
//        } else {
//            basket = new Basket(new String[]{"����", "���", "��������", "����", "������"}, new int[]{500, 300, 110, 30, 90});
//        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("shop.xml"));
        Node config = document.getDocumentElement();

        readSettingXml(config);

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
                System.out.println("������� �������������");
                basket.printCart();
            } else {
                System.out.println("������� �� �������������!");
            }
        } else {
            System.out.println("������� �� �������������!");
        }


        System.out.println("������ ��������� ������� ��� �������:");

        for (int i = 0; i < basket.products.length; i++) {
            System.out.println((i + 1) + ". " + basket.products[i] + " " + basket.prices[i] + " ���/��");
            //����� �� ����� ������ ���������
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("�������� ����� � ���������� ��� ������� 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1; //����� ��������
            int amount = Integer.parseInt(parts[1]); //����������
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
    public static void readSettingXml (Node config) {
        NodeList nodeList = config.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;

                if (element.getParentNode().getNodeName().equals("load")) {
                    if (element.getNodeName().equals("enabled")) {
                        loadBasket = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        loadFileName = element.getTextContent();
                    } else if (element.getNodeName().equals("format")) {
                        loadFileFormat = element.getTextContent();
                    }
                } else if (element.getParentNode().getNodeName().equals("save")) {
                    if (element.getNodeName().equals("enabled")) {
                        savingBasket = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        saveFileName = element.getTextContent();
                    } else if (element.getNodeName().equals("format")) {
                        saveFileFormat = element.getTextContent();
                    }
                } else if (element.getParentNode().getNodeName().equals("log")) {
                    if (element.getNodeName().equals("enabled")) {
                        savingLog = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        savingLogFileName = element.getTextContent();
                    }
                }
                readSettingXml(node);
            }
        }
    }
}
