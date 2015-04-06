package org.numenta.nupic.network;

import java.util.HashMap;
import java.util.Map;

import org.numenta.nupic.algorithms.CLAClassifier;
import org.numenta.nupic.algorithms.ClassifierResult;
import org.numenta.nupic.encoders.Encoder;
import org.numenta.nupic.util.MutableTuple;
import org.numenta.nupic.util.NamedTuple;

/**
 * <p>
 * Abstraction used within the Network API, to contain the significant return values of all {@link Layer}
 * inference participating algorithms.
 * </p>
 * Namely:
 * <ul>
 *      <li>Input Value</li>
 *      <li>Bucket Index</li>
 *      <li>SDR</li>
 *      <li>Previous SDR</li>
 *      <li>{@link ClassifierResult}</li>
 *      <li>anomalyScore</li>
 * </ul>
 * 
 * All of these fields are "optional", (meaning they depend on the configuration 
 * selected by the user and may not exist depending on the user's choice of "terminal"
 * point. A "Terminal" point is the end point in a chain of a {@code Layer}'s contained
 * algorithms. For instance, if the user does not include an {@link Encoder} in the 
 * {@link Layer} constructor, the slot containing the "Bucket Index" will be empty.
 * 
 * @author David Ray
 *
 */
public class ManualInput extends MutableTuple implements Inference {
    private int recordNum;
    /** Tuple = { Name, inputValue, bucketIndex, encoding } */
    private Map<String, NamedTuple> classifierInput;
    /** Holds one classifier for each field */
    private NamedTuple classifiers;
    private Object layerInput;
    private int[] sdr;
    private Map<String,ClassifierResult<Object>> classification;
    private double anomalyScore;
    
    
    /**
     * Constructs a new {@code ManualInput}
     */
    public ManualInput() {
        super(6);
    }
    
    /**
     * Sets the current record num associated with this {@code ManualInput}
     * instance
     * 
     * @param num   the current sequence number.
     * @return      this
     */
    public ManualInput recordNum(int num) {
        this.recordNum = num;
        return this;
    }
    
    /**
     * Returns the current record num associated with this {@code ManualInput}
     * instance
     * 
     * @return      the current sequence number
     */
    @Override
    public int getRecordNum() {
        return recordNum;
    }
    
    /**
     * Returns the {@link Map} used as input into the {@link CLAClassifier}
     * 
     * This mapping contains the name of the field being classified mapped
     * to a {@link NamedTuple} containing:
     * </p><p>
     * <ul>
     *      <li>name</li>
     *      <li>inputValue</li>
     *      <li>bucketIdx</li>
     *      <li>encoding</li>
     * </ul>
     * </p>
     * 
     * @return the current classifier input
     */
    @Override
    public Map<String, NamedTuple> getClassifierInput() {
        if(classifierInput == null) {
            classifierInput = new HashMap<String, NamedTuple>();
        }
        return classifierInput;
    }
    
    /**
     * Sets the current 
     * @param classifierInput
     * @return
     */
    ManualInput classifierInput(Map<String, NamedTuple> classifierInput) {
        this.classifierInput = classifierInput;
        return this;
    }
    
    /**
     * Sets the {@link NamedTuple} containing the classifiers used
     * for each particular input field.
     * 
     * @param tuple
     * @return
     */
    public ManualInput classifiers(NamedTuple tuple) {
        this.classifiers = tuple;
        return this;
    }
    
    /**
     * Returns a {@link NamedTuple} keyed to the input field
     * names, whose values are the {@link CLAClassifier} used 
     * to track the classification of a particular field
     */
    public NamedTuple getClassifiers() {
        return classifiers;
    }
    
    /**
     * Returns the most recent input object
     * 
     * @return      the input
     */
    @Override
    public Object getLayerInput() {
        return layerInput;
    }
    
    /**
     * Sets the input object to be used and returns 
     * this {@link ManualInput}
     * 
     * @param inputValue
     * @return
     */
    ManualInput layerInput(Object inputValue) {
        this.layerInput = inputValue;
        return this;
    }
    
    /**
     * Returns the <em>Sparse Distributed Representation</em> vector
     * which is the result of all algorithms in a series of algorithms
     * passed up the hierarchy.
     * 
     * @return
     */
    @Override
    public int[] getSDR() {
        return sdr;
    }
    
    /**
     * Inputs an sdr and returns this {@code ManualInput}
     * 
     * @param sdr
     * @return
     */
    ManualInput sdr(int[] sdr) {
        this.sdr = sdr;
        return this;
    }
    
    /**
     * Convenience method to provide an isolated copy of 
     * this {@link Inference}
     * 
     * @return
     */
    ManualInput copy() {
        ManualInput retVal = new ManualInput();
        retVal.classifierInput = new HashMap<String, NamedTuple>(this.classifierInput);
        retVal.classifiers = this.classifiers;
        retVal.layerInput = this.layerInput;
        retVal.sdr = this.sdr;
        retVal.classification = this.classification;
        retVal.anomalyScore = this.anomalyScore;
        
        return retVal;
    }
    
    /**
     * Returns the most recent {@link ClassifierResult}
     * 
     * @param fieldName
     * @return
     */
    @Override
    public ClassifierResult<Object> getClassification(String fieldName) {
        return classification.get(fieldName);
    }
    
    /**
     * Sets the specified field's last classifier computation and returns
     * this {@link Inference}
     * 
     * @param fieldName
     * @param classification
     * @return
     */
    ManualInput classify(String fieldName, ClassifierResult<Object> classification) {
        if(this.classification == null) {
            this.classification = new HashMap<String, ClassifierResult<Object>>();
        }
        this.classification.put(fieldName, classification);
        return this;
    }
    
    /**
     * Returns the most recent anomaly calculation.
     * @return
     */
    @Override
    public double getAnomalyScore() {
        return anomalyScore;
    }
    
    /**
     * Sets the current computed anomaly score and 
     * returns this {@link Inference}
     * 
     * @param d
     * @return
     */
    ManualInput anomalyScore(double d) {
        this.anomalyScore = d;
        return this;
    }

}
