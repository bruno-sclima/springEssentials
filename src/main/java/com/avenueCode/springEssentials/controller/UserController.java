package com.avenueCode.springEssentials.controller;

import com.avenueCode.springEssentials.model.User;
import com.avenueCode.springEssentials.model.UserPrincipal;
import com.avenueCode.springEssentials.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserService service;
    protected Long id = (long) 0;
    @GetMapping("list-users")
    public String getAllUsers(Model model){
        List<User> list = service.getAllUsers();

        model.addAttribute("users", list);
        return "list-users";
    }
    public void setId(Long id){
        this.id = id;
    }
    @GetMapping("add-edit-user")
    public String newUser(@ModelAttribute("user") User user) {
        return "add-edit-user";
    }
    @GetMapping("home")
    public String home() {
        String url = "index";
        if(id!=0) url = "homeLog";
        return url;
    }
    @GetMapping("homeLog")
    public String userHome(){
        return "homeLog";
    }
    @RequestMapping("login")
    public String pagLogin(@ModelAttribute("user") User user) {
        return "login";
    }
    @InitBinder
    private void dateBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }
    @GetMapping("profile")
    public String profile(Model model, @ModelAttribute("user") User user) {
        User u = service.getUserById(id);
        model.addAttribute("users", u);
        return "profile";
    }
    @GetMapping("logOut")
    public String logOut() {
        this.id=(long) 0;
        return "redirect:/";
    }
    @RequestMapping(path = "/createUser", method = RequestMethod.POST)
    public String createOrUpdateUser( User user)
    {
        String url = "redirect:/home";
        if(id!=0) url = "redirect:/homeLog";
        service.createOrUpdateUser(user);
        return url;
    }
    @GetMapping("/edit-user")
    public String editUserById(Model model){
        if (id != 0) {
            User entity = service.getUserById(id);
            model.addAttribute("user", entity);
        } else {
            model.addAttribute("user", new User());
        }
        return "add-edit-user";
    }
    @GetMapping("/delete-user")
    public String deleteUserById(Model model){
        service.deleteUserById(this.id);
        this.id =(long) 0;
        return "redirect:/";
    }
}
