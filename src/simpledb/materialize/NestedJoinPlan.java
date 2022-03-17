package simpledb.materialize;

import simpledb.plan.Plan;
import simpledb.query.Scan;
import simpledb.record.Schema;
import simpledb.record.TableScan;

public class NestedJoinPlan implements Plan {
    private String jfield1, jfield2;
    private Plan p1, p2;
    private Schema sch = new Schema();

    public NestedJoinPlan(Plan p1, Plan p2, String jfield1, String jfield2) {
        this.p1 = p1;
        this.p2 = p2;
        this.jfield1 = jfield1;
        this.jfield2 = jfield2;
        sch.addAll(p1.schema());
        sch.addAll(p2.schema());
    }

    public Scan open() {
        Scan tableScan1 = p1.open();
        TableScan tableScan2 = (TableScan) p2.open();
        return new NestedJoinScan(tableScan1, tableScan2, jfield1, jfield2);
    }

    public int blocksAccessed() {
        int records1 = p1.recordsOutput();
        int records2 = p2.recordsOutput();
        Plan outer, inner;
        if (records1 < records2) {
            outer = p1;
            inner = p2;
        } else {
            outer = p2;
            inner = p1;
        }
        return outer.recordsOutput() + (outer.blocksAccessed()*inner.recordsOutput());
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
