package simpledb.materialize;

import simpledb.query.Constant;
import simpledb.query.Scan;
import simpledb.record.TableScan;

/**
 * The Scan class for the <i>mergejoin</i> operator.
 * @author Edward Sciore
 */
public class NestedJoinScan implements Scan {
   private Scan outer;
   private TableScan inner;
   private String fldname1, fldname2;


   /**
    * Create a mergejoin scan for the two underlying sorted scans.
    * @param s1 the LHS sorted scan
    * @param s2 the RHS sorted scan
    * @param fldname1 the LHS join field
    * @param fldname2 the RHS join field
    */
   public NestedJoinScan(Scan s1, TableScan s2, String fldname1, String fldname2) {
      this.outer = s1;
      this.inner = s2;
      this.fldname1 = fldname1.split("-")[0];
      this.fldname2 = fldname2.split("-")[0];

      beforeFirst();
   }

   /**
    * Close the scan by closing the two underlying scans.
    * @see simpledb.query.Scan#close()
    */
   public void close() {
      outer.close();
      inner.close();
   }

  /**
    * Position the scan before the first record,
    * by positioning each underlying scan before
    * their first records.
    * @see simpledb.query.Scan#beforeFirst()
    */
   public void beforeFirst() {
      outer.beforeFirst();
      outer.next();
      inner.beforeFirst();
   }

   /**
    * Move to the next record.  This is where the action is.
    * <P>
    * If the next RHS record has the same join value,
    * then move to it.
    * Otherwise, if the next LHS record has the same join value,
    * then reposition the RHS scan back to the first record
    * having that join value.
    * Otherwise, repeatedly move the scan having the smallest
    * value until a common join value is found.
    * When one of the scans runs out of records, return false.
    * @see simpledb.query.Scan#next()
    */
   public boolean next() {
	   boolean innerHasNext = inner.next();
	   while (innerHasNext) {
		   if (outer.getVal(fldname1).equals(inner.getVal(fldname2))) {
			   return true;
		   }
		   innerHasNext = inner.next();
		   if (!innerHasNext)
			   if (outer.next()) {
				   inner.beforeFirst();
				   innerHasNext = inner.next();
			   }
	   }
	   return false;
//	   while (inner.next()) {
//        if (outer.getVal(fldname1).equals(inner.getVal(fldname2)))
//            return true;
//	   }
//	   inner.beforeFirst();
//	   
//	   if (!outer.next()) {
//		   return false;
//	   }
//		inner.beforeFirst();
//		outer.next();
   }

   /**
    * Return the integer value of the specified field.
    * The value is obtained from whichever scan
    * contains the field.
    * @see simpledb.query.Scan#getInt(java.lang.String)
    */
   public int getInt(String fldname) {
      if (outer.hasField(fldname))
         return outer.getInt(fldname);
      else
         return inner.getInt(fldname);
   }

   /**
    * Return the string value of the specified field.
    * The value is obtained from whichever scan
    * contains the field.
    * @see simpledb.query.Scan#getString(java.lang.String)
    */
   public String getString(String fldname) {
      if (outer.hasField(fldname))
         return outer.getString(fldname);
      else
         return inner.getString(fldname);
   }

   /**
    * Return the value of the specified field.
    * The value is obtained from whichever scan
    * contains the field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public Constant getVal(String fldname) {
      if (outer.hasField(fldname))
         return outer.getVal(fldname);
      else
         return inner.getVal(fldname);
   }

   /**
    * Return true if the specified field is in
    * either of the underlying scans.
    * @see simpledb.query.Scan#hasField(java.lang.String)
    */
   public boolean hasField(String fldname) {
      return outer.hasField(fldname) || inner.hasField(fldname);
   }
}


