package com.bc.fileshare.api;

import com.bc.fileshare.model.File;
import com.bc.fileshare.model.Secret;
import com.bc.fileshare.model.User;
import com.bc.fileshare.service.FileService;
import com.bc.fileshare.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;


@RestController
@RequestMapping("files")
public class FileApiController {

	private static Logger logger = LoggerFactory.getLogger(FileApiController.class);

	@Autowired
	private FileService fileService;

	@Autowired
	private UserService userService;


	@GetMapping(value = "/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id, @RequestParam(name = "secret") String secret) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			File fileToReturn = fileService.downloadFile(id, secret, auth.getName());
			if (fileToReturn != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileToReturn.getName() + "\"")
					.body(new ByteArrayResource(fileToReturn.getPayload().getData()));
			}
			logger.debug("File not found", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested file was not found.");
		} catch (IOException e) {
			logger.debug("Exception in downloadFile", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while reading file!");
		} catch (GeneralSecurityException e) {
			logger.debug("Exception in downloadFile", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while decrypting file!");
		}
	}

	@PostMapping(value = "/upload")
	public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("secret") String secret) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			fileService.storeFile(file, secret.trim(), auth.getName());
		} catch (Exception e) {
			logger.info("Exception in storeFile", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving file!");
		}
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public File getFile(@PathVariable int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return fileService.getFile(id, auth.getName());
	}

	@GetMapping(value = "/secret", produces = "application/json")
	public Secret getSecret() {
		try {
			return fileService.generateSecret();
		} catch (NoSuchAlgorithmException e) {
			logger.error("ERROR GENERATING SECRET", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating secret!");
		}
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<Set<File>> getFiles() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<File> files = userService.getUser(auth.getName()).getFiles();
		//files.forEach( file -> file.add(ControllerLinkBuilder.linkTo(FileApiController.class).slash(file.getFileId()).withSelfRel()));
		return ResponseEntity.ok(files);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	void deleteFile(@PathVariable int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		fileService.deleteFile(id, auth.getName());
	}

	@PostMapping(value = "/{id}/users")
	public void addUserToFile(@PathVariable int id, @RequestParam("username") String username) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		File file = fileService.getFile(id, auth.getName());
		boolean isAllowedToAdd = false;
		for (User user : file.getUsers()) {
			if (user.getUsername().equals(auth.getName())) {
				isAllowedToAdd = true;
			}
		}
		if (isAllowedToAdd) {
			User userToAdd = userService.getUser(username.trim());
			file.addUser(userToAdd);
			fileService.updateFile(file);
		}
	}

	@GetMapping(value = "/{id}/users", produces = "application/json")
	public ResponseEntity<Set<User>> getUsersForFile(@PathVariable int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<User> users = fileService.getFile(id, auth.getName()).getUsers();
		return ResponseEntity.ok(users);
	}

	@DeleteMapping(value = "/{fileId}/users/{userId}", produces = "application/json")
	public @ResponseBody
	void deleteFile(@PathVariable int fileId, @PathVariable int userId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		File file = fileService.getFile(fileId, auth.getName());
		Set<User> users = file.getUsers();
		boolean isAllowedToRemove = false;
		User userToRemove = null;
		for (User user : users) {
			if (user.getUsername().equals(auth.getName())) {
				isAllowedToRemove = true;
			}
			if (user.getUserId() == userId) {
				userToRemove = user;
			}
		}
		if (isAllowedToRemove) {
			file.removeUser(userToRemove);
			fileService.updateFile(file);

		}

	}


}
