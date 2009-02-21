package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

// TODO Document.
public final class ByteBufferInputStream
extends InputStream
{
    // TODO Document.
    private final ByteBuffer bytes;

    // TODO Document.
    public ByteBufferInputStream(ByteBuffer bytes)
    {
        this.bytes = bytes;
    }

    // TODO Document.
    public int read() throws IOException
    {
        if (!bytes.hasRemaining())
        {
            return -1;
        }
        return bytes.get() & 0xff;
    }

    // TODO Document.
    public int read(byte[] b, int off, int len) throws IOException
    {
        len = Math.min(len, bytes.remaining());
        if (len == 0)
        {
            return -1;
        }
        bytes.get(b, off, len);
        return len;
    }
}