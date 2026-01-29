package com.app.stockmanagement.presentation.supplier

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.app.stockmanagement.R
import com.app.stockmanagement.presentation.supplier.add_supplier.AddSupplierFragment
import launchFragmentInHiltContainer
import org.hamcrest.core.IsNot.not
import org.junit.Test

class AddSupplierFragmentTest {

    @Test
    fun givenInvalidOrValidFormDataSaveBtnShouldBeEnabledOrDisabled() {
        launchFragmentInHiltContainer<AddSupplierFragment>()

        onView(withId(R.id.name)).perform(
            typeText("Global supplier"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.contactPerson)).perform(
            typeText("Jane doe"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.phone)).perform(typeText("123123"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.email)).perform(
            typeText("jane@doe.com"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.action_save)).check(matches(not(isEnabled())))

        onView(withId(R.id.address)).perform(
            typeText("Example blvd 1"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.action_save)).check(matches(isEnabled()))
    }
}