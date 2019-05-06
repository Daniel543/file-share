package com.bc.fileshare.service;

import com.bc.fileshare.model.File;
import com.bc.fileshare.model.FilePayload;
import com.bc.fileshare.model.Secret;
import com.bc.fileshare.model.User;
import com.bc.fileshare.repository.FileRepository;
import com.bc.fileshare.repository.FileWithDataRepository;
import com.bc.fileshare.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	@Autowired
	FileRepository fileRepository;
	@Autowired
	FileWithDataRepository fileWithDataRepository;
	@Autowired
	UserRepository userRepository;

	@Override
	public void storeFile(MultipartFile multipartFile, String secret, String username) throws IOException, GeneralSecurityException {
		File fileToStore = new File();
		byte[] data = encryptData(multipartFile.getBytes(), secret);
		fileToStore.setPayload(new FilePayload(data, fileToStore));
		//fileToStore.getPayload().setData(data);
		fileToStore.setName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
		fileToStore.setUploaded(Instant.now());
		fileToStore.setSize(multipartFile.getSize());
		Optional<User> fileOwner = userRepository.findByUsernameEquals(username);

		fileToStore.addUser(fileOwner.orElseThrow(() -> new UsernameNotFoundException("User doesn't exist.")));
		fileRepository.saveAndFlush(fileToStore);
	}

	@Override
	public File downloadFile(int id, String secret, String username) throws IOException, GeneralSecurityException {
		Optional<File> fileOptional = fileWithDataRepository.findFileByIdAndUsername(id,username);

		if (fileOptional.isPresent()) {
			File fileToReturn = fileOptional.get();
			fileToReturn.getPayload().setData(decryptData(fileToReturn.getPayload().getData(), secret));
			return fileToReturn;
		}
		return null;
	}

	@Override
	public File getFile(int id, String username){
		Optional<File> fileOptional = fileRepository.findFileByIdAndUsername(id,username);
		return fileOptional.orElseThrow(() -> new ResourceNotFoundException("File doesn't exist."));
	}

	@Override
	public void updateFile(File file) {
		fileRepository.save(file);
	}

	@Override
	public Secret generateSecret() throws NoSuchAlgorithmException {
		SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
		return new Secret(Base64.getEncoder().encodeToString(secretKey.getEncoded()), Instant.now());
	}

	@Override
	public void deleteFile(int id, String username) {
		Optional<File> fileToRemove = fileRepository.findFileByIdAndUsername(id,username);
		fileRepository.delete(fileToRemove.orElseThrow(() -> new ResourceNotFoundException("File doesn't exist.")));
	}


	private byte[] encryptData(byte[] data, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException {
		SecretKey secretKey = getSecretKeyFromSecretString(secret);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
		cipherOutputStream.write(data);
		cipherOutputStream.close();
		byte[] encryptedData = ArrayUtils.addAll(cipher.getIV(), byteArrayOutputStream.toByteArray());
		byteArrayOutputStream.close();
		return encryptedData;
	}

	private byte[] decryptData(byte[] data, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, InvalidKeyException {
		SecretKey secretKey = getSecretKeyFromSecretString(secret);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		byte[] fileIv = new byte[16];
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		byteArrayInputStream.read(fileIv);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
		CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher);
		return IOUtils.toByteArray(cipherInputStream);
	}

	private SecretKey getSecretKeyFromSecretString(String secret) {
		byte[] keyEncoded = Base64.getDecoder().decode(secret);
		return new SecretKeySpec(keyEncoded, 0, keyEncoded.length, "AES");
	}

}
