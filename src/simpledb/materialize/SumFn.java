package simpledb.materialize;

import java.util.HashSet;
import java.util.Set;

import simpledb.query.*;

/**
 * The <i>count</i> aggregation function.
 * @author Edward Sciore
 */
public class SumFn implements AggregationFn {
   private String fldname;
   private int sum;
   private boolean isDistinct;
   private Set<Integer> vals = new HashSet<Integer>();
   /**
    * Create a sum aggregation function for the specified field.
    * @param fldname the name of the aggregated field
    */
   public SumFn(String fldname, boolean isDistinct) {
      this.fldname = fldname;
      this.isDistinct = isDistinct;
   }
   
   /**
    * Start a new count.
    * Since SimpleDB does not support null values,
    * every record will be counted,
    * regardless of the field.
    * The current count is thus set to 1.
    * @see simpledb.materialize.AggregationFn#processFirst(simpledb.query.Scan)
    */
   public void processFirst(Scan s) {
      sum = s.getInt(fldname);
      if (this.isDistinct) vals.add(sum);
   }
   
   /**
    * Since SimpleDB does not support null values,
    * this method always increments the count,
    * regardless of the field.
    * @see simpledb.materialize.AggregationFn#processNext(simpledb.query.Scan)
    */
   public void processNext(Scan s) {
	   int value = s.getInt(fldname);
	   
	  if (!this.isDistinct || !vals.contains(value)) {
		  sum+=value;
	      vals.add(value);
	  }
   }
   
   /**
    * Return the field's name, prepended by "countof".
    * @see simpledb.materialize.AggregationFn#fieldName()
    */
   public String fieldName() {
	  if (this.isDistinct) {
		  return "sum(DISTINCT " + fldname + ")";
	  }
      return "sum(" + fldname + ")";
   }
   
   /**
    * Return the current count.
    * @see simpledb.materialize.AggregationFn#value()
    */
   public Constant value() {
      return new Constant(sum);
   }
   
   public String field() {
	   return this.fldname;
   }
}
