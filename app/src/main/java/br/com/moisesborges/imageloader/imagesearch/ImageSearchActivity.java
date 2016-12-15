package br.com.moisesborges.imageloader.imagesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.moisesborges.imageloader.App;
import br.com.moisesborges.imageloader.R;
import br.com.moisesborges.imageloader.models.Image;
import br.com.moisesborges.imageloader.services.ImageService;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ImageSearchActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mImagesRecyclerView;
    private ImagesAdapter mAdapter;
    private ViewGroup mProgressBarViewGroup;
    private TextView mMessageTextView;

    private Subscription mSearchViewSubscription;
    private Subscription mSearchImagesSubscription;

    @Inject
    ImageService mImageService;
    @Inject
    Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (SearchView) findViewById(R.id.search);
        mImagesRecyclerView = (RecyclerView) findViewById(R.id.images_recycler_view);
        mProgressBarViewGroup = (ViewGroup) findViewById(R.id.loading_images_view);
        mMessageTextView = (TextView) findViewById(R.id.message_text_view);

        injectDependencies();
        setupToolbar();
        setupSearchView();
        setupRecyclerView();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchImages(query);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeFromSubscriptions();
    }

    private void unsubscribeFromSubscriptions() {
        if (mSearchViewSubscription != null && !mSearchViewSubscription.isUnsubscribed()) {
            mSearchViewSubscription.unsubscribe();
        }
        unsubscribeFromSearchImages();
    }

    private void unsubscribeFromSearchImages() {
        if (mSearchImagesSubscription != null && !mSearchImagesSubscription.isUnsubscribed()) {
            mSearchImagesSubscription.unsubscribe();
        }
    }

    private void injectDependencies() {
        ((App) getApplication()).getServiceComponent().inject(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchViewSubscription = RxSearchView.queryTextChanges(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            searchImages(charSequence.toString());
                        } else {
                            clearOldResults();
                            String message = getResources().getString(R.string.search_something_message);
                            showMessage(message);
                        }
                    }
                });
    }

    private void showMessage(String message) {
        mMessageTextView.setText(message);
        mMessageTextView.setVisibility(View.VISIBLE);
        mImagesRecyclerView.setVisibility(View.GONE);
        mProgressBarViewGroup.setVisibility(View.GONE);
    }


    private void setupRecyclerView() {
        int spanCount = getResources().getInteger(R.integer.images_recycler_view_columns);
        mAdapter = new ImagesAdapter(mPicasso);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setAutoMeasureEnabled(true);
        mImagesRecyclerView.setLayoutManager(layoutManager);
        mImagesRecyclerView.setAdapter(mAdapter);
        mImagesRecyclerView.setHasFixedSize(true);
        mImagesRecyclerView.setItemViewCacheSize(20);
    }

    private void searchImages(final String query) {
        unsubscribeFromSearchImages();
        clearOldResults();
        showProgressBar(true);

        mSearchImagesSubscription = mImageService.searchImages(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onCompleted() {
                        if (mAdapter.isEmpty()) {
                            String noResultsFound = getResources().getString(R.string.no_results_found, query);
                            showMessage(noResultsFound);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgressBar(false);
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            Log.d("onError", httpException.code() + " " + httpException.message());
                        }

                        if (e instanceof IOException) {
                            Log.d("onError", "network error");
                            String checkConnection = getResources().getString(R.string.check_connection);
                            showMessage(checkConnection);
                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Image image) {
                        showProgressBar(false);
                        mAdapter.addImage(image);
                    }
                });
    }


    private void showProgressBar(boolean loading) {
        if (loading) {
            mProgressBarViewGroup.setVisibility(View.VISIBLE);
            mImagesRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressBarViewGroup.setVisibility(View.GONE);
            mImagesRecyclerView.setVisibility(View.VISIBLE);
        }
        mMessageTextView.setVisibility(View.GONE);
    }

    private void clearOldResults() {
        mAdapter.clearImages();
    }

}
