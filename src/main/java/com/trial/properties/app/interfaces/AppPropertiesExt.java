package com.trial.properties.app.interfaces;

public interface AppPropertiesExt extends AppProperties {

    <T extends Object> T add(String key, String value);

    <T extends Object> T remove(String key);

}
