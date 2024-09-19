package br.edu.ifg.projetopraticoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    // Redireciona todas as requisições para a raiz para o index.html
    @RequestMapping("/")
    public String index() {
        return "forward:/pages/index.html";
    }

    @RequestMapping("/register")
    public String register() {
        return "forward:/pages/user/create.html";
    }

    @RequestMapping("/task/new")
    public String createTask() {
        return "forward:/pages/task/create.html";
    }

    @RequestMapping("/project/new")
    public String createProject() {
        return "forward:/pages/project/create.html";
    }

    @RequestMapping("/task/list")
    public String showTask() {
        return "forward:/pages/task/list.html";
    }

    @RequestMapping("/project/list")
    public String showProject() {
        return "forward:/pages/project/list.html";
    }

    @RequestMapping("/user/list")
    public String showUser() {
        return "forward:/pages/user/list.html";
    }
}
