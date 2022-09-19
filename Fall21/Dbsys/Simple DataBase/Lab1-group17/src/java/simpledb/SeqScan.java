package simpledb;

import java.util.*;


/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements OpIterator {

    private static final long serialVersionUID = 1L;
    //declaring variables
    private TransactionId transaction_id;
    private String alias_table;
    private int table_id;
    private HeapFile heap_file;
    private HeapFileIterator hf_iterator;
    private TupleDesc tuple_desc; 
   
    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     *
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    
  
    
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // some code goes here
    	this.transaction_id = tid;
    	reset(tableid,tableAlias);
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
    	
    	// returning the table name associated with the table id.
    	return Database.getCatalog().getTableName(table_id);
    }

    /**
     * @return Return the alias of the table this operator scans.
     * */
    public String getAlias()
    {
        // some code goes here
    	return alias_table;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        // some code goes here
    	this.table_id = tableid;
    	this.alias_table = tableAlias;
    	
    	//get the database file of the table the operator scans in HeapFile format
    	heap_file=(HeapFile) Database.getCatalog().getDatabaseFile(table_id);
    	hf_iterator = new HeapFileIterator(transaction_id,heap_file);
    	
    	
    	
    }

    public SeqScan(TransactionId tid, int tableId) {
        this(tid, tableId, Database.getCatalog().getTableName(tableId));
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	hf_iterator.open();
    	this.getTupleDesc();
    	
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.  The alias and name should be separated with a "." character
     * (e.g., "alias.fieldName").
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
    	int lengthTable=Database.getCatalog().getTupleDesc(table_id).numFields();
    	
    	String[] field_with_alias=new String[lengthTable];
    	Type[] type_of_field=new Type[lengthTable];
    	
    	int i=0;
    	while(i<lengthTable)
    	{
    		String name_field=Database.getCatalog().getTupleDesc(table_id).getFieldName(i);
    		field_with_alias[i]=alias_table+"."+name_field;
    		type_of_field[i]=Database.getCatalog().getTupleDesc(table_id).getFieldType(i);
    		i++;
    	}
    	
    	this.tuple_desc=new TupleDesc(type_of_field,field_with_alias);
    	return tuple_desc;
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // some code goes here
    	boolean hf_is_null=false, hf_has_next=false;
    	if(hf_iterator==null) {
    		hf_is_null = true;
    	}
    	if(hf_iterator.hasNext()) {
    		hf_has_next = true;
    	}
    	return (!hf_is_null&&hf_has_next);
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
    	// some code goes here
    	boolean hf_has_next=hf_iterator.hasNext();
    	if(hf_has_next)
    	{
    		return hf_iterator.next();
    	}
    	else {
    	throw new NoSuchElementException();
    	}
    }

    public void close() {
        // some code goes here
    	boolean hf_is_null = (hf_iterator==null);
    	if(!hf_is_null)
    	{hf_iterator.close();}
    	heap_file=null;
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
    	hf_iterator.rewind();
    }
}
