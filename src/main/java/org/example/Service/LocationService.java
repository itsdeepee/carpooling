package org.example.Service;


import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import jakarta.validation.constraints.NotNull;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.Entities.LocationEntity;
import org.example.Repository.LocationRepository;
import org.example.Service.Mappers.LocationMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class LocationService {

    @Value("${api.key}")
    private String apiKey;
    private LocationMapper locationMapper;
    private LocationRepository locationRepository;

    public LocationService(LocationMapper locationMapper,LocationRepository locationRepository){
        this.locationMapper=locationMapper;
        this.locationRepository=locationRepository;
    }

    public LocationDTO addLocation(LocationDTO locationDTO){


       LocationEntity resultLocationEntity =saveLocation(locationDTO);
        return  locationMapper.mapLocationEntitytoLocationDTO(resultLocationEntity);

    }

    public LocationEntity saveLocation(LocationDTO locationDTO){

        if(locationRepository.existsByFullPlaceName(locationDTO.getFullPlaceName())){
            return locationMapper.mapLocationDTOtoLocationEntity(locationDTO);
        }
        LocationEntity locationEntity=locationMapper.mapLocationDTOtoLocationEntity(locationDTO);
        LocationEntity resultLocationEntity=locationRepository.save(locationEntity);
        return locationEntity;
    }
    public List<LocationDTO> getCoordinatesByAddress(String address) throws IOException {

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(apiKey)
                .query(address)
                .build();
        List<LocationDTO> resultsList=new ArrayList<>();

        CompletableFuture<List<LocationDTO>> future=new CompletableFuture<>();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NotNull Call<GeocodingResponse> call, @NotNull Response<GeocodingResponse> response) {
                assert response.body() != null;
                List<CarmenFeature> results = response.body().features();

                System.out.println(results.size());

                if (results.size() > 0) {
                    for(int i=0;i<results.size();i++){
                        CarmenFeature feature = results.get(i);
                        LocationDTO locationDTO= locationMapper.mapCarmenFeatureToLocationDTO(feature);
                        resultsList.add(locationDTO);

                    }


                } else {

                    // No result for your request were found.
                    System.out.println("onResponse: No result found");


                }
                future.complete(resultsList);


            }

            @Override
            public void onFailure(@NotNull Call<GeocodingResponse> call, @NotNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });

      try{
          return future.get();
      }catch(InterruptedException | ExecutionException e){
          e.printStackTrace();
          return Collections.emptyList();
      }
    }
}
