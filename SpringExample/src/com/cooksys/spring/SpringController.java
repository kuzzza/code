package com.cooksys.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class SpringController {
 
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String start() throws IOException {
        return "index";
    }
 
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse response) throws IOException {
 
        File file = new File("C:/Code/file.pdf");
        InputStream is = new FileInputStream(file);
 
       
        response.setContentType("application/octet-stream");
   
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
      
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }
}