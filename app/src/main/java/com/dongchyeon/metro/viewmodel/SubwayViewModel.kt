package com.dongchyeon.metro.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongchyeon.metro.data.SeatInfo
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
                val seatInfo = HashMap<String, ArrayList<SeatInfo>>()

                for (x in result) {
                    if (stationInfo.containsKey(x.updnLine)) {
                        stationInfo[x.updnLine]!!.add(x)
                    } else {
                        stationInfo[x.updnLine] = ArrayList<RealtimeArrivalList>()
                        stationInfo[x.updnLine]!!.add(x)
                    }
                }

                for (x in result) {
                    // 임산부, 노약자 잔여석을 리스트로 받음
                    val pregnant = arrayListOf<Int>(1, 2, 0, 0, 2, 1, 1, 2)
                    val elderly = arrayListOf<Int>(0, 2, 1, 0, 0, 2, 1, 2)

                    if (seatInfo.containsKey(x.updnLine)) {
                        seatInfo[x.updnLine]!!.add(SeatInfo(x.trainLineNm, pregnant, elderly))
                    } else {
                        seatInfo[x.updnLine] = ArrayList<SeatInfo>()
                        seatInfo[x.updnLine]!!.add(SeatInfo(x.trainLineNm, pregnant, elderly))
                    }
                }

                val subwayList = ArrayList<SubwayInfo>()
                for ((key) in stationInfo) {
                    subwayList.add(SubwayInfo(key, stationInfo[key]!!, seatInfo[key]!!))
                }

                liveData.postValue(subwayList)
            } else {
                Log.d("result", "너 조진거야")
            }
        }
    }
}