package com.amayadream.webchat.controller;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.UserService;
import com.amayadream.webchat.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author :  Amayadream
 * Date   :  2016.01.08 14:57
 * TODO   :  用户登录与注销
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String userName, String password, HttpSession session, RedirectAttributes attributes,HttpServletRequest request) {
        ResultDTO<User> userResultDTO = userService.queryUserByName(userName);
        if (!userResultDTO.isSuccess()) {
            attributes.addFlashAttribute("error", WordDefined.LOGIN_USERID_ERROR);
            return "redirect:/user/login";
        }
        User user = userResultDTO.getModel();
        if (user == null) {
            attributes.addFlashAttribute("error", WordDefined.LOGIN_USERID_ERROR);
            return "redirect:/user/login";
        }
        if (!user.getPassword().equals(password)) {
            attributes.addFlashAttribute("error", WordDefined.LOGIN_PASSWORD_ERROR);
            return "redirect:/user/login";
        } else {
            if (user.getStatus() != 1) {
                attributes.addFlashAttribute("error", WordDefined.LOGIN_USERID_DISABLED);
                return "redirect:/user/login";
            }
            session.setAttribute("user", user);
            session.setAttribute("login_status", true);
            user.setLasttime(CommonDate.getTime24());
            userService.updateLoginTime(user.getUserId());
            attributes.addFlashAttribute("message", WordDefined.LOGIN_SUCCESS);
            return "redirect:/userinfo/currentUser";
        }

    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session, RedirectAttributes attributes, WordDefined defined) {
        session.removeAttribute("userid");
        session.removeAttribute("login_status");
        attributes.addFlashAttribute("message", defined.LOGOUT_SUCCESS);
        return "redirect:/user/login";
    }
}
