package com.reesen.Reesen.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class FavoriteRouteDTO {
	
	private Long id;
	private Set<LocationDTO> locations;
	private String favoriteName;


	public FavoriteRouteDTO(Set<LocationDTO> locations, String favoriteName) {
		this.locations = locations;
		this.favoriteName = favoriteName;
	}

	public FavoriteRouteDTO(Long id, Set<LocationDTO> locations, String favoriteName) {
		this.id = id;
		this.locations = locations;
		this.favoriteName = favoriteName;
	}

}
