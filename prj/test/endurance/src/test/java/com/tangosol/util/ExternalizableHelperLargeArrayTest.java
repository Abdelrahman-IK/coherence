/*
 * Copyright (c) 2000, 2021, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package com.tangosol.util;


import data.Person;
import org.junit.Test;

import com.tangosol.io.Utf8Writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;


/**
* Unit tests for ExternalizableHelper on large array deserialization.
*
* @author bbc  2021.02.02
*/
public class ExternalizableHelperLargeArrayTest extends ExternalizableHelper
    {
    // ----- unit tests -----------------------------------------------------

    /** Test byte array with length larger than threshold.
     */
    @Test
    public void testLargeByteArray() throws IOException
        {
        // test size equal to threshold
        byte[] ab = new byte[CHUNK_THRESHOLD];
        Arrays.fill(ab, (byte)1);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeByteArray(dos, ab);
        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        byte[]               abRead = readByteArray(dis);

        assertTrue(Arrays.equals(ab, abRead));

        outRaw.reset();
        // test size larger than threshold
        ab = new byte[CHUNK_THRESHOLD * 2 + 5];
        Arrays.fill(ab, (byte)2);
        writeByteArray(dos, ab);
        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        abRead = readByteArray(dis);

        assertTrue(Arrays.equals(ab, abRead));
        }

    /** Test Binary with size larger than threshold.
     */
    @Test
    public void testLargeBinary() throws IOException
        {
        // test size equal to threshold
        byte[] ab = new byte[CHUNK_THRESHOLD];
        Arrays.fill(ab, (byte)1);
        Binary bin = new Binary(ab);
        Map<Integer, Binary> map = new HashMap<>();
        map.put(1, bin);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeMap(dos, map);
        dos.flush();

        Map<Integer, Binary> mapRead = new HashMap<>();
        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        int c = readMap(dis, mapRead, null);
        assertTrue(c == 1);
        assertTrue(bin.equals(map.get(1)));

        outRaw.reset();
        // test size larger than threshold
        ab = new byte[CHUNK_THRESHOLD * 2 + 5];
        Arrays.fill(ab, (byte)2);
        writeMap(dos, map);
        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        c = readMap(dis, mapRead, null);

        assertTrue(c == 1);
        assertTrue(bin.equals(map.get(1)));
        }

    /** Test boolean array with length larger than threshold.
     */
    @Test
    public void testLargeBooleanArray() throws IOException
        {
        // test size equal to threshold
        boolean[] af = new boolean[CHUNK_THRESHOLD];
        Arrays.fill(af,  true);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeBooleanArray(dos, af);
        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        boolean[]            afRead = readBooleanArray(dis);

        assertTrue(Arrays.equals(af, afRead));

        outRaw.reset();
        // test size larger than threshold
        af = new boolean[CHUNK_THRESHOLD * 2 + 5];
        Arrays.fill(af,  true);
        writeBooleanArray(dos, af);
        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        afRead = readBooleanArray(dis);
        assertTrue(Arrays.equals(af, afRead));
        }

    /** Test String array with length larger than threshold.
     */
    @Test
    public void testLargeStringArray() throws IOException
        {
        // test size equal to threshold
        String[] as = new String[CHUNK_THRESHOLD >> 2];
        Arrays.fill(as,  "equal");

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeStringArray(dos, as);
        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        String[]            asRead  = readStringArray(dis);

        assertTrue(Arrays.equals(as, asRead));

        outRaw.reset();
        // test size larger than threshold
        as = new String[CHUNK_THRESHOLD >> 2 * 2 + 5];
        Arrays.fill(as,  "large");
        writeStringArray(dos, as);
        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        asRead = readStringArray(dis);
        assertTrue(Arrays.equals(as, asRead));
        }

    /** Test double array with length larger than threshold.
     */
    @Test
    public void testLargeDoubleArray() throws IOException
        {
        // test size equal to threshold
        double[] adfl = new double[CHUNK_THRESHOLD >> 3];
        Arrays.fill(adfl,  12.68);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeDoubleArray(dos, adfl);
        dos.flush();

        ByteArrayInputStream inRaw    = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis      = new DataInputStream(inRaw);
        double[]             adflRead = readDoubleArray(dis);

        assertTrue(Arrays.equals(adfl, adflRead));

        outRaw.reset();
        // test size larger than threshold
        adfl = new double[CHUNK_THRESHOLD >> 3 * 2 + 5];
        Arrays.fill(adfl,  12.68);
        writeDoubleArray(dos, adfl);
        dos.flush();

        inRaw    = new ByteArrayInputStream(outRaw.toByteArray());
        dis      = new DataInputStream(inRaw);
        adflRead = readDoubleArray(dis);
        assertTrue(Arrays.equals(adfl, adflRead));
        }

    /** Test float array with length larger than threshold.
     */
    @Test
    public void testLargeFloatArray() throws IOException
        {
        // test size equal to threshold
        float[] afl = new float[CHUNK_THRESHOLD >> 2];
        Arrays.fill(afl,  12);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeFloatArray(dos, afl);
        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        float[]             aflRead = readFloatArray(dis);

        assertTrue(Arrays.equals(afl, aflRead));

        outRaw.reset();
        // test size larger than threshold
        afl = new float[CHUNK_THRESHOLD >> 2 * 2 + 5];
        Arrays.fill(afl,  12);
        writeFloatArray(dos, afl);
        dos.flush();

        inRaw    = new ByteArrayInputStream(outRaw.toByteArray());
        dis      = new DataInputStream(inRaw);
        aflRead = readFloatArray(dis);
        assertTrue(Arrays.equals(afl, aflRead));
        }

    /** Test big String with length larger than threshold.
     */
    @Test
    public void testLargeString() throws IOException
        {
        // test size equal to threshold
        char[] chars = new char[CHUNK_THRESHOLD >> 2];
        Arrays.fill(chars,  'a');
        String s = new String(chars);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        writeUTF(dos, s);
        dos.flush();

        ByteArrayInputStream inRaw = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis   = new DataInputStream(inRaw);
        String               sRead = readUTF(dis);

        assertTrue(s.equals(sRead));

        outRaw.reset();
        // test size larger than threshold
        chars = new char[CHUNK_THRESHOLD >> 2 * 2 + 5];
        Arrays.fill(chars,  'a');
        s = new String(chars);
        writeUTF(dos, s);
        dos.flush();

        inRaw    = new ByteArrayInputStream(outRaw.toByteArray());
        dis      = new DataInputStream(inRaw);
        sRead = readUTF(dis);
        assertTrue(s.equals(sRead));
        }

    /** Test char array with length larger than threshold.
     */
    @Test
    public void testLargeCharArray() throws IOException
        {
        // test size equal to threshold
        char[] ach = new char[CHUNK_THRESHOLD];
        Arrays.fill(ach, 'a');

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);

        CharArrayWriter cw = new CharArrayWriter(1024);
        cw.write(ach);

        dos.writeInt(ach.length);

        cw.writeTo(new Utf8Writer(dos));

        dos.flush();
        cw.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);

        char[] achRead = readCharArray(dis);

        assertTrue(Arrays.equals(ach, achRead));

        outRaw.reset();
        cw.reset();

        // test size larger than threshold
        ach = new char[CHUNK_THRESHOLD * 2 + 5];
        Arrays.fill(ach, 'b');
        cw.write(ach);
        dos.writeInt(ach.length);
        cw.writeTo(new Utf8Writer(dos));

        dos.flush();
        cw.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);

        achRead = readCharArray(dis);

        assertTrue(Arrays.equals(ach, achRead));
        }

    /** Test long array with length larger than threshold.
     */
    @Test
    public void testLargeLongArray() throws IOException
        {
        // test size equal to threshold
        long[] al = new long[CHUNK_THRESHOLD >> 3];
        Arrays.fill(al,  6666L);

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        dos.writeInt(al.length);

        for (int i = 0; i < al.length; i++)
            {
            dos.writeLong(al[i]);
            }

        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        long[]               alRead = readLongArray(dis);

        assertTrue(Arrays.equals(al, alRead));

        outRaw.reset();
        // test size larger than threshold
        al = new long[CHUNK_THRESHOLD >> 3 * 2 + 5];
        Arrays.fill(al,  8888L);

        dos.writeInt(al.length);
        for (int i = 0; i < al.length; i++)
            {
            dos.writeLong(al[i]);
            }
        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        alRead = readLongArray(dis);
        assertTrue(Arrays.equals(al, alRead));
        }

    /** Test long array with length larger than threshold.
     */
    @Test
    public void testLargeObjectArray() throws IOException
        {
        // test size equal to threshold
        Object[] ao = new Object[CHUNK_THRESHOLD >> 4];
        Arrays.fill(ao,  new TestObject("equal", 1));

        ByteArrayOutputStream outRaw = new ByteArrayOutputStream();
        DataOutputStream      dos    = new DataOutputStream(outRaw);
        dos.writeInt(ao.length);

        for (int i = 0; i < ao.length; i++)
            {
            ExternalizableHelper.writeObject(dos, ao[i]);
            }

        dos.flush();

        ByteArrayInputStream inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        DataInputStream      dis    = new DataInputStream(inRaw);
        Object[]             aoRead = readObjectArray(dis);

        assertTrue(Arrays.equals(ao, aoRead));

        outRaw.reset();
        // test size larger than threshold
        ao = new Object[CHUNK_THRESHOLD >> 4 * 2 + 5];
        Arrays.fill(ao,  new TestObject("larger", 2));

        dos.writeInt(ao.length);
        for (int i = 0; i < ao.length; i++)
            {
            ExternalizableHelper.writeObject(dos, ao[i]);
            }

        dos.flush();

        inRaw  = new ByteArrayInputStream(outRaw.toByteArray());
        dis    = new DataInputStream(inRaw);
        aoRead = readObjectArray(dis);
        assertTrue(Arrays.equals(ao, aoRead));
        }

    // ---------------------- Test Class ------------------------------------

    public static class TestObject implements Serializable
        {
        public TestObject(String sName, int nId)
            {
            m_sName = sName;
            m_nId   = nId;
            }

        @Override
        public int hashCode()
            {
            return  Objects.hash(super.hashCode(), m_sName, m_nId);
            }

        @Override
        public boolean equals(Object o)
            {
            if (this == o)
                {
                return true;
                }
            if (!(o instanceof TestObject))
                {
                return false;
                }
            TestObject test = (TestObject) o;
            return m_nId == test.m_nId &&
                    Objects.equals(m_sName, test.m_sName);
            }

        @Override
        public String toString()
            {
            return super.toString();
            }
        // -----------------  Data members ----------------------------------
        private String m_sName;
        private int    m_nId;
        }
    }
