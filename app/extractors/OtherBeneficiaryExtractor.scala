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

package extractors

import models.beneficiaries.OtherBeneficiary
import models.{NonUkAddress, UkAddress, UserAnswers}
import pages.QuestionPage
import pages.other._
import pages.other.add.StartDatePage
import pages.other.amend.IndexPage
import play.api.libs.json.JsPath

import java.time.LocalDate
import scala.util.Try

class OtherBeneficiaryExtractor extends BeneficiaryExtractor[OtherBeneficiary] {

  override def apply(answers: UserAnswers,
                     otherBeneficiary: OtherBeneficiary,
                     index: Int): Try[UserAnswers] = {

    super.apply(answers, otherBeneficiary, index)
      .flatMap(_.set(DescriptionPage, otherBeneficiary.description))
      .flatMap(answers => extractShareOfIncome(otherBeneficiary.income, answers))
      .flatMap(answers => extractCountryOfResidence(otherBeneficiary.countryOfResidence, answers))
      .flatMap(answers => extractAddress(otherBeneficiary.address, answers))
  }

  override def shareOfIncomeYesNoPage: QuestionPage[Boolean] = DiscretionYesNoPage
  override def shareOfIncomePage: QuestionPage[Int] = ShareOfIncomePage

  override def countryOfResidenceYesNoPage: QuestionPage[Boolean] = CountryOfResidenceYesNoPage
  override def ukCountryOfResidenceYesNoPage: QuestionPage[Boolean] = CountryOfResidenceUkYesNoPage
  override def countryOfResidencePage: QuestionPage[String] = CountryOfResidencePage

  override def addressYesNoPage: QuestionPage[Boolean] = AddressYesNoPage
  override def ukAddressYesNoPage: QuestionPage[Boolean] = AddressUkYesNoPage
  override def ukAddressPage: QuestionPage[UkAddress] = UkAddressPage
  override def nonUkAddressPage: QuestionPage[NonUkAddress] = NonUkAddressPage

  override def startDatePage: QuestionPage[LocalDate] = StartDatePage

  override def indexPage: QuestionPage[Int] = IndexPage

  override def basePath: JsPath = pages.other.basePath
}
