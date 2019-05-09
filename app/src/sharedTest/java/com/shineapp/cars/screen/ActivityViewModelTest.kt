package com.shineapp.cars.screen

import com.shineapp.cars.utils.observeOnce
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.rules.TestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.system.mutable

val TEST_DATA = Data("testId", "testValue")

@RunWith(JUnit4::class)
class ActivityViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var activityModel: ActivityViewModel

    @Before
    fun setUp() {
        activityModel = ActivityViewModel()
    }

    @Test
    fun testInitialLiveDataValues() {

        activityModel.manufacturerLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }
        activityModel.modelLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }
        activityModel.yearLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }

    }

    @Test
    fun testSetManufacturer() {
        activityModel.setManufacturer(TEST_DATA)
        activityModel.manufacturerLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
    }

    @Test
    fun testSetModel() {
        activityModel.setModel(TEST_DATA)
        activityModel.modelLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
    }

    @Test
    fun testSetYear() {
        activityModel.setYear(TEST_DATA)
        activityModel.yearLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
    }

    @Test
    fun setAllUtilityMethodWorksCorrectly() {
        activityModel.setAll(TEST_DATA)

        activityModel.manufacturerLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
        activityModel.modelLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
        activityModel.yearLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
    }

    @Test
    fun modelAndYearAreClearedAfterManufacturerIsSet() {
        activityModel.setAll(TEST_DATA)

        activityModel.setManufacturer(TEST_DATA)

        activityModel.manufacturerLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
        activityModel.modelLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }
        activityModel.yearLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }

    }


    @Test
    fun yearAreClearedAfterModelIsSet() {
        activityModel.setAll(TEST_DATA)

        activityModel.setModel(TEST_DATA)

        activityModel.manufacturerLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
        activityModel.modelLiveData.observeOnce {
            assertEquals(TEST_DATA, it)
        }
        activityModel.yearLiveData.observeOnce {
            assertEquals(EMPTY_DATA, it)
        }
    }
}

fun ActivityViewModel.setAll(data: Data) {
    manufacturerLiveData.mutable.value = data
    modelLiveData.mutable.value = data
    yearLiveData.mutable.value = data
}
