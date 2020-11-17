package kz.openoffice.openoffice.demo;

import kz.openoffice.openoffice.util.FileUtil;
import kz.openoffice.openoffice.util.Office2PDF;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omarbek.Dinassil
 * on 2020-11-10
 * @project openoffice
 */
@ComponentScan
@RestController
@RequestMapping("/test")
public class ReadAndDownload {
    
    //base path
    @Value("${BASE_PATH}")
    private String BASE_PATH;
    
    
    /**
     * @return choose page
     */
    @RequestMapping("/choose")
    public String chooseFile() {
        return "ShowChoose";
    }
    
    /**
     * @param res      response object
     * @param fileName request preview file name
     *
     * @throws Exception
     */
    @GetMapping(path = "/read/{fileName}")
    public void readFile(HttpServletResponse res, @PathVariable String fileName) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        String filePath = fileHandler(fileName);
        //Determine whether it is pdf or word or excel
        //If it is pdf, read it directly, otherwise switch to pdf and read again  //
        try {
            if (filePath != null) {
                in = new FileInputStream(filePath);
            }
            res.setContentType("application/pdf");
            out = res.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
    
    @GetMapping(path = "get_files")
    public List<String> getFiles() {
        final File folder = new File(BASE_PATH);
        return listFilesForFolder(folder);
    }
    
    private List<String> listFilesForFolder(File folder) {
        List<String> ret = new ArrayList<>();
        for (final File fileEntry: folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                ret.add(fileEntry.getName());
            }
        }
        return ret;
    }
    
    /**
     * Document processing
     *
     * @param fileName
     *
     * @return
     */
    public String fileHandler(String fileName) {
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        System.out.println(fileSuffix);
        if ("pdf".equals(fileSuffix)) {
            return BASE_PATH + fileName;
        } else {
            return Office2PDF.openOfficeToPDF(BASE_PATH + fileName).getAbsolutePath();
        }
    }
    
}
