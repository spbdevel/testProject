package com.trial.properties.app;

import com.trial.properties.app.interfaces.AppProperties;
import com.trial.properties.app.interfaces.AppPropertiesManager;
import org.junit.Assert;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple main method to load and print properties. You should feel free to change this class
 * or to create additional class. You may add additional methods, but must implement the
 * AppPropertiesManager API contract.
 * 
 * Note: a default constructor is required
 *
 * @author code test administrator
 */
public final class TrialAppPropertiesManager implements AppPropertiesManager {


    private static final Logger logger = Logger.getLogger(TrialAppPropertiesManager.class.getName());


    private static final TrialAppPropertiesManager INSTANCE = new TrialAppPropertiesManager();


    static {
        URL.setURLStreamHandlerFactory(protocol -> "classpath".equals(protocol) ? new URLStreamHandler() {
            protected URLConnection openConnection(URL url) throws IOException {
                return new URLConnection(url) {

                    public void connect() throws IOException {
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        String file = url.getFile();
                        if(!file.startsWith("resources"))
                            throw new MalformedURLException("URL should begin with classpath:resources/");
                        String resourceName = file.replaceFirst("resources", "");
                        return getClass().getResourceAsStream(resourceName);
                    }

                };
            }
        } : null);
    }


    private TrialAppPropertiesManager () {
    }


    public static TrialAppPropertiesManager getInstance() {
        return INSTANCE;
    }

    @Override
    public AppProperties loadProps(List<String> propUris)  {
        //register custom protocol handler for classpath:resources pattern

        //Propeties object for loading InputStream later
        Properties props = new Properties();

        propUris.forEach(str -> {
            URLConnection urlConnection = null;
            try {
                urlConnection = new URL(str).openConnection();
            } catch (IOException e) {
                String msg = "Could not open connection " + str;
                logger.log(Level.SEVERE, msg, e);
            }
            props.putAll(str.endsWith(".json") ? loadFromUrl(urlConnection, true) : loadFromUrl(urlConnection, false));
        });

        TrialAppProperties trialAppProperties = new TrialAppProperties();
        trialAppProperties.add(props);
        return trialAppProperties;
    }


    private Properties loadFromUrl(URLConnection urlConnection, boolean isJson) {
        Properties props = new Properties();
        try (InputStream inputStream = urlConnection.getInputStream()) {
                if(isJson)
                    parseJson(props, inputStream);
                else
                    props.load(inputStream);
        } catch (Throwable e) {
            String msg = "Could not load URI " + urlConnection.getURL().getPath();
            logger.log(Level.SEVERE, msg, e);
        }
        return props;
    }

    private void parseJson(Properties props, InputStream inputStream) {
        JsonReader rdr = Json.createReader(inputStream);
        JsonObject obj = rdr.readObject();
        Assert.assertNotNull(obj);
        //then iterate over fields
        obj.entrySet().forEach(entry -> props.put(entry.getKey(), entry.getValue().toString().replaceAll("\"", "")));
    }

    @Override
    public void printProperties(AppProperties props, PrintStream sync) {
        Map<String, ?> all = props.getAll();
        all.forEach((key, val) -> sync.println(val));

    }
}
