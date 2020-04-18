package com.mm.modules.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 *
 * @author lwl
 */
@Controller
public class SysPageController {

    @RequestMapping("modules/{module}/{url}.html")
    public String module(@PathVariable("module") String module, @PathVariable("url") String url) {
        return "modules/" + module + "/" + url;
    }

    @RequestMapping(value = {"/", "index.html"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"login", "login.html"})
    public String login() {
        return "login";
    }

    @RequestMapping("main.html")
    public String main() {
        return "main";
    }

    @RequestMapping("404")
    public String notFound() {
        return "404";
    }

}
