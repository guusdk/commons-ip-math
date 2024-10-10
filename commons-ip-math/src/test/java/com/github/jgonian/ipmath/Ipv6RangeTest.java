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

import org.junit.Test;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static com.github.jgonian.ipmath.Ipv4.LAST_IPV4_ADDRESS;
import static com.github.jgonian.ipmath.Ipv6.FIRST_IPV6_ADDRESS;
import static com.github.jgonian.ipmath.Ipv6.LAST_IPV6_ADDRESS;
import static java.math.BigInteger.ONE;
import static org.junit.Assert.assertEquals;

public class Ipv6RangeTest extends AbstractRangeTest<Ipv6, Ipv6Range> {

    Ipv6 ip1 = Ipv6.of("::1");
    Ipv6 ip2 = Ipv6.of("::2");
    Ipv6 ip3 = Ipv6.of("::3");

    @Override
    protected Ipv6 from(String s) {
        return Ipv6.of(BigInteger.valueOf(Long.parseLong(s)));
    }

    @Override
    protected Ipv6 to(String s) {
        return Ipv6.of(BigInteger.valueOf(Long.parseLong(s)));
    }

    @Override
    protected Ipv6 item(String s) {
        return Ipv6.of(BigInteger.valueOf(Long.parseLong(s)));
    }

    @Override
    protected Ipv6Range getTestRange(Ipv6 start, Ipv6 end) {
        return new Ipv6Range(start, end);
    }

