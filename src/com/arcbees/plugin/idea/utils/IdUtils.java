package com.arcbees.plugin.idea.utils;

/**
 * Created by serg on 25.08.2014.
 */
public class IdUtils {

    public static String capitalizeFirst(String name){
        if (name == null || name.length() < 1) {
            return name;
        }else {
            return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
        }
    }
}
