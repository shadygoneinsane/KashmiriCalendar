package koushur.kashmirievents.network.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import koushir.kashmirievents.R
import koushur.kashmirievents.utility.AppConstants

/**
 * Remote config for Firebase
 *
 * Author: Vikesh Dass
 * Created on: 21-11-2020
 * Email : vikeshdass@gmail.com
 */
class RemoteConfigManager {
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(AppConstants.FETCH_TIMEOUT)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults_en)
        fetchAndActivate()
    }

    private fun fetchAndActivate() = firebaseRemoteConfig.fetchAndActivate()

    fun getVideoId(videoId: String) = firebaseRemoteConfig.getString(videoId)
}
