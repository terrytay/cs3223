package simpledb.parse;

import java.util.*;

import simpledb.materialize.*;
import simpledb.query.*;
import simpledb.record.*;

/**
 * The SimpleDB parser.
 * @author Edward Sciore
 */
public class Parser {
   private Lexer lex;
   
   public Parser(String s) {
      lex = new Lexer(s);
   }
   
// Methods for parsing predicates, terms, expressions, constants, and fields
   
   public String field() {
      return lex.eatId();
   }
   
   public Constant constant() {
      if (lex.matchStringConstant())
         return new Constant(lex.eatStringConstant());
      else
         return new Constant(lex.eatIntConstant());
   }
   
   public Expression expression() {
      if (lex.matchId())
         return new Expression(field());
      else
         return new Expression(constant());
   }
   
   public Term term() {
      Expression lhs = expression();
      
      String cmp = lex.eatOp();
      
      Expression rhs = expression();
      return new Term(lhs, rhs, cmp);
   }
   
   public Predicate predicate() {
      Predicate pred = new Predicate(term());
      if (lex.matchKeyword("and")) {
         lex.eatKeyword("and");
         pred.conjoinWith(predicate());
      }
      return pred;
   }
   
   private List<String> orderList() {
	   List<String> L = new ArrayList<String>();
	   String name = field();
	   String by;
	   try {
		   by = field();
	   } catch (Exception e) {
		   by = "asc";
	   }
	   L.add(name+"-"+by);
	      if (lex.matchDelim(',')) {
	         lex.eatDelim(',');
	         L.addAll(orderList());
	      }
	      return L;
	   }
   
   private List<String> groupList() {
	   List<String> L = new ArrayList<String>();
	   String name = field();
	   String by;
	   try {
		   by = field();
	   } catch (Exception e) {
		   by = "asc";
	   }
	   L.add(name+"-"+by);
	      if (lex.matchDelim(',')) {
	         lex.eatDelim(',');
	         L.addAll(groupList());
	      }
	      return L;
	   }
   
// Methods for parsing queries
   
   public QueryData query() {
      lex.eatKeyword("select");
      
      boolean distinct = isDistinct();
	  
      // aggregates and selection
      List<AggregationFn> aggregates = new ArrayList<>();
      List<String> selectedFields = new ArrayList<>();
      

      while (true) {
    	  if (lex.matchAggregate()) {
    		  AggregationFn aggFn = newAggregationFn();
    		  selectedFields.add(aggFn.fieldName());
    		  aggregates.add(aggFn);
    	  } else if (lex.matchDelim('*')) {
			  lex.eatDelim('*');
			  selectedFields.add("*");
    	  } else if (lex.matchId()){
    		  selectedFields.add(field());
    	  } else {
    		  throw new Error("select statements error");
    	  }
    	  
    	  if (!lex.matchDelim(',')) {
    		  break;
    	  }
    	  lex.eatDelim(',');
      }
      
      // relations
      lex.eatKeyword("from");
      Collection<String> tables = tableList();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
            
      // order by
	  List<String> orders = new ArrayList<String>();
      if (lex.matchKeyword("order")) {
    	  lex.eatKeyword("order");
    	  if (lex.matchKeyword("by")) {
    		  lex.eatKeyword("by");
    		  orders = orderList();
    		  
    	  }
      }
      
      // group by
	  List<String> groups = new ArrayList<String>();
      if (lex.matchKeyword("group")) {
    	  lex.eatKeyword("group");
    	  if (lex.matchKeyword("by")) {
    		  lex.eatKeyword("by");
    		  groups = groupList();
    		  
    	  }
      }
      
      return new QueryData(selectedFields, tables, pred, orders, aggregates, groups, distinct);
   }
   
   private boolean isDistinct() {
	  if (lex.matchKeyword("distinct")) {
		  lex.eatKeyword("distinct");
		  return true;
	  }
	  return false;
   }
   
   private AggregationFn newAggregationFn() {
	   String aggregate = lex.eatAggregate();
	   List<String> fields = new ArrayList<String>();
	   boolean distinct = false;
	   try {
		   lex.eatDelim('(');
		   distinct = isDistinct();
		   fields = selectList();
		   lex.eatDelim(')');
	   } catch (Exception e) {
		   throw new Error("aggregation fn error. template should be e.g. sum(eid)", e);
	   }
	   
	   if (fields.size() != 1) throw new Error("aggregation fn error. system accepts only one arg in aggregation fn");
	   
	   // TODO: add in more aggregation fns
	   switch (aggregate) {
	   case "count":
		   return new CountFn(fields.get(0), distinct);
	   case "max":
		   return new MaxFn(fields.get(0), distinct);
	   case "avg":
		   return new AvgFn(fields.get(0), distinct);
	   case "min":
		   return new MinFn(fields.get(0), distinct);
	   case "sum":
		   return new SumFn(fields.get(0), distinct);
	   default:
		   throw new Error("system error in aggregation fn");
	   }
   }
   
