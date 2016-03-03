package com.trial.properties.app;

import com.trial.properties.app.interfaces.AppPropertiesExt;
import com.trial.properties.app.util.Predefined;
import com.google.common.collect.ImmutableMap;
import com.trial.properties.app.util.PropsUtils;

import java.util.*;

/**
 * note: a default constructor is required.
 *
 * @author code test administrator
 */
public class TrialAppProperties implements AppPropertiesExt {


    private final Map<String, Entry> map;

    private static final ImmutableMap<String, Predefined> mapNameToEnum;

    static {
        ImmutableMap.Builder<String, Predefined> mapBuilder = ImmutableMap.builder();
        for(Predefined val: Predefined.values()) {
            mapBuilder.put(val.name(), val);
        }
        mapNameToEnum = mapBuilder.build();
    }

    private final class Entry {
        Class clazz;
        final String name;
        final String val;

        private Entry(Class type, String name, String val) {
            this.clazz = type;
            this.name = name;
            this.val = val;
        }

        void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return name + ", " + clazz.getName() + ", " + val;
        }
    }

    static Comparator<String> comparator = String::compareToIgnoreCase;


    class CustomTreeMap<K,V> extends TreeMap <K,V>{

        public CustomTreeMap(Comparator<K> comparator) {
            super(comparator);
        }

        public V get(Object key) {
            return super.get(normalize(key));
        }


        @Override
        public V put(K key, V prop) {
            Entry p = (Entry)prop;
            String normalized = normalize(key);
            Class tClass;
            Predefined confPredefined = mapNameToEnum.get(normalized);
            tClass = (confPredefined != null) ? confPredefined.getType().getTClazz() : PropsUtils.discoverType(p.val).getTClazz();
            p.setClazz(tClass);
            return super.put((K)normalized, prop);
        }

        private String normalize(Object key) {
            return key.toString().trim().toUpperCase().replace('.', '_');
        }

    }

    public TrialAppProperties() {
        map =  new CustomTreeMap<>(comparator);
    }

    public void add(Properties props) {
        props.forEach((key, value) -> add(key.toString(), value.toString()));
        props.size();
    }


    public Entry add(final String key, final String value) {
        return map.put(key, new Entry(null, key, value));
    }


    public Entry remove(final String key) {
        return map.remove(key);
    }


    @Override
    public List<String> getMissingProperties() {
        List<String> res = new ArrayList<>(PropsUtils.predefinedPropsSet().size());
        PropsUtils.predefinedPropsSet().stream().forEach(p -> {
            Entry e = getByEnum(p);
            if(invalidCondition(e)) res.add(e.name);
        });
        return res;
    }

    private boolean checkPredefined() {
        for(Predefined p:  PropsUtils.predefinedPropsSet()) {
            Entry e = getByEnum(p);
            if (invalidCondition(e)) return false;
        }
        return true;
    }

    /**
     * checks property is null or it's value is null or it's class of incorrect type
     * @param e - property to check
     * @return
     */
    private boolean invalidCondition(Entry e) {
        if(e == null || e.val == null || !PropsUtils.isTypeOf(e.clazz, e.val))
            return true;
        return false;
    }


    /**
     * check all present properties
     * @return
     */
    private boolean checkAll() {
        Collection<Entry> values = map.values();
        for(Entry e: values) {
            if(invalidCondition(e)) return false;
        }
        return  true;
    }

    @Override
    public List<String> getKnownProperties() {
        List<String> lst = new ArrayList<>(PropsUtils.predefinedPropsSet().size());
        PropsUtils.predefinedPropsSet().forEach(el -> lst.add(el.getOriginalName()));
        return lst;
    }

    @Override
    public boolean isValid() {
        return checkPredefined() && checkAll();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public String get(String key) {
        Entry entry = map.get(key);
        return  entry == null? null: entry.val;
    }

    private Entry getByEnum(Predefined key) {
        return map.get(key.name());
    }

    /**
     *
     * @return alphabetically sorted map of initialized properties
     */
    @Override
    public Map<String, ?> getAll() {
        Map<String, Object> copy = new CustomTreeMap<>(comparator);
        copy.putAll(map);
        return copy;
    }


}
