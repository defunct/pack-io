package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

// TODO Document.
public class PackInputStream extends InputStream
{
    // TODO Document.
    private final Mutator mutator;
    
    // TODO Document.
    private ByteBuffer next;
    
    // TODO Document.
    private boolean continued;
    
    // TODO Document.
    public PackInputStream(Mutator mutator, long address)
    {
        this.mutator = mutator;
        this.next = mutator.read(address);
        this.continued = mutator.isContinued(address);
        this.setLimit();
    }
    
    // TODO Document.
    private void setLimit()
    {
        if (continued)
        {
            next.limit(next.capacity() - (Long.SIZE / Byte.SIZE));
        }
    }

    // TODO Document.
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
