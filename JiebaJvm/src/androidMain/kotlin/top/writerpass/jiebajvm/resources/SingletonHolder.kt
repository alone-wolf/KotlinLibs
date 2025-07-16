package top.writerpass.jiebajvm.resources

import android.content.res.AssetManager

var assetManager: AssetManager? = null

fun initAssetManager(am: AssetManager) {
    assetManager = am
}