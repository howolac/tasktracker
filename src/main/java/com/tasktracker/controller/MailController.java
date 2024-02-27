//package com.tasktracker.controller;
//
//import com.tasktracker.common.lang.Result;
//import com.tasktracker.service.MailService;
//import com.tasktracker.entity.MailVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//@RestController
//@RequestMapping("/api/mail")
//public class MailController {
//    @Autowired
//    private MailService mailService;
//
//    @PostMapping("/send")
//    public Result sendMail(MailVo mailVo) {
//        try {
//            return Result.succ(mailService.sendMail(mailVo));
//        } catch (Exception e) {
//            return Result.fail("失败");
//        }
//    }
//}