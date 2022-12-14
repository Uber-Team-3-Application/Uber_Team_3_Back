package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.model.FavoriteRoute;
import com.reesen.Reesen.repository.FavoriteRouteRepository;
import com.reesen.Reesen.service.interfaces.IFavoriteRouteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteRouteService implements IFavoriteRouteService{
	
	private final FavoriteRouteRepository favoriteRouteRepository;
	
	@Autowired
	public FavoriteRouteService(FavoriteRouteRepository favoriteRouteRepository) {
		this.favoriteRouteRepository = favoriteRouteRepository;
	}

	@Override
	public void remove(Long id) {
		this.favoriteRouteRepository.deleteById(id);
	}

	@Override
	public FavoriteRoute findOne(Long id) {
		return this.favoriteRouteRepository.findById(id).orElseGet(null);
	}

	@Override
	public FavoriteRoute save(FavoriteRoute favoriteRoute) {
		return this.favoriteRouteRepository.save(favoriteRoute);
	}

}
