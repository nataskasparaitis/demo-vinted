package com.vinted.demovinted.application

import android.content.Context // a handle to the app environment
import android.graphics.Bitmap // an in-memory image
import com.bumptech.glide.GlideBuilder // used to configure Glide
import com.bumptech.glide.annotation.GlideModule // marks an app-wide Glide config
import com.bumptech.glide.load.DecodeFormat // image color depth
import com.bumptech.glide.module.AppGlideModule // base class for the app-wide Glide config
import com.bumptech.glide.request.RequestOptions // default options for image requests

@GlideModule // register this as Glide's app-wide configuration
public class GlideModule : AppGlideModule() { // our Glide settings

    override fun applyOptions(context: Context, builder: GlideBuilder) { // Glide calls this to apply defaults
        builder.setDefaultRequestOptions( // set defaults for every image load
            RequestOptions() // start from empty options
                .format(DecodeFormat.PREFER_ARGB_8888) // decode in full color
                .encodeFormat(Bitmap.CompressFormat.WEBP) // cache encoded as WEBP
                .skipMemoryCache(false) // keep using the memory cache
                .apply(RequestOptions.centerInsideTransform()) // scale images to fit inside the view
                .disallowHardwareConfig() // avoid hardware bitmaps (safer for some operations)
        )
    }
}