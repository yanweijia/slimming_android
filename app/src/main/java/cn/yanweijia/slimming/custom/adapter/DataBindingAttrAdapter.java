package cn.yanweijia.slimming.custom.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cn.yanweijia.slimming.utils.HttpUtils;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * Created by weijia on 06/10/2017.
 *
 * @author weijia
 */

public class DataBindingAttrAdapter {
    private static final String TAG = "DataBindingAttrAdapter";
    /**
     * usd Glide to load image
     *
     * @param imageView
     * @param foodId
     */
    @BindingAdapter({"app:foodImageId", "app:error"})
    public static void loadImage(final ImageView imageView, int foodId, final Drawable errorDrawable) {
        String url = RequestUtils.URL_FOOD_IMAGE.replace(RequestUtils.REPLACEMENT_FOODID,String.valueOf(foodId));
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "onLoadFailed: ",e );
                        imageView.setImageDrawable(errorDrawable);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
}
