package com.amayadream.webchat.controller;

import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.service.UserService;
import com.amayadream.webchat.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 */
@Controller
@SessionAttributes("userid")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 聊天主页
     */
    @RequestMapping(value = "chat")
    public ModelAndView getIndex(){
        ModelAndView view = new ModelAndView("chat");
        return view;
    }
    /**
     * 显示个人信息页面
     */
    @RequestMapping(value = "userinfo/currentUser",  method = RequestMethod.GET)
    public ModelAndView selectUserByUserid(HttpSession session){
        ModelAndView view = new ModelAndView("information");
        User user = (User) session.getAttribute("user");
        view.addObject("user",user);
        return view;
    }

    /**
     * 显示个人信息编辑页面
     * @return
     */
    @RequestMapping(value = "userinfo/currentUser/config")
    public ModelAndView setting(HttpSession session){
        ModelAndView view = new ModelAndView("info-setting");
        User user = (User) session.getAttribute("user");
        view.addObject("user",user);
        return view;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "userinfo/currentUser/update", method = RequestMethod.POST)
    public String  update(User user, RedirectAttributes attributes,
                          NetUtil netUtil, CommonDate date, WordDefined defined, HttpServletRequest request){
        ResultDTO resultDTO = userService.editUser(user);
        if (resultDTO.isUnSuccess()){
            attributes.addFlashAttribute("error", "["+user.getUserId()+"]资料更新失败!errorMsg:"+resultDTO.getErrorMsg());
            return "redirect:/userinfo/currentUser/config";
        }
        attributes.addFlashAttribute("message", "["+user.getUserId()+"]资料更新成功!");

        return "redirect:/userinfo/currentUser/config";
    }


    /**
     * 添加用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "userinfo/add", method = RequestMethod.POST)
    public String  add(User user,Integer groupId, RedirectAttributes attributes, HttpServletRequest request){
        ResultDTO resultDTO = userService.addUser(user, groupId);

        if (resultDTO.isUnSuccess()){
            attributes.addFlashAttribute("error", "["+user.getUserName()+"]添加失败,errorMsg:"+resultDTO.getErrorMsg());
            return "redirect:/userinfo/add";
        }

        attributes.addFlashAttribute("message", "["+user.getUserName()+"]资料更新成功!");
        return "redirect:/userinfo/add";
    }

    /**
     * 修改密码
     * @param oldpass
     * @param newpass
     * @return
     */
    @RequestMapping(value = "userinfo/{userid}/pass", method = RequestMethod.POST)
    public String changePassword(String oldpass, String newpass, RedirectAttributes attributes,HttpSession session, HttpServletRequest request){
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/user/login";
        if(oldpass.equals(user.getPassword())){
            user.setPassword(newpass);
            ResultDTO resultDTO = userService.resetPassword(user);
            if(resultDTO.isSuccess()){
                attributes.addFlashAttribute("message", "["+user.getUserName()+"]密码更新成功!");
            }else{
                attributes.addFlashAttribute("error", "["+user.getUserName()+"]密码更新失败!");
            }
        }else{
            attributes.addFlashAttribute("error", "密码错误!");
        }
        return "redirect:/user/login";
    }

    /**
     * 头像上传
     * @param request
     * @return
     */
    @RequestMapping(value = "userinfo/currentUser/upload")
    public String upload(HttpServletRequest request, UploadUtil uploadUtil,HttpSession session,
                         RedirectAttributes attributes , CommonDate date, WordDefined defined){
        try{
            User user = (User) session.getAttribute("user");
            if (user == null){
                attributes.addFlashAttribute("error", "头像更新失败!");
                return "redirect:/currentUser/config";

            }
            String fileurl = uploadUtil.upload(request, "upload", user.getUserId()+"");
            user.setAvatarUrl(fileurl);
            ResultDTO resultDTO = userService.editUser(user);
            if(resultDTO.isSuccess()){
                attributes.addFlashAttribute("message", "["+user.getUserName()+"]头像更新成功!");
            }else{
                attributes.addFlashAttribute("error", "["+user.getUserName()+"]头像更新失败!errorMsg:"+resultDTO.getErrorMsg());
            }
        } catch (Exception e){
            attributes.addFlashAttribute("error", "头像更新失败!,exception:"+e.getMessage());
        }
        return "redirect:/currentUser/config";
    }

    /**
     * 获取用户头像
     */
    @RequestMapping(value = "userinfo/{userid}/head")
    public void head(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        ServletOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            User user = (User) session.getAttribute("user");
            String path = user.getAvatarUrl();
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            String picturePath = rootPath + path;
            response.setContentType("image/jpeg; charset=UTF-8");
            outputStream = response.getOutputStream();
            inputStream = new FileInputStream(picturePath);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null)
                try {
                    outputStream.close();
                }catch (Exception e){
                    //ignore
                }
            if (inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    //ignore
                }
            }
        }
    }

}
