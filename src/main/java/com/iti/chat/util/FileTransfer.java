package com.iti.chat.util;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.iti.chat.service.ClientService;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileTransfer {
    private static final int BUFFER_SIZE = 1024 * 1024;
    public static void save(String name, RemoteInputStream remoteInputStream) throws IOException {
        InputStream fileData = RemoteInputStreamClient.wrap(remoteInputStream);
        ReadableByteChannel from = Channels.newChannel(fileData);
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        File rootPath = new File("uploaded files");
        if(!rootPath.exists()) {
            Files.createDirectories(rootPath.toPath());
        }
        String path = "uploaded files/" + name;
        WritableByteChannel to = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        int bytes = 0;
        long totalBytes = 0;
        while ((bytes = from.read(buffer)) != -1)
        {
            totalBytes += bytes;
            buffer.flip();
            while (buffer.hasRemaining()) {
                to.write(buffer);
            }
            buffer.clear();
        }
        from.close();
        to.close();
        fileData.close();
    }

    public static void get(String path, ClientService clientService) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        RemoteInputStreamServer remoteFileData = new SimpleRemoteInputStream(inputStream);
        clientService.download(remoteFileData);
    }
}
