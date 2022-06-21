package com.tuling.basicAuth.filter;

import com.tuling.basicAuth.util.MyConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "testFilter",urlPatterns = "/*")
public class TestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        if(requestURI.contains(".") || requestURI.startsWith("/"+MyConstants.RESOURCE_COMMON+"/")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else if(null == request.getSession().getAttribute(MyConstants.FLAG_CURRENTUSER)){
            servletResponse.getWriter().write("need to login first");
            return;
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }
}
