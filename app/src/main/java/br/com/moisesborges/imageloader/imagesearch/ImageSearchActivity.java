package br.com.moisesborges.imageloader.imagesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.moisesborges.imageloader.App;
import br.com.moisesborges.imageloader.R;
import br.com.moisesborges.imageloader.models.Image;
import br.com.moisesborges.imageloader.services.ImageService;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImageSearchActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mImagesRecyclerView;
    private ImagesAdapter mAdapter;
    private ViewGroup mProgressBarViewGroup;

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            searchImages(charSequence.toString());
                        } else {
                            clearOldResults();
                        }
                    }
                });
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

    private void searchImages(String query) {
        unsubscribeFromSearchImages();
        clearOldResults();
        showProgressBar(true);

        mSearchImagesSubscription = mImageService.searchImages(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showProgressBar(false);
                        if (e instanceof HttpException) {

                        }

                        if (e instanceof IOException) {

                        }
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
    }

    private void clearOldResults() {
        mAdapter.clearImages();
    }

    static class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

        private List<Image> mImages = new ArrayList<>();
        private Picasso mPicasso;

        public ImagesAdapter(Picasso picasso) {
            mPicasso = picasso;
        }

        public void clearImages() {
            mImages.clear();
            notifyDataSetChanged();
        }

        public void addImage(@NonNull Image image) {
            mImages.add(image);
            notifyItemInserted(mImages.size() - 1);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_view_group_item, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Image image = mImages.get(position);

            mPicasso.load(image.getThumbnailUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.mImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Log.d("Adapter", "error on load " + image.getContentUrl());
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return mImages.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            }
        }
    }
}
