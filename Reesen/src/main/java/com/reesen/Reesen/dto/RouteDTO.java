package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RouteDTO {

	@NotNull(message = "{required}")
	private LocationDTO departure;
	@NotNull(message = "{required}")
	private LocationDTO destination;

	public RouteDTO(Route route) {
		this.departure = new LocationDTO(route.getDeparture());
		this.destination = new LocationDTO(route.getDestination());
	}

}
