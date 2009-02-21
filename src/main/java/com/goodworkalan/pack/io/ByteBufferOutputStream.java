/* Copyright Alan Gutierrez 2006 */
package com.goodworkalan.pack.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

// TODO Document.
public class ByteBufferOutputStream
extends OutputStream
{
    // TODO Document.
    private final ByteBuffer bytes;
    
    // TODO Document.
    public ByteBufferOutputStream(ByteBuffer bytes)
    {
        this.bytes = bytes;
    }

    // TODO Document.
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
    
    // TODO Document.
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        try
        {
            bytes.put(b, off, len);
        }
        catch (BufferOverflowException e)
        {
            throw new EOFException("Buffer overflow."); 
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */