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

package utils.print

import java.time.format.DateTimeFormatter

import models.{Address, CombinedPassportOrIdCard, Description, HowManyBeneficiaries, IdCard, IdentificationDetailOptions, NonUkAddress, Passport, UkAddress}
import play.api.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}
import utils.countryOptions.CountryOptions
import uk.gov.hmrc.domain.Nino

object CheckAnswersFormatters {

  val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def yesOrNo(answer: Boolean)(implicit messages: Messages): Html = {
    if (answer) {
      HtmlFormat.escape(messages("site.yes"))
    } else {
      HtmlFormat.escape(messages("site.no"))
    }
  }

  def formatNino(nino: String): Html = HtmlFormat.escape(Nino(nino).formatted)

  def formatAddress(address: Address, countryOptions: CountryOptions): Html = {
    address match {
      case a: UkAddress => formatUkAddress(a)
      case a: NonUkAddress => formatNonUkAddress(a, countryOptions)
    }
  }

  private def formatUkAddress(address: UkAddress): Html = {
    val lines =
      Seq(
        Some(HtmlFormat.escape(address.line1)),
        Some(HtmlFormat.escape(address.line2)),
        address.line3.map(HtmlFormat.escape),
        address.line4.map(HtmlFormat.escape),
        Some(HtmlFormat.escape(address.postcode))
      ).flatten

    Html(lines.mkString("<br />"))
  }

  private def formatNonUkAddress(address: NonUkAddress, countryOptions: CountryOptions): Html = {
    val lines =
      Seq(
        Some(HtmlFormat.escape(address.line1)),
        Some(HtmlFormat.escape(address.line2)),
        address.line3.map(HtmlFormat.escape),
        Some(country(address.country, countryOptions))
      ).flatten

    Html(lines.mkString("<br />"))
  }

  private def country(code: String, countryOptions: CountryOptions): String =
    countryOptions.options.find(_.value.equals(code)).map(_.label).getOrElse("")

  def formatIdentificationDetails(identificationDetailOptions: IdentificationDetailOptions): Html = {
    identificationDetailOptions match {
      case IdentificationDetailOptions.IdCard => HtmlFormat.escape("ID card")
      case IdentificationDetailOptions.Passport => HtmlFormat.escape("Passport")
    }
  }

  def formatPassportOrIdCardDetails(id: CombinedPassportOrIdCard, countryOptions: CountryOptions): Html = {
    val lines =
      Seq(
        Some(country(id.countryOfIssue, countryOptions)),
        Some(HtmlFormat.escape(id.number)),
        Some(HtmlFormat.escape(id.expirationDate.format(dateFormatter)))
      ).flatten

    Html(lines.mkString("<br />"))
  }


  def formatPassportDetails(passport: Passport, countryOptions: CountryOptions): Html = {
    val lines =
      Seq(
        Some(country(passport.countryOfIssue, countryOptions)),
        Some(HtmlFormat.escape(passport.number)),
        Some(HtmlFormat.escape(passport.expirationDate.format(dateFormatter)))
      ).flatten

    Html(lines.mkString("<br />"))
  }

  def formatIdCardDetails(idCard: IdCard, countryOptions: CountryOptions): Html = {
    val lines =
      Seq(
        Some(country(idCard.countryOfIssue, countryOptions)),
        Some(HtmlFormat.escape(idCard.number)),
        Some(HtmlFormat.escape(idCard.expirationDate.format(dateFormatter)))
      ).flatten

    Html(lines.mkString("<br />"))
  }

  def percentage(value: Int): Html = HtmlFormat.escape(s"$value%")

  def formatDescription(description: Description): Html = {
    val lines =
      Seq(
        Some(HtmlFormat.escape(description.description)),
        description.description1.map(HtmlFormat.escape),
        description.description2.map(HtmlFormat.escape),
        description.description3.map(HtmlFormat.escape),
        description.description4.map(HtmlFormat.escape)
      ).flatten

    Html(lines.mkString("<br />"))
  }

  def formatNumberOfBeneficiaries(answer: HowManyBeneficiaries)(implicit messages: Messages): Html = {
    answer match {
      case HowManyBeneficiaries.Over1 => HtmlFormat.escape(messages("employmentBeneficiary.numberOfBeneficiaries.over1"))
      case HowManyBeneficiaries.Over101 => HtmlFormat.escape(messages("employmentBeneficiary.numberOfBeneficiaries.over101"))
      case HowManyBeneficiaries.Over201 => HtmlFormat.escape(messages("employmentBeneficiary.numberOfBeneficiaries.over201"))
      case HowManyBeneficiaries.Over501 => HtmlFormat.escape(messages("employmentBeneficiary.numberOfBeneficiaries.over501"))
      case HowManyBeneficiaries.Over1001 => HtmlFormat.escape(messages("employmentBeneficiary.numberOfBeneficiaries.over1001"))
    }
  }

}
