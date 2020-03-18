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

package utils

import models.beneficiaries.{Beneficiaries, ClassOfBeneficiary, IndividualBeneficiary}
import play.api.i18n.Messages
import viewmodels.addAnother.{AddRow, AddToRows}

class AddABeneficiaryViewHelper(beneficiaries: Beneficiaries)(implicit messages: Messages) {

  private def render(beneficiary: IndividualBeneficiary, index: Int) : AddRow = {
        AddRow(
          name = beneficiary.name.displayName,
          typeLabel = messages(s"entities.beneficiaries.individual"),
          changeLabel = messages("site.change.details"),
          changeUrl = None,
          removeLabel =  messages("site.delete"),
          removeUrl = None
        )
  }

  private def render(beneficiary: ClassOfBeneficiary, index: Int) : AddRow = {
    AddRow(
      name = beneficiary.description,
      typeLabel = messages(s"entities.beneficiaries.unidentified"),
      changeLabel = messages("site.change.details"),
      changeUrl = Some(controllers.classofbeneficiary.routes.DescriptionController.onPageLoad(index).url),
      removeLabel =  messages("site.delete"),
      removeUrl = None
    )
  }

  def rows : AddToRows = {
    val complete =
      beneficiaries.individualDetails.zipWithIndex.map(x => render(x._1, x._2)) ++
      beneficiaries.unidentified.zipWithIndex.map(x => render(x._1, x._2))

    AddToRows(Nil, complete)
  }

}
