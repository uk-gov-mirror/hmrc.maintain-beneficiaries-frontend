/*
 * Copyright 2021 HM Revenue & Customs
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

package utils.mappers

import models.UserAnswers
import models.beneficiaries.ClassOfBeneficiary
import pages.classofbeneficiary.{DescriptionPage, EntityStartPage}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads

import java.time.LocalDate

class ClassOfBeneficiaryMapper extends Mapper[ClassOfBeneficiary] {

  def apply(answers: UserAnswers): Option[ClassOfBeneficiary] = {
    val readFromUserAnswers: Reads[ClassOfBeneficiary] =
      (
        DescriptionPage.path.read[String] and
        EntityStartPage.path.read[LocalDate]
      )(
        (desc, start) =>
          ClassOfBeneficiary(desc, start, provisional = true)
      )

    mapAnswersWithExplicitReads(answers, readFromUserAnswers)
  }
}