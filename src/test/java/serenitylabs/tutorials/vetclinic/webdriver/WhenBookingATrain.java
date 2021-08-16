package serenitylabs.tutorials.vetclinic.webdriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static serenitylabs.tutorials.vetclinic.webdriver.DepaturePreference.*;

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
        depatureStationIs("Town Hall Station");

// search for the text and select logic.

        //Step:2 select  To Location
        destinationStationIs("Parramatta Rd");


        //Step:3 click on More Options Link

        WebElement MoreOptionsLink = driver.findElement(By.xpath("//button[contains(@class,'more-options-link')]"));
        MoreOptionsLink.click();
        //Step:4  click on leaving now

        WebElement LeavingNowTabTitle = driver.findElement(By.cssSelector("#time-tab.tni-tab-header"));
        LeavingNowTabTitle.click();



        //Step:5  click on leaving button  //label[contains(@class,'leaving')]/span[contains(.,'Leaving')]
        theTrainShould(Leaving,11,55,TravelDay.Tomorrow);

        //Step:6 select date of travel


        //Step7: Select Hour :
       
        //Step8: Select Minute:   #search-select-minute index : 00,05,10,15,20,25,30,35,40,45,50,55 value: 0 -11
      
        //Step:9  click  on apply : //button[contains(@class,'date-time-btn')]
        planTrip();

        assertThat(LeavingNowTabTitle.getText(), containsString(LeavingNowTabTitle.getText()));

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

    private void planTrip() {
        WebElement ApplyButton = driver.findElement(By.xpath("//button[contains(@class,'date-time-btn')]"));
        ApplyButton.click();
    }

    static final Map<DepaturePreference, By> DEPATURE_RADIO_BUTTONS = new HashMap<>();
    static {
        DEPATURE_RADIO_BUTTONS.put(Now,By.xpath("//label[contains(@class,'now')]/span[contains(.,'Now')]"));
        DEPATURE_RADIO_BUTTONS.put(Leaving,By.xpath("//label[contains(@class,'leaving')]/span[contains(.,'Leaving')]"));
        DEPATURE_RADIO_BUTTONS.put(Arriving,By.xpath("//label[contains(@class,'arriving')]/span[contains(.,'Arriving')]"));
    }

    private static final DecimalFormat TIME_UNIT_FORMAT  = new DecimalFormat("##");

    private void theTrainShould(DepaturePreference depaturePreference,int hour, int minute,TravelDay travelDay) {
        driver.findElement(DEPATURE_RADIO_BUTTONS.get(depaturePreference)).click();
        TravelDay.setDaysInFuture(2);
        new Select(driver.findElement(By.cssSelector("select#search-select-date")))

                .selectByIndex(TravelDay.getDaysInFuture());
                // select hour  and minute
        Select hourList = new Select(driver.findElement(By.cssSelector("#search-select-hour")));
        hourList.selectByVisibleText(TIME_UNIT_FORMAT.format(hour));
        //Step8: Select Minute:   #search-select-minute index : 00,05,10,15,20,25,30,35,40,45,50,55 value: 0 -11
        Select minuteList = new Select(driver.findElement(By.cssSelector("#search-select-minute")));
        minuteList.selectByVisibleText(TIME_UNIT_FORMAT.format(minute));
    }

    private void destinationStationIs(String station) {
        WebElement toLocation = driver.findElement(By.xpath("//input[@id='tniToTripLocation'] [contains(@class,'autosuggest-input')]"));
        toLocation.clear();
        toLocation.click();
        toLocation.sendKeys(station);
        //WebElement toAutoPopulated = driver.findElement(By.xpath("//div [contains(@class,'autosuggest-buttons')]//div[contains(.,'Parramatta Rd')]"));
        //toAutoPopulated.click();
        selectAutoSuggestionList( station);
    }



    private void depatureStationIs(String depatureStation) {
        WebElement fromLocation = driver.findElement(By.cssSelector("#tniFromTripLocation.autosuggest-input.form-control"));
        fromLocation.clear();
        fromLocation.click();
        fromLocation.sendKeys(depatureStation);
//        WebElement fromAutoPopulated = driver.findElement(By.xpath("//div [contains(@class,'autosuggest-buttons')]//div[contains(.,depatureStation)]"));
//        fromAutoPopulated.click();
        selectAutoSuggestionList( depatureStation);
    }

    private String selectAutoSuggestionList(String depatureStation) {
        // cssSelector: div.autosuggest-buttons [class='item']
        // Xpath : to select the first auto suggestion : //div [contains(@class,'autosuggest-buttons')]//div[@text()='Town Hall Station']
        String choosenLocation = "";
        List<WebElement> chooseALocations = driver.findElements(By.cssSelector("div.item[id*='autosuggest-item-']"));
        for ( WebElement chooseLocation :  chooseALocations ) {
            System.out.println("choosen Item Get TExt before "+chooseLocation.getText().toString() );
            chooseLocation.click();
            break;
//            if (chooseLocation.getText().equals(depatureStation)){
//                System.out.println("choosen Item Get TExt After "+chooseLocation.getText() );
//                chooseLocation.click();
//               String  selectedLocation = chooseLocation.getText();
//            }

        }

        return choosenLocation;
    }
}
