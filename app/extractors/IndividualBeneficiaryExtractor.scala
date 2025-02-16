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

import java.time.LocalDate

import models._
import models.beneficiaries.IndividualBeneficiary
import pages.QuestionPage
import pages.individualbeneficiary._
import pages.individualbeneficiary.add._
import pages.individualbeneficiary.amend._
import play.api.libs.json.JsPath

import scala.util.{Success, Try}

class IndividualBeneficiaryExtractor extends BeneficiaryExtractor[IndividualBeneficiary] {

  override def apply(answers: UserAnswers,
                     individual: IndividualBeneficiary,
                     index: Int): Try[UserAnswers] = {

    super.apply(answers, individual, index)
      .flatMap(_.set(RoleInCompanyPage, individual.roleInCompany))
      .flatMap(_.set(NamePage, individual.name))
      .flatMap(answers => extractDateOfBirth(individual.dateOfBirth, answers))
      .flatMap(answers => extractShareOfIncome(individual.income, answers))
      .flatMap(answers => extractCountryOfNationality(individual.nationality, answers))
      .flatMap(answers => extractCountryOfResidence(individual.countryOfResidence, answers))
      .flatMap(answers => extractAddress(individual.address, answers))
      .flatMap(answers => extractIdentification(individual, answers))
      .flatMap(_.set(MentalCapacityYesNoPage, individual.mentalCapacityYesNo))
      .flatMap(_.set(VPE1FormYesNoPage, individual.vulnerableYesNo))
  }

  override def shareOfIncomeYesNoPage: QuestionPage[Boolean] = IncomeDiscretionYesNoPage
  override def shareOfIncomePage: QuestionPage[Int] = IncomePercentagePage

  override def countryOfResidenceYesNoPage: QuestionPage[Boolean] = CountryOfResidenceYesNoPage
  override def ukCountryOfResidenceYesNoPage: QuestionPage[Boolean] = CountryOfResidenceUkYesNoPage
  override def countryOfResidencePage: QuestionPage[String] = CountryOfResidencePage

  override def addressYesNoPage: QuestionPage[Boolean] = AddressYesNoPage
  override def ukAddressYesNoPage: QuestionPage[Boolean] = LiveInTheUkYesNoPage
  override def ukAddressPage: QuestionPage[UkAddress] = UkAddressPage
  override def nonUkAddressPage: QuestionPage[NonUkAddress] = NonUkAddressPage

  override def startDatePage: QuestionPage[LocalDate] = StartDatePage

  override def indexPage: QuestionPage[Int] = IndexPage

  override def basePath: JsPath = pages.individualbeneficiary.basePath

  private def extractCountryOfNationality(countryOfNationality: Option[String],
                                          answers: UserAnswers): Try[UserAnswers] = {
      extractCountryOfResidenceOrNationality(
        country = countryOfNationality,
        answers = answers,
        yesNoPage = CountryOfNationalityYesNoPage,
        ukYesNoPage = CountryOfNationalityUkYesNoPage,
        page = CountryOfNationalityPage
      )
  }

  private def extractIdentification(individualBeneficiary: IndividualBeneficiary,
                                    answers: UserAnswers): Try[UserAnswers] = {
    if (answers.isTaxable) {
      individualBeneficiary.identification match {
        case Some(NationalInsuranceNumber(nino)) =>
          answers.set(NationalInsuranceNumberYesNoPage, true)
          .flatMap(_.set(NationalInsuranceNumberPage, nino))
        case Some(p: Passport) =>
          answers.set(NationalInsuranceNumberYesNoPage, false)
          .flatMap(_.set(PassportOrIdCardDetailsYesNoPage, true))
          .flatMap(_.set(PassportOrIdCardDetailsPage, p.asCombined))
        case Some(id: IdCard) =>
          answers.set(NationalInsuranceNumberYesNoPage, false)
          .flatMap(_.set(PassportOrIdCardDetailsYesNoPage, true))
          .flatMap(_.set(PassportOrIdCardDetailsPage, id.asCombined))
        case Some(combined: CombinedPassportOrIdCard) =>
          answers.set(NationalInsuranceNumberYesNoPage, false)
          .flatMap(_.set(PassportOrIdCardDetailsYesNoPage, true))
          .flatMap(_.set(PassportOrIdCardDetailsPage, combined))
        case _ =>
          answers.set(NationalInsuranceNumberYesNoPage, false)
          .flatMap(answers => extractPassportOrIdCardDetailsYesNo(individualBeneficiary.address.isDefined, answers))
      }
    } else {
      Success(answers)
    }
  }

  private def extractDateOfBirth(dateOfBirth: Option[LocalDate],
                                  answers: UserAnswers): Try[UserAnswers] = {
    dateOfBirth match {
      case Some(dob) =>
        answers.set(DateOfBirthYesNoPage, true)
        .flatMap(_.set(DateOfBirthPage, dob))
      case None =>
        answers.set(DateOfBirthYesNoPage, false)
    }
  }

  private def extractPassportOrIdCardDetailsYesNo(hasAddress: Boolean, answers: UserAnswers): Try[UserAnswers] = {
    if (hasAddress) {
      answers.set(PassportOrIdCardDetailsYesNoPage, false)
    } else {
      Success(answers)
    }
  }

}

