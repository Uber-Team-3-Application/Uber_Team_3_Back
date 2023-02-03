package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	@NotNull(message = "{required}")
	@Min(value = 1, message = "{regex}")
	@Max(value = 10, message = "{regex}")
	private int rating;
	@NotNull(message = "{required}")
	@Length(max = 500, message = "{regex}")
	private String comment;


}
