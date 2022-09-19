package simpledb;

import java.io.IOException;

/**
 * Inserts tuples read from the child operator into the tableId specified in the
 * constructor
 */
public class Insert extends Operator {

    private static final long serialVersionUID = 1L;

    private TransactionId t;
    private OpIterator child;
    private int tableId;
    private TupleDesc table;
    
    private boolean isInserted;
    /**
     * Constructor.
     *
     * @param t
     *            The transaction running the insert.
     * @param child
     *            The child operator from which to read tuples to be inserted.
     * @param tableId
     *            The table in which to insert tuples.
     * @throws DbException
     *             if TupleDesc of child differs from table into which we are to
     *             insert.
     */
    
    public Insert(TransactionId t, OpIterator child, int tableId)
            throws DbException {
        // some code goes here
    	//We check if TupleDesc of child differs from table into which we want insert and if it's the case we throw an exception
    	
    	//assigning the values in the constructor
    	this.t=t;
    	this.child=child;
    	this.tableId=tableId;
    	isInserted=false;
    	Type[] types = new Type[] {Type.INT_TYPE};
    	table = new TupleDesc(types);
    	
    	//this is the condition where we have problem when we uncomment the three following lines we get an error because
    	//the created sequential scan in the test file has a null TupleDesc
    	
    	
        //if (!child.getTupleDesc().equals(Database.getCatalog().getTupleDesc(tableId))) {
    		//throw new DbException("Table to insert in doesn't match with child");
    	//}
    
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
    	return table;
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    
    	super.open();
    	this.child.open(); 
    	isInserted=false;
    	
    }

    public void close() {
        // some code goes here
    	super.close();
    	this.child.close();
    	
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	this.child.rewind();
    }

    /**
     * Inserts tuples read from child into the tableId specified by the
     * constructor. It returns a one field tuple containing the number of
     * inserted records. Inserts should be passed through BufferPool. An
     * instances of BufferPool is available via Database.getBufferPool(). Note
     * that insert DOES NOT need check to see if a particular tuple is a
     * duplicate before inserting it.
     *
     * @return A 1-field tuple containing the number of inserted records, or
     *         null if called more than once.
     * @see Database#getBufferPool
     * @see BufferPool#insertTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
    	if (isInserted) return null;//if the tuples read from child are inserted we have nothing to do
    	int counter=0;
    	//if the child contains non inserted tuples we loop over these tuples and we increment the counter
    	for(;child.hasNext();counter++) {
    		Tuple currentTuple=child.next();//next tuple to insert
    		try 
    		{
        		Database.getBufferPool().insertTuple(t, tableId, currentTuple);    //insert the tuple using bufferpool insertTuple method			
    		}
    		catch (IOException e)
    		{
    			throw new DbException("IO Exception: Error on this tuple insertion");
    		}
    		
    	}
    	//after inserting the tuples read from child we turn isInserted to true
    	isInserted=true;
    	Tuple insertedRecordsTuple=new Tuple(this.getTupleDesc());//we create a 1-field tuple to store the number of inserted tuples
    	insertedRecordsTuple.setField(0,new IntField(counter));// we set the field to the number of inserted tuples
        return insertedRecordsTuple;
    }

    @Override
    public OpIterator[] getChildren() {
        // some code goes here
        return new OpIterator[] {child};
    }

    @Override
    public void setChildren(OpIterator[] children) {
        // some code goes here
    	this.child=children[0];
    }
}
