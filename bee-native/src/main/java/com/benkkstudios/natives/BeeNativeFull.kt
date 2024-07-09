/*
 * *
 *  * Created by BENKKSTUDIOS on 7/9/24, 11:05 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/9/24, 11:04 PM
 *
 */


package com.benkkstudios.natives

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.benkkstudios.natives.databinding.FullViewBinding
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


class BeeNativeFull @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    private var nativeAd: NativeAd? = null
    private var binding: FullViewBinding = FullViewBinding.inflate(LayoutInflater.from(context), this)

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
        val adView = binding.nativeAdView
        val mediaView: MediaView = binding.adMedia
        adView.mediaView = mediaView
        adView.headlineView = binding.adHeadline
        adView.bodyView = binding.adBody
        adView.callToActionView = binding.adCallToAction
        val imageView: ImageView = binding.adAppIcon
        imageView.clipToOutline = true
        adView.iconView = imageView
        (adView.headlineView as TextView).text = nativeAd.headline
        mediaView.mediaContent = nativeAd.mediaContent
        if (nativeAd.body == null) {
            (adView.bodyView as TextView).visibility = INVISIBLE
        } else {
            (adView.bodyView as TextView).visibility = VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            (adView.callToActionView as Button).visibility = INVISIBLE
        } else {
            (adView.callToActionView as Button).visibility = VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            (adView.iconView as ImageView).visibility = GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
            (adView.iconView as ImageView).visibility = VISIBLE
        }
        adView.setNativeAd(nativeAd)
        val videoController = nativeAd.mediaContent!!.videoController

        if (videoController.hasVideoContent()) {
            videoController.videoLifecycleCallbacks = object : VideoLifecycleCallbacks() {
            }
        }
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean = !TextUtils.isEmpty(nativeAd.store) && TextUtils.isEmpty(nativeAd.advertiser)

    fun getNativeAdView(): NativeAdView {
        return binding.nativeAdView
    }

    fun destroyNative() {
        nativeAd?.destroy()
    }
}