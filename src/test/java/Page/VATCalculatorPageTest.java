package Page;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

class VATCalculatorPageTest {
    VATCalculatorPage page = new VATCalculatorPage(new ChromeDriver());
    //Country select tests
    @Test
    public void hasCountrySelectAnyOptionsTest() {
        page.openPage();
        int numberOfOptions = page.getAllOptions().size();
        page.closePage();
        assertTrue(numberOfOptions != 0);
    }
    @Test
    public void notvalidCountrySelectedTest() {
        page.openPage();
        assertThrows(NoSuchElementException.class, () -> {
            page.selectSpecificCountry("");
        });
        page.closePage();
    }
    @Test
    public void hasDefaultCountryTest() {
        page.openPage();
        assertNotNull(page.getDefaultCountry());
        page.closePage();
    }
    @Test
    public void getCorrectSelectValueWithValidOptionsTest() {
        String name = "";
        page.openPage();
        name = page.selectSpecificCountry("Hungary");
        assertEquals(name,"Hungary");
        name = page.selectSpecificCountry("United Kingdom");
        assertEquals(name, "United Kingdom");
        name = page.selectSpecificCountry("Mozambique");
        assertEquals(name,"Mozambique");
        name = page.selectSpecificCountry("Belgium");
        assertNotEquals(name,"Austria");
        name = page.selectSpecificCountry("Jamaica");
        assertNotEquals(name,"Oman");
        page.closePage();
    }

}