package net.ripe.commons.ip.range;

import static junit.framework.Assert.*;
import static net.ripe.commons.ip.resource.Asn.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.ripe.commons.ip.resource.Asn;
import org.junit.Test;

public class AsnRangeTest extends AbstractRangeTest<Asn, AsnRange> {

    private final Asn as1 = Asn.of(1L);
    private final Asn as2 = Asn.of(2L);
    private final Asn as3 = Asn.of(3L);

    @Override
    protected Asn from(String s) {
        return Asn.of(s);
    }

    @Override
    protected Asn to(String s) {
        return Asn.of(s);
    }

    @Override
    protected Asn item(String s) {
        return Asn.of(s);
    }

    @Override
    protected AsnRange getTestRange(Asn start, Asn end) {
        return new AsnRange(start, end);
    }

    @Override
    public void testToString() {
        assertEquals("AS1", AsnRange.from(as1).to(as1).toString());
        assertEquals("AS1-AS3", AsnRange.from(as1).to(as3).toString());
    }

    @Test
    public void testSize() {
        assertEquals(new Long(1), as1.asRange().size());
        assertEquals(new Long(ASN_16_BIT_MAX_VALUE + 1), AsnRange.from(FIRST_ASN).to(Asn.LAST_16_BIT_ASN).size());
        assertEquals(new Long(ASN_32_BIT_MAX_VALUE + 1), AsnRange.from(FIRST_ASN).to(Asn.LAST_32_BIT_ASN).size());
    }

    @Override
    public void testIterator() {
        List<Asn> result = new ArrayList<Asn>();
        for (Asn asn : new AsnRange(as1, as3)) {
            result.add(asn);
        }
        assertEquals(Arrays.asList(as1, as2, as3), result);
    }

    @Test
    public void testBuilderWithLongs() {
        AsnRange range = AsnRange.from(1l).to(3l);
        assertEquals(as1, range.start());
        assertEquals(as3, range.end());
    }

    @Test
    public void testBuilder() {
        AsnRange range = AsnRange.from(as1).to(as3);
        assertEquals(as1, range.start());
        assertEquals(as3, range.end());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidRange() {
        AsnRange.from(as3).to(as1);
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullStart() {
        AsnRange.from((Asn)null).to(as3);
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderWithNullEnd() {
        AsnRange.from(as1).to((Asn)null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullStart() {
        new AsnRange(null, as3);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEnd() {
        new AsnRange(as1, null);
    }
}