   private List<String> selectList() {
      List<String> L = new ArrayList<String>();
      if (lex.matchDelim('*')) {
    	  lex.eatDelim('*');
    	  L.add("*");
      } else {
	      L.add(field());
	      if (lex.matchDelim(',')) {
	         lex.eatDelim(',');
	         L.addAll(selectList());
	      }
      }
      return L;
   }
   
   private Collection<String> tableList() {
      Collection<String> L = new ArrayList<String>();
      L.add(lex.eatId());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(tableList());
      }
      return L;
   }
   
// Methods for parsing the various update commands
   
   public Object updateCmd() {
      if (lex.matchKeyword("insert"))
         return insert();
      else if (lex.matchKeyword("delete"))
         return delete();
      else if (lex.matchKeyword("update"))
         return modify();
      else
         return create();
   }
   
   private Object create() {
      lex.eatKeyword("create");
      if (lex.matchKeyword("table"))
         return createTable();
      else if (lex.matchKeyword("view"))
         return createView();
      else
         return createIndex();
   }
   
// Method for parsing delete commands
   
   public DeleteData delete() {
      lex.eatKeyword("delete");
      lex.eatKeyword("from");
      String tblname = lex.eatId();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
      return new DeleteData(tblname, pred);
   }
   
// Methods for parsing insert commands
   
   public InsertData insert() {
      lex.eatKeyword("insert");
      lex.eatKeyword("into");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      List<String> flds = fieldList();
      lex.eatDelim(')');
      lex.eatKeyword("values");
      lex.eatDelim('(');
      List<Constant> vals = constList();
      lex.eatDelim(')');
      return new InsertData(tblname, flds, vals);
   }
   
   private List<String> fieldList() {
      List<String> L = new ArrayList<String>();
      L.add(field());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(fieldList());
      }
      return L;
   }
   
   private List<Constant> constList() {
      List<Constant> L = new ArrayList<Constant>();
      L.add(constant());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(constList());
      }
      return L;
   }
   
// Method for parsing modify commands
   
   public ModifyData modify() {
      lex.eatKeyword("update");
      String tblname = lex.eatId();
      lex.eatKeyword("set");
      String fldname = field();
      lex.eatDelim('=');
      Expression newval = expression();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
      return new ModifyData(tblname, fldname, newval, pred);
   }
   
// Method for parsing create table commands
   
   public CreateTableData createTable() {
      lex.eatKeyword("table");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      Schema sch = fieldDefs();
      lex.eatDelim(')');
      return new CreateTableData(tblname, sch);
   }
   
   private Schema fieldDefs() {
      Schema schema = fieldDef();
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         Schema schema2 = fieldDefs();
         schema.addAll(schema2);
      }
      return schema;
   }
   
   private Schema fieldDef() {
      String fldname = field();
      return fieldType(fldname);
   }
   
   private Schema fieldType(String fldname) {
      Schema schema = new Schema();
      if (lex.matchKeyword("int")) {
         lex.eatKeyword("int");
         schema.addIntField(fldname);
      }
      else {
         lex.eatKeyword("varchar");
         lex.eatDelim('(');
         int strLen = lex.eatIntConstant();
         lex.eatDelim(')');
         schema.addStringField(fldname, strLen);
      }
      return schema;
   }
   
// Method for parsing create view commands
   
   public CreateViewData createView() {
      lex.eatKeyword("view");
      String viewname = lex.eatId();
      lex.eatKeyword("as");
      QueryData qd = query();
      return new CreateViewData(viewname, qd);
   }
   
   
//  Method for parsing create index commands
   
   public CreateIndexData createIndex() {
	  String idxmethod = "btree";
	  
      lex.eatKeyword("index");
      String idxname = lex.eatId();
      lex.eatKeyword("on");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      String fldname = field();
      lex.eatDelim(')');
      if (lex.matchKeyword("using")) {
          lex.eatKeyword("using");
          idxmethod = field(); 
      }

      return new CreateIndexData(idxname, tblname, fldname, idxmethod);
   }
}

