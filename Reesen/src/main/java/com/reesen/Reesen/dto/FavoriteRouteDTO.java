package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.FavoriteRoute;

public class FavoriteRouteDTO {
	
	private Long id;
	private Long routeId;

	public FavoriteRouteDTO() {
	}

	public FavoriteRouteDTO(FavoriteRoute favoriteRoute) {
		this.id = favoriteRoute.getId();
		this.routeId = favoriteRoute.getRoute().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	
}
