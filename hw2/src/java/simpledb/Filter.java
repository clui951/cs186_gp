package simpledb;

import java.util.*;

/**
 * Filter is an operator that implements a relational select.
 */
public class Filter extends Operator {

    private static final long serialVersionUID = 1L;
    private Predicate pred;
    private DbIterator child;
    private ArrayList<Tuple> childTups = new ArrayList<Tuple>();
    private TupleDesc td;
    private Iterator<Tuple> it;


    /**
     * Constructor accepts a predicate to apply and a child operator to read
     * tuples to filter from.
     * 
     * @param p
     *            The predicate to filter tuples with
     * @param child
     *            The child operator
     */
    public Filter(Predicate p, DbIterator child) {
        // IMPLEMENTED
        this.pred = p;
        this.child = child;
        td = child.getTupleDesc();
    }

    public Predicate getPredicate() {
        // IMPLEMENTED
        return this.pred;
    }

    /**
     * Returns the schema of the operator.
     */
    public TupleDesc getTupleDesc() {
        // IMPLEMENTED
        return this.td;
    }

    public void open() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // IMPLEMENTED
        this.child.open();
        while (child.hasNext()) {
            childTups.add((Tuple) child.next());
        }
        it = childTups.iterator();
        super.open();
    }

    public void close() {
        // IMPLEMENTED
        super.close();
        it = null;
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // IMPLEMENTED
        it = childTups.iterator();
    }

    /**
     * AbstractDbIterator.readNext implementation. Iterates over tuples from the
     * child operator, applying the predicate to them and returning those that
     * pass the predicate (i.e. for which the Predicate.filter() returns true.)
     * 
     * @return The next tuple that passes the filter, or null if there are no
     *         more tuples
     * @see Predicate#filter
     */
    protected Tuple fetchNext() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // IMPLEMENTED
        while (it != null && it.hasNext()) {
            Tuple itNext = it.next();
            if (this.pred.filter(itNext) == true) {
                return itNext;
            } 
        }
        return null;
    }

    @Override
    public DbIterator[] getChildren() {
        return new DbIterator[] { this.child };
    }

    @Override
    public void setChildren(DbIterator[] children) {
        this.child = children[0];
    }

}
