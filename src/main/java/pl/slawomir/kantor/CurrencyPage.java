package pl.slawomir.kantor;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrencyPage {

    private static final String currencyURL = "https://panel.rkantor.com/#/currency";
    private WebDriver driver;
    private String currency;
    private Select select;
    private WebElement exchangeBuy;
    private WebElement exchangeSell;
    private WebDriverWait wait;
    private LoginAction session;

    public CurrencyPage(WebDriver driver, LoginAction session){
        this.driver = driver;
        this.session = session;
        driver.get(currencyURL);

        currency = "EUR/PLN";
        wait = new WebDriverWait(driver, 5);
        refresh();
    }

    public void setCurrency(String currency){
        this.currency = currency;
        if(!driver.getCurrentUrl().equals(currencyURL)) {
            refresh();
        }

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("currency-pair")));
        } catch (NoSuchElementException e){
            System.out.println("Coś się popusło");
        }
        select.selectByVisibleText(currency);
    }

    public String[] getCurrencyList(){
        try {
            String all = driver.findElement(By.id("currency-pair")).getText();
            all = all.replace(" ", "");
            String[] currencyList = all.split("\n");
            return currencyList;
        } catch (NoSuchElementException e){
            refresh();
            return getCurrencyList();
        }
    }

    public String getCurrency(){
        return currency;
    }

    public Double getExchangeBuy(){
        if(!driver.getCurrentUrl().equals(currencyURL)){
            refresh();
        }
        return Double.valueOf(exchangeBuy.getText().replace(',', '.'));
    }

    public Double getExchangeSell(){
        if(!driver.getCurrentUrl().equals(currencyURL)){
            refresh();
        }
        return Double.valueOf(exchangeSell.getText().replace(',', '.'));
    }

    public void refresh(){
        driver.get(currencyURL);
        if(!driver.getCurrentUrl().equals(currencyURL)){
            session.login();
            driver.get(currencyURL);
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("currency-pair")));
            select = new Select(driver.findElement(By.id("currency-pair")));
            exchangeBuy = driver.findElement(By.xpath("//div[contains(@ng-click, 'currency.doTransaction(currency.action.buy)')]/div[contains(@class, 'rate ng-binding')]"));
            exchangeSell = driver.findElement(By.xpath("//div[contains(@ng-click, 'currency.doTransaction(currency.action.sell)')]")).
                    findElement(By.xpath("//div[contains(@class, 'rate ng-binding')]"));
            setCurrency(currency);
        } catch (TimeoutException e){
            session.login();
            refresh();
        } catch (NoSuchElementException e){
            session.login();
            refresh();
        } catch (StaleElementReferenceException e){
            session.login();
            refresh();
        }
    }
}
