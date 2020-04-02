/*
 * Copyright 2020 HM Revenue & Customs
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

package forms

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class IncomePercentageFormProvider @Inject() extends Mappings {

  def withPrefix(prefix: String): Form[Int] =
    Form(
      "value" -> incomePercentage(
        requiredKey = s"$prefix.error.income_percentage.required",
        wholeNumberKey = s"$prefix.error.income_percentage.integer",
        nonNumericKey = s"$prefix.error.income_percentage.non_numeric",
        validPercentageKey = s"$prefix.error.income_percentage.less_than_100"
      )
    )
}
