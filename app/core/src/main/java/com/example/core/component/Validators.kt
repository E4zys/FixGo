package com.example.core.component

// ─── ValidationResult ─────────────────────────────────────────────────────────

/**
 * Hasil satu validator: isValid + pesan error opsional.
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun valid() = ValidationResult(isValid = true)
        fun error(message: String) = ValidationResult(isValid = false, errorMessage = message)
    }
}

// ─── FieldValidator ───────────────────────────────────────────────────────────

/**
 * Tipe validator: menerima input String, mengembalikan [ValidationResult].
 *
 * Dipakai di dua tempat:
 * 1. Langsung di field (validasi real-time saat mengetik)
 * 2. Di ViewModel via [FormField] + [Validators.validateAll] (validasi saat submit)
 */
typealias FieldValidator = (String) -> ValidationResult

// ─── Built-in Validators ──────────────────────────────────────────────────────

object Validators {

    /** Field tidak boleh kosong. */
    val notBlank: FieldValidator = { input ->
        if (input.isBlank()) ValidationResult.error("Tidak boleh kosong")
        else ValidationResult.valid()
    }

    /** Validasi format email. */
    val validEmail: FieldValidator = { input ->
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        when {
            input.isBlank() -> ValidationResult.error("Email tidak boleh kosong")
            !input.matches(emailRegex) -> ValidationResult.error("Format email tidak valid")
            else -> ValidationResult.valid()
        }
    }

    /** Validasi nomor HP Indonesia (+62 / 62 / 08). */
    val validPhone: FieldValidator = { input ->
        val clean = input.replace(" ", "").replace("-", "")
        val phoneRegex = "^\\+?[0-9]{9,15}$".toRegex()
        when {
            clean.isBlank() -> ValidationResult.error("Nomor HP tidak boleh kosong")
            !clean.matches(phoneRegex) -> ValidationResult.error("Format nomor HP tidak valid (9-15 digit)")
            !clean.startsWith("08") && !clean.startsWith("62") && !clean.startsWith("+62") ->
                ValidationResult.error("Nomor HP harus diawali 08 atau +62")
            else -> ValidationResult.valid()
        }
    }

    /** Panjang minimum. */
    fun minLength(min: Int): FieldValidator = { input ->
        if (input.length < min) ValidationResult.error("Minimal $min karakter")
        else ValidationResult.valid()
    }

    /** Panjang maksimum. */
    fun maxLength(max: Int): FieldValidator = { input ->
        if (input.length > max) ValidationResult.error("Maksimal $max karakter")
        else ValidationResult.valid()
    }

    /**
     * Nilai harus sama dengan [other] (konfirmasi password).
     */
    fun matchWith(other: String, fieldLabel: String = "Password"): FieldValidator = { input ->
        if (input != other) ValidationResult.error("$fieldLabel tidak cocok")
        else ValidationResult.valid()
    }

    /** Hanya boleh berisi digit. */
    val onlyDigits: FieldValidator = { input ->
        if (input.isNotEmpty() && !input.all { it.isDigit() }) ValidationResult.error("Hanya boleh berisi angka")
        else ValidationResult.valid()
    }

    // ─── Runner ───────────────────────────────────────────────────────────────

    /**
     * Jalankan daftar [validators] berurutan.
     * @return [ValidationResult] pertama yang gagal, atau valid jika semua lolos.
     */
    fun validate(input: String, vararg validators: FieldValidator): ValidationResult {
        for (validator in validators) {
            val result = validator(input)
            if (!result.isValid) return result
        }
        return ValidationResult.valid()
    }
}

// ─── Model-based form validation (untuk submit di ViewModel) ──────────────────

/**
 * Representasi satu field form beserta daftar validator-nya.
 *
 * ```kotlin
 * FormField(value = state.user.email, validators = listOf(Validators.validEmail))
 * ```
 */
data class FormField(
    val value: String,
    val validators: List<FieldValidator> = emptyList()
)

/**
 * Jalankan validasi untuk semua field sekaligus.
 * Key bisa berupa tipe apapun (String, enum, dll.).
 *
 * @return Map key → errorMessage (null = valid).
 */
fun <K> Validators.validateAll(fields: Map<K, FormField>): Map<K, String?> {
    return fields.mapValues { (_, field) ->
        val result = validate(field.value, *field.validators.toTypedArray())
        result.errorMessage
    }
}