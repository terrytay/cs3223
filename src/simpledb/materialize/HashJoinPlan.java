package simpledb.materialize;

import simpledb.plan.Plan;
import simpledb.query.Scan;
import simpledb.record.Schema;
import simpledb.record.TableScan;
import simpledb.tx.Transaction;

public class HashJoinPlan implements Plan {
    private String jfield1, jfield2;
    private Plan p1, p2;
    private Schema sch = new Schema();
    private int partitionSize;

    public HashJoinPlan(Plan p1, Plan p2, String jfield1, String jfield2, Transaction tx) {
        this.p1 = p1;
        this.p2 = p2;
        this.jfield1 = jfield1;
        this.jfield2 = jfield2;
        sch.addAll(p1.schema());
        sch.addAll(p2.schema());
        this.partitionSize = tx.availableBuffs() - 1;
    }

    public Scan open() {
 	   System.out.println("Hash join plan activated.");
        Scan scan1 = p1.open();
        TableScan tableScan2 = (TableScan) p2.open();
        return new HashJoinScan(scan1, tableScan2, jfield1, jfield2, partitionSize, p1.schema().fields());
    }

    public int blocksAccessed() {
        return 3 * (p1.blocksAccessed() + p2.blocksAccessed());
    }

    public int recordsOutput() {
        return p1.recordsOutput();
    }

    public int distinctValues(String fldname) {
        if (p1.schema().hasField(fldname))
            return p1.distinctValues(fldname);
        else
            return p2.distinctValues(fldname);
    }

    public Schema schema() {
        return sch;
    }
}
