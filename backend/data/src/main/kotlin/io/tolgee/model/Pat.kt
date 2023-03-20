package io.tolgee.model

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.Transient
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["tokenHash"], name = "pat_token_hash_unique")])
class Pat(
  @field:NotEmpty
  @field:NotNull
  var tokenHash: String = "",

  @field:NotEmpty
  @field:NotNull
  var description: String = "",

  @Temporal(value = TemporalType.TIMESTAMP)
  var expiresAt: Date? = null,

  @Temporal(value = TemporalType.TIMESTAMP)
  var lastUsedAt: Date? = null,

  @Transient
  var token: String? = null
) : StandardAuditModel() {

  @ManyToOne
  @NotNull
  lateinit var userAccount: UserAccount
}
