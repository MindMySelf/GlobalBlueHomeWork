package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VATCalculatorPage {
    private WebDriver driver;
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

    public String baseOption() {
        Select select = getSelect();
        return select.getFirstSelectedOption().getText();
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", firstButton);   //wanted to use firstButton.click(), but did not work on my device
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
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", priceWithoutVAT); //wanted to use priceWithoutVAT.click(), but did not work on my device
    }

    public void selectVAT() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", valueAddedTax); //wanted to use valueAddedTax.click(), but did not work on my device
    }

    public void selectGross() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", priceInclVAT); //wanted to use priceInclVAT.click(), but did not work on my device
    }

    //write value to Net-Gross-VAT inputs
    public void addNetInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", netPrice); //wanted to use netPrice.click(), but did not work on my device
        netPrice.clear();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", netPrice); //wanted to use vatSum.sendKeys but did not work on my device
    }

    public void addVATInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", vatSum); //wanted to use vatSum.click(), but did not work on my device
        vatSum.clear();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", vatSum); //wanted to use vatSum.sendKeys but did not work on my device
    }

    public void addGrossInput(String inputValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", price); //wanted to use price.click(), but did not work on my device
        price.clear();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + inputValue + "'", price); //wanted to use vatSum.sendKeys but did not work on my device
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
