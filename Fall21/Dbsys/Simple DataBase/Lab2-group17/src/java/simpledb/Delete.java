package simpledb;

import java.io.IOException;

/**
 * The delete operator. Delete reads tuples from its child operator and removes
 * them from the table they belong to.
 */
public class Delete extends Operator {

    private static final long serialVersionUID = 1L;
    
    private TransactionId t;
    private OpIterator child;
    private boolean isDeleted;
    private TupleDesc table;
    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     * 
     * @param t
     *            The transaction this delete runs in
     * @param child
     *            The child operator from which to read tuples for deletion
     */
    public Delete(TransactionId t, OpIterator child) {
        // some code goes here
    	this.t=t;
    	this.child=child;
    	this.isDeleted=false;
    	//a tupleDesc table that will be needed
    	Type[] types = new Type[] {Type.INT_TYPE};
    	table = new TupleDesc(types);

    }

    public TupleDesc getTupleDesc() {
        // some code goes here
        return table;
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	super.open();
    	this.child.open();
    	this.isDeleted=false;
    }

    public void close() {
        // some code goes here
    	super.close();
    	this.child.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	child.rewind();
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be accessed via the
     * Database.getBufferPool() method.
     * 
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
    	if (isDeleted) return null;//if the tuples read from child are deleted we have nothing to do
    	int counter=0;
    	//if the child contains non deleted tuples we loop over these tuples and we increment the counter
    	for(;child.hasNext();counter++) {
    		Tuple currentTuple=child.next();//next tuple to delete
    		try 
    		{
        		Database.getBufferPool().deleteTuple(t, currentTuple);   //delete the tuple using bufferpool deleteTuple method 			
    		}
    		catch (IOException e)
    		{
    			throw new DbException("IO Exception: Error on this tuple deletion");
    		}
    		
    	}
    	//after deleting the tuples read from child we turn isDeleted to true
    	isDeleted=true;
    	Tuple deletedRecordsTuple=new Tuple(this.getTupleDesc());//we create a 1-field tuple to store the number of deleted tuples
    	deletedRecordsTuple.setField(0,new IntField(counter));// we set the field to the number of deleted tuples
        return deletedRecordsTuple;
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
