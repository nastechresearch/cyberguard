package com.nastech.nia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import com.nastech.nia.CyberGuardApp
import com.nastech.nia.data.repository.AntiTheftRepository
import kotlinx.coroutines.runBlocking

/**
 * Listens for incoming SMS and executes registered anti-theft commands
 * ("cia locate", "cia lock", "cia wipe", "cia alarm") when SMS commands are enabled.
 */
class SmsCommandReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (message in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val body = message?.displayMessageBody ?: continue
                runBlocking {
                    AntiTheftRepository(CyberGuardApp.context()).handleIncomingSms(body)
                }
            }
        }
    }
}