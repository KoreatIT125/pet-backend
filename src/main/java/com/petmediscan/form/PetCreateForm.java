package com.petmediscan.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

import com.disaster.safety.user.SiteUser;
import com.petmediscan.entity.Species;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetCreateForm {

    @NotBlank(message = "이름은 필수 항목입니다.")
	//@Size(min = 3, max = 25)
	private String name;
	
	@NotBlank(message = "종은 필수 항목입니다.")
	private Species species;
	
    private String breed;

    private LocalDateTime birth_date;
    
	@NotBlank(message = "반려인은 필수 항목입니다.")
	private SiteUser user;	
}