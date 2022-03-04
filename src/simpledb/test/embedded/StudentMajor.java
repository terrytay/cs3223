package simpledb.test.embedded;

import simpledb.plan.Plan;
import simpledb.plan.Planner;
import simpledb.query.Scan;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class StudentMajor {
   public static void main(String[] args) {
      String qry = "select sname, dname "
                 + "from DEPT, STUDENT "
                 + "where MajorId = DId";
      
      try {
    	  
          SimpleDB db = new SimpleDB("studentdb");
          Transaction tx  = db.newTx();
          Planner planner = db.planner();
          
          Plan p = planner.createQueryPlan(qry, tx);
          
          Scan s = p.open();
         
         System.out.println("Name\tMajor");
         while (s.next()) {
            String sname = s.getString("sname");
            String dname = s.getString("dname");
            System.out.println(sname + "\t" + dname);
         }
         s.close();
         tx.commit();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}

