
package com.example.ciclospa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeMainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()



    @Test
    fun navegarARegistro() {
        composeTestRule.onNodeWithText("Crear mi cuenta")
            .performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun navegarALogin() {
        composeTestRule.onNodeWithText("Ya tengo una cuenta")
            .performClick()

        composeTestRule.waitForIdle()
    }
}
