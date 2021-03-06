package com.zdnf.bbs.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zdnf.bbs.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zdnf.bbs.domain.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZDNF on 2017/5/7.
 * 这个类用来处理用户的登录和注册
 */

/**
 *
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 *
 * ━━━━━━感觉萌萌哒━━━━━━
 */
@Controller
public class LoginController {
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    LoginService LoginService;

    public String ToMd5(String str) throws NoSuchAlgorithmException {
        String res="skyisblue"+str;
        MessageDigest md =MessageDigest.getInstance("md5");
        md.update(res.getBytes());
        return new BigInteger(1,md.digest()).toString();
    }


    //注册的表单
    @RequestMapping(value = "/regist",method = RequestMethod.GET)
    public String regist(){return "regist";}

    //注册表单提交
    @RequestMapping(value = "/registing",method = RequestMethod.POST)
    @ResponseBody
    public String registing(HttpServletRequest httpServletRequest,@Valid User user){
        String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
        String parameter = httpServletRequest.getParameter("vrifyCode");
        if (LoginService.HasUsername(user.getName())){
            return "<p>账户已存在<br>换个用户名呗QAQ";
        }
      //  if (captchaId.equals(parameter)) {
            user.setPower("0");
            LoginService.adduser(user);
            return "注册成功<br><a href=\"/\">点我回主页</a>";
      //  }
    //   return "<p>验证码输错了吧";
    }

    //登陆的表单
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    //处理登录
    @RequestMapping(value = "/logining",method = RequestMethod.POST)
    public String logining(HttpServletRequest httpServletRequest, HttpServletResponse response,User user){
       // String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
        // String parameter = httpServletRequest.getParameter("vrifyCode");
        //验证码和账号密码都正确
        System.out.println(user.getName()+"  "+user.getPasswd());
        if(//captchaId.equals(parameter)
                //&&
                LoginService.get_passwd(user.getName(),user.getPasswd())) {
            try {
                //一个cookie，存储当前用户名字
                Cookie cookie2 = new Cookie("id", LoginService.GetIdByName(user.getName()));
                cookie2.setMaxAge(3600*24); //设置cookie的过期时间是10s
                response.addCookie(cookie2);
                //将用户密码加上 sky 转md5 存入cookie
                //存储用户加密后的密码 当做密匙
                Cookie cookie = new Cookie("key",ToMd5(user.getPasswd()));
                cookie.setMaxAge(3600*24); //设置cookie的过期时间是一天
                response.addCookie(cookie);
                return "redirect:/";
            }catch (Exception e){
                return "error";
            }
        }
        return "error";
    }

    //用来生成验证码
    @RequestMapping("/Kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
