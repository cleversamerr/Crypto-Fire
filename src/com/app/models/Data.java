package com.app.models;

import javafx.scene.control.Label;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;

public class Data {

    private static final String dataFolderPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\Crypto Fire";

    public static String dataFilePath = dataFolderPath + "\\data.dat";

    public static LinkedList<Account> accounts = new LinkedList<>();

    public static Label[] labels = new Label[6];

    public static Account account;

    public static void addAccount(Account account) {
        accounts.add(account);
    }

    public static void importData()  {
        try {
            ObjectInputStream data = new ObjectInputStream(new FileInputStream(dataFilePath));
            Account account;
            while ((account = (Account) data.readObject()) != null)
                accounts.addLast(account);

            data.close();
        } catch (FileNotFoundException e) {
            createFolder();
            importData();
        } catch (Exception ignored) {}
    }

    public static void exportData() {
        try {
            ObjectOutputStream data = new ObjectOutputStream(new FileOutputStream(dataFilePath));
            for (Account account : accounts)
                data.writeObject(account);

            data.flush();
            data.close();
        } catch (FileNotFoundException e) {
            createFolder();
            exportData();
        } catch (Exception ignored) {}
    }

    private static void createFolder() {
        File file = new File(dataFolderPath);
        file.mkdirs();
    }
}
