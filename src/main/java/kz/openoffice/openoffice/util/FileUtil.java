package kz.openoffice.openoffice.util;

/**
 * @author Omarbek.Dinassil
 * on 2020-11-10
 * @project openoffice
 */
public final class FileUtil {
    
    private FileUtil() {}
    
    /**
     * Get suffix
     *
     * @param fileName file name
     *
     * @return Suffix
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtil.isEmpty(fileName) || fileName.lastIndexOf(".") < 0) {
            return "error";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
}
