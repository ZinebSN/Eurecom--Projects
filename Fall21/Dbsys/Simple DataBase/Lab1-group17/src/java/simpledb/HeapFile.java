package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {
	private File f;
	private TupleDesc td;

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	this.f=f;
    	this.td=td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return f;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere to ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
    	return f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
    	int pageSize = BufferPool.getPageSize();
    	//calculate the offset
    	int offset=pageSize*pid.getPageNumber();
    	byte[] data = new byte[pageSize];
    	//Random Access
    	try (RandomAccessFile file = new RandomAccessFile(f, "r")) {
    		//Seek 
    		try {
    	         file.seek(offset);
    	    } catch (IOException e1) {
    	         throw new IllegalArgumentException("Unable to seek in HeapFile to correct position");
    	    }
    		//try to read
    	    int byteRead = file.read(data);
    	    if (byteRead == -1) {
    	         throw new IllegalArgumentException("Invalid byte to read");
    	    }
    	    if (byteRead < pageSize) {
    	        throw new IllegalArgumentException("Unable to read "
    	                        + BufferPool.getPageSize() + " bytes");
    	   }

    	   return new HeapPage((HeapPageId) pid, data);

        } catch (IOException e) {
    	            e.printStackTrace();
        }
    	return null;
       
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
    	int numPages=(int) Math.ceil(f.length()/BufferPool.getPageSize());
        return numPages;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
       return new HeapFileIterator(tid, this);
    	
        
    }
}

