package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class VATCalculatorPage {
    private WebDriver driver;

    public VATCalculatorPage(WebDriver driver) {
        this.driver = driver;
    }

    //Select element with countries
    private By country = By.name("Country");

    //Radio inputs
    private By PriceWithoutVAT = By.id("F1");
    private By ValueAddedTax = By.id("F2");
    private By PriceInclVAT = By.id("F3");

    //Text inputs next to Radio box inputs above (same order)
    private By NetPrice = By.id("NetPrice");
    private By VATSum = By.id("VATsum");
    private By Price = By.id("Price");

    private void navigateToPage() {
        driver.get("https://www.calkoo.com/en/vat-calculator");
    }


}
