/* Copyright Alan Gutierrez 2006 */
package com.goodworkalan.pack.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * An output stream wrapper around an underlying byte buffer.
 *
 * @author Alan Gutierrez
 */
public class ByteBufferOutputStream
extends OutputStream
{
    /** The byte buffer. */
    private final ByteBuffer bytes;
    
    /**
     * Create a byte buffer output stream that reads from the given byte
     * buffer.
     * 
     * @param bytes The byte buffer.
     */
    public ByteBufferOutputStream(ByteBuffer bytes)
    {
        this.bytes = bytes;
    }

    /**
     * Writes the given byte to to the underlying byte buffer. This method will
     * call the put method of the underlying byte buffer. The position of the
     * byte buffer will advance by one. If the position of the byte buffer is
     * equal to the limit, and <code>EOFException</code> is thrown.
     * 
     * @param b
     *            The byte to write.
     * @throws EOFException
     *             If the byte buffer position is equal to the byte buffer limit
     *             so that the write causes a buffer overflow.
     */
    @Override
    public void write(int b) throws IOException
    {
        try
        {
            bytes.put((byte) b);
        }
        catch (BufferOverflowException e)
        {
            throw new EOFException("Buffer overflow."); 
        }
    }

    /**
     * Writes up to the given length in bytes of data from the input stream into
     * the given array of bytes at the given offset.
     * 
     * @param buffer
     *            An array of bytes.
     * @param offset
     *            The offset where read bytes are written.
     * @param length
     *            the number of bytes to read.
     * @return The number of bytes actually read.
     */
    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        if (buffer == null)
        {
            throw new NullPointerException();
        }
        try
        {
            bytes.put(buffer, offset, length);
        }
        catch (BufferOverflowException e)
        {
            throw new EOFException("Buffer overflow."); 
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */