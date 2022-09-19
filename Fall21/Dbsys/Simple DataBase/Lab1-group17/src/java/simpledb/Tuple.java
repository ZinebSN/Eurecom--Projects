package simpledb;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    
    //creating RecordId, TupleDesc and Field objects
    private RecordId my_record;
    private TupleDesc my_tupleDesc;                   
    private Field[] my_field;
    
    public Tuple(TupleDesc td) {
        // some code goes here
    	//check if td is valid TupleDesc and contains at least one field
    	assert(td instanceof TupleDesc);
    	assert(td.numFields()>0);
    	//generate the Tuple
    	this.my_tupleDesc=td;
    	this.my_field=new Field[td.numFields()];
    	
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
    	return my_tupleDesc;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
    	return my_record;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
    	this.my_record=rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
    	
    	//checking the validity of index
    	if (0 <=i & i<my_field.length) {
    	//assigning new value to field
    	    my_field[i]=f;
    	}
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
    	//checking if the index is valid
    	assert (i>=0 & i<my_field.length);
    	// return the value of the corresponding field
        return my_field[i];
    	
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     *
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        // some code goes here
        
    	//creating a string object
        String my_string=new String();
        
        //Concatenating all the field information
    	for(int i=0;i<my_field.length-1;i++)
    	{
    		my_string=my_string+my_field[i]; //adding field info
    		my_string=my_string+"\t"; //formatting
    	}
    	//adding last field and move to the next line
    	my_string+=my_field[my_field.length-1]+"\n";
    	//returning the concatenated string
        return my_string;
    }

    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
    	
    	//this iterates all over the fields
    	 return Arrays.asList(my_field).iterator();
    }

    /**
     * reset the TupleDesc of this tuple (only affecting the TupleDesc)
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        // some code goes here
    	
    	//resetting the tupleDesc variable to a new value
    	this.my_tupleDesc=td;
    	this.my_field=new Field[td.numFields()];
    }
}
