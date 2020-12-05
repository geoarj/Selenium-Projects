import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


@RunWith(Parameterized.class)

public class SberScenarioTest extends AfterBefore {

    @Parameterized.Parameters
    public static Collection<Object[]> getParams(){
        return Arrays.asList(new Object[][]{
                {"Пупов", "Юрий", "Григорьевич", "laiman@mail.ru", " (971) 333-44-55", "07.09.1998"},
                {"Васильев", "Иван", "Васильевич", "gerogut@mail.ru", " (911) 654-78-98", "11.11.1911"},
                {"Волк", "Волк", "Волкович", "volk@yandex.ru", " (499) 543-33-44", "03.12.1987"},
        });
    }
    @Parameterized.Parameter(0)
    public String lName;
    @Parameterized.Parameter(1)
    public String fName;
    @Parameterized.Parameter(2)
    public String mName;
    @Parameterized.Parameter(3)
    public String emailVal;
    @Parameterized.Parameter(4)
    public String phoneVal;
    @Parameterized.Parameter(5)
    public String birthDate;

    @Test
    public void SberTest() {

        //2 find Cards
        String cardsLinkXpath = "//a[@aria-label='Меню  Карты']";
        WebElement cardsLinkEl = driver.findElement(By.xpath(cardsLinkXpath));
        cardsLinkEl.click();

        //3 select debit cards
        String concreteCardXpath = "//li/a[contains(text(), 'Дебетовые карты')]";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(concreteCardXpath)));
        driver.findElement(By.xpath(concreteCardXpath)).click();

        //4 verify the title
        Assert.assertEquals("Does not correspond the expected result", "Дебетовые карты", (driver.findElement(By.xpath("//h1[contains(text(), 'Дебетовые карты')]")).getText()));

        //5 choose "Молодёжная карта"
        String youthCardXpath = "//a[@data-product=\"Молодёжная карта\"]/span[contains(text(), 'Заказать онлайн')]";
        WebElement youthCardEl = driver.findElement(By.xpath(youthCardXpath));
        scrollToElement(youthCardEl);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(youthCardXpath)));
        youthCardEl.click();

        //6 check the title «Молодёжная карта»
        Assert.assertEquals("Does not correspond the expected result", "Молодёжная карта", (driver.findElement(By.xpath("//h1[contains(text(), 'Молодёжная карта')]")).getText()));

        //7 Кликнуть на кнопку «Оформить онлайн»
        String orderOnlineXpath = "//a[@data-test-id=\"PageTeaserDict_button\"]/span[contains(text(), 'Оформить онлайн')]";
        WebElement orderOnlineEl = driver.findElement(By.xpath(orderOnlineXpath));
        scrollToElement(orderOnlineEl);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(orderOnlineXpath)));
        sleep();
        orderOnlineEl.click();

        //8 fill the fields

        String lastNameXpath = "//input[@data-name=\"lastName\"]";
        WebElement lastNameElement = driver.findElement(By.xpath(lastNameXpath));
        lastNameElement.sendKeys(lName);

        String firstNameXpath = "//input[@data-name=\"firstName\"]";
        WebElement firstNameElement = driver.findElement(By.xpath(firstNameXpath));
        firstNameElement.sendKeys(fName);

        String middleNameXpath = "//input[@data-name=\"middleName\"]";
        WebElement middleNameElement = driver.findElement(By.xpath(middleNameXpath));
        middleNameElement.sendKeys(mName);

        String phoneXpath = "//input[@data-name=\"phone\"]";
        WebElement phoneElement = driver.findElement(By.xpath(phoneXpath));
        scrollToElement(phoneElement);
        sleep();
        phoneElement.click();
        phoneElement.sendKeys(phoneVal);

        String birthDateXpath = "//input[@data-name=\"birthDate\"]";
        WebElement birthDateElement = driver.findElement(By.xpath(birthDateXpath));
        birthDateElement.sendKeys(birthDate);

        String emailXpath = "//input[@data-name=\"email\"]";
        WebElement emailElement = driver.findElement(By.xpath(emailXpath));
        emailElement.sendKeys(emailVal);

        //9 Проверить, что все поля заполнены правильно
        Assert.assertEquals("Поле не заполнено " + lastNameElement.toString(), lastNameElement.getAttribute("value"), lName);
        Assert.assertEquals("Поле не заполнено " + firstNameElement.toString(), firstNameElement.getAttribute("value"), fName);
        Assert.assertEquals("Поле не заполнено " + middleNameElement.toString(), middleNameElement.getAttribute("value"), mName);
        Assert.assertEquals("Поле не заполнено " + phoneElement.toString(), phoneElement.getAttribute("value"), "+7" + phoneVal);
        Assert.assertEquals("Поле не заполнено " + birthDateElement.toString(), birthDateElement.getAttribute("value"), birthDate);
        Assert.assertEquals("Поле не заполнено " + emailElement.toString(), emailElement.getAttribute("value"), emailVal);

        //10 Нажать «Далее»
        String pressNextXpath = "//span[contains(text(), 'Далее')]";
        WebElement pressNextEl = driver.findElement(By.xpath(pressNextXpath));
        scrollToElement(pressNextEl);
        sleep();
        pressNextEl.click();

        //11 Проверить, что появилось сообщение именно у незаполненных полях – «Обязательное поле»
        Assert.assertEquals("Does not correspond the expected result", "Обязательное поле", (driver.findElement(By.xpath("//input[@data-name=\"series\"]/following-sibling::div")).getText()));
        Assert.assertEquals("Does not correspond the expected result", "Обязательное поле", (driver.findElement(By.xpath("//input[@data-name=\"number\"]/following-sibling::div")).getText()));
        Assert.assertEquals("Does not correspond the expected result", "Обязательное поле", (driver.findElement(By.xpath("//label[contains(text(), 'Дата выдачи')]/following-sibling::div[@class='odcui-error__text']")).getText()));

    }

    private void scrollToElement(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
