package pl.slawomir.kantor;

/**
 * Created by Sławomir on 06.09.2017.
 */

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Dim;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.util.concurrent.TimeUnit;


public class LoginAction {

    public static final String panelURL = "https://panel.rkantor.com/";
    public static final String LOC_BTN_LOGIN = "span[class='btn-text ng-binding']";
    private static WebDriver driver;
    private static String username;
    private static char[] password;
    public boolean isOnline;
    private WebDriverWait wait;
    private JFrame frame;

    public LoginAction(LoginFrame frame, String username, char[] password) throws InterruptedException, NoSuchMethodException {
        this.frame = frame;
        this.isOnline = false;
        this.username = username;
        this.password = password;

        driver = frame.driver;
    }

    public boolean login(){
        WebDriverWait wait;
        wait = new WebDriverWait(driver, 5);

        driver.get(panelURL);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email-field")));
        } catch (TimeoutException e){
            JOptionPane.showMessageDialog(frame, "Przekroczono limit czasu oczekiwania na połączenie.\n", "TimeoutException", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        driver.findElement(By.id("email-field")).sendKeys(username);
        driver.findElement(By.id("password-field")).sendKeys(String.valueOf(password));
        driver.findElement(By.id("button-login-submit")).submit();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-menu-logout")));
        } catch (TimeoutException e){
            JOptionPane.showMessageDialog(frame, "Nieudana próba logowania.", "TimeoutException", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        isOnline = true;
        return true;
    }

    public boolean logout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e){
            return false;
        }
        try {
            driver.findElement(By.id("button-menu-logout")).click();
            isOnline = false;
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public WebDriver getDriver(){
        return driver;
    }
}
