package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.FavoriteRoute;

public interface IFavoriteRouteService {
    void remove(Long id);
    FavoriteRoute findOne(Long id);
    FavoriteRoute save(FavoriteRoute favoriteRoute);
}
