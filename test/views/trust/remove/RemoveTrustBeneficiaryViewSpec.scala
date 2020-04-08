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

package views.trust.remove

import controllers.trust.remove.routes
import forms.YesNoFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.trust.remove.RemoveIndexView

class RemoveTrustBeneficiaryViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "removeTrustBeneficiary"
  val form = (new YesNoFormProvider).withPrefix(messageKeyPrefix)
  val name = "Name"
  val index = 0

  "RemoveTrustBeneficiary view" must {

    val view = viewFor[RemoveIndexView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, index, name)(fakeRequest, messages)

    behave like dynamicTitlePage(applyView(form), messageKeyPrefix, name)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, Some(name), routes.RemoveTrustBeneficiaryController.onSubmit(index).url)
  }
}
