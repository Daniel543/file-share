package com.bc.fileshare.service;

import com.bc.fileshare.model.File;
import com.bc.fileshare.model.Secret;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public interface FileService {

	public void storeFile(MultipartFile multipartFile, String secret, String username) throws IOException, GeneralSecurityException;

	public File downloadFile(int id, String secret, String username) throws IOException, GeneralSecurityException;

	public Secret generateSecret() throws NoSuchAlgorithmException;

	public void deleteFile(int id, String username);

	public File getFile(int id, String username);

	public void updateFile(File file);
}
