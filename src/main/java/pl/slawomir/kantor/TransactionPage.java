package pl.slawomir.kantor;

//import com.sun.deploy.util.Waiter;
//import org.junit.rules.Timeout;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Sławomir on 12.09.2017.
 */
public class TransactionPage {

    public static final String transactionURL = "https://panel.rkantor.com/#/transaction";

    Select baseCurrency;
    Select quotedCurrency;
    Select buySellSelect;

    WebElement baseAmountField;
    WebElement quotedAmountField;
    WebDriver driver;

    public final static int BUY = 0;
    public final static int SELL = 1;

    public TransactionPage(WebDriver driver){
        this.driver = driver;
        if(!driver.getCurrentUrl().equals(transactionURL))
            driver.get(transactionURL);

        boolean work = true;

        WebDriverWait wait = new WebDriverWait(driver,2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@id, 'button-type-otp')]")));

        while(work) {
            try {
                baseAmountField = driver.findElement(By.name("baseAmount"));
                System.out.println("OK");
                quotedAmountField = driver.findElement(By.name("quotedAmount"));
                System.out.println("OK");
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@id, 'button-type-otp')]")));
                driver.findElement(By.xpath("//button[contains(@id, 'button-type-otp')]")).click();
                System.out.println("OK");
                work = false;
            } catch (WebDriverException e) {
                work = true;
                if(!driver.getCurrentUrl().equals(transactionURL))
                    driver.get(transactionURL);
            }
        }

        while(driver.findElement(By.xpath("//input[contains(@name, 'isOrder')]")).isSelected()) {
            try{TimeUnit.MILLISECONDS.sleep(500);}catch(InterruptedException e){System.out.println("InterruptedException[TransactionPage]");}
            try{driver.findElement(By.xpath("//input[contains(@name, 'isOrder')]")).click();}catch(WebDriverException e){}
        }

        while(!driver.findElement(By.xpath("//input[contains(@name, 'otpAgreement')]")).isSelected()) {
            try{TimeUnit.MILLISECONDS.sleep(500);}catch(InterruptedException e){System.out.println("InterruptedException[TransactionPage]");}
            try{driver.findElement(By.xpath("//input[contains(@name, 'otpAgreement')]")).click();}catch(WebDriverException e){};
        }

        buySellSelect = new Select(driver.findElement(By.name("exchangeAction")));
        baseCurrency = new Select(driver.findElement(By.name("baseCurrency")));
        quotedCurrency = new Select(driver.findElement(By.name("quotedCurrency")));
    }

    public void setCurrency(String currency){
        String first = currency.substring(0, 3);
        String second = currency.substring(currency.length() - 3, currency.length());

        baseCurrency.selectByVisibleText(first);
        quotedCurrency.selectByVisibleText(second);
    }

    public void setAction(int index){
        buySellSelect.selectByIndex(index);
    }

    public void setBaseAmount(String base){
        if(base.length() > 0){
            base = base.replace('.',',');
            baseAmountField.clear();
            baseAmountField.sendKeys(base);
        }
    }

    public void setQuotedAmount(String quoted){
        if(quoted.length() > 0){
            quoted = quoted.replace('.',',');
            quotedAmountField.clear();
            quotedAmountField.sendKeys(quoted);
        }
    }

    public void realize(){
        WebElement next = driver.findElement(By.xpath("//button[contains(@id, 'button-transaction-submit')]"));
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            //wait.until(ExpectedConditions.elementToBeClickable(next));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@id, 'button-transaction-submit')]")));
        } catch (WebDriverException e){
            System.out.println("Przekroczono czas[realize.TransactionPage]");
        }

        try{
            TimeUnit.MILLISECONDS.sleep(200);
        } catch(InterruptedException e){
            System.out.println("Przekroczono czas[realize.TransactionPage]");
        }

        next.click();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().maximize();
    }
}
