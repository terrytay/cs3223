package simpledb.test.embedded;
import simpledb.plan.Planner;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class ChangeMajor {
   public static void main(String[] args) {

      try {
         SimpleDB db = new SimpleDB("studentdb");
         Transaction tx = db.newTx();
         Planner planner = db.planner();
          
         String cmd = "update STUDENT "
                    + "set MajorId=30 "
                    + "where SName = 'amy'";
         
         planner.executeUpdate(cmd, tx);
         
         tx.commit();
         
         System.out.println("Amy is now a drama major.");
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
