/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.features.logout.impl

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.element.android.libraries.architecture.AsyncAction
import io.element.android.libraries.testtags.TestTags
import io.element.android.libraries.ui.strings.CommonStrings
import io.element.android.tests.testutils.EnsureNeverCalled
import io.element.android.tests.testutils.EnsureNeverCalledWithParam
import io.element.android.tests.testutils.EventsRecorder
import io.element.android.tests.testutils.clickOn
import io.element.android.tests.testutils.ensureCalledOnce
import io.element.android.tests.testutils.ensureCalledOnceWithParam
import io.element.android.tests.testutils.pressBack
import io.element.android.tests.testutils.pressTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogoutViewTest {
    @get:Rule val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `clicking on logout sends a LogoutEvents`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>()
        rule.setContent {
            LogoutView(
                aLogoutState(
                    eventSink = eventsRecorder
                ),
                onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                onBackClicked = EnsureNeverCalled(),
                onSuccessLogout = EnsureNeverCalledWithParam(),
            )
        }
        rule.clickOn(CommonStrings.action_signout)
        eventsRecorder.assertSingle(LogoutEvents.Logout(false))
    }

    @Test
    fun `confirming logout sends a LogoutEvents`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>()
        rule.setContent {
            LogoutView(
                aLogoutState(
                    logoutAction = AsyncAction.Confirming,
                    eventSink = eventsRecorder
                ),
                onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                onBackClicked = EnsureNeverCalled(),
                onSuccessLogout = EnsureNeverCalledWithParam(),
            )
        }
        rule.pressTag(TestTags.dialogPositive.value)
        eventsRecorder.assertSingle(LogoutEvents.Logout(false))
    }

    @Test
    fun `clicking on back invoke back callback`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>(expectEvents = false)
        ensureCalledOnce { callback ->
            rule.setContent {
                LogoutView(
                    aLogoutState(
                        eventSink = eventsRecorder
                    ),
                    onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                    onBackClicked = callback,
                    onSuccessLogout = EnsureNeverCalledWithParam(),
                )
            }
            rule.pressBack()
        }
    }

    @Test
    fun `clicking on confirm after error sends a LogoutEvents`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>()
        rule.setContent {
            LogoutView(
                aLogoutState(
                    logoutAction = AsyncAction.Failure(Exception("Failed to logout")),
                    eventSink = eventsRecorder
                ),
                onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                onBackClicked = EnsureNeverCalled(),
                onSuccessLogout = EnsureNeverCalledWithParam(),
            )
        }
        rule.clickOn(CommonStrings.action_signout_anyway)
        eventsRecorder.assertSingle(LogoutEvents.Logout(true))
    }

    @Test
    fun `clicking on cancel after error sends a LogoutEvents`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>()
        rule.setContent {
            LogoutView(
                aLogoutState(
                    logoutAction = AsyncAction.Failure(Exception("Failed to logout")),
                    eventSink = eventsRecorder
                ),
                onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                onBackClicked = EnsureNeverCalled(),
                onSuccessLogout = EnsureNeverCalledWithParam(),
            )
        }
        rule.clickOn(CommonStrings.action_cancel)
        eventsRecorder.assertSingle(LogoutEvents.CloseDialogs)
    }

    @Test
    fun `success logout invoke onSuccessLogout`() {
        val data = "data"
        val eventsRecorder = EventsRecorder<LogoutEvents>(expectEvents = false)
        ensureCalledOnceWithParam<String?>(data) { callback ->
            rule.setContent {
                LogoutView(
                    aLogoutState(
                        logoutAction = AsyncAction.Success(data),
                        eventSink = eventsRecorder
                    ),
                    onChangeRecoveryKeyClicked = EnsureNeverCalled(),
                    onBackClicked = EnsureNeverCalled(),
                    onSuccessLogout = callback,
                )
            }
        }
    }

    @Test
    fun `last session setting button invoke onChangeRecoveryKeyClicked`() {
        val eventsRecorder = EventsRecorder<LogoutEvents>(expectEvents = false)
        ensureCalledOnce { callback ->
            rule.setContent {
                LogoutView(
                    aLogoutState(
                        isLastSession = true,
                        eventSink = eventsRecorder
                    ),
                    onChangeRecoveryKeyClicked = callback,
                    onBackClicked = EnsureNeverCalled(),
                    onSuccessLogout = EnsureNeverCalledWithParam(),
                )
            }
            rule.clickOn(CommonStrings.common_settings)
        }
    }
}
