package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreateFavoriteRideDTO;
import com.reesen.Reesen.dto.FavoriteRideDTO;
import com.reesen.Reesen.model.FavoriteRide;

import java.util.Set;

public interface IFavoriteRideService {
    void remove(Long id);
    FavoriteRide findOne(Long id);
    FavoriteRide save(FavoriteRide favoriteRide);

    void deleteFavouriteRides(Long id, Long passId);

    FavoriteRideDTO addFavouriteRide(CreateFavoriteRideDTO favouriteRide, Long id);

    Set<FavoriteRideDTO> getFavouriteRides(Long idFromToken);

    boolean validateRideDTO(CreateFavoriteRideDTO favouriteRide);
}