    @Override
    protected Ipv6Range getFullRange() {
        return new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS);
    }

    @Override
    public void shouldCalculateRangeSize() {
        assertEquals(new BigInteger("1"), FIRST_IPV6_ADDRESS.asRange().size());
        assertEquals(new BigInteger("340282366920938463463374607431768211456"), Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS).size());
    }

    @Test
    public void shouldParseDashNotation() {
        assertEquals(Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS), Ipv6Range.parse("::-ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
    }

    @Test
    public void shouldParseDashNotationWhenEmptyRange() {
        assertEquals(Ipv6.of("::1").asRange(), Ipv6Range.parse("::1-::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseDashNotationWhenIllegalRange() {
        Ipv6Range.parse("::10-::1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseDashNotationWhenSingleResource() {
        Ipv6Range.parse("::1");
    }

    @Test
    public void shouldParseCidrNotation() {
        assertEquals(Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS), Ipv6Range.parseCidr("::/0"));
    }

    @Test
    public void shouldParseCidrWhenEmptyRange() {
        assertEquals(Ipv6.parse("ffce:abcd::").asRange(), Ipv6Range.parseCidr("ffce:abcd::/128"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseCidrWhenIllegalPrefix() {
        Ipv6Range.parseCidr("ffce:abcd::/129");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseCidrWhenSingleResource() {
        Ipv6Range.parseCidr("ffce:abcd::");
    }

    @Test
    public void shouldParseWithPrefix() {
        assertEquals(Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS), Ipv6Range.from("::").andPrefixLength("0"));
    }

    @Test
    public void shouldParseWithPrefixWhenEmptyRange() {
        assertEquals(Ipv6.parse("ffce:abcd::").asRange(), Ipv6Range.from("ffce:abcd::").andPrefixLength("128"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseWithPrefixWhenIllegalPrefix() {
        Ipv6Range.from("ffce:abcd::").andPrefixLength("129");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseWithPrefixWhenPrefixIsNull() {
        Ipv6Range.from("ffce:abcd::").andPrefixLength(null);
    }

    @Test
    public void shouldParseDecimalNotation() {
        assertEquals(Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS), Ipv6Range.parseDecimalNotation(FIRST_IPV6_ADDRESS.value() + "-" + LAST_IPV6_ADDRESS.value()));
    }

    @Test
    public void shouldParseDecimalWhenEmptyRange() {
        assertEquals(Ipv6.parse("::1").asRange(), Ipv6Range.parseDecimalNotation("1-1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToParseDecimalNotationWhenSingleResource() {
        Ipv6Range.parseDecimalNotation("1");
    }

    @Test
    @Override
    public void testToString() {
        assertEquals("::1-::3", new Ipv6Range(ip1, ip3).toString());
        assertEquals("::/0", new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS).toString());
    }

    @Test
    public void testToStringInRangeNotation() {
        assertEquals("::-ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS).toStringInRangeNotation());
    }

    @Test
    public void testToStringInRangeNotationWithSpaces() {
        assertEquals(":: - ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS).toStringInRangeNotationWithSpaces());
    }

    @Test
    public void testToStringInCidrNotation() {
        assertEquals("::/0", new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS).toStringInCidrNotation());
    }

    @Test
    public void testToStringInDecimalNotation() {
        assertEquals("0-340282366920938463463374607431768211455", new Ipv6Range(FIRST_IPV6_ADDRESS, LAST_IPV6_ADDRESS).toStringInDecimalNotation());
    }

    @Test
    public void testSize() {
        assertEquals(ONE, ip1.asRange().size());
        assertEquals(Ipv6.MAXIMUM_VALUE.add(ONE), Ipv6Range.from(FIRST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS).size());
    }

    @Override
    public void testIterator() {
        List<Ipv6> result = new ArrayList<Ipv6>();
        for (Ipv6 ipv6 : new Ipv6Range(ip1, ip3)) {
            result.add(ipv6);
        }
        assertEquals(Arrays.asList(ip1, ip2, ip3), result);
    }

    @Override
    public void testIteratorEnd() {
        List<Ipv6> result = new ArrayList<Ipv6>();
        for (Ipv6 ipv6 : Ipv6Range.from(LAST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS)) {
            result.add(ipv6);
        }
        assertEquals(Collections.singletonList(LAST_IPV6_ADDRESS), result);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void testIteratorOutOfBounds() {
        Ipv6Range range = Ipv6Range.from(LAST_IPV6_ADDRESS).to(LAST_IPV6_ADDRESS);
        Iterator<Ipv6> iterator = range.iterator();
        assertEquals(LAST_IPV6_ADDRESS, iterator.next());
        iterator.next();
    }

    @Test
    public void testBuilderWithBigIntegers() {
        Ipv6Range range = Ipv6Range.from(BigInteger.valueOf(1l)).to(BigInteger.valueOf(3l));
        assertEquals(ip1, range.start());
        assertEquals(ip3, range.end());
    }

    @Test
    public void testBuilderWithByteArrays() {
        Ipv6Range range = Ipv6Range.from(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}).to(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3});
        assertEquals(ip1, range.start());
        assertEquals(ip3, range.end());
    }

    @Test
    public void testBuilderWithInetAddresses() throws UnknownHostException {
        Ipv6Range range = Ipv6Range.from((Inet6Address) InetAddress.getByAddress(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1})).to((Inet6Address) InetAddress.getByAddress(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3}));
        assertEquals(ip1, range.start());
        assertEquals(ip3, range.end());
    }

    @Test
    public void testBuilderWithStrings() {
        Ipv6Range range = Ipv6Range.from("::1").to("::3");
        assertEquals(ip1, range.start());
        assertEquals(ip3, range.end());
    }

    @Test
    public void testBuilderWithIpv6s() {
        Ipv6Range range = Ipv6Range.from(ip1).to(ip3);
        assertEquals(ip1, range.start());
        assertEquals(ip3, range.end());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidRange() {
        Ipv6Range.from(ip3).to(ip1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithNullStart() {
        Ipv6Range.from((Ipv6) null).to(ip3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithNullEnd() {
        Ipv6Range.from(ip1).to((Ipv6) null);
    }

    @Test
    public void testBuilderWithValidAddressAndPrefixLength() {
        Ipv6Range range = Ipv6Range.from("::2").andPrefixLength(127);
        assertEquals(Ipv6.of("::2"), range.start());
        assertEquals(Ipv6.of("::3"), range.end());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidAddressAndPrefixLength() {
        Ipv6Range.from("::3").andPrefixLength(127);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithTooSmallPrefixLength() {
        Ipv6Range.from("::2").andPrefixLength(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithTooBigPrefixLength() {
        Ipv6Range.from("::2").andPrefixLength(129);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullStart() {
        new Ipv6Range(null, ip3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEnd() {
        new Ipv6Range(ip1, null);
    }

    @Test
    public void shouldSplitToPrefixes() {
        validateSplitIntoPrefixes(new String[]{"::/128"}, "::0-::0");
        validateSplitIntoPrefixes(new String[]{"::/127"}, "::0-::1");
        validateSplitIntoPrefixes(new String[]{"::/126"}, "::0-::3");
        validateSplitIntoPrefixes(new String[]{"::/125"}, "::0-::7");
        validateSplitIntoPrefixes(new String[]{"::/127", "::2/128"}, "::0-::2");
        validateSplitIntoPrefixes(new String[]{"::/126", "::4/128"}, "::0-::4");
        validateSplitIntoPrefixes(new String[]{"::/126", "::4/127"}, "::0-::5");
        validateSplitIntoPrefixes(new String[]{"::/126", "::4/127", "::6/128"}, "::0-::6");
        validateSplitIntoPrefixes(new String[]{"::1/128"}, "::1-::1");
        validateSplitIntoPrefixes(new String[]{"::1/128", "::2/128"}, "::1-::2");
        validateSplitIntoPrefixes(new String[]{"::1/128", "::2/127"}, "::1-::3");
        validateSplitIntoPrefixes(new String[]{"::2/127"}, "::2-::3");
        validateSplitIntoPrefixes(new String[]{"::2/127", "::4/128"}, "::2-::4");
    }

    private void validateSplitIntoPrefixes(String[] expectedPrefixes, String rangeToSplit) {
        List<Ipv6Range> expected = new ArrayList<Ipv6Range>();
        for (String prefix : expectedPrefixes) {
            expected.add(Ipv6Range.parse(prefix));
        }
        assertEquals(expected, Ipv6Range.parse(rangeToSplit).splitToPrefixes());
    }

    @Test
    public void shouldSplitIntoPrefixesAllIpv6SpaceExceptFirstAddress() {
        Ipv6Range range = Ipv6Range.from("::1").to(Ipv6.LAST_IPV6_ADDRESS);
        assertEquals(128, range.splitToPrefixes().size());
    }
}
