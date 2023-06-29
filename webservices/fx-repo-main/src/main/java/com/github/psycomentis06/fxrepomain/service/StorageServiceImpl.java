package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private StorageProperties storageProperties;

    @Autowired
    StorageServiceImpl(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public StorageServiceStatus init() {
        var dir = new File(storageProperties.getDir());
        if (!dir.exists()) {
            if (dir.mkdir()) {
                return StorageServiceStatus.UPLOAD_DIR_CREATED;
            }
        } else {
            return StorageServiceStatus.UPLOAD_DIR_FOUND;
        }
        return StorageServiceStatus.UPLOAD_DIR_NOT_CREATED;
    }

    @Override
    public String store(MultipartFile file) {
        String newFileName = UUID.randomUUID().toString();
        try {
            Files.copy(file.getInputStream(), new File(storageProperties.getDir() + "/" + newFileName).toPath());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
        return newFileName;
    }

    public BufferedInputStream getFileBuffer(String filename) throws IOException {
        var file = new File(storageProperties.getDir() + "/" + filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }
        return new BufferedInputStream(new FileInputStream(file));
    }

    @Override
    public byte[] load(String filename) throws IOException {
        return getFileBuffer(filename).readAllBytes();
    }

    public byte[] load(String filename, int start, int length) throws IOException {
        byte[] b = new byte[length];
        getFileBuffer(filename).readNBytes(b, start, length);
        return b;
    }

    @Override
    public StorageServiceStatus delete(String filename) {
        var file = new File(storageProperties.getDir() + "/" + filename);
        if (!file.exists()) {
            return StorageServiceStatus.FILE_NOT_FOUND;
        } else {
            if (file.delete()) {
                return StorageServiceStatus.FILE_REMOVED;
            } else {
                return StorageServiceStatus.FILE_NOT_REMOVED;
            }
        }
    }
}
