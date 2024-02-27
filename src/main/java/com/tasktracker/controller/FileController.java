package com.tasktracker.controller;

import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.File;
import com.tasktracker.service.FileService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

// 控制器类注解，指明这是一个REST控制器
@RestController
// 定义基础路径为/files，所有请求都会以这个路径开始
@RequestMapping("/api/files")
public class FileController {

    // 自动注入FileService
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    // 处理文件上传请求
    @PostMapping("/upload")
    public Result uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("path") String path) {
        try {
            List<String> uploadedFiles = fileService.uploadFiles(userService.getCurrentUserId(), files, path);
            return Result.succ(uploadedFiles);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 处理文件下载请求
    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable int fileId, HttpServletResponse response) {
        try {
            fileService.downloadFile(fileId, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 如果出现错误，设置 HTTP 状态码为 500
            try {
                response.getWriter().write(e.getMessage()); // 写入具体的错误信息到响应体，以便前端可以显示给用户
            } catch (IOException ioException) {
                ioException.printStackTrace(); // 如果在写入错误信息时发生了新的异常，打印这个异常的堆栈跟踪
            }
        }
    }

    // 处理文件删除请求
    @DeleteMapping("/delete-file")
    public Result deleteFile(@RequestParam int fileId) {
        try {
            String result = fileService.deleteFile(fileId);
            // 如果文件删除成功，返回成功的结果
            return Result.succ(result);
        } catch (Exception e) {
            // 如果发生异常，返回失败的结果
            return Result.fail(e.getMessage());
        }
    }

    // 列出所有文件
    @GetMapping("/list")
    public Result listFiles(@RequestParam(value = "path", defaultValue = "") String path) {
        try {
            List<File> files = fileService.listFilesByFilePath(userService.getCurrentUserId(), path);
            return Result.succ(files);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 创建文件夹
    @PostMapping("/create-folder")
    public Result createFolder(@RequestParam String folderName, @RequestParam String path) {
        // 定义不允许的字符
        Pattern pattern = Pattern.compile("[\\\\/:*?\"<>|]");
        if (pattern.matcher(folderName).find()) {
            return Result.fail("文件夹名称不能包含以下字符: \\ / : * ? \" < > |");
        }

        try {
            String result = fileService.createFolder(userService.getCurrentUserId(), folderName, path);
            return Result.succ(result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 删除文件夹
    @DeleteMapping("/delete-folder")
    public Result deleteFolder(@RequestParam int fileId) {
        try {
            String result = fileService.deleteFolder(fileId);
            return Result.succ(result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 根据文件名搜索文件或文件夹
    @GetMapping("/search")
    public Result searchFiles(@RequestParam String searchText, @RequestParam(required = false) String searchTypeText) {
        try {
            List<File> files = fileService.searchFiles(userService.getCurrentUserId(), searchText, searchTypeText);
            return Result.succ(files);
        } catch (Exception e) {
            return Result.fail("文件搜索失败：" + e.getMessage());
        }
    }
}