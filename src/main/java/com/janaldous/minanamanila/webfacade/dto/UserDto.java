package com.janaldous.minanamanila.webfacade.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDto {

	@ApiModelProperty(value = "Auth0 id", name = "auth0Id", dataType = "String", example = "google|alsiu234kjhiufkjn2ij3bkj234bj")
	private String auth0Id;

	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@MobileNumberConstraint
	private String contactNumber;
	@Email
	private String email;

}
