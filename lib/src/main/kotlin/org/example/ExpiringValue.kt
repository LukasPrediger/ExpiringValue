package org.example

import java.time.Clock
import java.time.Instant
import kotlin.jvm.Throws

class ExpiringValue<T : Any>(
    private val clock: Clock = Clock.systemDefaultZone(),
    private val valueSupplier: () -> Pair<T, Instant>
) {
    private lateinit var value: T

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var validUntil: Instant
        private set

    init {
        getNewValue(clock.instant())
    }

    @Throws(ValueExpiredException::class)
    fun get(): T {
        val now = clock.instant()

        return if (validUntil.isAfter(now)) value else getNewValue(now)
    }

    @Throws(ValueExpiredException::class)
    private fun getNewValue(now: Instant): T {
        val (newValue, newValidUntil) = valueSupplier()
        if (newValidUntil.isBefore(now)) {
            throw ValueExpiredException(newValidUntil, now)
        }
        value = newValue
        validUntil = newValidUntil
        return value
    }

}

class ValueExpiredException(newValueValidUntil: Instant, minValidUntil: Instant) : Exception(
    "New value received by the supplier is already expired. Expiry date: $newValueValidUntil is before: $minValidUntil"
)