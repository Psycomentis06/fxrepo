package com.github.psycomentis06.fxrepomain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    /**
     * Init storage service by creating default directory and other necessary stuff
      * @return byte flag indicating the state of the operation
     */
    StorageServiceStatus init();

    /**
     * Store file to the filesystem
     * @param file multipart sent file
     * @return byte flag indicating the state of the operation
     */
    StorageServiceStatus store(MultipartFile file);

    /**
     * Get the file
     * @param filename filename
     * @return buffer containing the file
     * @throws IOException Caused by the fileInputStream class
     */
    byte[] load(String filename) throws IOException;

    /**
     * Get a chunk/piece of the file
     * @param filename filename
     * @param start offset/position to start reading
     * @param length length of bytes to read
     * @return return a buffer with the requested chunk
     * @throws IOException Exception caused thrown by the getFileBuffer method
     */
    byte[] load(String filename, int start, int length) throws IOException;


    /**
     * Get the file as a BufferedInputStream
     * @param filename filename
     * @return BufferedInputStream object
     * @throws IOException If file does not exist or  other IO problem occurs
     */
    BufferedInputStream getFileBuffer(String filename) throws IOException;

    /**
     * Remove a file from the file system
     * @param filename filename
     * @return byte flag indicating the state of the operation
     */
    StorageServiceStatus delete(String filename);

}
