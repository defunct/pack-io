package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

public final class PackOutputStream
extends OutputStream
{
    private long firstAddress;
    
    private long lastAddress;
    
    private ByteBuffer previous;

    private ByteBuffer next;
    
    private final Mutator mutator;

    public PackOutputStream(Mutator mutator)
    {
        this.mutator = mutator;
        this.next = ByteBuffer.allocate(mutator.getPack().getPageSize());
    }
    
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
    
    private void reset()
    {
        firstAddress = 0L;
        lastAddress = 0L;
        previous = null;
        next.clear();
    }

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