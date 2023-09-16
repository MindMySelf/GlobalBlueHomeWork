package Page;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
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
    //VAT rate option tests
    @Test
    public void hasAnyVATOptionsTest() {
        page.openPage();
        assertTrue(page.hasAnyVAT());
        page.closePage();
    }
    @Test
    public void hasSelectedVATOptionTest() {
        page.openPage();
        page.selectSpecificCountry("Hungary");
        assertEquals("27", page.getDefaultSelectedVATValue());
        page.selectSpecificCountry("Oman");
        assertEquals("5", page.getDefaultSelectedVATValue());
        page.selectSpecificCountry("Greece");
        assertEquals("24", page.getDefaultSelectedVATValue());
        page.closePage();
    }
    @Test
    public void canSelectAnotherVATRateIfPossibleTest() {
        page.openPage();
        page.selectSpecificCountry("Hungary");
        assertEquals("5", page.selectFirstVAT());
        page.selectSpecificCountry("Oman");
        assertEquals("Has only one element", page.selectFirstVAT());
        page.selectSpecificCountry("Greece");
        assertEquals("6", page.selectFirstVAT());
        page.closePage();
    }

    //Input type selection and input field usability tests
    @Test
    public void canSelectNetRadioButtonOnlyNetInputFieldWritableTest() {
        page.openPage();
        page.selectFirstVAT();
        page.selectNet();
        page.addVATInput("123");
        page.addGrossInput("123");
        assertEquals("",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
        //Somehow can write into not selected parts without interruption, so this test fails
        //
    }
    @Test
    public void isValueCorrectWithNoDigitsWithInputInValidFieldTest() {
        page.openPage();
        page.selectFirstVAT();
        page.selectNet();
        page.addNetInput("100");
        assertEquals("100",page.getNetVATGrossValues().get("Net"));
        assertEquals("20.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("120.00",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
}