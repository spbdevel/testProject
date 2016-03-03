package com.trial.properties;

import com.trial.properties.app.TrialAppPropertiesManager;
import com.trial.properties.app.interfaces.AppProperties;
import com.trial.properties.app.interfaces.AppPropertiesManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;


/**
 * Provides example usage of the API and classes - although candidates can use this
 * Main method to test if their changes will be accepted by the autograder. If your
 * solution is incompatible with this main method, it will be incompatible with the
 * autograder and may cause your solution to be failed.
 * 
 * @author code test administrator
 */
public final class Main {

    /**
     * Main method useful for your testing, this method is not tested by the grader.
     *
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */

    public static void main(String[] args) throws URISyntaxException, IOException {
        List<String> propertySourceUris = Arrays.asList(args);

        // invoke the property parser and print out properties alphabetically
        AppPropertiesManager manager = TrialAppPropertiesManager.getInstance();
        AppProperties props = manager.loadProps(propertySourceUris);
        manager.printProperties(props, System.out);
        if(!props.isValid())
            throw new RuntimeException("Not all properties are set");
    }
}
