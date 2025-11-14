package org.example.myproject.libarary.controller;


import org.example.myproject.common.controller.AbstractBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("library")
public class LibraryController extends AbstractBaseController {

    @GetMapping("/my")
    public String myLibrary(Model model) {


        return layout("/library/my-library.jsp", model);
    }
}
