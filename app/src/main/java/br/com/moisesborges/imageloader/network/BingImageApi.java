package br.com.moisesborges.imageloader.network;

import br.com.moisesborges.imageloader.models.BingImageResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by moises.anjos on 13/12/2016.
 */

public interface BingImageApi {

    @Headers("Ocp-Apim-Subscription-Key : 0971542cece2493796f1b315fea28de5")
    @GET("search?")
    Observable<BingImageResponse> searchImages(@Query("q") String query);
}
