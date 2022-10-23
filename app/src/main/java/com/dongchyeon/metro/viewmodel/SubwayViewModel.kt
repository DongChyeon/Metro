package com.dongchyeon.metro.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongchyeon.metro.data.SubwayInfo
import com.dongchyeon.metro.data.network.NetworkRepository
import com.dongchyeon.metro.data.network.dto.RealtimeArrivalList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubwayViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {
    val liveData = MutableLiveData<List<SubwayInfo>>()

    fun getData() = liveData

    fun loadData(statnNm: String) {
        viewModelScope.launch {
            val data = repository.getRealTimeStationArrival(statnNm)
            if (data.isSuccessful) {
                val result = data.body()!!.realtimeArrivalList
                val stationInfo = HashMap<String, ArrayList<RealtimeArrivalList>>()

                for (x in result) {
                    if (stationInfo.containsKey(x.updnLine)) {
                        stationInfo[x.updnLine]!!.add(x)
                    } else {
                        stationInfo[x.updnLine] = ArrayList<RealtimeArrivalList>()
                        stationInfo[x.updnLine]!!.add(x)
                    }
                }

                val subwayList = ArrayList<SubwayInfo>()
                for ((key, value) in stationInfo) {
                    subwayList.add(SubwayInfo(key, value))
                }

                liveData.postValue(subwayList)
            } else {
                Log.d("result", "너 조진거야")
            }
        }
    }
}