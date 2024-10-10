/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2017, Yannis Gonianakis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.jgonian.ipmath;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Ipv4Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Ipv4.class).suppress(Warning.NULL_FIELDS).withRedefinedSuperclass().verify();
    }

    @Test
    public void testBuilderMethods() throws UnknownHostException {
        Ipv4 sample = new Ipv4(1l);
        assertEquals(sample, Ipv4.of(BigInteger.ONE));
        assertEquals(sample, Ipv4.of(1l));
        assertEquals(sample, Ipv4.of("0.0.0.1"));
        assertEquals(sample, Ipv4.of(new byte[] {0, 0, 0, 1}));
        assertEquals(sample, Ipv4.of((Inet4Address) InetAddress.getByAddress(new byte[] {0, 0, 0, 1})));
        assertEquals(InetAddress.getByAddress(new byte[] {0, 0, 0, 1}), InetAddress.getByName("0.0.0.1"));
    }

    @Test
    public void testBuilderWithNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("from cannot be null");
        Ipv4.of((BigInteger) null);
    }

    @Test
    public void testBuilderWithNullInetAddress() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("value is required");
        Ipv4.of((Inet4Address) null);
    }

    @Test
    public void testBuilderNotEnoughBytes() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("exactly 4 octets are required");
        Ipv4.of(new byte[3]);
    }

    @Test
    public void testBuilderToManyBytes() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("exactly 4 octets are required");
        Ipv4.of(new byte[5]);
    }

    @Test
    public void testUpperBound() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value of IPv4 has to be less than or equal to 4294967295");
        new Ipv4(Ipv4.MAXIMUM_VALUE + 1);
    }

    @Test
    public void testLowerBound() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value of IPv4 has to be greater than or equal to 0");
        new Ipv4(Ipv4.MINIMUM_VALUE - 1);
    }

    @Test
    public void testHasNext() {
        assertTrue(Ipv4.FIRST_IPV4_ADDRESS.hasNext());
        assertTrue(Ipv4.of("192.168.0.1").hasNext());
        assertFalse(Ipv4.LAST_IPV4_ADDRESS.hasNext());
    }

    @Test
    public void testHasPrevious() {
        assertFalse(Ipv4.FIRST_IPV4_ADDRESS.hasPrevious());
        assertTrue(Ipv4.of("192.168.0.1").hasPrevious());
        assertTrue(Ipv4.LAST_IPV4_ADDRESS.hasPrevious());
    }

    @Test
    public void shouldParseDottedDecimalNotation() {
        assertEquals("127.0.8.23", Ipv4.parse("127.0.8.23").toString());
        assertEquals("193.168.15.255", Ipv4.parse("193.168.15.255").toString());
    }

    @Test
    public void shouldParseIPv4AddressWithLeadingAndTrailingSpaces() {
        assertEquals("127.0.8.12", Ipv4.parse("  127.0.8.12  ").toString());
    }

    @Test
    public void shouldFailOnLessOctets() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '10.1.1'");
        Ipv4.parse("10.1.1");
    }

    @Test
    public void shouldFailOnMoreOctets() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '10.1.1.1.1'");
        Ipv4.parse("10.1.1.1.1");
    }

    @Test
    public void shouldFailWhenEndingWithNonDigit() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '10.1.1.1.'");
        Ipv4.parse("10.1.1.1.");
    }

    @Test
    public void shouldFailWhenStartingWithNonDigit() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '.10.1.1.1'");
        Ipv4.parse(".10.1.1.1");
    }

    @Test
    public void shouldFailWhenOctetContainsNonDigit() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '10.a.1.0'");
        Ipv4.parse("10.a.1.0");
    }

    @Test
    public void shouldFailOnOutOfBoundsByte() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '256.0.0.0'");
        Ipv4.parse("256.0.0.0");
    }

    @Test
    public void shouldFailOnOutOfBoundsByte_NegativeByte() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '13.-40.0.0'");
        Ipv4.parse("13.-40.0.0");
    }

    @Test
    public void shouldFailOnLeadingZeros() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid IPv4 address: '192.168.08.1'");
        Ipv4.parse("192.168.08.1");
    }

    @Test
    public void shouldHave32BitsSize() {
        assertEquals(Ipv4.NUMBER_OF_BITS, Ipv4.FIRST_IPV4_ADDRESS.bitSize());
    }

    @Test
    public void shouldCalculateBoundsGivenAnAddressAndAPrefix() {
        Ipv4 address = Ipv4.parse("192.168.0.100");
        assertEquals(Ipv4.parse("0.0.0.0"), address.lowerBoundForPrefix(0));
        assertEquals(Ipv4.parse("255.255.255.255"), address.upperBoundForPrefix(0));

        assertEquals(Ipv4.parse("192.168.0.0"), address.lowerBoundForPrefix(16));
        assertEquals(Ipv4.parse("192.168.255.255"), address.upperBoundForPrefix(16));

        assertEquals(Ipv4.parse("192.168.0.100"), address.lowerBoundForPrefix(32));
        assertEquals(Ipv4.parse("192.168.0.100"), address.upperBoundForPrefix(32));
    }

    @Test
    public void shouldFailToCalculateLowerBoundWhenPrefixIsOutOfRange() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value [33] out of range: [0..32]");
        Ipv4.parse("192.168.0.100").lowerBoundForPrefix(33);
    }

    @Test
    public void shouldFailToCalculateUpperBoundWhenPrefixIsOutOfRange() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value [33] out of range: [0..32]");
        Ipv4.parse("192.168.0.100").upperBoundForPrefix(33);
    }

    @Test
    public void shouldCalculateCommonPrefixLength() {
        Ipv4 ipv4 = Ipv4.of("192.168.0.0");
        assertEquals(0, ipv4.getCommonPrefixLength(Ipv4.of("0.0.0.0")));
        assertEquals(16, ipv4.getCommonPrefixLength(Ipv4.of("192.168.255.255")));
        assertEquals(32, ipv4.getCommonPrefixLength(Ipv4.of("192.168.0.0")));
    }
}
