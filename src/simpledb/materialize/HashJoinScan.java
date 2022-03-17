package simpledb.materialize;

import simpledb.record.TableScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simpledb.query.*;

/**
 * The scan class corresponding to the hashjoin relational
 * algebra operator.
 * The LHS relation is first partitioned based on the hash
 * of the join column using buffers - 1 available.
 * After which, the RHS is scanned and look for hash mapping
 * with the partitions. Upon hit, the record will be retrieved. 
 * @author Terry Tay
 */
public class HashJoinScan implements Scan {
   private Scan lhs;
   private String joinfield1, joinfield2;
   private TableScan rhs;  
   private int partitionSize;
   private List<String> attrs;
   private Map<String, ArrayList<Map<String, Constant>>> ht;  
   
   private int currentRecord = -1;
   private boolean inRecord = false;
   private ArrayList<Map<String, Constant>> specificLHSPartition;
   
   /**
    * Creates an index join scan for the specified LHS scan and 
    * RHS index.
    * @param lhs the LHS scan
    * @param idx the RHS index
    * @param joinfield the LHS field used for joining
    * @param rhs the RHS scan
    */
   public HashJoinScan(Scan lhs,  TableScan rhs, String joinfield1, String joinfield2, int partitionSize, List<String> attrs) {
      this.lhs = lhs;
      this.joinfield1 = joinfield1;
      this.joinfield2 = joinfield2;
      this.rhs = rhs;
      this.partitionSize = partitionSize;
      this.attrs = attrs;
      
      
      beforeFirst();
   }
   
   public void beforeFirst() {
	   partitionLHS();
	   rhs.beforeFirst();
   }
   
   /*
    * First, set the LHS to before the first record of the relation.
    * Next, get the partition number by getting the hash of the joinfield value
    * and is limited by the partition size.
    * Add the record to the hash table.
    */
   private void partitionLHS() {
	   lhs.beforeFirst();
	   
	   ht = new HashMap<>();
	   
	   while (lhs.next()) {
		   int partitionNumber = lhs.getVal(joinfield1).hashCode() % partitionSize;
		   
		   Map<String, Constant> record = new HashMap<>();
		   
		   for (String fldname : attrs) {
			   record.put(fldname, lhs.getVal(fldname));
		   }
		   
		   ht.computeIfAbsent(String.valueOf(partitionNumber), k -> new ArrayList<Map<String, Constant>>()).add(record);
	   }
	   
   }
   
   public boolean next() {
	   // This will only run if a RHS records has matched a partition in the hash table.
	   // If the current RHS record has not matched with all the records in the current partition,
	   // continue matching it by returning true
	   if (this.inRecord && ++this.currentRecord < this.specificLHSPartition.size()) {
		   return true;
	   }
	   
	   // For each RHS record,
	   // check if the joinfield's hash matches any partition
	   // If do, then extract the partition content to specificLHSPartition
	   // Set inRecord to true so the above 'if statement' will run later.
	   // Return true
	   if (rhs.next()) {
		   this.inRecord = false;
		   this.currentRecord = 0;
		   
		   int partitionNumber = rhs.getVal(joinfield2).hashCode() % partitionSize;
		   
		   if (ht.keySet().contains(String.valueOf(partitionNumber))) {
			   this.specificLHSPartition = ht.get(String.valueOf(partitionNumber));
			   this.inRecord = true;
			   return true;
		   }
	   }
	   
	   // Return false when everything is done
	   return false;
   }
   
   /**
    * Returns the integer value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public int getInt(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getInt(fldname);
      else  
    	  return this.specificLHSPartition.get(this.currentRecord).get(fldname).asInt();
   }
   
   /**
    * Returns the Constant value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public Constant getVal(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getVal(fldname);
      else
    	  return this.specificLHSPartition.get(this.currentRecord).get(fldname);
   }
   
   /**
    * Returns the string value of the specified field.
    * @see simpledb.query.Scan#getVal(java.lang.String)
    */
   public String getString(String fldname) {
      if (rhs.hasField(fldname))
         return rhs.getString(fldname);
      else
    	 return this.specificLHSPartition.get(this.currentRecord).get(fldname).asString();
   }
   
   /** Returns true if the field is in the schema.
     * @see simpledb.query.Scan#hasField(java.lang.String)
     */
   public boolean hasField(String fldname) {
      return rhs.hasField(fldname) || lhs.hasField(fldname);
   }
   
   /**
    * Closes the scan by closing its LHS scan and its RHS index.
    * @see simpledb.query.Scan#close()
    */
   public void close() {
      lhs.close();
      rhs.close();
   }

}
