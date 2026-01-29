package com.app.stockmanagement.presentation.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.app.stockmanagement.R
import com.app.stockmanagement.presentation.MainActivity
import com.app.stockmanagement.util.Constants.INVALID_CREDENTIALS
import org.junit.Rule
import org.junit.Test


class LoginFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun givenInvalidOrValidCredentialsWhenLoginClickedShouldDisplayErrorOrNavigateToDashboard() {

        onView(withId(R.id.username)).perform(
            typeText("test"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("wrong password"),
            ViewActions.closeSoftKeyboard()
        )

        onView(withId(R.id.btnLogIn)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(INVALID_CREDENTIALS)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.password)).perform(
            clearText(),
            typeText("test"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.btnLogIn)).perform(click())
        onView(withId(R.id.navigation_dashboard))
            .check(matches(isDisplayed()))

    }
}
