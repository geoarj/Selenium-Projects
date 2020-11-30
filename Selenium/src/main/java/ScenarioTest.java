import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScenarioTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before () {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 150);

    }

    @Test
    public void exampleScenario () {
        // 1 адрес сайта

        driver.get("https://www.rgs.ru/");

        // 2 выбрать пункт Меню
        String menuButtonXPath = "//a[contains(text(),'Меню') and @data-toggle='dropdown']";
        List<WebElement> menuButtonList = driver.findElements(By.xpath(menuButtonXPath));
        waitUtilElementToBeClickable(menuButtonList.get(0));
        if (!menuButtonList.isEmpty()) {
            menuButtonList.get(0).click();
        }

        // 3 выбрать пункт подменю - "ДМС"
        String RgsInsuranceButtonXPath = "//a[contains(text(), 'ДМС')]";
        WebElement rgsInsuranceButton = driver.findElement(By.xpath(RgsInsuranceButtonXPath));
        waitUtilElementToBeClickable(rgsInsuranceButton);
        rgsInsuranceButton.click();

        // 4 проверка открытия страницы "ДМС"
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС — добровольное медицинское страхование", driver.getTitle());

        // 5 перейти к опции "Отправить заявку"
        String rgsRequest = "//a[contains(text(),'Отправить заявку')]";
        WebElement rgsSendRequest = driver.findElement(By.xpath(rgsRequest));
        waitUtilElementToBeClickable(rgsSendRequest);
        rgsSendRequest.click();

        // 6 проверка открытия страницы "Заявка на ДМС"
        String pageTitleXPath = "//b[contains(text(), 'Заявка на добровольное медицинское страхование')]";
        waitUtilElementToBeVisible(By.xpath(pageTitleXPath));
        WebElement pageTitle = driver.findElement(By.xpath(pageTitleXPath));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Заявка на добровольное медицинское страхование", pageTitle.getText());

        //7 Заполнить данными
        String lastNameXpath = "//input[contains(@data-bind, 'value:LastName')]";
        WebElement lastNameElement = driver.findElement(By.xpath(lastNameXpath));
        lastNameElement.sendKeys("Пупкин");

        String firstNameXpath = "//input[contains(@data-bind, 'value:FirstName')]";
        WebElement firstNameElement = driver.findElement(By.xpath(firstNameXpath));
        firstNameElement.sendKeys("Вася");

        String middleNameXpath = "//input[contains(@data-bind, 'value:MiddleName')]";
        WebElement middleNameElement = driver.findElement(By.xpath(middleNameXpath));
        middleNameElement.sendKeys("Иванович");

        String selectRegionXpath = "//select[@name = 'Region']";
        WebElement selectRegionElement = driver.findElement(By.xpath(selectRegionXpath));
        new Select(selectRegionElement).selectByVisibleText("Санкт-Петербург");

        String phoneXpath = "//input[contains(@data-bind, 'value: Phone')]";
        WebElement phoneElement = driver.findElement(By.xpath(phoneXpath));
        phoneElement.click();
        phoneElement.sendKeys("9174040897");

        String emailXpath = "//input[contains(@data-bind, 'value: Email')]";
        WebElement emailElement = driver.findElement(By.xpath(emailXpath));
        emailElement.sendKeys("qwertyqwerty");

        String contactDateXpath = "//input[@name='ContactDate']";
        WebElement contactDateElement = driver.findElement(By.xpath(contactDateXpath));
        contactDateElement.click();
        contactDateElement.sendKeys("30112020");

        String commentsXpath = "//textarea[contains(@data-bind, 'value: Comment')]";
        WebElement commentsElement = driver.findElement(By.xpath(commentsXpath));
        commentsElement.sendKeys("Не звонить до 12 утра!");

        String checkboxXpath = "//input[@class='checkbox']";
        WebElement checkboxElement = driver.findElement(By.xpath(checkboxXpath));
        if ( !checkboxElement.isSelected() )
        {
            checkboxElement.click();
        }

        // 8 Проверить все поля

        Assert.assertEquals("Не все поля заполнены " + lastNameElement.toString(), lastNameElement.getAttribute("value"), "Пупкин");
        Assert.assertEquals("Не все поля заполнены " + firstNameElement.toString(), firstNameElement.getAttribute("value"), "Вася");
        Assert.assertEquals("Не все поля заполнены " + middleNameElement.toString(), middleNameElement.getAttribute("value"), "Иванович");
        Assert.assertEquals("Не все поля заполнены " + emailElement.toString(), emailElement.getAttribute("value"), "qwertyqwerty");
        Assert.assertEquals("Не все поля заполнены " + commentsElement.toString(), commentsElement.getAttribute("value"), "Не звонить до 12 утра!");
        Assert.assertEquals("Не все поля заполнены " + phoneElement.toString(), phoneElement.getAttribute("value"), "+7" + " (917) 404-08-97");
        Assert.assertEquals("Не все поля заполнены " + contactDateElement.toString(), contactDateElement.getAttribute("value"), "30112020");
        Assert.assertEquals("Не все поля заполнены " + selectRegionElement.toString(), "78", selectRegionElement.getAttribute("value"));
        Assert.assertTrue("Checkbox is not selected", checkboxElement.isSelected());

        // 9 нажать кнопку "Отправить"
        String sendButtonXPath = "//button[contains(text(), 'Отправить')]";
        WebElement sendButton = driver.findElement(By.xpath(sendButtonXPath));
        waitUtilElementToBeClickable(sendButton);
        sendButton.click();

        // 10. Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке - Введите корректный email
        String errorMessageXPath = "//span[contains(text(), 'Введите адрес электронной почты')]";
        WebElement errorMessage = driver.findElement(By.xpath(errorMessageXPath));
        waitUtilElementToBeClickable(errorMessage);
        Assert.assertEquals("Проверка на ошибку не пройдена", "Введите адрес электронной почты", errorMessage.getText());
    }

    @After
    public void after(){
        driver.quit();
    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assert.assertEquals("Поле было заполнено некорректно",
                value, element.getAttribute("value"));
    }

}
