/*
 * *
 *  * Created by BENKKSTUDIOS on 7/9/24, 11:05 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 7/7/24, 4:14 PM
 *
 */

package com.benkkstudios.natives

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.benkkstudios.natives.databinding.MediumViewBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class BeeNativeMedium @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    private var nativeAd: NativeAd? = null
    private var binding: MediumViewBinding = MediumViewBinding.inflate(LayoutInflater.from(context), this)

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
        with(binding) {
            primary.text = nativeAd.headline
            secondary.visibility = VISIBLE
            val secondaryText = if (adHasOnlyStore(nativeAd)) {
                nativeAdView.storeView = secondary
                nativeAd.store
            } else if (nativeAd.advertiser!!.isNotEmpty()) {
                nativeAdView.advertiserView = secondary
                nativeAd.advertiser
            } else {
                ""
            }

            nativeAd.callToAction?.let { cta.text = it }
            nativeAd.starRating?.let {
                if (it > 0) {
                    secondary.visibility = GONE
                    ratingBar.visibility = VISIBLE
                    ratingBar.rating = it.toFloat()
                    nativeAdView.starRatingView = ratingBar
                } else {
                    secondary.text = secondaryText
                    secondary.visibility = VISIBLE
                    ratingBar.visibility = GONE
                }
            }

            nativeAd.icon?.let {
                icon.visibility = VISIBLE
                icon.setImageDrawable(it.drawable)
            }

            nativeAd.body?.let {
                body.text = it
                nativeAdView.bodyView = body
            }

            nativeAdView.setNativeAd(nativeAd)
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