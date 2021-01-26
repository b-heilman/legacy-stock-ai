/**
 * 
 */
package myCB.CGS;

/**
 * This class gets passed around the CGS to keep all control variables in one
 * place.  This keeps everything simple.
 */
public class CGSData {

	private int maxChain;			// Max size of each chain
	private int minChain;			// Minimum size of each chain
	private int Chains;				// Number of chains in each genecode
	private int Span;					// How many past dates are being considered
	private int Ahead;				// How far ahead is being evaluated
	private Double Tolerance;	// The level at which something must be that accurate
	private Double Confidence;// The level at which the system counts it as a pick
	
	public CGSData(int minC, int maxC, int ch, int span, int ahead, Double tol, Double con){
		maxChain = maxC;
		minChain = minC;
		Chains = ch;
		Span = span;
		Ahead = ahead;
		Tolerance = tol;
		Confidence = con;
		}
	
	public int maxChainSize(){
		return maxChain;
		}
	
	public int minChainSize(){
		return minChain;
		}
	
	public int chainCount(){
		return Chains;
		}
	
	public int ahead(){
		return Ahead;
		}
	
	public int span(){
		return Span;
		}
	
	public Double tolerance(){
		return Tolerance;
		}
	
	public void setTolerance(Double t){
		Tolerance = t;
		}
	
	public Double confidence(){
		return Confidence;
		}
	}
