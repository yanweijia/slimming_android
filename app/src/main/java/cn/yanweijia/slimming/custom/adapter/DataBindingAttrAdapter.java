package cn.yanweijia.slimming.custom.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.yanweijia.slimming.utils.HttpUtils;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * Created by weijia on 06/10/2017.
 *
 * @author weijia
 */

public class DataBindingAttrAdapter {

    /**
     * usd Glide to load image
     *
     * @param imageView
     * @param foodId
     */
    @BindingAdapter({"app:foodImageId", "app:error"})
    public static void loadImage(ImageView imageView, int foodId, Drawable errorDrawable) {
        String url = RequestUtils.URL_FOOD_IMAGE.replace(RequestUtils.REPLACEMENT_FOODID,String.valueOf(foodId));
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView)
                .onLoadFailed(errorDrawable);
    }
}
