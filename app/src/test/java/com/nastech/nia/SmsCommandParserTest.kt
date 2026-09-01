package com.nastech.nia

import com.nastech.nia.core.security.SmsCommandParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SmsCommandParserTest {

    @Test
    fun locateCommand_parsed() {
        assertEquals(SmsCommandParser.Command.Locate, SmsCommandParser.parse("cia locate"))
        assertEquals(SmsCommandParser.Command.Locate, SmsCommandParser.parse("CIA LOCATE"))
    }

    @Test
    fun lockAndWipe_parsed() {
        assertEquals(SmsCommandParser.Command.Lock, SmsCommandParser.parse("cyberguard lock"))
        assertEquals(SmsCommandParser.Command.Wipe, SmsCommandParser.parse("niasec wipe"))
    }

    @Test
    fun unknownCommand_unrecognized() {
        assertEquals(SmsCommandParser.Command.Unrecognized, SmsCommandParser.parse("hello"))
        assertEquals(SmsCommandParser.Command.Unrecognized, SmsCommandParser.parse("cyberguard"))
    }

    @Test
    fun validPrefixUnknownAction_isCustom() {
        assertEquals(SmsCommandParser.Command.Custom("cia"), SmsCommandParser.parse("cia nope"))
    }

    @Test
    fun isCommandForUs_detectsPrefix() {
        assertTrue(SmsCommandParser.isCommandForUs("cia locate"))
        assertTrue(SmsCommandParser.isCommandForUs("niasec alarm"))
        assertEquals(false, SmsCommandParser.isCommandForUs("just a normal msg"))
    }
}