package kz.openoffice.openoffice.util;

import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Omarbek.Dinassil
 * on 2020-11-10
 * @project openoffice
 */
public class Office2PDF {
    private static String SAVE_PATH = "/Users/omar/Desktop/save_file/";
    
    private Office2PDF() {}
    
    /**
     * Convert files in office format to pdf
     *
     * @param sourceFilePath source file path
     *
     * @return
     */
    public static File openOfficeToPDF(String sourceFilePath) {
        return office2pdf(sourceFilePath);
    }
    
    /**
     * Convert office documents to pdf documents
     *
     * @param sourceFilePath Original file path
     *
     * @return
     */
    public static File office2pdf(String sourceFilePath) {
        OfficeManager officeManager = null;
        try {
            if (StringUtil.isEmpty(sourceFilePath)) {
                //Print log...
                return null;
            }
            File sourceFile = new File(sourceFilePath);
            if (!sourceFile.exists()) {
                //Print log...
                return null;
            }
            
            String after_convert_file_path = getAfterConverFilePath(sourceFilePath);
            //Start openOffice
            officeManager = getOfficeManager();
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            return convertFile(sourceFile, after_convert_file_path, sourceFilePath, converter);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion exception");
        } finally {
            if (officeManager != null) {
                try {
                    officeManager.stop();
                } catch (OfficeException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    /**
     * Convert files
     *
     * @param sourceFile              original file
     * @param after_convert_file_path storage location after conversion
     * @param sourceFilePath          Original file path
     * @param converter               converter
     *
     * @return
     */
    public static File convertFile(File sourceFile,
                                   String after_convert_file_path, String sourceFilePath,
                                   OfficeDocumentConverter converter) throws OfficeException {
        File outputFile = new File(after_convert_file_path);
        if (!outputFile.getParentFile().exists()) {
            //If the parent directory does not exist, that is, the folder E:/pdfFile does not exist, create one
            outputFile.getParentFile().mkdirs();
        }
        converter.convert(sourceFile, outputFile);
        return outputFile;
    }
    
    public static OfficeManager getOfficeManager() {
        DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();
        builder.setOfficeHome(getOfficeHome());
        OfficeManager officeManager = builder.build();
        try {
            officeManager.start();
        } catch (OfficeException e) {
            //Print log
            System.out.println("start openOffice Fail!");
            e.printStackTrace();
        }
        return officeManager;
    }
    
    /**
     * Get the path where the converted file is stored
     *
     * @param sourceFilePath source file
     *
     * @return
     */
    public static String getAfterConverFilePath(String sourceFilePath) {
        //Intercept source file file name
        String sourceFileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1);
        return SAVE_PATH + sourceFileName.replaceAll("\\." + FileUtil.getFileSuffix(sourceFileName), ".pdf");
    }
    
    /**
     * Get the installation directory of openOffice
     *
     * @return
     */
    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Windows.*", osName)) {
            return "C:/Program Files (x86)/OpenOffice 4";
        } else if (Pattern.matches("Linux.*", osName)) {
            return "/usr/temp";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Applications/OpenOffice.app/Contents/MacOS";
        }
        return null;
    }
    
}
