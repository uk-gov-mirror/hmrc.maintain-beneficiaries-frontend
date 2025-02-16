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

package models.beneficiaries

import models.Address
import play.api.libs.json.{JsPath, JsSuccess, Reads}
import java.time.LocalDate

trait Beneficiary {
  val entityStart: LocalDate
}

trait OrgBeneficiary extends Beneficiary {
  val name: String
  val utr: Option[String]
  val income: Option[String]
  val countryOfResidence: Option[String]
  val address: Option[Address]
}

trait BeneficiaryReads {
  def readNullableAtSubPath[T: Reads](subPath: JsPath): Reads[Option[T]] = Reads(
    _.transform(subPath.json.pick)
      .flatMap(_.validate[T])
      .map(Some(_))
      .recoverWith(_ => JsSuccess(None))
  )
}
