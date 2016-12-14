package br.com.moisesborges.imageloader;

import android.app.Application;
import android.content.Context;

import br.com.moisesborges.imageloader.di.DaggerServiceComponent;
import br.com.moisesborges.imageloader.di.ServiceComponent;
import br.com.moisesborges.imageloader.di.NetModule;
import br.com.moisesborges.imageloader.di.ServiceModule;

/**
 * Created by moises.anjos on 12/12/2016.
 */

public class App extends Application {

    private ServiceComponent mServiceComponent;
    private static final String PIXABAY_BASE_URL = "https://pixabay.com/";
    private static final String BING_BASE_URL = "https://api.cognitive.microsoft.com/bing/v5.0/images/";

    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = getApplicationContext();
        mServiceComponent = DaggerServiceComponent.builder()
                .netModule(new NetModule(PIXABAY_BASE_URL, BING_BASE_URL, context))
                .serviceModule(new ServiceModule())
                .build();

    }

    public ServiceComponent getServiceComponent() {
        return mServiceComponent;
    }
}
