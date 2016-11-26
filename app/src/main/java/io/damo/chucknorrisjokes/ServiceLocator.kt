package io.damo.chucknorrisjokes

import android.content.Context
import io.damo.chucknorrisjokes.favorites.AndroidFileStorage
import io.damo.chucknorrisjokes.favorites.Favorites
import io.damo.chucknorrisjokes.icndb.IcndbApi
import okhttp3.OkHttpClient

class ServiceLocator(
    appContext: Context,
    okHttp: OkHttpClient = OkHttpClient(),
    baseApiUrl: String = "https://api.icndb.com/"
) {

    val api = IcndbApi(okHttp, baseApiUrl)
    val fileStorage = AndroidFileStorage(appContext)
    val favorites = Favorites.load(fileStorage)
}
