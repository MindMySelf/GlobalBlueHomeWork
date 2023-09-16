package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VATCalculatorPage {
    private WebDriver driver;
    //Select element with countries
    @FindBy(name = "Country")
    private WebElement country;
    //Radio inputs
    @FindBy(id = "F1")
    private WebElement priceWithoutVAT;
    @FindBy(id = "F2")
    private WebElement valueAddedTax;
    @FindBy(id = "F3")
    private WebElement priceInclVAT;
    //Text inputs next to Radio box inputs above (same order)
    @FindBy(id = "NetPrice")
    private WebElement netPrice;
    @FindBy(id = "VATsum")
    private WebElement vatSum;
    @FindBy(id = "Price")
    private WebElement price;

    //VAT rate radio button(s)'s div
    @FindBy(className = "col-sm-6")
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
        return VATDiv.findElements(By.cssSelector("input[type='radio']"));
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
        return hasDefault.getText();
    }
    public String selectFirstVAT() {
        List<WebElement> radioButtons = getVATButtons();
        if(radioButtons.size() > 1) {
            return radioButtons.get(0).getText();
        } else {
            return "Has only one element";
        }
    }
    public Double getNumberValueOfSelected() {
        return Double.parseDouble(selectFirstVAT());
    }

    //Net-Gross-VAT radiobutton and input
    //select Net-Gross-VAT buttons
    public void selectNet () {
        priceWithoutVAT.click();
    }
    public void selectVAT () {
        valueAddedTax.click();
    }
    public void selectGross () {
        priceInclVAT.click();
    }
    //write value to Net-Gross-VAT inputs
    public void addNetInput(String value) {
        netPrice.clear();
        netPrice.sendKeys(value);
    }
    public void addVATInput(String value) {
        vatSum.clear();
        vatSum.sendKeys(value);
    }
    public void addGrossInput(String value) {
        price.clear();
        price.sendKeys(value);
    }
    //get Net-Gross-VAT input values
    public Map<String, Double> getNetVATGrossValues() {
        Map<String, Double> values = new HashMap<String, Double>();
        values.put("Net", Double.parseDouble(netPrice.getAttribute("value")));
        values.put("VAT", Double.parseDouble(vatSum.getAttribute("value")));
        values.put("Gross", Double.parseDouble(price.getAttribute("value")));
        return values;
    }
}
