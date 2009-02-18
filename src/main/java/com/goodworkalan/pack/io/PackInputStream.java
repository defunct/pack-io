package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

public class PackInputStream extends InputStream
{
    private final Mutator mutator;
    
    private ByteBuffer next;
    
    private boolean continued;
    
    public PackInputStream(Mutator mutator, long address)
    {
        this.mutator = mutator;
        this.next = mutator.read(address);
        this.continued = mutator.isContinued(address);
        this.setLimit();
    }
    
    private void setLimit()
    {
        if (continued)
        {
            next.limit(next.capacity() - (Long.SIZE / Byte.SIZE));
        }
    }

    @Override
    public int read() throws IOException
    {
        if (next.remaining() == 0)
        {
            if (continued)
            {
                long address = next.getLong(next.capacity() - (Long.SIZE / Byte.SIZE));
                next = mutator.read(address);
                continued = mutator.isContinued(address);
                setLimit();
            }
            return -1;
        }

        return next.get();
    }
}
