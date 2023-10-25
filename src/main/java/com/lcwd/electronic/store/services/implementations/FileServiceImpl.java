package com.lcwd.electronic.store.services.implementations;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.interfaces.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + fileNameWithExtension;
        logger.info("originalFilename : {}", fullPathWithFileName);

        if(extension.equalsIgnoreCase(".png")
            || extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
        ){
            //file save
            logger.info("before file save {}", path);
            File folder = new File(path);
            if(!folder.exists()){
                //create folder
                logger.info("run before folder ");
                folder.mkdirs();
                logger.info("run after folder {} " , folder.getName());
            }

            //upload file
            Files.copy(file.getInputStream() , Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else{
            throw  new BadApiRequestException("File with this " + extension + " not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;
        logger.info("full path name : {}" ,fullPath);
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }


}
