package serenitylabs.tutorials.vetclinic.webdriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class WhenBookingATrain {
    WebDriver driver;
    private final String  fromLocationtext = "Town Hall Station";
    private final String  toLocationtext = "Perramatta Station";
    @Before
    public void setup() throws InterruptedException {
//        System.setProperty("webdriver.gecko.driver","C:\\geckodriver.exe");
//        driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.get("https://www.sydneytrains.info/");
        Thread.sleep(2000);
    }

    @After
    public void tearDown() {
        //driver.quit();
    }

    @Test
    public void should_be_able_to_plan_a_trip() {



        //Step:1 select from location
        WebElement fromLocation = driver.findElement(By.cssSelector("#tniFromTripLocation.autosuggest-input.form-control"));
        //fromlistAutoPolulated  css selector  : div.autosuggest-buttons.ng-star-inserted

        fromLocation.clear();
        fromLocation.click();

        fromLocation.sendKeys("Town Hall Station");
        WebElement fromAutoPopulated = driver.findElement(By.xpath("//div [contains(@class,'autosuggest-buttons')]//div[contains(.,'Town Hall Station')]"));
        fromAutoPopulated.click();

// search for the text and select logic.

        //Step:2 select  To Location
        WebElement toLocation = driver.findElement(By.xpath("//input[@id='tniToTripLocation'] [contains(@class,'autosuggest-input')]"));
        toLocation.clear();
        toLocation.click();
        toLocation.sendKeys("Parramatta Rd");
        WebElement toAutoPopulated = driver.findElement(By.xpath("//div [contains(@class,'autosuggest-buttons')]//div[contains(.,'Parramatta Rd')]"));
        toAutoPopulated.click();

        //Step:3 click on More Options Link

        WebElement MoreOptionsLink = driver.findElement(By.xpath("//button[contains(@class,'more-options-link')]"));
        MoreOptionsLink.click();
        //Step:4  click on leaving now

        WebElement LeavingNowTabTitle = driver.findElement(By.xpath("//span[contains(@class,'tab-title')][contains(.,'Leaving')]"));
        LeavingNowTabTitle.click();



        //Step:5  click on leaving button  //label[contains(@class,'leaving')]/span[contains(.,'Leaving')]
        WebElement LeavingButton = driver.findElement(By.xpath("//label[contains(@class,'leaving')]/span[contains(.,'Leaving')]"));
        LeavingButton.click();

        //Step:6 select date of travel
        Select searchSelectDate = new Select(driver.findElement(By.id("search-select-date")));
        // select hour  and minute
//		searchSelectDate.selectByIndex(1);
//		searchSelectDate.selectByValue("1");
        searchSelectDate.selectByVisibleText("Tomorrow (Sat)");

        //Step7: Select Hour :
        Select searchSelectHour = new Select(driver.findElement(By.id("search-select-hour")));
        searchSelectHour.selectByIndex(5);
        //Step8: Select Minute:   #search-select-minute index : 00,05,10,15,20,25,30,35,40,45,50,55 value: 0 -11
        Select searchSelectMinute = new Select(driver.findElement(By.id("search-select-hour")));
        searchSelectMinute.selectByIndex(10);
        //Step:9  click  on apply : //button[contains(@class,'date-time-btn')]
        WebElement ApplyButton = driver.findElement(By.xpath("//button[contains(@class,'date-time-btn')]"));
        ApplyButton.click();

        assertThat(LeavingNowTabTitle.getText(), containsString("Leaving 4:10, tomorrow (Sat)"));

        //Step10: verify for the trip planner results
        List<WebElement> TripPlannerSummaries =  driver.findElements(By.xpath("//div[contains(@class,'card-wrapper')]//div [contains(@class,'summary-header')]"));
        if(TripPlannerSummaries.size()>0) {
            System.out.println(" Search results for the planner :  "+TripPlannerSummaries.size());
        }

        for(WebElement planSummary : TripPlannerSummaries) {
            String textsummary = planSummary.getText();
            System.out.println(" Trip planner details :  "+planSummary.getText());
        }







    }
}
