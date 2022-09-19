package simpledb;

import java.util.*;

/**
 * Filter is an operator that implements a relational select.
 */
public class Filter extends Operator {
	
	// Creating class attributes
	private Predicate predicate;
    private OpIterator opChild;
	

    private static final long serialVersionUID = 1L;

    /**
     * Constructor accepts a predicate to apply and a child operator to read
     * tuples to filter from.
     * 
     * @param p
     *            The predicate to filter tuples with
     * @param child
     *            The child operator
     */
    
    
    public Filter(Predicate p, OpIterator child) {
        // some code goes here
    	
    	//initializing the class attributes
    	this.predicate = p;
        this.opChild = child;
    }

    public Predicate getPredicate() {
        // some code goes here
    	
    	// returning the predicate value
    	return predicate;
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
    	
    	// returning the OpIterator's tupledesc
    	 return opChild.getTupleDesc();
    }

    public void open() throws DbException, NoSuchElementException,
            TransactionAbortedException {
    	// some code goes here
    	
    	// opening OpIterator opChild
    	 opChild.open();
         super.open();
    }

    public void close() {
        // some code goes here

    	// closing OpIterator opChild
    	super.close();
        opChild.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	
    	// rewinding OpIterator opChild
    	 opChild.rewind();
    }

    /**
     * AbstractDbIterator.readNext implementation. Iterates over tuples from the
     * child operator, applying the predicate to them and returning those that
     * pass the predicate (i.e. for which the Predicate.filter() returns true.)
     * 
     * @return The next tuple that passes the filter, or null if there are no
     *         more tuples
     * @see Predicate#filter
     */
    protected Tuple fetchNext() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // some code goes here
    	
    	Tuple tup1;
    	boolean filtered;
    	while (opChild.hasNext()) {
            tup1 = opChild.next(); //iterating over all the tuples
            filtered = predicate.filter(tup1);
            if (filtered != false)
            {
            	return tup1; //returning the tuple if passes the predicate
            }
        }
    	
		return null; // if no more tuples
    	
    }

    @Override
    public OpIterator[] getChildren() {
        // some code goes here
    	 return new OpIterator[] { this.opChild }; 
    }

    @Override
    public void setChildren(OpIterator[] children) {
        // some code goes here
    	 this.opChild = children[0];
    }

}
