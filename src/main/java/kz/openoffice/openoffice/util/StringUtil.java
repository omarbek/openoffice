package kz.openoffice.openoffice.util;

/**
 * @author Omarbek.Dinassil
 * on 2020-11-10
 * @project openoffice
 */
public final class StringUtil {
    
    private StringUtil() {}
    
    public static boolean isEmpty(String target) {
        return "".equals(target) || null == target;
    }
    
}
