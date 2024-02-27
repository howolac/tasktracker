package com.tasktracker.service;

import com.tasktracker.entity.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService {
    List<String> uploadFiles(int userId, MultipartFile[] files, String path) throws Exception;

    void downloadFile(int fileId, HttpServletResponse response) throws Exception;

    String deleteFile(int fileId) throws Exception;


    List<File> listFilesByFilePath(int userId, String path) throws Exception;

    String createFolder(int userId, String folderName, String path) throws Exception;

    String deleteFolder(int fileId) throws Exception;

    List<File> searchFiles(int userId, String searchText,String searchType);

}