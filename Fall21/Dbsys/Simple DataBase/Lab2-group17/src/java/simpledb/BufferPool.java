package simpledb;

import java.io.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;


/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool checks that the transaction has the appropriate
 * locks to read/write the page.
 * 
 * @Threadsafe, all fields are final
 */
public class BufferPool {
    /** Bytes per page, including header. */
    private static final int DEFAULT_PAGE_SIZE = 4096;
    
    private static int pageSize = DEFAULT_PAGE_SIZE;
    private ConcurrentHashMap<PageId, Page> pages;
    
    /** Default number of pages passed to the constructor. This is used by
    other classes. BufferPool should use the numPages argument to the
    constructor instead. */
    public static final int DEFAULT_PAGES = 50;
	
	private final int numPages;
	private PageBufferPool bufferPool;

    /**
     * Creates a BufferPool that caches up to numPages pages.
     *
     * @param numPages maximum number of pages in this buffer pool.
     */
    public BufferPool(int numPages) {
        // some code goes here
		this.numPages = numPages;
		this.bufferPool = new PageBufferPool(numPages);
		pages = new ConcurrentHashMap<PageId, Page>();
		  
    }
    
    public static int getPageSize() {
      return pageSize;
    }
    
    // THIS FUNCTION SHOULD ONLY BE USED FOR TESTING!!
    public static void setPageSize(int pageSize) {
    	BufferPool.pageSize = pageSize;
    }
    
