package com.trial.properties.app.util;

import java.util.EnumSet;

final public class PropsUtils {
    final private static EnumSet<Predefined.AllowedType> types = EnumSet.allOf(Predefined.AllowedType.class);

    final private static EnumSet<Predefined> confs = EnumSet.allOf(Predefined.class);

    private PropsUtils() {
    }

    /**
     * Tries to find most appropriate Type for val. Checks enum Type in order types are defined.
     * If other types are not valid then STRING is returned.
     *
     * @param val - string representation ov property value
     * @return
     */
    public static Predefined.AllowedType discoverType(String val) {
        return types.stream().filter(t -> t.isValid(val)).findFirst().get();
    }

    /**
     * Checks if string val can be converted to class cls.  cls should be either one of classes represented by this values.
     * @param cls one of classes represented by this values (Inetger, URL, etc.)
     * @param val string to check
     * @param <T> class Inetger, URL, etc.
     * @return true if string can be converted to cls class. otherwise false
     */
    public  static <T> boolean isTypeOf(T cls, String val) {
        Predefined.AllowedType first = discoverType(val);
        return first == cls || first.getTClazz().equals(cls);
    }

    /**
     * Checks if string val can be converted to class represented by cls.
     * @param cls one of Type values
     * @param val string to check
     * @return true if string can be converted to cls class. otherwise false
     */
/*
    public  static boolean isTypeOf(Type cls, String val) {
        return cls.isValid(val);
    }
*/


    /**
     *
     * @return EnumsSet of predefined properties
     */
    public static EnumSet<Predefined> predefinedPropsSet() {
        return confs.clone();
    }
}
