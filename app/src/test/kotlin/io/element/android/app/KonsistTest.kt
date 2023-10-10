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

package io.element.android.app

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withAllAnnotationsOf
import com.lemonappdev.konsist.api.ext.list.withAllParentsOf
import com.lemonappdev.konsist.api.verify.assertTrue
import io.element.android.libraries.architecture.Presenter
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import org.junit.Test

class KonsistTest {

    @Test
    fun `classes extending 'Presenter' should have 'Presenter' suffix`() {
        Konsist.scopeFromProject()
            .classes()
            .withAllParentsOf(Presenter::class)
            .assertTrue { it.name.endsWith("Presenter") }
    }

    @Test
    fun `function with '@PreviewsDayNight' annotation should have 'Preview' suffix`() {
        Konsist
            .scopeFromProject()
            .functions()
            .withAllAnnotationsOf(PreviewsDayNight::class)
            .assertTrue {
                it.hasNameEndingWith("Preview") &&
                    it.hasNameEndingWith("LightPreview").not() &&
                    it.hasNameEndingWith("DarkPreview").not()
            }
    }
}
