package com.tasktracker.service.impl;

import com.tasktracker.entity.File;
import com.tasktracker.mapper.FileMapper;
import com.tasktracker.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private static final String BASE_PATH_TEMPLATE = "D:/TCloud/%s";

    @Autowired
    private FileMapper fileMapper;

    private String getBaseDirectory(int userId) {
        return String.format(BASE_PATH_TEMPLATE, userId); // 格式化路径包含用户id
    }

    @Override
    public List<String> uploadFiles(int userId, MultipartFile[] files, String path) throws Exception {
//        本地
        List<String> uploadedFileNames = new ArrayList<>();
        for (MultipartFile file : files) {
//            if (file.isEmpty()) {
//                continue; // 跳过空文件
//            }
            String originalFileName = file.getOriginalFilename();//文件名
            Path destinationFilePath = Paths.get(getBaseDirectory(userId), path, originalFileName);//路径

            //检查文件夹是否存在
            Path directory = destinationFilePath.getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory); // 创建所有缺失的父路径，包括最后的目录
            }

//            如果文件已存在，重命名文件
            String fileBaseName = FilenameUtils.getBaseName(originalFileName);//去掉扩展的名字
            String extension = FilenameUtils.getExtension(originalFileName);//扩展名
            int fileIndex = 1; // 用于文件重命名的索引
            while (Files.exists(destinationFilePath)) {//尝试fileName_1.extension，还存在就fileName_2.extension...
                originalFileName = fileBaseName + "_" + fileIndex + (extension.isEmpty() ? "" : "." + extension);
                destinationFilePath = Paths.get(getBaseDirectory(userId), path, originalFileName);
                fileIndex++;
            }

            // 确保输入流被正确关闭
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING); // 保存文件到本地文件系统
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("Error while copying file.", e);
            }

//            数据库
            String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//            String filePath = (getBaseDirectory() + path).replace("\\", "/"); // 确保路径是以 '/' 分隔的
            File dbFile = new File();
            dbFile.setUserId(userId);
            dbFile.setFileName(originalFileName);
            dbFile.setFileType(fileType);
            dbFile.setFilePath(path);
            dbFile.setFileSize(file.getSize());
            fileMapper.insertFile(dbFile);
            uploadedFileNames.add(originalFileName);
        }
        return uploadedFileNames;
    }

    private static int readableFileSize(String sizeStr) {
        long size = Long.parseLong(sizeStr); // 将字符串转换为长整型
        if (size <= 0) return 0;
        // 由于要返回int类型，所以我们直接将结果转换为KB
        return (int) (size / 1024);
    }

    @Override
    public void downloadFile(int fileId, HttpServletResponse response) throws Exception {
        File file = fileMapper.findFileById(fileId);
        if (file == null) {
            throw new IOException("未找到文件记录");
        }

        Path filePath = Paths.get(getBaseDirectory(file.getUserId()), file.getFilePath(), file.getFileName());// 构建文件的完整路径

        // 检查文件是否存在并且不是目录
        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            throw new IOException("文件不存在或是一个目录: " + filePath);
        }

        String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");

//        设置Content-Disposition响应头让浏览器以附件下载方式打开 。设置Content-Type为application/octet-stream让浏览器知道这是二进制文件
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setContentType("application/octet-stream");

        Files.copy(filePath, response.getOutputStream());// 复制文件
        response.getOutputStream().flush();//强制将内容输出
    }

    @Override
    public String deleteFile(int fileId) throws Exception {
        File file = fileMapper.findFileById(fileId);
        if (file == null) {
            throw new IOException("未找到文件记录");
        }

        Path path = Paths.get(getBaseDirectory(file.getUserId()), file.getFilePath(), file.getFileName());
        if (Files.exists(path) && !Files.isDirectory(path)) {
            Files.delete(path);

            // 删除数据库中的文件记录
            fileMapper.deleteFileById(fileId);
            return "文件删除成功: " + path;
        } else {
            throw new IOException("文件不存在或是一个目录: " + path);
        }
    }

    @Override
    public List<File> listFilesByFilePath(int userId, String path) throws Exception {
//        if (path.equals("")) {
//            return fileMapper.listFilesByFilePath(getBaseDirectory());
//        }
        return fileMapper.listFilesByFilePath(userId, path);
    }

    @Override
    public String createFolder(int userId, String folderName, String path) throws Exception {
//       本地
        Path folderPath = Paths.get(getBaseDirectory(userId), path, folderName);
        if (Files.exists(folderPath)) {
            throw new IOException("Folder already exists: " + folderPath);
        }
        Files.createDirectories(folderPath);
//        数据库
        String filePath = (getBaseDirectory(userId) + path).replace("\\", "/");
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        File file = new File();
        file.setFileName(folderName);
//        file.setFilePath(filePath);
        file.setFilePath(path);
        file.setUserId(userId);
        fileMapper.createFolder(file);

        return "文件夹创建成功: " + folderPath;
    }

    @Override
    public String deleteFolder(int fileId) throws Exception {
//        本地
        File file = fileMapper.findFileById(fileId);
        Path path = Paths.get(getBaseDirectory(file.getUserId()), file.getFilePath() + "/" + file.getFileName());
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                if (directoryStream.iterator().hasNext()) {
                    throw new IOException("Folder is not empty: " + path);
                }
            }
            Files.delete(path);

//            数据库
            fileMapper.deleteFolder(fileId);
            return "文件夹删除成功: " + path;
        } else {
            throw new IOException("Folder does not exist or is not a directory: " + path);
        }
    }

    @Override
    public List<File> searchFiles(int userId, String searchText, String searchType) {
        return fileMapper.searchFiles(userId, searchText, searchType);
    }
}