package com.fameoflight.falcon.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
public class UserContext {
	private String requestId;
}