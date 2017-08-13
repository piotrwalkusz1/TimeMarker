package com.piotrwalkusz.timemarker;

import com.piotrwalkusz.timemarker.view.MainWindow;
import javafx.application.Application;

public class Main {

    public static void main(String... args) throws Exception {
        JustOneAppLock appLock = new JustOneAppLock();
        if (appLock.tryLock()) {
            Application.launch(MainWindow.class, args);
        }
        else {
            System.exit(1);
        }
    }
}
