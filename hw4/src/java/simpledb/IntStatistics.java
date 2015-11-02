package simpledb;

import simpledb.Predicate.Op;  

/** A class to represent statistics for a single integer-based field.
 */
public class IntStatistics {

    // You made add any other fields you think are necessary.

    private int numTuples;
    private int numDistinctTuples;
    private final boolean[] distinctInts;
    private int highVal;
    private int lowVal;
    private boolean valsSet;

    // TODO: IMPLEMENT ME

    /**
     * Create a new IntStatistic.
     * 
     * This IntStatistic should maintain a statistics about the integer values that it receives.
     * 
     * The integer values will be provided one-at-a-time through the "addValue()" function.
     */
    public IntStatistics(int bins) {
        numTuples = 0;
        numDistinctTuples = 0;
        distinctInts = new boolean[bins];
        valsSet = false;
        // TODO: IMPLEMENT ME
    }

    /**
     * Add a value to the set of values that you are tracking statistics for
     * @param v Value to add to the statistics
     */
    public void addValue(int v) {
        // TODO: IMPLEMENT ME
        if (!valsSet) {
            highVal = v;
            lowVal = v;
            valsSet = true;
        }
        else {
            if (v > highVal) {
                highVal = v;
            }
            if (v < lowVal) {
                lowVal = v;
            }
        }

        // hashes the value and keeps an estimate to the number of distinct tuples we've seen
        int index = (hashCode(v) % distinctInts.length + distinctInts.length) % distinctInts.length;
        if (distinctInts[index] == false) {
            distinctInts[index] = true;
            numDistinctTuples++;
        }

        numTuples++;
    }

    /**
     * Estimate the selectivity of a particular predicate and operand on this table.
     * 
     * For example, if "op" is "GREATER_THAN" and "v" is 5, 
     * return your estimate of the fraction of elements that are greater than 5.
     * 
     * @param op Operator
     * @param v Value
     * @return Predicted selectivity of this particular operator and value
     */
    public double estimateSelectivity(Predicate.Op op, int v) {
        // the approximate number of distinct tuples we've seen in total
        double numDistinct = ((double) numTuples) * numDistinctTuples / distinctInts.length;

        // TODO: IMPLEMENT ME
        if (op == Predicate.Op.NOT_EQUALS) {
            if (v < lowVal || v > highVal) {
                return 1.0;
            }
            else {
                // return 1.0 - 1.0/numDistinct;
                return 1 - estimateSelectivity(Predicate.Op.EQUALS, v);
            }
        }
        else if (op == Predicate.Op.EQUALS) {
            if (v < lowVal || v > highVal) {
                return 0.0;
            }
            else {
                return 1.0/numDistinct;
            }
        }
        else if (op == Predicate.Op.GREATER_THAN) {
            int numVals = highVal - lowVal;
            int subsetCardinality = highVal - v;
            if (numVals == 0 && v < highVal) { // Case v < all vals
                return 1.0;
            }
            else if (numVals == 0 && v >= highVal) { // Case v >= all vals
                return 0.0;
            }
            else if (v >= highVal) {
                return 0.0;
            }
            else if (v < lowVal) {
                return 1.0;
            }
            else {
                return ((double) subsetCardinality)/numVals; 
            }
        }
        else if (op == Predicate.Op.GREATER_THAN_OR_EQ) {
            return estimateSelectivity(Predicate.Op.EQUALS, v) + estimateSelectivity(Predicate.Op.GREATER_THAN, v);
            // int numVals = highVal - lowVal;
            // int subsetCardinality = highVal - v;
            // if (numVals == 0 && v <= highVal) { // Case v < all vals
            //     return 1.0;
            // }
            // else if (numVals == 0 && v > highVal) { // Case v >= all vals
            //     return 0.0;
            // }
            // else if (v > highVal) {
            //     return 0.0;
            // }
            // else if (v <= lowVal) {
            //     return 1.0;
            // }
            // else {
            //     return ((double) subsetCardinality)/numVals; 
            // }
        }
        else if (op == Predicate.Op.LESS_THAN) {
            int numVals = highVal - lowVal;
            int subsetCardinality = v - lowVal;
            if (numVals == 0 && v > lowVal) { // Case v < all vals
                return 1.0;
            }
            else if (numVals == 0 && v <= highVal) { // Case c >= all vals
                return 0.0;
            }
            else if (v > highVal) {
                return 1.0;
            }
            else if (v <= lowVal) {
                return 0.0;
            }
            else {
                return ((double) subsetCardinality)/numVals; 
            }
        }
        else if (op == Predicate.Op.LESS_THAN_OR_EQ) {
            return estimateSelectivity(Predicate.Op.EQUALS, v) + estimateSelectivity(Predicate.Op.LESS_THAN, v);
            // int numVals = highVal - lowVal;
            // int subsetCardinality = v - lowVal;
            // if (numVals == 0 && v >= lowVal) { // Case v < all vals
            //     return 1.0;
            // }
            // else if (numVals == 0 && v < highVal) { // Case c >= all vals
            //     return 0.0;
            // }
            // else if (v >= highVal) {
            //     return 1.0;
            // }
            // else if (v < lowVal) {
            //     return 0.0;
            // }
            // else {
            //     return ((double) subsetCardinality)/numVals; 
            // }
        }
        return -1.0; // Who knows what happens at this point
    }

    /**
     * Helper function to make a good hash value of an integer
     */
    static int hashCode(int v) {
        v ^= (v >>> 20) ^ (v >>> 12);
        return v ^ (v >>> 7) ^ (v >>> 4);
    }
}