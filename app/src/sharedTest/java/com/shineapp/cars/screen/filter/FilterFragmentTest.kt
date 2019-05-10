package com.shineapp.cars.screen.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.shineapp.cars.FragmentTestActivity
import com.shineapp.cars.R
import com.shineapp.cars.di.viewmodel.ViewModelFactory
import com.shineapp.cars.screen.ActivityViewModel
import com.shineapp.cars.screen.EMPTY_DATA
import com.shineapp.cars.screen.TEST_DATA
import com.shineapp.cars.screen.list.ListType
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Provider


class FilterFragmentTest {

    lateinit var fragment: FilterFragment
    lateinit var activityViewModel: ActivityViewModel

    @get:Rule
    var instantTaskRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = object : ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java, true, true) {
        override fun afterActivityLaunched() = runOnUiThread {
            fragment = FilterFragment()
            activity.startFragment(fragment, this@FilterFragmentTest::inject)
        }
    }

    val mockNavController = mock(NavController::class.java)

    fun inject(fragment: FilterFragment) {
        activityViewModel = ActivityViewModel()
        fragment.viewModelFactory = TestViewModelFactory(activityViewModel)
    }

    @Test
    fun testNavigationToListIfEmptyManufacturerClicked() {
        Navigation.setViewNavController(fragment.requireView(), mockNavController)
        onView(withId(R.id.manufacturerLayout)).perform(ViewActions.click())
        verify(mockNavController).navigate(ArgumentMatchers.argThat<FilterFragmentDirections.ActionShowList> {
            it.listType == ListType.MANUFACTURER &&
                    it.manufacturer!!.isEmpty()
        })
    }


    @Test
    fun testNavigationToListIfNonEmptyManufacturerClicked() {
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
        }
        Navigation.setViewNavController(fragment.requireView(), mockNavController)
        onView(withId(R.id.manufacturerLayout)).perform(ViewActions.click())
        verify(mockNavController).navigate(ArgumentMatchers.argThat<FilterFragmentDirections.ActionShowList> {
            it.listType == ListType.MANUFACTURER &&
                    it.manufacturer!!.isNotEmpty()
        })
    }

    @Test
    fun testModelAndYearAreDisabledInTheBeginning() {
        assertViewEnabled(R.id.manufacturerLayout)
        assertViewDisabled(R.id.yearLayout)
        assertViewDisabled(R.id.modelLayout)
    }


    @Test
    fun testModelEnabledIfHasManufacturer() {
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
        }
        assertViewEnabled(R.id.manufacturerLayout)
        assertViewEnabled(R.id.modelLayout)
        assertViewDisabled(R.id.yearLayout)
    }

    @Test
    fun testYearEnabledIfHasModel() {
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
            activityViewModel.setModel(TEST_DATA)
        }
        assertViewEnabled(R.id.manufacturerLayout)
        assertViewEnabled(R.id.modelLayout)
        assertViewEnabled(R.id.yearLayout)
    }


    @Test
    fun testYearIsDisabledAfterManufacturerIsSet() {
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
            activityViewModel.setModel(TEST_DATA)
            activityViewModel.setYear(TEST_DATA)
        }
        assertViewEnabled(R.id.manufacturerLayout)
        assertViewEnabled(R.id.modelLayout)
        assertViewEnabled(R.id.yearLayout)
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
        }

        assertViewDisabled(R.id.yearLayout)
        assertViewEnabled(R.id.modelLayout)

        rule.runOnUiThread {
            activityViewModel.setManufacturer(EMPTY_DATA)
        }
        assertViewDisabled(R.id.modelLayout)
        assertViewDisabled(R.id.yearLayout)
    }


    @Test
    fun testNavigationToListIfNonEmptyYearClicked() {
        rule.runOnUiThread {
            activityViewModel.setManufacturer(TEST_DATA)
            activityViewModel.setModel(TEST_DATA)
            activityViewModel.setYear(TEST_DATA)
        }
        Navigation.setViewNavController(fragment.requireView(), mockNavController)
        onView(withId(R.id.yearLayout)).perform(ViewActions.click())
        verify(mockNavController).navigate(ArgumentMatchers.argThat<FilterFragmentDirections.ActionShowList> {
            it.listType == ListType.YEAR &&
                    it.manufacturer!! == TEST_DATA.id &&
                    it.model!! == TEST_DATA.id &&
                    it.year!! == TEST_DATA.id
        })
    }
}

fun assertViewEnabled(id: Int) {
    onView(withId(id)).check(matches(isEnabled()))
}


fun assertViewDisabled(id: Int) {
    onView(withId(id)).check(matches(not(isEnabled())))
}

class TestViewModelFactory(
    val model: ViewModel
) : ViewModelFactory(mapOf(Pair(model.javaClass, Provider { model })))

