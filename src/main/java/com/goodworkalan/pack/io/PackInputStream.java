package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

/**
 * An input stream that reads bytes from a block or series of blocks stored in a
 * pack format file format.
 * 
 * @author Alan Gutierrez
 */
public class PackInputStream extends InputStream
{
    /** The pack mutator. */
    private final Mutator mutator;
    
    /** The byte buffer of the contents of the current block. */
    private ByteBuffer byteBuffer;
    
    /** If true, the content continues on a subsequent block. */
    private boolean continued;

    /**
     * Create a pack input stream with the given mutator that reads the content
     * at the given address.
     * 
     * @param mutator
     *            The pack mutator.
     * @param address
     *            The block address.
     */
    public PackInputStream(Mutator mutator, long address)
    {
        this.mutator = mutator;
        this.byteBuffer = mutator.read(address);
        this.continued = mutator.isContinued(address);
        this.setLimit();
    }

    /**
     * If the content is continued on a subsequent block, adjust the limit of
     * the byte buffer to stop before the last 8 bytes which contain the address
     * of the next block.
     */
    private void setLimit()
    {
        if (continued)
        {
            byteBuffer.limit(byteBuffer.capacity() - (Long.SIZE / Byte.SIZE));
        }
    }

    /**
     * Reads the next byte of data from the block or linked list of blocks at
     * the underlying pack address.
     * 
     * @return The next byte from the block content or <code>-1</code> if there
     *         is no more content.
     */
    @Override
    public int read() throws IOException
    {
        if (byteBuffer.remaining() == 0)
        {
            if (continued)
            {
                long address = byteBuffer.getLong(byteBuffer.capacity() - (Long.SIZE / Byte.SIZE));
                byteBuffer = mutator.read(address);
                continued = mutator.isContinued(address);
                setLimit();
            }
            return -1;
        }

        return byteBuffer.get();
    }
}
