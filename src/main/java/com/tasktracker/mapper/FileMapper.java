package com.tasktracker.mapper;

import com.tasktracker.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-23
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

    int insertFile(File file);

    List<File> listFilesByFilePath(int userId, String filePath);

    File findFileById(int fileId);


    void deleteFileById(int fileId);

    void deleteFolder(int fileId);

    int createFolder(File file);

    List<File> searchFiles(int userId, String searchText, String searchType);
}
