package com.dongchyeon.metro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongchyeon.metro.data.model.SeatInfo
import com.dongchyeon.metro.data.model.SubwayInfo
import com.dongchyeon.metro.data.model.dto.RealtimeArrivalList
import com.dongchyeon.metro.data.repository.SubwayRepository
import com.dongchyeon.metro.repository.BleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubwayViewModel @Inject constructor(
    private val subwayRepository: SubwayRepository,
    private val bleRepository: BleRepository
) : ViewModel() {
    private val _subwayData = MutableLiveData<List<SubwayInfo>>()
    val subwayData: MutableLiveData<List<SubwayInfo>> get() = _subwayData

    fun loadData(statnNm: String) {
        viewModelScope.launch {
            subwayRepository.getRealTimeStationArrival(statnNm)
                .collect {
                    val stationInfo = HashMap<String, ArrayList<RealtimeArrivalList>>()
                    val seatInfo = HashMap<String, ArrayList<SeatInfo>>()

                    for (x in it.realtimeArrivalList) {
                        if (stationInfo.containsKey(x.updnLine)) {
                            stationInfo[x.updnLine]!!.add(x)
                        } else {
                            stationInfo[x.updnLine] = ArrayList<RealtimeArrivalList>()
                            stationInfo[x.updnLine]!!.add(x)
                        }
                    }

                    for (x in it.realtimeArrivalList) {
                        // 임산부, 노약자 잔여석을 리스트로 받음
                        val pregnant = ArrayList<Int>()
                        val elderly = ArrayList<Int>()

                        if (bleRepository.isConnected()) {
                            pregnant.add(bleRepository.getPressure("pregnant"))
                            elderly.add(bleRepository.getPressure("elderly"))
                        }
                        pregnant.addAll(arrayListOf(3, 0, 2, 0, 2, 2, 1))
                        elderly.addAll(arrayListOf(2, 0, 2, 0, 2, 2, 1))

                        if (seatInfo.containsKey(x.updnLine)) {
                            seatInfo[x.updnLine]!!.add(SeatInfo(x.trainLineNm, pregnant, elderly))
                        } else {
                            seatInfo[x.updnLine] = ArrayList<SeatInfo>()
                            seatInfo[x.updnLine]!!.add(SeatInfo(x.trainLineNm, pregnant, elderly))
                        }
                    }

                    val keyList: List<String> =
                        stationInfo.keys.toList().sortedWith(UpdnLineComparator())

                    val subwayList = ArrayList<SubwayInfo>()
                    for (key in keyList) {
                        subwayList.add(SubwayInfo(key, stationInfo[key]!!, seatInfo[key]!!))
                    }

                    subwayData.postValue(subwayList)
                }
        }
    }

    class UpdnLineComparator : Comparator<String> {
        override fun compare(p0: String?, p1: String?): Int {
            val a = when (p0) {
                "상행" -> 0
                "하행" -> 1
                "외선" -> 2
                "내선" -> 3
                else -> 4
            }
            val b = when (p1) {
                "상행" -> 0
                "하행" -> 1
                "외선" -> 2
                "내선" -> 3
                else -> 4
            }

            return a - b
        }
    }
}