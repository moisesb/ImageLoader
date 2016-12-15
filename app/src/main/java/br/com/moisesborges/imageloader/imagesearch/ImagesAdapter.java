package br.com.moisesborges.imageloader.imagesearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.moisesborges.imageloader.R;
import br.com.moisesborges.imageloader.models.Image;

/**
 * Created by moises.anjos on 15/12/2016.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

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
