package com.bc.fileshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FILES", schema = "dbo")
@EqualsAndHashCode
public class File implements Serializable {
    private int fileId;
    private Instant uploaded;
    private String name;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
	private Set<User> users = new HashSet<>();
    private Long size;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private FilePayload payload;

    @Id
    @Column(name = "FILE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getFileId(){
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public FilePayload getPayload() {
        return payload;
    }

    public void setPayload(FilePayload payload) {
        this.payload = payload;
    }

    @Column(name = "uploaded", nullable = false)
    public Instant getUploaded(){
        return uploaded;
    }

    public void setUploaded(Instant uploaded){
        this.uploaded = uploaded;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "files", cascade = CascadeType.PERSIST)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Column(name = "size")
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void addUser(User user){
        users.add(user);
        user.getFiles().add(this);
    }
    public void removeUser(User user){
        users.remove(user);
        user.getFiles().remove(this);
    }

    @PreRemove
    private void removeFileFromUsers() {
        for (User u : users) {
            u.getFiles().remove(this);
        }
    }
}
