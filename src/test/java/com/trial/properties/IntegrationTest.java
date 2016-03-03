package com.trial.properties;

import com.trial.properties.app.TrialAppPropertiesManager;
import com.trial.properties.app.util.PropsUtils;
import com.trial.properties.app.interfaces.AppPropertiesManager;
import com.trial.properties.app.util.Predefined;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import static org.junit.Assert.*;


public class IntegrationTest {

    private AppPropertiesManager instance;


    @Before
    public void initData() throws ClassNotFoundException{
        instance = TrialAppPropertiesManager.getInstance();
    }


    @Test
    public void customUrlCheck() throws IOException {
        URLConnection urlConnection = new URL("classpath:resources/jdbc.properties").openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        assertNotNull(inputStream);
        Properties props = new Properties();
        props.load(inputStream);
        assertTrue(props.size() != 0);
    }



    @Test
    public void propsByEnum() throws Exception {

        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.STRING, null));             //null value considered as string
        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.STRING, "asdf"));
        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.STRING, "localhost:8080/"));    //wrong url
        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.STRING, "httpp://localhost:8080/"));   //wrong url
        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.STRING, "treu"));                //wrong boolean

        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.URLL, "http://localhost:8080/"));

        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.BOOLEAN, "true"));
        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.BOOLEAN, "True "));

        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.INTEGER, "1"));

        assertTrue(PropsUtils.isTypeOf(Predefined.AllowedType.FLOAT, "1.1"));
    }


    @Test
    public void propsByObjectsType() throws Exception {

        assertTrue(PropsUtils.isTypeOf(String.class, null));             //null value - type STRING

        assertFalse(PropsUtils.isTypeOf(Boolean.class, null));
        assertTrue(PropsUtils.isTypeOf(Boolean.class, "true"));
        assertTrue(PropsUtils.isTypeOf(Boolean.class, " true "));
        assertTrue(PropsUtils.isTypeOf(Boolean.class, "TRue"));
        assertFalse(PropsUtils.isTypeOf(Boolean.class, "truee"));

        assertTrue(PropsUtils.isTypeOf(Integer.class, "1"));
        assertTrue(PropsUtils.isTypeOf(Integer.class, "1 "));
        assertFalse(PropsUtils.isTypeOf(Integer.class, "1 1"));

        assertTrue(PropsUtils.isTypeOf(Float.class, "1.1"));
        assertTrue(PropsUtils.isTypeOf(Float.class, " 1.1"));
        assertFalse(PropsUtils.isTypeOf(Float.class, "1.1.1"));

        assertTrue(PropsUtils.isTypeOf(URL.class, "http://localhost"));
        assertFalse(PropsUtils.isTypeOf(URL.class, "://localhost"));
    }


}
