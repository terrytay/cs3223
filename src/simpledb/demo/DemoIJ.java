package simpledb.demo;

import java.sql.Types;
import java.util.Scanner;

import simpledb.plan.Plan;
import simpledb.plan.Planner;
import simpledb.query.Scan;
import simpledb.record.Schema;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class DemoIJ {
	public static void main(String[] args) {
	      Scanner sc = new Scanner(System.in);
//	      System.out.println("Connect> ");
//	      String s = sc.nextLine();
//	      Driver d = (s.contains("//")) ? new NetworkDriver() : new EmbeddedDriver();
	      

	      try {
	    	 SimpleDB db = new SimpleDB("demodb");
	    	 Transaction tx = db.newTx();
	    	 Planner planner = db.planner();
	    	 
	         System.out.print("\nSQL> ");
	         while (sc.hasNextLine()) {
	            // process one line of input
	            String cmd = sc.nextLine().trim();
	            if (cmd.startsWith("exit"))
	               break;
	            else if (cmd.startsWith("select"))
	               doQuery(planner, tx, cmd);
	            else
	               doUpdate(planner, tx, cmd);
	            System.out.print("\nSQL> ");
	         }
	      }
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	      sc.close();
	   }

	   private static void doQuery(Planner planner, Transaction tx, String cmd) {
	      try {
	    	 Plan p = planner.createQueryPlan(cmd, tx);
	    	 Scan s = p.open();
	    	 
	    	 Schema schema = p.schema();
	         int numcols = schema.fields().size();
	         int totalwidth = 0;

	         // print header
	         for(int i=1; i<=numcols; i++) {
	            String fldname = schema.fields().get(i-1);
	            int fldtype = schema.type(fldname);
	            String delim = "";
		           if (fldtype == Types.INTEGER) {
		              int len = 13 - fldname.length();
		              for(int k = 0; k < len; k++) {
		            	  delim += " ";
		              }
		              delim += "|";
		           }
		           else {
		              int len = 30-fldname.length();
		              for(int k = 0; k < len; k++) {
		            	  delim += " ";
		              }
		              delim += "|";
		           }
	           
	            int width = schema.fields().get(i-1).length();
	            totalwidth += width + delim.length();
	            System.out.format("%s" + delim, fldname);
	         }
	         System.out.println();
	         for(int i=0; i<totalwidth; i++)
	            System.out.print("-");
	         System.out.println();

	         // print records
	         while(s.next()) {
	            for (int i=1; i<=numcols; i++) {
	            	String fldname = schema.fields().get(i-1);
	            	int fldtype = schema.type(fldname);
	            	String delim = "";
		           if (fldtype == Types.INTEGER) {
		              int ival = s.getInt(fldname);
		              int len = 13 - String.valueOf(ival).length();
		              
		              for(int k = 0; k < len; k++) {
		            	  delim += " ";
		              }
		              delim += "|";
		              
		              System.out.format("%d" + delim, ival);
		           }
		           else {
		              String sval = s.getString(fldname);
		              if (sval.length() > 30) {
		            	  sval = sval.substring(0, 27) + "...";
		              }
		              int len = 30-sval.length();
		              for(int k = 0; k < len; k++) {
		            	  delim += " ";
		              }
		              delim += "|";
		              System.out.format("%s" + delim, sval);
		           }
	               
	            }
	            System.out.println();
	         }
	         s.close();
	         tx.commit();
	      }
	      catch (Exception e) {
	         System.out.println("SQL Exception: " + e.getMessage());
	      }
	   }

	   private static void doUpdate(Planner planner, Transaction tx, String cmd) {
	      try {
	         int howmany = planner.executeUpdate(cmd, tx);
	         tx.commit();
	         System.out.println(howmany + " records processed");
	      }
	      catch (Exception e) {
	         System.out.println("SQL Exception: " + e.getMessage());
	      }
	   }
}
