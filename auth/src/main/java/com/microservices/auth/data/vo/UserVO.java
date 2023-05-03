package com.microservices.auth.data.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserVO implements Serializable {

	private static final long serialVersionUID = 4483107857512074089L;

	private String userName;
	private String password;
}
