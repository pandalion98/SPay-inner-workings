package com.android.internal.util;

import android.util.Printer;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class FastPrintWriter extends PrintWriter {
    private final boolean mAutoFlush;
    private final int mBufferLen;
    private final ByteBuffer mBytes;
    private CharsetEncoder mCharset;
    private boolean mIoError;
    private final OutputStream mOutputStream;
    private int mPos;
    private final Printer mPrinter;
    private final String mSeparator;
    private final char[] mText;
    private final Writer mWriter;

    private static class DummyWriter extends Writer {
        private DummyWriter() {
        }

        public void close() throws IOException {
            throw new UnsupportedOperationException("Shouldn't be here");
        }

        public void flush() throws IOException {
            close();
        }

        public void write(char[] buf, int offset, int count) throws IOException {
            close();
        }
    }

    public FastPrintWriter(OutputStream out) {
        this(out, false, 8192);
    }

    public FastPrintWriter(OutputStream out, boolean autoFlush) {
        this(out, autoFlush, 8192);
    }

    public FastPrintWriter(OutputStream out, boolean autoFlush, int bufferLen) {
        super(new DummyWriter(), autoFlush);
        if (out == null) {
            throw new NullPointerException("out is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = ByteBuffer.allocate(this.mBufferLen);
        this.mOutputStream = out;
        this.mWriter = null;
        this.mPrinter = null;
        this.mAutoFlush = autoFlush;
        this.mSeparator = System.lineSeparator();
        initDefaultEncoder();
    }

    public FastPrintWriter(Writer wr) {
        this(wr, false, 8192);
    }

    public FastPrintWriter(Writer wr, boolean autoFlush) {
        this(wr, autoFlush, 8192);
    }

    public FastPrintWriter(Writer wr, boolean autoFlush, int bufferLen) {
        super(new DummyWriter(), autoFlush);
        if (wr == null) {
            throw new NullPointerException("wr is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = null;
        this.mOutputStream = null;
        this.mWriter = wr;
        this.mPrinter = null;
        this.mAutoFlush = autoFlush;
        this.mSeparator = System.lineSeparator();
        initDefaultEncoder();
    }

    public FastPrintWriter(Printer pr) {
        this(pr, 512);
    }

    public FastPrintWriter(Printer pr, int bufferLen) {
        super(new DummyWriter(), true);
        if (pr == null) {
            throw new NullPointerException("pr is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = null;
        this.mOutputStream = null;
        this.mWriter = null;
        this.mPrinter = pr;
        this.mAutoFlush = true;
        this.mSeparator = System.lineSeparator();
        initDefaultEncoder();
    }

    private final void initEncoder(String csn) throws UnsupportedEncodingException {
        try {
            this.mCharset = Charset.forName(csn).newEncoder();
            this.mCharset.onMalformedInput(CodingErrorAction.REPLACE);
            this.mCharset.onUnmappableCharacter(CodingErrorAction.REPLACE);
        } catch (Exception e) {
            throw new UnsupportedEncodingException(csn);
        }
    }

    public boolean checkError() {
        boolean z;
        flush();
        synchronized (this.lock) {
            z = this.mIoError;
        }
        return z;
    }

    protected void clearError() {
        synchronized (this.lock) {
            this.mIoError = false;
        }
    }

    protected void setError() {
        synchronized (this.lock) {
            this.mIoError = true;
        }
    }

    private final void initDefaultEncoder() {
        this.mCharset = Charset.defaultCharset().newEncoder();
        this.mCharset.onMalformedInput(CodingErrorAction.REPLACE);
        this.mCharset.onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    private void appendLocked(char c) throws IOException {
        int pos = this.mPos;
        if (pos >= this.mBufferLen - 1) {
            flushLocked();
            pos = this.mPos;
        }
        this.mText[pos] = c;
        this.mPos = pos + 1;
    }

    private void appendLocked(String str, int i, int length) throws IOException {
        int BUFFER_LEN = this.mBufferLen;
        if (length > BUFFER_LEN) {
            int end = i + length;
            while (i < end) {
                int next = i + BUFFER_LEN;
                appendLocked(str, i, next < end ? BUFFER_LEN : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > BUFFER_LEN) {
            flushLocked();
            pos = this.mPos;
        }
        str.getChars(i, i + length, this.mText, pos);
        this.mPos = pos + length;
    }

    private void appendLocked(char[] buf, int i, int length) throws IOException {
        int BUFFER_LEN = this.mBufferLen;
        if (length > BUFFER_LEN) {
            int end = i + length;
            while (i < end) {
                int next = i + BUFFER_LEN;
                appendLocked(buf, i, next < end ? BUFFER_LEN : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > BUFFER_LEN) {
            flushLocked();
            pos = this.mPos;
        }
        System.arraycopy(buf, i, this.mText, pos, length);
        this.mPos = pos + length;
    }

    private void flushBytesLocked() throws IOException {
        int position = this.mBytes.position();
        if (position > 0) {
            this.mBytes.flip();
            this.mOutputStream.write(this.mBytes.array(), 0, position);
            this.mBytes.clear();
        }
    }

    private void flushLocked() throws IOException {
        if (this.mPos > 0) {
            if (this.mOutputStream != null) {
                CharBuffer charBuffer = CharBuffer.wrap(this.mText, 0, this.mPos);
                CoderResult result = this.mCharset.encode(charBuffer, this.mBytes, true);
                while (!result.isError()) {
                    if (result.isOverflow()) {
                        flushBytesLocked();
                        result = this.mCharset.encode(charBuffer, this.mBytes, true);
                    } else {
                        flushBytesLocked();
                        this.mOutputStream.flush();
                    }
                }
                throw new IOException(result.toString());
            } else if (this.mWriter != null) {
                this.mWriter.write(this.mText, 0, this.mPos);
                this.mWriter.flush();
            } else {
                int nonEolOff = 0;
                int sepLen = this.mSeparator.length();
                int len = sepLen < this.mPos ? sepLen : this.mPos;
                while (nonEolOff < len && this.mText[(this.mPos - 1) - nonEolOff] == this.mSeparator.charAt((this.mSeparator.length() - 1) - nonEolOff)) {
                    nonEolOff++;
                }
                if (nonEolOff >= this.mPos) {
                    this.mPrinter.println("");
                } else {
                    this.mPrinter.println(new String(this.mText, 0, this.mPos - nonEolOff));
                }
            }
            this.mPos = 0;
        }
    }

    public void flush() {
        synchronized (this.lock) {
            try {
                flushLocked();
                if (this.mOutputStream != null) {
                    this.mOutputStream.flush();
                } else if (this.mWriter != null) {
                    this.mWriter.flush();
                }
            } catch (IOException e) {
                setError();
            }
        }
    }

    public void close() {
        synchronized (this.lock) {
            try {
                flushLocked();
                if (this.mOutputStream != null) {
                    this.mOutputStream.close();
                } else if (this.mWriter != null) {
                    this.mWriter.close();
                }
            } catch (IOException e) {
                setError();
            }
        }
    }

    public void print(char[] charArray) {
        synchronized (this.lock) {
            try {
                appendLocked(charArray, 0, charArray.length);
            } catch (IOException e) {
            }
        }
    }

    public void print(char ch) {
        synchronized (this.lock) {
            try {
                appendLocked(ch);
            } catch (IOException e) {
            }
        }
    }

    public void print(String str) {
        if (str == null) {
            str = String.valueOf(null);
        }
        synchronized (this.lock) {
            try {
                appendLocked(str, 0, str.length());
            } catch (IOException e) {
                setError();
            }
        }
    }

    public void print(int inum) {
        if (inum == 0) {
            print(SmartFaceManager.PAGE_MIDDLE);
        } else {
            super.print(inum);
        }
    }

    public void print(long lnum) {
        if (lnum == 0) {
            print(SmartFaceManager.PAGE_MIDDLE);
        } else {
            super.print(lnum);
        }
    }

    public void println() {
        synchronized (this.lock) {
            try {
                appendLocked(this.mSeparator, 0, this.mSeparator.length());
                if (this.mAutoFlush) {
                    flushLocked();
                }
            } catch (IOException e) {
                setError();
            }
        }
    }

    public void println(int inum) {
        if (inum == 0) {
            println(SmartFaceManager.PAGE_MIDDLE);
        } else {
            super.println(inum);
        }
    }

    public void println(long lnum) {
        if (lnum == 0) {
            println(SmartFaceManager.PAGE_MIDDLE);
        } else {
            super.println(lnum);
        }
    }

    public void println(char[] chars) {
        print(chars);
        println();
    }

    public void println(char c) {
        print(c);
        println();
    }

    public void write(char[] buf, int offset, int count) {
        synchronized (this.lock) {
            try {
                appendLocked(buf, offset, count);
            } catch (IOException e) {
            }
        }
    }

    public void write(int oneChar) {
        synchronized (this.lock) {
            try {
                appendLocked((char) oneChar);
            } catch (IOException e) {
            }
        }
    }

    public void write(String str) {
        synchronized (this.lock) {
            try {
                appendLocked(str, 0, str.length());
            } catch (IOException e) {
            }
        }
    }

    public void write(String str, int offset, int count) {
        synchronized (this.lock) {
            try {
                appendLocked(str, offset, count);
            } catch (IOException e) {
            }
        }
    }

    public PrintWriter append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        String output = csq.subSequence(start, end).toString();
        write(output, 0, output.length());
        return this;
    }
}
