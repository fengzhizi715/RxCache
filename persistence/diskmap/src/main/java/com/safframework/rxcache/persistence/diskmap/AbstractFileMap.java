package com.safframework.rxcache.persistence.diskmap;

import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.persistence.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @FileName: com.safframework.rxcache.persistence.diskmap.AbstractFileMap
 * @author: Tony Shen
 * @date: 2020-07-03 20:55
 * @version: V1.7
 */
public abstract class AbstractFileMap<K,V>  implements FileMap<K,V> {

    protected File file;
    protected BufferedRandomAccessFile fileIO;

    private static final String MODE = "rw";
    private long entriesWritten;

    private Converter converter;

    Class<K> keyType;
    Class<V> valueType;

    public AbstractFileMap(File file, Class<K> keyType, Class<V> valueType, Converter converter) throws IOException {
        this.file = file;
        this.keyType = keyType;
        this.valueType = valueType;
        this.converter = converter;
        init();
        if(fileIO != null)
            fileIO.close();

        entriesWritten = 0;
        fileIO = new BufferedRandomAccessFile(file, MODE);

        while(!fileIO.isEOF()) {
            long offset = fileIO.pos();

            String line = fileIO.readLine();
            if( line == null ||  line.isEmpty() || line.startsWith("#") )
                continue;

            loadEntry(offset, line);
            entriesWritten++;
        }
    }

    protected long getEntriesWritten() {
        return entriesWritten;
    }

    abstract protected void init() throws IOException;

    protected abstract void loadEntry(long offset, String line) throws IOException;

    public File getFile() {
        return file;
    }

    public class LineEntry implements Entry<K, V> {

        String line;
        int tabPos;

        LineEntry(String line) throws IOException {
            this.line = line;
            this.tabPos = line.indexOf('\t');
            if( tabPos <= 0 ) {
                throw new IOException("Failed to parse line: " + line);
            }
        }

        public String getKeyJson() {
            String keyJson = line.substring(0, tabPos);
            return keyJson;
        }

        @Override
        public K getKey() {
            return converter.fromJson(getKeyJson(),keyType);
        }

        @Override
        public V getValue() {
            return converter.fromJson(getValueJson(),valueType);
        }

        public String getValueJson() {
            String valueJson = line.substring(tabPos+1);
            return valueJson;
        }

        @Override
        public V setValue(V value) {
            throw new RxCacheException("This operation is not supported.");
        }

    }

    protected Entry<K, V> parseLine(String line) throws IOException {
        int i = line.indexOf('\t');
        if( i <= 0 ) {
            throw new IOException("Failed to parse line: " + line);
        }
        String keyJson = line.substring(0, i);
        String valueJson = line.substring(i+1);
        K key = converter.fromJson(keyJson,keyType);
        V value = converter.fromJson(valueJson,valueType);

        return new AbstractMap.SimpleEntry<K,V>(key, value);
    }

    protected K parseKey(String line) throws IOException {
        int i = line.indexOf('\t');
        if( i <= 0 ) {
            throw new IOException("Failed to parse line: " + line);
        }
        String keyJson = line.substring(0, i);
        K key = converter.fromJson(keyJson,keyType);
        return key;
    }

    protected V parseValue(String line) throws IOException {
        int i = line.indexOf('\t');
        if( i <= 0 ) {
            throw new IOException("Failed to parse line: " + line);
        }
        String valueJson = line.substring(i+1);
        V value = converter.fromJson(valueJson,valueType);

        return value;
    }

    protected String readLine(long offset) throws IOException {
        fileIO.seek(offset);
        return fileIO.readLine();
    }

    protected long writeLine(K key, V value) {
        try {
            entriesWritten++;

            String keyJson = converter.toJson(key);
            String valueJson = converter.toJson(value);
            String line = keyJson + "\t" + valueJson + "\n";

            long offset = fileIO.length();
            fileIO.seek(offset);
            fileIO.write( line.getBytes(StandardCharsets.UTF_8) );
            return offset;
        } catch (IOException e) {
            throw new RxCacheException("Failed to save entry for " + key, e);
        }
    }


    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        for( Entry<? extends K, ? extends V> entry : m.entrySet() )
            put(entry.getKey(), entry.getValue());
    }

    protected synchronized void clearLines() {
        try {
            fileIO.seek(0);
            fileIO.truncate(0);
            entriesWritten = 0;
        } catch (IOException e) {
            throw new RxCacheException("Failed to clear persistent map", e);
        }
    }

    /**
     * Returns an estimate of the file's content fragmentation. It is the ratio of obsolete data in the file.
     * When entries are frequently updated and removed, the old entries are still stored in the file.
     * For example, a fragmentation of 2/3 would mean that roughly 2/3 of the file is filled with obsolete content.
     *
     * @return
     */
    public double getFragmentation() {
        return 1.0 - ((double) this.size() / entriesWritten);
    }


    public long diskSize() {
        return fileIO.length();
    }


    public void close() throws IOException {
        fileIO.close();
    }
}