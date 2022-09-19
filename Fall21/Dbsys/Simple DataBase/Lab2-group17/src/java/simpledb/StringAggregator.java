package simpledb;
import java.util.ArrayList;
import java.util.List;
/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;
    private int gbfield;
	private Type gbfieldtype;
	private int afield;
	private Op what;
    private List<Tuple> tpList = new ArrayList<Tuple>();
    private OpIterator obIt;
	private List<Tuple> aggList = new ArrayList<Tuple>();
    private int tpNum;
    private TupleDesc td;
	private TupleDesc tdInt;
    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        this.gbfield = gbfield;
    	this.gbfieldtype = gbfieldtype;
    	this.afield = afield;
    	this.what = what;
        this.tpNum = 0;
    	Type [] typeAr = new Type[2];
    	typeAr[0] = Type.INT_TYPE;
    	typeAr[1] = Type.INT_TYPE;
    	this.tdInt = new TupleDesc(typeAr);
        // some code goes here
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void mergeTupleIntoGroup(Tuple tup) {
        
    	int i = 0;
    	for(i = 0; i < tpNum; i++){
    		Tuple tempt = tpList.get(i);
    		if(tempt.getField(gbfield).equals(tup.getField(gbfield))){
    			tpList.add(i, tup);
    			tpNum ++;
    			break;
    		}
    	}
    	if(i == tpNum){
    		tpList.add(i, tup);
			tpNum ++;
			//this.td = tup.getTupleDesc();
	    	Type [] typeAr = new Type[2];
	    	typeAr[0] = gbfieldtype;
	    	typeAr[1] = tup.getTupleDesc().getFieldType(afield);
	    	this.td = new TupleDesc(typeAr);
    	}
        // some code goes here
    }
    public void aggCount(){
    	aggList = new ArrayList<Tuple>();
    	int groupVal;
    	int aggNum = 0;
    	Tuple aggTp = new Tuple(tdInt);
		groupVal = ((IntField)(tpList.get(0).getField(gbfield))).getValue();
		aggNum ++;
		
		int i = 1;
		for(; i < tpNum; i++){
			int gbvalue = ((IntField)(tpList.get(i).getField(gbfield))).getValue();
			if(groupVal != gbvalue){
				aggTp.setField(gbfield, new IntField(groupVal));
	    		aggTp.setField(afield, new IntField(aggNum));
	    		aggList.add(aggTp);
	    		aggTp = new Tuple(tdInt);
				groupVal = gbvalue;
				aggNum = 1;
			}else{
				aggNum ++;
			}
		}
		aggTp.setField(gbfield, new IntField(groupVal));
		aggTp.setField(afield, new IntField(aggNum));
		aggList.add(aggTp);
    }

    /**
     * Create a OpIterator over group aggregate results.
     *
     * @return a OpIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public OpIterator iterator() {
        aggList = new ArrayList<Tuple>();
    	//int groupVal;
    	//int aggregateVal;
    	//Tuple aggTp = new Tuple(td);
    	switch(what){
    	case COUNT: aggCount();
    		break;
    	default:
    		System.out.println("Unsupported operation\n");
    	}
    	obIt = new TupleIterator(tdInt, aggList);
    	return obIt;
        // some code goes here
}
}
