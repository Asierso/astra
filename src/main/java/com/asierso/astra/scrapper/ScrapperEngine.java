
package com.asierso.astra.scrapper;

import com.asierso.astra.exceptions.ActionException;
import com.asierso.astra.models.Action;
import com.asierso.astra.models.Action.FindableBy;
import static com.asierso.astra.models.Action.FindableBy.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author Asierso
 */
public class ScrapperEngine {

    private final ChromeDriver driver;

    public ScrapperEngine() {
        driver = new ChromeDriver();
    }

    public String executeAction(Action act) {
        List<WebElement> target = null;
        
        if (act.getFinder() != null && act.getElement() != null) {
            target = findElements(act.getFinder(), act.getElement());
        }
        
        String output = "";

        try {
            switch (act.getType()) {
                case GOTO -> { //Navigate to web
                    try {
                        driver.get(act.getParameters());
                    } catch (Exception e) { //URL format or page ot exists
                        System.out.println("Error. Tried to access to not-existand or invalid resource");
                    }
                }
                case CLICK -> { //Make click
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    target.get(0).click();
                }
                case GET_TEXT -> { //Get target text
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    output = target.get(0).getText();
                }
                case DELAY -> { //Wait time
                    try {
                        Thread.sleep(Integer.parseInt(act.getParameters()));
                    } catch (InterruptedException ignore) {

                    }
                }
                case PROMPT -> { //Prompt text
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    if (act.getParameters() == null) {
                        throw new ActionException("No parameters detected");
                    }
                    target.get(0).sendKeys(act.getParameters());
                }
                case SUBMIT -> { //Sumbit
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    target.get(0).submit();
                }
                case GET_ALL_TEXT -> { //Get all target text
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    for (WebElement handle : target) {
                        output += handle.getText() + " ";
                    }
                }
                case GET_ATTRIBUTE -> {
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    output = target.get(0).getAttribute(act.getParameters());
                }
                case GET_ALL_ATTRIBUTE -> {
                    if (target == null) {
                        throw new ActionException("Null target selected");
                    }
                    for (WebElement handle : target) {
                        output += handle.getAttribute(act.getParameters()) + " ";
                    }
                }
            }
        } catch (ActionException e) {
            System.out.println("Action exception " + e.getMessage() + " at " + e.getLocalizedMessage());
        }

        return output;
    }

    private List<WebElement> findElements(FindableBy findable, String params) {
        if (findable != null) {
            switch (findable) {
                case ID -> {
                    return driver.findElements(By.id(params));
                }
                case CLASS -> {
                    return driver.findElements(By.className(params));
                }
                case CSS -> {
                    return driver.findElements(By.cssSelector(params));
                }
                case TAG -> {
                    return driver.findElements(By.tagName(params));
                }
                case XPATH -> {
                    return driver.findElements(By.xpath(params));
                }
            }
        }

        return null;
    }

    public void stop() {
        driver.quit();
    }
}
