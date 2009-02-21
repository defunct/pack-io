package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

// TODO Document.
public final class PackOutputStream
extends OutputStream
{
    // TODO Document.
    private long firstAddress;
    
    // TODO Document.
    private long lastAddress;
    
    // TODO Document.
    private ByteBuffer previous;

    // TODO Document.
    private ByteBuffer next;
    
    // TODO Document.
    private final Mutator mutator;

    // TODO Document.
    public PackOutputStream(Mutator mutator)
    {
        this.mutator = mutator;
        this.next = ByteBuffer.allocate(mutator.getPack().getPageSize());
    }
    
    // TODO Document.
    @Override
    public void write(int b) throws IOException
    {
        if (next.remaining() == 0)
        {
            long newAddress = mutator.allocate(next.capacity());
            mutator.setContinued(newAddress);
            next.flip();
            mutator.write(newAddress, next);
            next.clear();
            int overflow = (Long.SIZE / Byte.SIZE);
            if (firstAddress == 0)
            {
                firstAddress = newAddress;
            }
            else
            {
                previous.putLong(previous.capacity() - overflow, newAddress);
                mutator.write(lastAddress, previous);
            }
            lastAddress = newAddress;
            previous = next;
            next = ByteBuffer.allocate(previous.capacity());
            for (int i = 0; i < overflow; i++)
            {
                next.put(previous.get(previous.capacity() - overflow + i));
            }
        }
        next.put((byte) b);
    }
    
    // TODO Document.
    private void reset()
    {
        firstAddress = 0L;
        lastAddress = 0L;
        previous = null;
        next.clear();
    }

    // TODO Document.
    public long allocate()
    {
        long newAddress = mutator.allocate(next.position());
        next.flip();
        mutator.write(newAddress, next);
        if (firstAddress == 0)
        {
            reset();
            return newAddress;
        }

        long address = firstAddress;
        previous.putLong(previous.capacity() - (Long.SIZE / Byte.SIZE), newAddress);
        mutator.write(lastAddress, previous);
        reset();
        return address;
    }
}