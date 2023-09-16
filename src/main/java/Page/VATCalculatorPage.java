package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public VATCalculatorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
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

    public WebElement getDefaultCountry() {
        return getSelect().getFirstSelectedOption();
    }

    public String selectSpecificCountry(String country) {
        Select select = getSelect();
        select.selectByVisibleText(country);
        return select.getFirstSelectedOption().getText();
    }

    public List<WebElement> getAllOptions() {
        return getSelect().getOptions();
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", firstButton);

            return firstButton.getAttribute("value");
        } else {
            return "Has only one element";
        }
    }

    public Double getNumberValueOfSelected() {
        return Double.parseDouble(selectFirstVAT());
    }

    //Net-Gross-VAT radiobutton and input
    //select Net-Gross-VAT buttons
    public void selectNet() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", priceWithoutVAT);
    }

    public void selectVAT() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", valueAddedTax);
    }

    public void selectGross() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", priceInclVAT);

    }

    //write value to Net-Gross-VAT inputs
    public void addNetInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", netPrice);
        wait.until(ExpectedConditions.stalenessOf(netPrice));
    }

    public void addVATInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", vatSum);
        wait.until(ExpectedConditions.stalenessOf(vatSum));
    }

    public void addGrossInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", price);
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
}
