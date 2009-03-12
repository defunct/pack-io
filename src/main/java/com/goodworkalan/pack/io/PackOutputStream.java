package com.goodworkalan.pack.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.goodworkalan.pack.Mutator;

/**
 * An input stream that writes bytes to a block or a linked list of blocks
 * stored in a pack format file format. The pack output stream will allocate
 * multiple blocks and link them together as a linked list of blocks when move
 * bytes are written than can fit on a single block.
 * <p>
 * The allocate method obtains the address of block or linked list of blocks and
 * resets the pack output stream for a new allocation.
 * 
 * @author Alan Gutierrez
 */
public final class PackOutputStream extends OutputStream
{
    /** The pack mutator. */
    private final Mutator mutator;

    /** The address of the first block in a linked list of blocks. */
    private long firstAddress;

    /** The last address allocated. */
    private long lastAddress;

    /** The previous byte buffer in a linked list of byte buffers. */
    private ByteBuffer previous;

    /** The current byte buffer. */
    private ByteBuffer byteBuffer;

    /**
     * Create a pack output stream that allocates blocks from the given pack
     * mutator.
     * 
     * @param mutator
     *            The pack mutator.
     */
    public PackOutputStream(Mutator mutator)
    {
        this.mutator = mutator;
        this.byteBuffer = ByteBuffer.allocate(mutator.getPack().getPageSize());
    }

    /**
     * Write a byte to a block or linked list of blocks in the underlying
     * mutator.
     * 
     * @param b
     *            The byte to write.
     */
    @Override
    public void write(int b) throws IOException
    {
        if (byteBuffer.remaining() == 0)
        {
            long newAddress = mutator.allocate(byteBuffer.capacity());
            mutator.setContinued(newAddress);
            byteBuffer.flip();
            mutator.write(newAddress, byteBuffer);
            byteBuffer.clear();
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
            previous = byteBuffer;
            byteBuffer = ByteBuffer.allocate(previous.capacity());
            for (int i = 0; i < overflow; i++)
            {
                byteBuffer.put(previous.get(previous.capacity() - overflow + i));
            }
        }
        byteBuffer.put((byte) b);
    }

    /**
     * Reset the pack output stream for a new allocation.
     */
    private void reset()
    {
        firstAddress = 0L;
        lastAddress = 0L;
        previous = null;
        byteBuffer.clear();
    }

    /**
     * Allocate a block or linked list of blocks to store the bytes written to
     * this pack output stream and reset the pack output stream for a new
     * allocation.
     * 
     * @return The address of a block or linked list of blocks containing the
     *         bytes written to this pack output stream.
     */
    public long allocate()
    {
        long newAddress = mutator.allocate(byteBuffer.position());
        byteBuffer.flip();
        mutator.write(newAddress, byteBuffer);
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