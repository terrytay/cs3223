package simpledb.parse;

import java.util.*;

import simpledb.materialize.*;
import simpledb.query.*;

/**
 * Data for the SQL <i>select</i> statement.
 * @author Edward Sciore
 */
public class QueryData {
   private List<String> fields;
   private Collection<String> tables;
   private Predicate pred;
   private List<String> orders;
   private List<AggregationFn> aggregates;
   private List<String> aggregatesFields;
   private List<String> groups;
   private boolean isDistinct;
   
   /**
    * Saves the field and table list and predicate.
    */
   public QueryData(List<String> fields, Collection<String> tables, Predicate pred, List<String> orders, List<AggregationFn> aggregates, List<String> groups, boolean isDistinct) {
      this.fields = fields;
      this.tables = tables;
      this.pred = pred;
      this.orders = orders;
      this.aggregates = aggregates;
      
      this.aggregatesFields = new ArrayList<String>();
      
      for (AggregationFn fn : aggregates) {
    	  aggregatesFields.add(fn.field());
      }
      
      this.groups = groups;
      this.isDistinct = isDistinct;
   }
   
   public boolean isDistinct() {
	   return isDistinct;
   }
   
   public List<String> groups() {
	   return groups;
   }
   public List<String> aggregatesFields() {
	   return aggregatesFields;
   }
   
   public List<AggregationFn> aggregates() {
	   return aggregates;
   }
   
   public List<String> orders() {
	   return orders;
   }
   
   /**
    * Returns the fields mentioned in the select clause.
    * @return a list of field names
    */
   public List<String> fields() {
      return fields;
   }
   
   /**
    * Returns the tables mentioned in the from clause.
    * @return a collection of table names
    */
   public Collection<String> tables() {
      return tables;
   }
   
   /**
    * Returns the predicate that describes which
    * records should be in the output table.
    * @return the query predicate
    */
   public Predicate pred() {
      return pred;
   }
   
   public String toString() {
      String result = "select ";
      for (String fldname : fields)
         result += fldname + ", ";
      result = result.substring(0, result.length()-2); //remove final comma
      result += " from ";
      for (String tblname : tables)
         result += tblname + ", ";
      result = result.substring(0, result.length()-2); //remove final comma
      String predstring = pred.toString();
      if (!predstring.equals(""))
         result += " where " + predstring;
      return result;
   }
}
