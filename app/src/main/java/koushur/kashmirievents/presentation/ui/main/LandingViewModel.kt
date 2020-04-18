package koushur.kashmirievents.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.repository.WeatherRepository
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val dataRepository: WeatherRepository) :
    BaseViewModel() {

    val showErrorView = MutableLiveData<Boolean>()
    val cityName = MutableLiveData<String>()
}