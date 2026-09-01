package com.nastech.nia.core.security

/** Parses Anti-Theft SMS remote commands. Pure logic, unit-testable. */
object SmsCommandParser {

    sealed class Command {
        data object Unrecognized : Command()
        data object Locate : Command()
        data object Lock : Command()
        data object Wipe : Command()
        data object Alarm : Command()
        data class Custom(val tag: String) : Command()
    }

    // Commands take forms: "cia locate", "cia lock", "cia wipe", "cia alarm"
    private val PREFIXES = listOf("cia", "cyberguard", "niasec")

    fun parse(rawBody: String?): Command {
        val body = rawBody?.trim()?.lowercase() ?: return Command.Unrecognized
        val parts = body.split(Regex("\\s+"))
        val tag = parts.firstOrNull()?.takeIf { it in PREFIXES } ?: return Command.Unrecognized
        val action = parts.getOrNull(1) ?: return Command.Unrecognized
        return when (action) {
            "locate" -> Command.Locate
            "lock" -> Command.Lock
            "wipe" -> Command.Wipe
            "alarm" -> Command.Alarm
            else -> Command.Custom(tag)
        }
    }

    fun isCommandForUs(rawBody: String?): Boolean =
        rawBody?.trim()?.lowercase()?.split(Regex("\\s+"))?.firstOrNull() in PREFIXES
}