package com.trial.properties;

import com.trial.properties.app.TrialAppPropertiesManager;
import com.trial.properties.app.interfaces.AppPropertiesExt;
import com.trial.properties.app.interfaces.AppPropertiesManager;
import com.trial.properties.app.util.Predefined;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;


public class TestFunctional {
    private String[] arr = {"classpath:resources/jdbc.properties", "classpath:resources/config.json"};


    private AppPropertiesExt props;
    private AppPropertiesManager manager;


    @Before
    public void init() throws ClassNotFoundException {
        manager = TrialAppPropertiesManager.getInstance();
        List<String> propertySourceUris = new LinkedList<>(Arrays.asList(arr));
        addLocalFile(propertySourceUris);
        props = (AppPropertiesExt)manager.loadProps(propertySourceUris);
    }

    private void addLocalFile(List<String> propertySourceUris) {
        String pth = Paths.get(".").toAbsolutePath().toString();
        pth = pth + "/src/main/resources/aws.properties";
        pth = new File(pth).toURI().toString();
        propertySourceUris.add(pth);
    }


    @Test
    public void checkMissing() {
        List<?> lst = props.getMissingProperties();
        assertEquals(lst.size(), 0);
        props.add("JDBC_PASSWORD", null);    //invalid - null value
        lst = props.getMissingProperties();
        assertEquals(lst.size(), 1);
        props.add("AWS_ACCOUNT_ID", "asdf");    //invalid - expected integer
        lst = props.getMissingProperties();
        assertEquals(lst.size(), 2);
        props.add("JDBC_PASSWORD", "asdf");
        props.add("AWS_ACCOUNT_ID", "1234");    //valid again
        lst = props.getMissingProperties();
        assertEquals(lst.size(), 0);

        //System.out.println("Missing properties (size  " + lst.size() + " ):");
        //lst.forEach(System.out::println);
    }


    @Test
    public void checkPrint() {
        System.out.println(" ** Loaded properties ** ");
        manager.printProperties(props, System.out);
    }



    @Test
    public void getExisting() {
        assertNotNull(props.get("aws_access_key"));
        assertNotNull(props.get("auth.endpoint.uri"));
        assertNotNull(props.get("jpa.showSql"));
    }


    @Test
    public void getNonExisting() {
        assertNull(props.get("jpa__showSql"));      //not exist
        assertNull(props.get("jpa__showSql11"));    //not exist
    }

    @Test(expected = NullPointerException.class)
    public void getNullKey() {
        props.get(null);
        fail("null keys not supported");
    }


    @Test(expected = NullPointerException.class)
    public void putNullKey() {
        props.add(null, "asdasdf");
        fail("null keys not supported");
    }


    @Test
    public void addWrongType() {
        props.add("job.timeout", "asdfa");    //wrong type
        assertFalse(props.isValid());
    }

    @Test(expected = NullPointerException.class)                    //ensure NullPointer suppressed
    public void addNullKey() {
        props.add(null, "asdfa");
        fail("null keys can not be added");                         //fail test if reached this point
    }

    @Test
    public void checkValidity() {
        assertTrue(props.isValid());
        props.add("JDBC_PASSWORD", null);    //not valid
        assertFalse(this.props.isValid());
        props.add("JDBC_PASSWORD", "pass");  //valid
        assertTrue(this.props.isValid());
    }

    @Test
    public void checkRemove() {
        props.add("asdfasd", null);          //not valid
        assertFalse(this.props.isValid());
        props.remove("asdfasd");              //valid
        assertTrue(this.props.isValid());
    }


    @Test
    public void checkKnownProperties() {
        assertTrue(props.getKnownProperties().size() == Predefined.values().length);
        //props.getKnownProperties().stream().forEach(System.out::println);
    }


    @Test
    public void checkClear() {
        props.clear();
        Map<String, ?> all = props.getAll();
        assertTrue(all.size() == 0);
    }

}
