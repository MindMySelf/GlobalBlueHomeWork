package Page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VATCalculatorPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor javascriptExecutor;
    //Select element with countries
    @FindBy(name = "Country")
    private WebElement country;
    //Radio inputs
    @FindBy(css = "input[type='radio'][id='F1'][name='find']")
    private WebElement priceWithoutVAT;
    @FindBy(css = "input[type='radio'][id='F2'][name='find']")
    private WebElement valueAddedTax;
    @FindBy(css = "input[type='radio'][id='F3'][name='find']")
    private WebElement priceInclVAT;
    //Text inputs next to Radio box inputs above (same order)
    @FindBy(id = "NetPrice")
    private WebElement netPrice;
    @FindBy(id = "VATsum")
    private WebElement vatSum;
    @FindBy(id = "Price")
    private WebElement price;

    //VAT rate radio button(s)'s div
    @FindBy(xpath = "(//div[@class='row']//div[@class='col-sm-6 col-12 p-0 m-0'])[2]")
    private WebElement VATDiv;

    //error message span
    @FindBy(css = "span[background-color='rgb(192, 0, 0)'][color='white'] [padding='2px']")
    private WebElement errorSpan;

    public VATCalculatorPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.javascriptExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    //Opening and closing the webpage
    public void openPage() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
    }

    public void closePage() {
        driver.close();
    }

    //Country selection methods
    private Select getSelect() {
        return new Select(country);
    }

    public String getDefaultCountry() {
        return getSelect().getFirstSelectedOption().getText();
    }

    public String selectSpecificCountry(String country) {
        Select select = getSelect();
        select.selectByVisibleText(country);
        return select.getFirstSelectedOption().getText();
    }

    public int getAllOptions() {
        return getSelect().getOptions().size();
    }


    //VAT rate selection methods
    private List<WebElement> getVATButtons() {
        return VATDiv.findElements(By.cssSelector("input[class='css-radio']"));
    }

    public boolean hasAnyVAT() {
        return getVATButtons().size() != 0;
    }

    public String getDefaultSelectedVATValue() {
        List<WebElement> radioButtons = getVATButtons();
        WebElement hasDefault = null;
        for (WebElement radioButton : radioButtons) {
            if (
                    radioButton.getAttribute("checked") != null &&
                            radioButton.getAttribute("checked").equals("true")
            ) {
                javascriptExecutor.executeScript("arguments[0].click()", radioButton);
                hasDefault = radioButton;
                break;
            }
        }
        return hasDefault.getAttribute("value");
    }

    public String selectFirstVAT() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        List<WebElement> radioButtons = getVATButtons();
        if (radioButtons.size() > 1) {
            WebElement firstButton = radioButtons.get(0);
            javascriptExecutor.executeScript("arguments[0].click()", firstButton);

            return firstButton.getAttribute("value");
        } else {
            return "Has only one element";
        }
    }


    //Net-Gross-VAT radiobutton and input
    //select Net-Gross-VAT buttons
    public void selectNet() {
        javascriptExecutor.executeScript("arguments[0].click()", priceWithoutVAT);
    }

    public void selectVAT() {
        javascriptExecutor.executeScript("arguments[0].click()", valueAddedTax);
    }

    public void selectGross() {
        javascriptExecutor.executeScript("arguments[0].click()", priceInclVAT);

    }

    //write value to Net-Gross-VAT inputs
    public void addNetInput(String inputValue) {
//        javascriptExecutor.executeScript("arguments[0].value='" + inputValue + "'", netPrice);
        wait.until(ExpectedConditions.visibilityOf(netPrice));
        actions.moveToElement(netPrice)
                .click(netPrice)
                .sendKeys(netPrice, inputValue)
                .build()
                .perform();
        //wait.until(ExpectedConditions.stalenessOf(netPrice));
    }

    public void addVATInput(String inputValue) {
        javascriptExecutor.executeScript("arguments[0].value='" + inputValue + "'", vatSum);
        wait.until(ExpectedConditions.stalenessOf(vatSum));
    }

    public void addGrossInput(String inputValue) {
        javascriptExecutor.executeScript("arguments[0].value='" + inputValue + "'", price);
        wait.until(ExpectedConditions.stalenessOf(price));
    }

    //get Net-Gross-VAT input values
    public Map<String, String> getNetVATGrossValues() {
        Map<String, String> values = new HashMap<String, String>();
        values.put("Net", netPrice.getAttribute("value"));
        values.put("VAT", vatSum.getAttribute("value"));
        values.put("Gross", price.getAttribute("value"));
        return values;
    }
    //is error message displayed
    public boolean isErrorDisplayed() {
        return errorSpan.isDisplayed();
    }
}
