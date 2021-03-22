package com.ex.oauthauthserver.utils;

import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Service
public class Common {
    @Value("${spring.mail.username}")
    String mailAddress;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 邮件发送
     * @param to 接收方邮箱
     * @param subject title
     * @param content
     * @return
     */
    public Boolean sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            InternetAddress internetAddress = new InternetAddress(mailAddress, "OpenGMS Team", "UTF-8");
            helper.setFrom(internetAddress);
            helper.setTo(to);
            helper.setCc(mailAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return true;

        } catch (Exception e) {
            System.out.println("邮件发送异常:" + e);
            return false;
        }
    }


    /**
     * 用户信息更新接口，必须包含uuid作为用户唯一标识符
     * uuid及password不能使用此接口更新
     * @param request
     * @return
     */
    public Update setUpdate(HttpServletRequest request) {
        Update update = new Update();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);

            if (key.equals("userId") || key.equals("password")){
                continue;
            }
            update.set(key, value);
        }
        return update;
    }

    public Update setUpdate(Map<String,Object> infoMap){
        Update update = new Update();
        for (String key: infoMap.keySet()){
            /*
            用户属性值的四种情况
            1.对象 resource,存储的子对象
            2.动态数组 ArrayList<String> 只需要构造String[]即可。
            3.long phoneNum
            4.String 普通属性
            特殊情况，userId,email,Password.loginIp,domain不能通过此修改
             */
            if (key.equals("userId")||key.equals("password")||key.equals("loginIp")||key.equals("domain")){
                continue;
            }
            if (key.equals("resource")){
                Object value = infoMap.get(key);
                update.set(key,value);
                continue;
            }
            if (key.equals("organizations")){
                String[] value = infoMap.get(key).toString().split(",");
                update.set(key,value);
                continue;
            }
            if (key.equals("phoneNum")){
                long value = (long)infoMap.get(key);
                update.set(key,value);
                continue;
            }
            String value = (String) infoMap.get(key);
            update.set(key,value);
        }
        return update;
    }

    /**
     * 获取IpAddress
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = null;
        try {
           ipAddress = request.getHeader("x-forwarded-for");
           if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
               ipAddress = request.getHeader("Proxy-Client-IP");
           }
           if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
               ipAddress =request.getHeader("WL-Proxy-Client-IP");
           }
           if (ipAddress == null || ipAddress.length() ==0 || "unknown".equalsIgnoreCase(ipAddress)){
               ipAddress = request.getRemoteAddr();
               if (ipAddress.equals("127.0.0.1")){
                   //根据网卡取本机配置的IP
                   InetAddress inet = null;
                   try{
                       inet = InetAddress.getLocalHost();
                   }catch (UnknownHostException e){
                       e.printStackTrace();
                   }
                   ipAddress = inet.getHostAddress();
               }
           }
           //对于通过多个代理的情况，第一个Ip为真实Ip，多个Ip按照“,”分割
            if (ipAddress !=null && ipAddress.length()>15){
                if (ipAddress.indexOf(",")>0){
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
                }
            }

        }catch (Exception e){
            ipAddress = "";
        }
        return ipAddress;
    }

}
