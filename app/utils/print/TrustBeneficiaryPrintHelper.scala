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

import com.google.inject.Inject
import controllers.charityortrust.trust.routes._
import models.{CheckMode, NormalMode, UserAnswers}
import pages.charityortrust.trust._
import play.api.i18n.Messages
import utils.countryOptions.CountryOptions
import viewmodels.AnswerSection

class TrustBeneficiaryPrintHelper @Inject()(answerRowConverter: AnswerRowConverter,
                                            countryOptions: CountryOptions
                                 ) {

  def apply(userAnswers: UserAnswers, provisional: Boolean, name: String)(implicit messages: Messages) = {

    val bound: answerRowConverter.Bound = answerRowConverter.bind(userAnswers, name, countryOptions)

      val add = Seq(
        bound.stringQuestion(NamePage, "trustBeneficiary.name", NameController.onPageLoad(CheckMode).url),
        bound.yesNoQuestion(DiscretionYesNoPage, "trustBeneficiary.discretionYesNo", DiscretionYesNoController.onPageLoad(CheckMode).url),
        bound.percentageQuestion(ShareOfIncomePage, "trustBeneficiary.shareOfIncome", ShareOfIncomeController.onPageLoad(CheckMode).url),
        bound.yesNoQuestion(AddressYesNoPage, "trustBeneficiary.addressYesNo", AddressYesNoController.onPageLoad(CheckMode).url),
        bound.yesNoQuestion(AddressUkYesNoPage, "trustBeneficiary.addressUkYesNo", AddressUkYesNoController.onPageLoad(CheckMode).url),
        bound.addressQuestion(UkAddressPage, "trustBeneficiary.ukAddress", UkAddressController.onPageLoad(CheckMode).url),
        bound.addressQuestion(NonUkAddressPage, "trustBeneficiary.nonUkAddress", NonUkAddressController.onPageLoad(CheckMode).url),
        bound.dateQuestion(StartDatePage, "trustBeneficiary.startDate", StartDateController.onPageLoad().url)
      ).flatten

    val amend = Seq(
      bound.stringQuestion(NamePage, "trustBeneficiary.name", NameController.onPageLoad(CheckMode).url),
      bound.yesNoQuestion(DiscretionYesNoPage, "trustBeneficiary.discretionYesNo", DiscretionYesNoController.onPageLoad(CheckMode).url),
      bound.percentageQuestion(ShareOfIncomePage, "trustBeneficiary.shareOfIncome", ShareOfIncomeController.onPageLoad(CheckMode).url),
      bound.stringQuestion(UtrPage, "trustBeneficiary.checkDetails.utr",""),
      bound.yesNoQuestion(AddressYesNoPage, "trustBeneficiary.addressYesNo", AddressYesNoController.onPageLoad(CheckMode).url),
      bound.yesNoQuestion(AddressUkYesNoPage, "trustBeneficiary.addressUkYesNo", AddressUkYesNoController.onPageLoad(CheckMode).url),
      bound.addressQuestion(UkAddressPage, "trustBeneficiary.ukAddress", UkAddressController.onPageLoad(CheckMode).url),
      bound.addressQuestion(NonUkAddressPage, "trustBeneficiary.nonUkAddress", NonUkAddressController.onPageLoad(CheckMode).url)
    ).flatten

    AnswerSection(
      None,
      if (provisional) add else amend
    )
  }
}
