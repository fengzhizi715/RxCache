package com.safframework.rxcache.persistence.diskmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @FileName: com.safframework.rxcache.persistence.diskmap.BufferedRandomAccessFile
 * @author: Tony Shen
 * @date: 2020-07-03 21:02
 * @version: V1.0 <描述当前版本功能>
 */
public class BufferedRandomAccessFile extends InputStream {

    static final int DEFAULT_BUFFER_SIZE = 8192;

    // keep track of this separately since RandomAccessFile.getFilePointer() is an expensive operation
    private long file_pos = 0;

    // keep track of this separately since RandomAccessFile.length() is an expensive operation
    private long length = 0;

    // the buffer used when reading
    private byte[] buffer;

    // the position inside the buffer
    private int buffer_pos = 0;

    // the underlying file
    private RandomAccessFile raf;


    public BufferedRandomAccessFile(File file, String mode) throws IOException {
        raf = new RandomAccessFile(file, mode);
        length = raf.length();
    }


    void clearBuffer() {
        buffer = null;
        buffer_pos = 0;
    }

    public void write(byte[] data) throws IOException {
        clearBuffer();
        raf.write(data);
        file_pos += data.length;
        if( length < file_pos )
            length = file_pos;
    }

    public void write(String str) throws IOException {
        write(str.getBytes(StandardCharsets.UTF_8));
    }

    public void write(byte b) throws IOException {
        clearBuffer();
        raf.write(b);
        file_pos += 1;
        if( length < file_pos )
            length = file_pos;
    }

    public void close() throws IOException {
        clearBuffer();
        raf.close();
    }

    public void truncate(long len) throws IOException {
        clearBuffer();
        raf.setLength(len);
        length = len;
        if( file_pos > len )
            file_pos = len;
    }

    public void seek(long pos) throws IOException {
        clearBuffer();
        if( pos != file_pos ) {
            raf.seek(pos);
            file_pos = pos;
        }
    }

    public long pos() {
        if( buffer_pos > 0 )
            return file_pos - buffer.length + buffer_pos;
        else
            return file_pos;
    }

    public long length() {
        return length;
    }


    public boolean isEOF() {
        return pos() >= length();
    }

    public byte[] readUntil(byte delimiter) throws IOException {
        if( isEOF() )
            return null;

        if( buffer == null || buffer_pos == buffer.length ) {
            fillBuffer(); // fill it
        }
        else if( buffer.length > DEFAULT_BUFFER_SIZE ) {
            // trim "left side" of buffer if unnecessary big due to a previous large read
            buffer = Arrays.copyOfRange(buffer, buffer_pos, buffer.length); // removes everything before buffer_pos
            buffer_pos = 0;
        }

        assert buffer.length > 0;
        assert buffer_pos < buffer.length;

        // Note: this might make the buffer grow.
        // Using a constant size buffer and constructing the string line chunk after chunk might sound easier.
        // However, this would be problematic due to multibyte characters spanning consecutive buffers.
        int start = buffer_pos;
        while(buffer[buffer_pos] != delimiter) {
            buffer_pos++;
            if( buffer_pos == buffer.length ) {
                if( isEOF() )
                    break;
                else
                    expandBuffer();
            }
        }
        assert isEOF() || buffer[buffer_pos] == delimiter;

        byte[] result = Arrays.copyOfRange(buffer, start, buffer_pos);

        if( !isEOF() )
            buffer_pos++; // "consume" the new line character

        return result;
    }

    /**
     * Skip all bytes until delimiter(inclusive) is encountered.
     *
     * @param delimiter
     * @throws IOException
     */
    public void skipUntil(byte delimiter) throws IOException {
        if( isEOF() )
            return;

        if( buffer == null || buffer_pos == buffer.length )
            fillBuffer(); // fill it

        while(buffer[buffer_pos] != delimiter) {
            buffer_pos++;
            if( buffer_pos == buffer.length ) {
                if( isEOF() )
                    return;
                else
                    fillBuffer();
            }
        }
        assert buffer[buffer_pos] == delimiter;
        buffer_pos++; // read the character
    }

    /**
     * Reads the next line, interpreted as UTF-8, excluding the newline character.
     */
    public String readLine() throws IOException {
        if( isEOF() )
            return null;
        byte[] bytes = readUntil((byte) '\n');
        String line = new String(bytes, StandardCharsets.UTF_8);
        return line;
    }



    /**
     * Reads the next chunk of file and append it to the buffer.
     */
    private void expandBuffer() throws IOException {
        assert !isEOF();
        assert buffer == null || buffer_pos <= buffer.length;

        if( buffer == null ) {
            // create it
            buffer_pos = 0;
            buffer = new byte[DEFAULT_BUFFER_SIZE];
        }
        if( buffer_pos == buffer.length ) {
            // expand it
            buffer = Arrays.copyOf(buffer, buffer.length + DEFAULT_BUFFER_SIZE);
        }
        // read last chunk
        int len = read(buffer, buffer_pos, DEFAULT_BUFFER_SIZE);

        // if necessary (when EOF), trim the empty buffer at the end
        if(len < DEFAULT_BUFFER_SIZE) {
            buffer = Arrays.copyOf(buffer, buffer.length - DEFAULT_BUFFER_SIZE + len);
        }

        //System.out.println("New buffer size: " + buffer.length);
    }

    /**
     * Reads the next chunk of file into the buffer.
     */
    private void fillBuffer() throws IOException {
        assert !isEOF();
        assert buffer == null || buffer_pos == buffer.length;

        // reset buffer
        buffer_pos = 0;
        if( buffer == null || buffer.length != DEFAULT_BUFFER_SIZE)
            buffer = new byte[DEFAULT_BUFFER_SIZE];

        // read last chunk
        int len = read(buffer, 0, DEFAULT_BUFFER_SIZE);

        // if necessary, trim the end
        if(len < DEFAULT_BUFFER_SIZE) {
            buffer = Arrays.copyOf(buffer, len);
        }
    }

    @Override
    public int read() throws IOException {
        file_pos++;
        return raf.read();
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        len = raf.read(b, off, len);
        file_pos += len;
        return len;
    }
}