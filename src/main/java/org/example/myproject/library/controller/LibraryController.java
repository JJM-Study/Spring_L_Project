package org.example.myproject.library.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.common.controller.AbstractBaseController;
import org.example.myproject.library.dto.MyLibraryItemsDto;
import org.example.myproject.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("library")
public class LibraryController extends AbstractBaseController {

    private static final Logger logger = LogManager.getLogger(LibraryController.class);

    @Autowired
    LibraryService libraryService;

    @Autowired
    AuthService authService;

    @GetMapping("/my")
    public String myLibrary(Model model, HttpServletRequest request) {

        String userId = authService.getAuthenticUserId(request);

        List<MyLibraryItemsDto> libraryList = libraryService.selectMyLibraryItems(userId);

        model.addAttribute("library",libraryList);

        return layout("/library/my-library.jsp", model);
    }


    // 나중에 아이템의 detail에 대해서 볼 수 있도록 하기 위해 view 추가 고민.
//    @GetMapping("/detail/{prodNo}")
//    public String productDetailView {
//
//        return layout("/")
//    }

//    @GetMapping("/download/{prodNo}")
//    public ResponseEntity<> downloadItem() {
//
//    }

}
