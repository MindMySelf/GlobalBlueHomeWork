package Page;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

class VATCalculatorPageTest {
    //write webdriver initialization or object creation into before each
    VATCalculatorPage page = new VATCalculatorPage(new ChromeDriver());
    //Country select tests
    @Test
    public void hasCountrySelectAnyOptionsTest() {
        page.openPage();
        int numberOfOptions = page.getAllOptions();
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
        assertEquals("",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.addGrossInput("123");
        assertEquals("",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
        //Somehow can write into not selected parts without interruption, so this test fails
    }
    @Test
    public void canSelectVATRadioButtonOnlyVATInputFieldWritableTest() {
        page.openPage();
        page.selectFirstVAT();
        page.selectVAT();
        page.addNetInput("123");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.addGrossInput("123");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void canSelectGrossRadioButtonOnlyGrossInputFieldWritableTest() {
        page.openPage();
        page.selectFirstVAT();
        page.selectGross();
        page.addVATInput("123");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("",page.getNetVATGrossValues().get("Gross"));
        page.addNetInput("123");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
        //Somehow can write into not selected parts without interruption, so this test fails
    }
    //Calculator tests with no digits
    @Test
    public void noDigitsInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("100");
        assertEquals("100",page.getNetVATGrossValues().get("Net"));
        assertEquals("20.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("120.00",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("100");
        assertEquals("500.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("100",page.getNetVATGrossValues().get("VAT"));
        assertEquals("600.00",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("100");
        assertEquals("83.33",page.getNetVATGrossValues().get("Net"));
        assertEquals("16.67",page.getNetVATGrossValues().get("VAT"));
        assertEquals("100",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void oneDigitInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("100.1");
        assertEquals("100.1",page.getNetVATGrossValues().get("Net"));
        assertEquals("20.02",page.getNetVATGrossValues().get("VAT"));
        assertEquals("120.12",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("100.4");
        assertEquals("502.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("100",page.getNetVATGrossValues().get("VAT"));
        assertEquals("602.40",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("124.5");
        assertEquals("103.75",page.getNetVATGrossValues().get("Net"));
        assertEquals("20.75",page.getNetVATGrossValues().get("VAT"));
        assertEquals("124.5",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void twoDigitsInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("59.43");
        assertEquals("59.43",page.getNetVATGrossValues().get("Net"));
        assertEquals("11.89",page.getNetVATGrossValues().get("VAT"));
        assertEquals("71.32",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("31.48");
        assertEquals("157.40",page.getNetVATGrossValues().get("Net"));
        assertEquals("31.48",page.getNetVATGrossValues().get("VAT"));
        assertEquals("188.88",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("100.09");
        assertEquals("83.41",page.getNetVATGrossValues().get("Net"));
        assertEquals("16.68",page.getNetVATGrossValues().get("VAT"));
        assertEquals("100.09",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void threeDigitsInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("59.434");
        assertEquals("59.43",page.getNetVATGrossValues().get("Net"));
        assertEquals("11.89",page.getNetVATGrossValues().get("VAT"));
        assertEquals("71.32",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("31.489");
        assertEquals("157.40",page.getNetVATGrossValues().get("Net"));
        assertEquals("31.48",page.getNetVATGrossValues().get("VAT"));
        assertEquals("188.88",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("100.090");
        assertEquals("83.41",page.getNetVATGrossValues().get("Net"));
        assertEquals("16.68",page.getNetVATGrossValues().get("VAT"));
        assertEquals("100.09",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void moreDigitsInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("121.3478");
        assertEquals("121.34",page.getNetVATGrossValues().get("Net"));
        assertEquals("24.27",page.getNetVATGrossValues().get("VAT"));
        assertEquals("145.61",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("13.0195");
        assertEquals("65.05",page.getNetVATGrossValues().get("Net"));
        assertEquals("13.01",page.getNetVATGrossValues().get("VAT"));
        assertEquals("78.06",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("1,5482");
        assertEquals("1.28",page.getNetVATGrossValues().get("Net"));
        assertEquals("0.26",page.getNetVATGrossValues().get("VAT"));
        assertEquals("1,54",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void zeroInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("0");
        assertEquals("0",page.getNetVATGrossValues().get("Net"));
        assertEquals("0.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("0.00",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("0");
        assertEquals("0.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("0",page.getNetVATGrossValues().get("VAT"));
        assertEquals("0.00",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("0");
        assertEquals("0.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("0.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void biggerThanLimitInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("1000000000");
        assertEquals("1000000000",page.getNetVATGrossValues().get("Net"));
        assertEquals("0.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("0.00",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("1000000000");
        assertEquals("0.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("0",page.getNetVATGrossValues().get("VAT"));
        assertEquals("0.00",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("1000000000");
        assertEquals("0.00",page.getNetVATGrossValues().get("Net"));
        assertEquals("0.00",page.getNetVATGrossValues().get("VAT"));
        assertEquals("",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void negativeInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("-10");
        assertTrue(page.isErrorDisplayed());
        page.selectVAT();
        page.addVATInput("-1235");
        assertTrue(page.isErrorDisplayed());
        page.selectGross();
        page.addGrossInput("-8629.15 ");
        assertTrue(page.isErrorDisplayed());
        page.closePage();
    }
    @Test
    public void noInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("");
        assertEquals("",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }
    @Test
    public void invalidInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.selectNet();
        page.addNetInput("vrwab");
        assertEquals("vrwab",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.selectVAT();
        page.addVATInput("+bt32-vds");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("+bt32-vds",page.getNetVATGrossValues().get("VAT"));
        assertEquals("NaN",page.getNetVATGrossValues().get("Gross"));
        page.selectGross();
        page.addGrossInput("/*-+:_Üő^");
        assertEquals("NaN",page.getNetVATGrossValues().get("Net"));
        assertEquals("NaN",page.getNetVATGrossValues().get("VAT"));
        assertEquals("/*-+:_Üő^",page.getNetVATGrossValues().get("Gross"));
        page.closePage();
    }

    //additional tests
    @Test
    public void actionsInputTest() {
        page.openPage();
        page.getDefaultSelectedVATValue();
        page.addNetInput("100");
        assertEquals("20",page.getNetVATGrossValues().get("VAT"));
        page.closePage();
    }
}