package com.bc.fileshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FILES_PAYLOAD")
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FilePayload {

	public FilePayload (){

	}

	public FilePayload (byte[] data ,File file){
		this.setData(data);
		this.setFile(file);
	}

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id")
	private int id;


	@OneToOne(fetch= FetchType.LAZY)
	@MapsId
	private File file;

	@JsonIgnore
	@Column(name="DATA", columnDefinition="varbinary(max)" )
	@Lob
	private byte[] data;




}
