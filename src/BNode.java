import java.util.ArrayList;

//SUBMIT
public class BNode implements BNodeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private final int t;
	private int numOfBlocks;
	private boolean isLeaf;
	private ArrayList<Block> blocksList;
	private ArrayList<BNode> childrenList;
	
	
	//        <<<<<<<<<<<<<<<<   Constructor    >>>>>>>>>>>>>>>>>>>

	/**
	 * Constructor for creating a node with a single child.<br>
	 * Useful for creating a new root.
	 */
	public BNode(int t, BNode firstChild) {
		this(t, false, 0);
		this.childrenList.add(firstChild);
	}

	/**
	 * Constructor for creating a <b>leaf</b> node with a single block.
	 */
	public BNode(int t, Block firstBlock) {
		this(t, true, 1);
		this.blocksList.add(firstBlock);
	}

	public BNode(int t, boolean isLeaf, int numOfBlocks) {
		this.t = t;
		this.isLeaf = isLeaf;
		this.numOfBlocks = numOfBlocks;
		this.blocksList = new ArrayList<Block>();
		this.childrenList = new ArrayList<BNode>();
	}

	// For testing purposes.
	public BNode(int t, int numOfBlocks, boolean isLeaf,
			ArrayList<Block> blocksList, ArrayList<BNode> childrenList) {
		this.t = t;
		this.numOfBlocks = numOfBlocks;
		this.isLeaf = isLeaf;
		this.blocksList = blocksList;
		this.childrenList = childrenList;
	}

	

	//        <<<<<<<<<<<<<<<<     Methods      >>>>>>>>>>>>>>>>>>>
	 
	
	@Override
	public int getT() {
		return t;
	}

	@Override
	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public ArrayList<Block> getBlocksList() {
		return blocksList;
	}

	@Override
	public ArrayList<BNode> getChildrenList() {
		return childrenList;
	}

	@Override
	public boolean isFull() {
		return numOfBlocks == 2 * t - 1;
	}

	@Override
	public boolean isMinSize() {
		return numOfBlocks == t - 1;
	}
	
	@Override
	public boolean isEmpty() {
		return numOfBlocks == 0;
	}
	
	@Override
	public int getBlockKeyAt(int indx) {
		return blocksList.get(indx).getKey();
	}
	
	@Override
	public Block getBlockAt(int indx) {
		return blocksList.get(indx);
	}

	@Override
	public BNode getChildAt(int indx) {
		return childrenList.get(indx);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blocksList == null) ? 0 : blocksList.hashCode());
		result = prime * result
				+ ((childrenList == null) ? 0 : childrenList.hashCode());
		result = prime * result + (isLeaf ? 1231 : 1237);
		result = prime * result + numOfBlocks;
		result = prime * result + t;
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BNode other = (BNode) obj;
		if (blocksList == null) {
			if (other.blocksList != null)
				return false;
		} else if (!blocksList.equals(other.blocksList))
			return false;
		if (childrenList == null) {
			if (other.childrenList != null)
				return false;
		} else if (!childrenList.equals(other.childrenList))
			return false;
		if (isLeaf != other.isLeaf)
			return false;
		if (numOfBlocks != other.numOfBlocks)
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BNode [t=" + t + ", numOfBlocks=" + numOfBlocks + ", isLeaf="
				+ isLeaf + ", blocksList=" + blocksList + ", childrenList="
				+ childrenList + "]";
	}

	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
		
	public boolean isMaxSize() {
		return numOfBlocks == (2*t - 1);
	}
	
	@Override
	public Block search(int key) {
	
		int i = 0;
		while (i < getNumOfBlocks() && key > getBlockKeyAt(i)){
			i++;
		}
		if (i < getNumOfBlocks() && key == getBlockKeyAt(i)){
			return (getBlockAt(i));
		}
		else { 
			if (isLeaf()) {
				return null;
			}
			else{
				return getChildAt(i).search(key);
			}
		}
	}

	@Override
	public void insertNonFull(Block d) {
	
		int i = getNumOfBlocks()-1;
		
		while ( ( i >= 0) && ( d.getKey() < getBlockKeyAt(i)) ){
			i--;	
		}
		
		if (isLeaf()) {
			blocksList.add(i+1 , d);
			numOfBlocks++;
		}
		else{
			i++;
			if ( getChildAt(i).isMaxSize()){
				splitChild(i);
				if (d.getKey() > getBlockKeyAt(i)){
					i++;
				}
			}
			getChildAt(i).insertNonFull(d);
		}	
	}
	

	@Override
	public void delete(int key) {
		
		int i = 0;
		
		while (i < getNumOfBlocks() && key > getBlockKeyAt(i)){ 	//Search for an block or children where the key will be found
			i++;
		}
		
		if (i < getNumOfBlocks() && getBlockKeyAt(i)== key){											//the key found in this node!!!
			if (isLeaf){ 															//if the key in a leaf
				blocksList.remove(i);												//Simple Delete
				numOfBlocks--;
			}
			else {
				if (childHasNonMinimalLeftSibling(i)){							// The child Left has enough keys
					Block helpblock = getChildAt(i).getMaxKeyBlock();					//	 		
					getChildAt(i).delete(helpblock.getKey());							// Recursive delete Max child
					getBlocksList().remove(i);											// Delete the desired block
					getBlocksList().add(i,helpblock);									// add the Max Block from child Left
				}
				else {
					if (childHasNonMinimalRightSibling(i)){						//	The child Left has enough keys
						Block helpblock = getChildAt(i+1).getMinKeyBlock();				//	 		
						getChildAt(i+1).delete(helpblock.getKey());						// Recursive delete min child
						getBlocksList().remove(i);										// Delete the desired block
						getBlocksList().add(i,helpblock);								// add the min Block from child Right	
					}
					else {														// Two children do not have enough keys
						mergeChildWithSibling(i);										//					
						delete(key);													//
					}
				}
			}
		}
		else {												//The key is not found but it can be in children 
			if (!isLeaf){
				if (getChildAt(i).isMinSize()){
					shiftOrMergeChildIfNeeded(i);
					delete(key);
				}
				else {
					getChildAt(i).delete(key);
				}
			}
		}
	}

	@Override
	public MerkleBNode createHashNode() {
		if(isLeaf){
			ArrayList<byte[]> MerklL = new ArrayList<byte[]>();
			for (int i = 0; i < getNumOfBlocks(); i++) {
				MerklL.add(getBlockAt(i).getData());
			}
			return new MerkleBNode(new HashUtils().sha1Hash(MerklL));
		}
		ArrayList<MerkleBNode> children2 = new ArrayList<MerkleBNode>();
		for (int i = 0; i < getNumOfBlocks()+1; i++) {
			children2.add(getChildAt(i).createHashNode());
		}
		int i;
		ArrayList<byte[]> mergelist = new ArrayList<byte[]>();
		for (i = 0; i < getNumOfBlocks(); i++) {
			mergelist.add(children2.get(i).getHashValue());
			mergelist.add(getBlockAt(i).getData());
		}
		mergelist.add(children2.get(i).getHashValue());
		return new  MerkleBNode(new HashUtils().sha1Hash(mergelist),children2);
	}
	
	
	/**
	* Splits the child node at childIndex into 2 nodes.
	* @param childIndex
	*/
	public void splitChild(int childIndex){
		
		BNode help1 = getChildAt(childIndex);
		BNode help2 = new BNode(this.t,help1.isLeaf(), t-1);
		
		for (int j = 0; j<this.t-1;j++){	//Copies half the blocks of y to z
			(help2.getBlocksList()).add(help1.getBlockAt(t)); 
			(help1.getBlocksList()).remove(t);
		}
		
		if (! (help1.isLeaf()) ){	//If it is not Leaf
			for (int j = 0; j<this.t;j++){	//Copies half the Child of y to z
				(help2.getChildrenList()).add(help1.getChildAt(t));
				(help1.getChildrenList()).remove(t);
			}
		}
		
		getChildrenList().add(childIndex+1,help2);
		getBlocksList().add(childIndex, help1.getBlockAt(this.t-1));
		help1.getBlocksList().remove(this.t-1);
		numOfBlocks++;
		help1 = new BNode(this.t,this.t-1,help1.isLeaf(),help1.getBlocksList(),help1.getChildrenList()); //y.numOfBlocks = t-1
		childrenList.set(childIndex,help1);
	}
	
	
	// function help for testing children with minimal organs
	private boolean childHasNonMinimal(int childIndx){
		return ((childIndx>0) && (childIndx<getNumOfBlocks()) && (!getChildAt(childIndx).isMinSize()));
	}
	// Returns true if my Left child has more than t-1 blocks
	private boolean childHasNonMinimalLeftSibling(int childIndx){			
		return (childHasNonMinimal(childIndx));
	}
	// Returns true if my Right child has more than t-1 blocks
	private boolean childHasNonMinimalRightSibling(int childIndx){ 
		return (childHasNonMinimal(childIndx+1));
	}
	// Returns true if my Left Brother has more than t-1 blocks
	private boolean leftBrotherNotMinimal(int childIndx){
		return (childHasNonMinimal(childIndx-1));
	}
	// Returns true if my Right Brother has more than t-1 blocks
	private boolean rightBrotherNotMinima(int childIndx){ 
		return (childHasNonMinimal(childIndx+1));
	}
	
	
	// Checks if Brothers can help and does not Merge
	private void shiftOrMergeChildIfNeeded(int childIndx){  
		if (leftBrotherNotMinimal(childIndx)){
			shiftFromLeftSibling(childIndx);
		}
		else {
			if (rightBrotherNotMinima(childIndx)){
				shiftFromRightSibling(childIndx);
			}
			else {
				mergeChildWithSibling(childIndx);
			}
		}	
	}
	
	/**
	* Add additional block to the child node at childIndx, by shifting from left sibling.
	* @param childIndx
	*/
	private void shiftFromLeftSibling(int childIndx){
		int numchild1 = getChildAt(childIndx-1).getNumOfBlocks();				// Number of organs in the left brother
		Block helpBlock1 = getChildAt(childIndx-1).getBlockAt(numchild1-1);		//(y = ) Keeping the last organ of the array in the left brother		
		BNode childHelpBlock1 = getChildAt(childIndx-1).getChildAt(numchild1);	//(c = ) Keeping the right children of the last member of the array in the left bro'
		Block helpBlock2 = getBlockAt(childIndx-1);								//(k = ) Keeping the father of the two organs that we will replace 
		
		getChildAt(childIndx-1).getBlocksList().remove(numchild1-1);			// Delete "y"
		getChildAt(childIndx-1).getChildrenList().remove(numchild1);			// Delete "c"
		getBlocksList().remove(childIndx-1);									// Delete "k"
		
		getBlocksList().add(childIndx-1,helpBlock1);							// add "y" where the father was
		getChildAt(childIndx).getBlocksList().add(0,helpBlock2);				// add "k" In the child "childIndx"
		getChildAt(childIndx).getChildrenList().add(0,childHelpBlock1);			// add "c" In the child "childIndx"
	}
	
	/**
	* Add additional block to the child node at childIndx, by shifting from right sibling.
	* @param childIndx
	*/
	private void shiftFromRightSibling(int childIndx){ 
		Block helpBlock1 = getChildAt(childIndx+1).getBlockAt(0);				//(y = ) Keeping the first organ of the array in the right brother		
		BNode childHelpBlock1 = getChildAt(childIndx+1).getChildAt(0);			//(c = ) Keeping the left children of the last member of the array in the right bro'
		Block helpBlock2 = getBlockAt(childIndx);								//(k = ) Keeping the father of the two organs that we will replace 
		
		getChildAt(childIndx+1).getBlocksList().remove(0);						// Delete "y"
		getChildAt(childIndx+1).getChildrenList().remove(0);					// Delete "c"
		getBlocksList().remove(childIndx);										// Delete "k"
		
		getBlocksList().add(childIndx,helpBlock1);								// add "y" where the father was
		getChildAt(childIndx).getBlocksList().add(helpBlock2);					// add "k" In the child "childIndx"
		getChildAt(childIndx).getChildrenList().add(childHelpBlock1);			// add "c" In the child "childIndx"
	}
	
	/**
	* Merges the child node at childIndx with its left or right sibling.
	* @param childIndx
	*/
	private void mergeChildWithSibling(int childIndx){
		if (childIndx==0){
			mergeWithRightSibling(childIndx);
		}
		else{
			mergeWithLeftSibling(childIndx);
		}
	}
	
	/**
	* Merges the child node at childIndx with its left sibling.<br>
	* The left sibling node is removed.
	* @param childIndx
	*/
	private void mergeWithLeftSibling(int childIndx){ 

		int i = (this.t-2);
		BNode childH = getChildAt(childIndx);
		BNode childLeft = getChildAt(childIndx-1);
		
		childH.getBlocksList().add(0,getBlockAt(childIndx-1));
		getBlocksList().remove(childIndx-1);
		getChildrenList().remove(childIndx-1);
		numOfBlocks--;
		
		while ( i >= 0 ){
			childH.getBlocksList().add(0,childLeft.getBlockAt(i));
			if (!(childH.isLeaf)){
				childH.getChildrenList().add(0,childLeft.getChildAt(i));
			}
			i--;
		}
		childH = new BNode(this.t,childH.getBlocksList().size(),childH.isLeaf(),childH.getBlocksList(),childH.getChildrenList()); //childH.numOfBlocks = 2t-1
			
		if (numOfBlocks==0){
			numOfBlocks = childH.getNumOfBlocks();
			isLeaf =  childH.isLeaf();
			blocksList = childH.getBlocksList();
			childrenList = childH.getChildrenList();
		}
		else {
			getChildrenList().set(childIndx,childH);
		}
	}
	
	/**
	* Merges the child node at childIndx with its right sibling.<br>
	* The right sibling node is removed.
	* @param childIndx
	*/
	private void mergeWithRightSibling(int childIndx){ 
		mergeWithLeftSibling(childIndx+1);
	}
	
	/**
	* Finds and returns the block with the min key in the subtree.
	* @return min key block
	*/
	private Block getMinKeyBlock(){
		if (isLeaf()){
			return getBlockAt(0);
		}
		else{
			return getChildAt(0).getMinKeyBlock();
			
		}
	}
	
	/**
	* Finds and returns the block with the max key in the subtree.
	* @return max key block
	*/
	private Block getMaxKeyBlock(){
		if (isLeaf()){
			return getBlockAt(numOfBlocks-1);
		}
		else{
			return getChildAt(numOfBlocks-1).getMaxKeyBlock();
			
		}
	}
	
}
