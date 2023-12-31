package org.nico.quoted.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StringUtilTest {

    @Test
    void isCorrectEmail() {
        assert StringUtil.isCorrectEmail("peter@lustig.de");
        assert StringUtil.isCorrectEmail("peter.lustig@lustig.com");
        assert StringUtil.isCorrectEmail("Peter-Lustig@LUSTIG.ORG");
        assertFalse(StringUtil.isCorrectEmail("peter@lustig"));
        assertFalse(StringUtil.isCorrectEmail("peter@lustig."));
        assertFalse(StringUtil.isCorrectEmail("peter@lustig.d"));
        assertFalse(StringUtil.isCorrectEmail("pl.de"));
        assertFalse(StringUtil.isCorrectEmail("peter"));
    }

    @Test
    void trim() {
        assertEquals(StringUtil.trim("abc"), "abc");
        assertEquals(StringUtil.trim("  abc"), "abc");
        assertEquals(StringUtil.trim("abc  "), "abc");
        assertEquals(StringUtil.trim("  abc  "), "abc");
        assertEquals(StringUtil.trim("  \"  \n  abc  \"  "), "abc");
        assertEquals(StringUtil.trim("  \"  %22abc  \n  \"  \n  def%22  \"  "), "abc  \n  \"  \n  def");
    }
}