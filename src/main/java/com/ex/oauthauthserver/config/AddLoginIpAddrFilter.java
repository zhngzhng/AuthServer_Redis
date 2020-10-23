package com.ex.oauthauthserver.config;

import com.ex.oauthauthserver.model.User;
import com.ex.oauthauthserver.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 只要有登录请求，则记录登录的ip地址
 */
@Component
public class AddLoginIpAddrFilter implements Filter{
//public class AddLoginIpAddrFilter extends GenericFilter{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if("POST".equals(req.getMethod()) && "/in".equals(req.getServletPath())) {
            String email = req.getParameter("account");
            String ipAddr = Common.getIpAddr(req);
            Query query = new Query(Criteria.where("email").is(email));
            User user = mongoTemplate.findOne(query,User.class);
            if (user != null){
                ArrayList<String> loginIp = user.getLoginIp();
                if (loginIp == null){
                    loginIp = new ArrayList<>();
                    loginIp.add(ipAddr);
                    mongoTemplate.updateFirst(query,new Update().set("loginIp",loginIp),User.class);
                }
                boolean contains = loginIp.contains(ipAddr);
                if (!contains){
                    loginIp.add(ipAddr);
                    mongoTemplate.updateFirst(query,new Update().set("loginIp",loginIp),User.class);
                }
            }
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
