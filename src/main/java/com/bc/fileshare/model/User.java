package com.bc.fileshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS", schema = "dbo")
@EqualsAndHashCode
public class User implements Serializable {
	private int userId;
	private String username;
	private String email;
	@JsonIgnore
	private String password;
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	private Set<File> files = new HashSet<>();

	@Id
	@Column(name = "USER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
		name = "USEER_FILES",
		joinColumns = {@JoinColumn(name = "USER_ID")},
		inverseJoinColumns = {@JoinColumn(name = "FILE_ID")}
	)
	public Set<File> getFiles() {
		return files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}

	public void addFile(File file) {
		files.add(file);
		file.getUsers().add(this);
	}

	public void removeFile(File file) {
		files.remove(file);
		file.getUsers().remove(this);
	}
}
