package io.damo.chucknorrisjokes

import android.content.Context
import io.damo.chucknorrisjokes.icndb.IcndbApi
import okhttp3.OkHttpClient

class ServiceLocator(
    val appContext: Context,
    okHttp: OkHttpClient = OkHttpClient(),
    baseApiUrl: String = "https://api.icndb.com/"
) {

    val api = IcndbApi(okHttp, baseApiUrl)
}
