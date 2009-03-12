package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * An input stream wrapper around an underlying byte buffer.
 *
 * @author Alan Gutierrez
 */
public final class ByteBufferInputStream
extends InputStream
{
    /** The byte buffer. */
    private final ByteBuffer bytes;

    /**
     * Create a byte buffer input stream that reads from the given byte
     * buffer.
     * 
     * @param bytes The byte buffer.
     */
    public ByteBufferInputStream(ByteBuffer bytes)
    {
        this.bytes = bytes;
    }

    /**
     * Read the next byte of data from the underlying byte buffer. The value
     * byte is returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned.
     * 
     * @return The next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     */
    public int read() throws IOException
    {
        if (!bytes.hasRemaining())
        {
            return -1;
        }
        return bytes.get() & 0xff;
    }

    /**
     * Reads up to the given length in bytes of data from the input stream into
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
    public int read(byte[] buffer, int offset, int length) throws IOException
    {
        length = Math.min(length, bytes.remaining());
        if (length == 0)
        {
            return -1;
        }
        bytes.get(buffer, offset, length);
        return length;
    }
}