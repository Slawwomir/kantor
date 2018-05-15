package pl.slawomir.kantor;

import java.awt.*;

public class StartPage {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}
