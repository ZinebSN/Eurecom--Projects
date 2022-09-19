package simpledb;


import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {
	public ArrayList<TDItem> tupleDesc;
    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }

    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
    	Iterator<TDItem> it=tupleDesc.iterator();
        return it;

    }

    private static final long serialVersionUID = 1L;
 
    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	//Check if it contains at least one entry
    	assert(typeAr.length>0);
        tupleDesc = new ArrayList<TDItem>(typeAr.length);
        for (int j = 0; j < typeAr.length; j++) {
        	TDItem newItem=new TDItem(typeAr[j],fieldAr[j]);
            tupleDesc.add(newItem);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        this(typeAr, new String[typeAr.length]);
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tupleDesc.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
    	try{
    		TDItem my_item=tupleDesc.get(i);
    		return my_item.fieldName;
    	}catch (Exception e) {
    		throw new NoSuchElementException("invalid field reference");
    	}

    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
    	try{
    		TDItem my_item=tupleDesc.get(i);
    		return my_item.fieldType;
    	}catch (Exception e) {
    		throw new NoSuchElementException("invalid field reference");
    	}
       

    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
	    for (int i = 0; i < tupleDesc.size(); i++) {
		     if(tupleDesc.get(i).fieldName!=null && tupleDesc.get(i).fieldName.equals(name) ) {
		         return i;
		     }
	    }
	    throw new NoSuchElementException("no such field name");
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for (int i = 0; i < tupleDesc.size(); i++) {
        	//add size of current tuple(corresponding to tupleDesc[i]
            size += tupleDesc.get(i).fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
               
    	Type[] mergedTypes=new Type[td1.numFields()+td2.numFields()];
    	String[] mergedNames=new String[td1.numFields()+td2.numFields()];

        for (int i = 0; i < td1.numFields(); i++) {
            mergedTypes[i] = td1.getFieldType(i);
            mergedNames[i] = td1.getFieldName(i);
        }

        for (int i = td1.numFields(); i < td1.numFields()+td2.numFields(); i++) {
            mergedTypes[i] = td2.getFieldType(i-td1.numFields());
            mergedNames[i] = td2.getFieldName(i-td1.numFields());
        	
        }
        return new TupleDesc(mergedTypes, mergedNames);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
    	//check the constraints
        if (o==null || !(o instanceof TupleDesc)) {
        	return false;
        }
        TupleDesc newO= (TupleDesc) o;
        
        if (this.getSize() != newO.getSize()) {
        	return false;
        }
        for (int i=0;i<this.numFields();i++) {
        	if(newO.getSize()!=this.getSize()) 
        		return false;
        	
        }
       
        return true;
    }
    

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        //throw new UnsupportedOperationException("unimplemented");
    	return this.toString().hashCode();
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        String values = new String();
        for (int i = 0; i < this.numFields(); i++) {
            values += (tupleDesc.get(i).toString() + ", ");
        }
        return values;
    }
}