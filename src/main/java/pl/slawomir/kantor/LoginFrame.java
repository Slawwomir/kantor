package pl.slawomir.kantor;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sławomir on 08.09.2017.
 */
public class LoginFrame extends JFrame implements ActionListener{

    private JTextField mailField;
    private JPasswordField passwordField;
    private LoginAction session;
    private JButton loginButton;
    public WebDriver driver;

    public LoginFrame(){
        super("Logowanie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width/5, screenSize.height/2);
        setResizable(false);
        setLocationRelativeTo(null);
        createView();
        createWebDriver();
        pack();
        setVisible(true);
    }

    private void createWebDriver(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new org.openqa.selenium.Point(-1920,0));
        driver.manage().window().maximize();
    }

    private void createView(){
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelMain.add(panelForm);

        passwordField = new JPasswordField(15);
        mailField = new JTextField(15);
        loginButton = new JButton("Zaloguj");
        loginButton.setPreferredSize(new Dimension(200, 20));
        loginButton.addActionListener(this);



        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 50, 0, 50);

        c.gridx = 0;
        c.gridy = 0;
        panelForm.add(new JLabel("E-mail "), c);

        c.insets = new Insets(5, 50, 0, 50);
        c.gridy++;
        panelForm.add(mailField, c);

        c.gridy++;
        panelForm.add(new JLabel("Hasło"), c);

        c.gridy++;
        panelForm.add(passwordField ,c);

        c.gridy++;
        c.insets = new Insets(200, 0,20,0);
        panelForm.add(loginButton, c);

        c.gridx++;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == loginButton){
            try {

                session = new LoginAction(this, mailField.getText(), passwordField.getPassword());
                session.login();

                if(session.isOnline == false){
                    driver.manage().deleteAllCookies();
                }else{
                    dispose();
                    new MainWindow(this);
                }
            }catch (NoSuchMethodException ex){

            }catch (InterruptedException ep){

            }
        }
    }

    public LoginAction getSession(){
        return session;
    }

}
