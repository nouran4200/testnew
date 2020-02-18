package com.iti.chat.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileTransferServiceProvider extends UnicastRemoteObject implements FileTransferService {
    protected static final String ROOT_FILES_PATH = "uploaded files";
    private static FileTransferServiceProvider instance;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private FileTransferServiceProvider() throws RemoteException {

    }

    public static FileTransferServiceProvider getInstance() throws RemoteException {
        if(instance == null) {
            instance = new FileTransferServiceProvider();
        }
        return instance;
    }

    @Override
    public void uploadFile(String saveLocation, RemoteInputStream remoteInputStream, ClientService clientService) throws IOException {
        InputStream fileData = RemoteInputStreamClient.wrap(remoteInputStream);
        ReadableByteChannel from = Channels.newChannel(fileData);
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        File rootPath = new File("uploaded files");
        if(!rootPath.exists()) {
            Files.createDirectories(rootPath.toPath());
        }
        WritableByteChannel to = FileChannel.open(Paths.get(saveLocation), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
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

    @Override
    public void uploadImage(String saveLocation, RemoteInputStream remoteInputStream, ClientService clientService) throws IOException {
        uploadFile(saveLocation, remoteInputStream, clientService);
    }

    @Override
    public void downloadFile(String remoteLocation, ClientService clientService) throws IOException {
        InputStream inputStream = new FileInputStream(remoteLocation);
        RemoteInputStreamServer remoteFileData = new SimpleRemoteInputStream(inputStream);
        clientService.downloadFile(remoteFileData);
    }

    @Override
    public void downloadImage(String remoteLocation, ClientService clientService) throws IOException {
        InputStream inputStream = new FileInputStream(remoteLocation);
        RemoteInputStreamServer remoteFileData = new SimpleRemoteInputStream(inputStream);
        clientService.downloadImage(remoteFileData);
    }
}
