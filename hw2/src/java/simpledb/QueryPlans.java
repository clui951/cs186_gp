
package simpledb;

public class QueryPlans {

	public QueryPlans(){
	}

	//SELECT * FROM T1, T2 WHERE T1.column0 = T2.column0;
	public Operator queryOne(DbIterator t1, DbIterator t2) {
		// IMPLEMENTED
		JoinPredicate joinPred = new JoinPredicate(0, Predicate.Op.EQUALS, 0);
		Join joinInstance = new Join(joinPred, t1, t2);
		return joinInstance;
	}

	//SELECT * FROM T1, T2 WHERE T1. column0 > 1 AND T1.column1 = T2.column1;
	public Operator queryTwo(DbIterator t1, DbIterator t2) {
		// IMPLEMENTED
		t1 = new Filter(new Predicate(0, Predicate.Op.GREATER_THAN, new IntField(1)), t1);

		JoinPredicate joinPred = new JoinPredicate(1, Predicate.Op.EQUALS, 1);
		Join joinInstance = new Join(joinPred, t1, t2);
		return joinInstance;
	}

	//SELECT column0, MAX(column1) FROM T1 WHERE column2 > 1 GROUP BY column0;
	public Operator queryThree(DbIterator t1) {
		// IMPLEMENTED
		// column2 > 1
		t1 = new Filter(new Predicate(2, Predicate.Op.GREATER_THAN, new IntField(1)), t1);
		// group by column 0, max column1
		Operator table = new Aggregate(t1, 1, 0, Aggregator.Op.MAX);

		return table;
	}

	// SELECT ​​* FROM T1, T2 								
	// WHERE T1.column0 < (SELECT COUNT(*​​) FROM T3) 		FILTER (AGGREGATE)
	// AND T2.column0 = (SELECT AVG(column0) FROM T3) 		FILTER (AGGREGATE)
	// AND T1.column1 >= T2. column1						JOIN, PREDICATE
	// ORDER BY T1.column0 DESC; 							ORDER BY
	public Operator queryFour(DbIterator t1, DbIterator t2, DbIterator t3) throws TransactionAbortedException, DbException {
		// IMPLEMENT ME
		t1 = new Filter(new Predicate(0, Predicate.Op.LESS_THAN, new Aggregate(t3, 0, -1, Aggregator.Op.COUNT).fetchNext().getField(0)), t1);
		t3.rewind();
		t2 = new Filter(new Predicate(0, Predicate.Op.EQUALS, new Aggregate(t3, 0, -1, Aggregator.Op.AVG).fetchNext().getField(0)), t2);
		JoinPredicate pred = new JoinPredicate(1, Predicate.Op.GREATER_THAN_OR_EQ, 1);
		t1 = new Join(pred, t1, t2);
		OrderBy result = new OrderBy(0, false, t1);
		return result;
	}


}