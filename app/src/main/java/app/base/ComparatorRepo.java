/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.base;

import java.util.Comparator;

/**
 * @author chenliang
 */
public class ComparatorRepo {
    public static final Comparator<? super Integer> intKey = new Comparator<Integer>() {

        public int compare(Integer arg0, Integer arg1) {
            return arg0 - arg1;
        }
    };
    public static final Comparator<? super Integer> intDecKey = new Comparator<Integer>() {

        public int compare(Integer arg0, Integer arg1) {
            return arg1 - arg0;
        }
    };
    public static Comparator<String> stringKey = new Comparator<String>() {

        public int compare(String arg0, String arg1) {
            if (arg1 == null) {
                if (arg0 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (arg0 == null) {
                    return -1;
                }
            }
            return arg0.compareTo(arg1);
        }
    };
    public static Comparator<String> stringKeyReverse = new Comparator<String>() {

        public int compare(String arg0, String arg1) {
            if (arg1 == null) {
                if (arg0 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (arg0 == null) {
                    return -1;
                }
            }
            return arg1.compareTo(arg0);
        }
    };
}
