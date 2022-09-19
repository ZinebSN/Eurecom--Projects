package simpledb;

import java.util.Iterator;
//Helper class to generate a DbFileIterator for HeapFile.iterator method
public class HeapFileIterator extends AbstractDbFileIterator {
	//Declaration of variables
	private final HeapFile heap_file;
    private final TransactionId tid;
    private Iterator<Tuple> tuple_iterator;
    private int currentPage;
    // Constructor
    public HeapFileIterator(TransactionId tid, HeapFile heap_file) {
        this.tid = tid;
        this.heap_file = heap_file;
    }
    //Open iterator method
    public void open() throws DbException, TransactionAbortedException {
        HeapPage heapPage = (HeapPage) Database.getBufferPool().getPage(tid,
                new HeapPageId(heap_file.getId(), 0), Permissions.READ_ONLY);
        tuple_iterator = heapPage.iterator();
        currentPage = 0;
    }
    //read next tuple method
    protected Tuple readNext() throws DbException, TransactionAbortedException {
    	//check if iterator not null
        if (tuple_iterator != null) {
        	//check if the current tuple has next and terurn it
            if (tuple_iterator.hasNext()) {
                return tuple_iterator.next();
            } else {
            	//move to next page and iterate the same instructions
                if (currentPage < heap_file.numPages() - 1) {
                    HeapPage heapPage = (HeapPage) Database.getBufferPool().getPage(tid,
                            new HeapPageId(heap_file.getId(), ++currentPage), Permissions.READ_ONLY);
                    tuple_iterator = heapPage.iterator();
                    return readNext();
                }
            }
        }
        return null;
    }
   //reset the iterator to the start
    public void rewind() throws DbException, TransactionAbortedException {
        HeapPage heapPage = (HeapPage) Database.getBufferPool().getPage(tid,
                new HeapPageId(heap_file.getId(), 0), Permissions.READ_ONLY);
        //creating the iterator
        tuple_iterator = heapPage.iterator();
        //reset the iterator to the start
        currentPage = 0;
    }
    //close iterator method
    public void close() {
        super.close();
        tuple_iterator = null;
        currentPage = -1;
    }
}
