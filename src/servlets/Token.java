package servlets;

import java.util.Enumeration;
import javax.servlet.ServletContext;

public class Token {

    public static int tokenToUser(ServletContext servletContext, String token) {
        Enumeration<String> e = servletContext.getAttributeNames();
        System.out.println(e);
        while (e.hasMoreElements()) {
            if (token.equals((String) e.nextElement())) {
                return (Integer) servletContext.getAttribute(token);
            }
        }
        return 0;
    }
}
