package servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64; 
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioInputStream;

import com.RapidFeedback.MysqlFunction;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class AudioRecorderServlet
 */
@WebServlet("/AudioRecorderServlet")
public class AudioRecorderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static int i = 0;  
    private boolean isMultipart;
    private int maxFileSize = 1024 * 1024 * 10;
    private int maxMemSize = 100 * 1024;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AudioRecorderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*String fileName = (String) request.getParameter("filename"); 
		System.out.println(fileName);
		if (fileName == null || fileName.equals("")) 
		    throw new ServletException(
		     "Invalid or non-existent file parameter in SendMp3 servlet."); 

		    if (fileName.indexOf(".mp4") == -1) {
		    	fileName = fileName + ".mp4"; 
		    }
		    
		String feedback = getServletContext().getRealPath("/"); 
		System.out.println(feedback);
		if (feedback == null || feedback.equals("")) 
		    throw new ServletException(
		     "Invalid or non-existent feedback context-param."); */
        
		String feedback = getServletContext().getRealPath("/"); 
		System.out.println(feedback);
		String fileName = "recorder.mp4"; 
		ServletOutputStream stream = null; 
		BufferedInputStream buf = null; 
		try { 

		    stream = response.getOutputStream(); 
		    File mp4 = new File(feedback + "/" + fileName); 

		    //set response headers 
		    response.setContentType("audio/mpeg"); 

		    response.addHeader("Content-Disposition", "attachment; filename=" 
		     + fileName); 

		    response.setContentLength((int) mp4.length()); 

		    FileInputStream input = new FileInputStream(mp4); 
		    buf = new BufferedInputStream(input); 
		    int readBytes = 0; 
		    //read from the file; write to the ServletOutputStream 
		    while ((readBytes = buf.read()) != -1) 
		    stream.write(readBytes); 
		} catch (IOException ioe) { 
		    throw new ServletException(ioe.getMessage()); 
		} finally { 
		    if (stream != null) 
		    stream.close(); 
		    if (buf != null) 
		    buf.close(); 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // listen the request 
        isMultipart = ServletFileUpload.isMultipartContent(request);
        String result = "";
        response.setContentType("audio/mpeg");
        if (!isMultipart) {
            result = "cannot find file";
            response.getWriter().println(result);
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // set the maxium of file 
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        String path = getServletContext().getRealPath("/") + "/";
        factory.setRepository(new File(path));
        System.out.println(path);
        // create a new file addressed process
        ServletFileUpload upload = new ServletFileUpload(factory);
        MysqlFunction db = new MysqlFunction();
        
        upload.setSizeMax(maxFileSize);

        try {
            // parse the request of file
            List fileItems = upload.parseRequest(request);
            // deal with all the file upload
            Iterator i = fileItems.iterator();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // get the parameter of file
                    String fieldName = fi.getFieldName();
                  
                    String fileName = String.valueOf(1910250000 + db.createAudio());
                    
                    String contentType = fi.getContentType();
                  
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // write file
                    File file = new File(path + fileName + ".mp4");
                    fi.write(file);
                }
            }
            result = "success";
        } catch (Exception ex) {
            System.out.println("ex:" + ex.getMessage());
            result = "fail";
        }

        response.getWriter().println(result);
    }

}
