package com.bc.fileshare.model;

import java.time.Instant;

public class Secret {
	private final String value;
	private final Instant generated;
	public Secret (String secret, Instant generated){
		this.value = secret;
		this.generated = generated;
	}

	public Instant getGenerated() {
		return generated;
	}

	public String getValue() {
		return value;
	}
}
