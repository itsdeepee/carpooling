package org.example.Service.Mappers;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.Entities.LocationEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LocationMapper {

    public LocationEntity mapCarmenFeatureToLocationEntity(CarmenFeature carmenFeature){


        LocationEntity locationEntity=new LocationEntity();
        locationEntity.setText(carmenFeature.text());
        locationEntity.setFullPlaceName(carmenFeature.placeName());

        locationEntity.setAddress(carmenFeature.properties().get("address").toString());
        locationEntity.setCoordinates(carmenFeature.center().coordinates());

        return locationEntity;

    }

    public LocationDTO mapCarmenFeatureToLocationDTO(CarmenFeature carmenFeature){

        LocationDTO locationDTO=new LocationDTO();
        locationDTO.setText(carmenFeature.text());
        System.out.println(carmenFeature.placeName());
        locationDTO.setFullPlaceName(carmenFeature.placeName());
        if(!Objects.isNull(carmenFeature.properties())){
            if(!Objects.isNull(carmenFeature.properties().get("address"))){
                locationDTO.setAddress(carmenFeature.properties().get("address").toString());
            }else{
                locationDTO.setAddress("");
            }
        }else {
            locationDTO.setAddress("");
        }
        locationDTO.setCoordinates(Objects.requireNonNull(carmenFeature.center()).coordinates());

        return locationDTO;

    }

    public LocationEntity mapLocationDTOtoLocationEntity(LocationDTO locationDTO){
        LocationEntity locationEntity=new LocationEntity();
        locationEntity.setText(locationDTO.getText());
        locationEntity.setAddress(locationDTO.getAddress());
        locationEntity.setFullPlaceName(locationDTO.getFullPlaceName());
        locationEntity.setCoordinates(locationDTO.getCoordinates());

        return locationEntity;
    }

    public LocationDTO mapLocationEntitytoLocationDTO(LocationEntity locationEntity){
        LocationDTO locationDTO=new LocationDTO();
        locationDTO.setText(locationEntity.getText());
        locationDTO.setAddress(locationEntity.getAddress());
        locationDTO.setFullPlaceName(locationEntity.getFullPlaceName());
        locationDTO.setCoordinates(locationEntity.getCoordinates());

        return locationDTO;
    }
}
