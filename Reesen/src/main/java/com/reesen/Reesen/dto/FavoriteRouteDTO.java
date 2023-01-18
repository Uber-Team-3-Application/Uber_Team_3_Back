package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.FavoriteRoute;

import java.util.Set;

public class FavoriteRouteDTO {
	
	private Long id;
	private Set<LocationDTO> locations;
	private String favoriteName;

	public FavoriteRouteDTO() {
	}

	public FavoriteRouteDTO(Set<LocationDTO> locations, String favoriteName) {
		this.locations = locations;
		this.favoriteName = favoriteName;
	}

	public FavoriteRouteDTO(Long id, Set<LocationDTO> locations, String favoriteName) {
		this.id = id;
		this.locations = locations;
		this.favoriteName = favoriteName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(Set<LocationDTO> locations) {
		this.locations = locations;
	}

	public String getFavoriteName() {
		return favoriteName;
	}

	public void setFavoriteName(String favoriteName) {
		this.favoriteName = favoriteName;
	}
}
