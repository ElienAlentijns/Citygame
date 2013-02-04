package be.citygamephl.webservice;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter(urlPatterns = { "/*" })
public class CrossOriginResourceSharingFilter implements Filter {

    public CrossOriginResourceSharingFilter() { }

    @Override
    public void init(FilterConfig fConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(
        ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        ((HttpServletResponse)response).addHeader(
            "Access-Control-Allow-Origin", "*"
        );
            
                ((HttpServletResponse)response).addHeader(
                        "Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS"
                );
                
                    ((HttpServletResponse)response).addHeader(
            "Access-Control-Allow-Headers", "X-Requested-With"
        );
                    
                                ((HttpServletResponse)response).addHeader(
            "Access-Control-Allow-Headers", "Content-Type"
        );
                    
      

        chain.doFilter(request, response);
    }
}