package br.com.moisesborges.imageloader.network;

import br.com.moisesborges.imageloader.models.PixabayImageResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by moises.anjos on 12/12/2016.
 */

public interface PixabayApi {

    @GET("api")
    Observable<PixabayImageResponse> searchImages(@Query("q") String query, @Query("key") String key);

}