    // THIS FUNCTION SHOULD ONLY BE USED FOR TESTING!!
    public static void resetPageSize() {
    	BufferPool.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, a page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public  Page getPage(TransactionId tid, PageId pid, Permissions perm)
        throws TransactionAbortedException, DbException {
        // some code goes here
        if (this.pages.containsKey(pid)) {
			return this.pages.get(pid);
		} else {
			if (this.pages.size() < this.numPages) {
				Page page = Database.getCatalog().getDatabaseFile(pid.getTableId()).readPage(pid);
				this.pages.put(pid, page);
				return page;
				} 
			else {
				Page page = Database.getCatalog().getDatabaseFile(pid.getTableId()).readPage(pid);
				if (this.pages.size() == this.numPages) evictPage();
				this.pages.put(page.getId(), page);
				return page;
      }
    }
    }

    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param pid the ID of the page to unlock
     */
    public  void releasePage(TransactionId tid, PageId pid) {
        // some code goes here
        // not necessary for lab1|lab2
    }

    /**
     * Release all locks associated with a given transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     */
    public void transactionComplete(TransactionId tid) throws IOException {
        // some code goes here
        // not necessary for lab1|lab2
    }

    /** Return true if the specified transaction has a lock on the specified page */
    public boolean holdsLock(TransactionId tid, PageId p) {
        // some code goes here
        // not necessary for lab1|lab2
        return false;
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public void transactionComplete(TransactionId tid, boolean commit)
        throws IOException {
        // some code goes here
        // not necessary for lab1|lab2
    }

    /**
     * Add a tuple to the specified table on behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to and any other 
     * pages that are updated (Lock acquisition is not needed for lab2). 
     * May block if the lock(s) cannot be acquired.
     * 
     * Marks any pages that were dirtied by the operation as dirty by calling
     * their markDirty bit, and adds versions of any pages that have 
     * been dirtied to the cache (replacing any existing versions of those pages) so 
     * that future requests see up-to-date pages. 
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public void insertTuple(TransactionId tid, int tableId, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        // not necessary for lab1
    	  DbFile f = Database.getCatalog().getDatabaseFile(tableId);
    	  updateBufferPool(f.insertTuple(tid, t),tid);
		
    }
    
    private void updateBufferPool(ArrayList<Page> pagelist, TransactionId tid)  throws DbException {
    for(Page p:pagelist) {
    	p.markDirty(true, tid);
    	if(pages.size()>numPages)
    		evictPage();
    	pages.put(p.getId(), p);
    }
    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is removed from and any
     * other pages that are updated. May block if the lock(s) cannot be acquired.
     *
     * Marks any pages that were dirtied by the operation as dirty by calling
     * their markDirty bit, and adds versions of any pages that have 
     * been dirtied to the cache (replacing any existing versions of those pages) so 
     * that future requests see up-to-date pages. 
     *
     * @param tid the transaction deleting the tuple.
     * @param t the tuple to delete
     */
    public  void deleteTuple(TransactionId tid, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        // not necessary for lab1

        HeapFile f = (HeapFile) Database.getCatalog().getDatabaseFile(t.getRecordId().getPageId().getTableId());
        updateBufferPool(f.deleteTuple(tid, t),tid);
    }

    /**
     * Flush all dirty pages to disk.
     * NB: Be careful using this routine -- it writes dirty data to disk so will
     *     break simpledb if running in NO STEAL mode.
     */
    public synchronized void flushAllPages() throws IOException {
        // some code goes here
        // not necessary for lab1
    	for (PageId pageId : pages.keySet()) {
            flushPage(pageId);
        }
    }

    /** Remove the specific page id from the buffer pool.
        Needed by the recovery manager to ensure that the
        buffer pool doesn't keep a rolled back page in its
        cache.
        
        Also used by B+ tree files to ensure that deleted pages
        are removed from the cache so they can be reused safely
    */
    public synchronized void discardPage(PageId pid) {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Flushes a certain page to disk
     * @param pid an ID indicating the page to flush
     */
    private synchronized  void flushPage(PageId pid) throws IOException {
        // some code goes here
        // not necessary for lab1
    	if (pages.containsKey(pid)) {
            Page page = pages.get(pid);
            if (page.isDirty() != null) {
                HeapFile file = (HeapFile) Database.getCatalog().getDatabaseFile(pid.getTableId());
                TransactionId dirtier = page.isDirty();
                if (dirtier != null) {
                    Database.getLogFile().logWrite(dirtier, page.getBeforeImage(), page);
                    Database.getLogFile().force();
                }
                file.writePage(page);
                page.markDirty(false, null);
            }
        }
    	
    }

    /** Write all pages of the specified transaction to disk.
     */
    public synchronized  void flushPages(TransactionId tid) throws IOException {
        // some code goes here
        // not necessary for lab1|lab2
    }

    /**
     * Discards a page from the buffer pool.
     * Flushes the page to disk to ensure dirty pages are updated on disk.
     */
    private synchronized  void evictPage() throws DbException {
        // some code goes here
        // not necessary for lab1
    	synchronized (pages) {
            if (pages.keySet().size() == numPages) {

                int randomNumber = (int) (Math.random() * (pages.size() - 1));
                ArrayList<PageId> arrayList = new ArrayList<PageId>(pages.keySet());
                PageId pageId = arrayList.get(randomNumber);
                try {
                    flushPage(pageId);
                } catch (IOException e) {
                    throw new DbException("Page could not be flushed while evicting");
                }
                pages.remove(pageId);
            }
        }

  }

 private static class PageBufferPool {
    private final int capacity;
    private final ArrayList<Page> pageBuffer;
    private final ConcurrentHashMap<PageId, Page> pageIdToPage;

    public PageBufferPool(int size) {
      this.capacity = size;
      this.pageBuffer = new ArrayList<>(size);
      this.pageIdToPage = new ConcurrentHashMap<>(size);
    }

    public boolean containsKey(PageId id) {
      return pageIdToPage.containsKey(id);
    }

    public Page get(PageId id) {
      return pageIdToPage.get(id);
    }

    public void put(PageId id, Page page) {
      if (pageBuffer.size() < capacity) {
        if (pageIdToPage.containsKey(id)) {
          pageBuffer.remove(page);
        } else {
          pageIdToPage.put(id, page);
        }
        pageBuffer.add(page);
      }
    }

    public Page remove(PageId pid) {
      if (pageIdToPage.containsKey(pid)) {
        Page pg = pageIdToPage.remove(pid);
        pageBuffer.remove(pg);
        return pg;
      }
      return null;
    }

    public int size() {
      return pageBuffer.size();
    }

    public Page evictPage() {
      int bufferSize = pageBuffer.size();
      if (bufferSize > 0 && bufferSize == capacity) {
        return pageBuffer.get(0);
      }
      return null;
    }

  }

    

}
