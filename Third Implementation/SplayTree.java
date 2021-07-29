/**
 * Name:
 * Student Number:
 */

public class SplayTree<T extends Comparable<T>> {

	protected enum SplayType {
		SPLAY,
		SEMISPLAY,
		NONE
	}	

	protected Node<T> root = null;
	
	/**
	 * Prints out all the elements in the tree
	 * @param verbose
	 *			If false, the method simply prints out the element of each node in the tree
	 *			If true, then the output provides additional detail about each of the nodes.
	 */
	public void printTree(boolean verbose) {
		String result;
		result = preorder(root, verbose);
		System.out.println(result);
	}
	
	protected String preorder(Node<T> node, boolean verbose) {
		if (node != null) {
			String result = "";
			if (verbose) {
				result += node.toString()+"\n";
			} else {
				result += node.elem.toString() + " ";
			}
			result += preorder(node.left, verbose);
			result += preorder(node.right, verbose);
			return result;
		}
		return "";
	}
	
	////// You may not change any code above this line //////

	////// Implement the functions below this line //////
	
	/**
	* Inserts the given element into the tree, but only if it is not already in the tree.
	* @param elem 
	* 		 	The element to be inserted into the tree
	* @return 
	*			Returns true if the element was successfully inserted into the tree. 
	*			Returns false if elem is already in the tree and no insertion took place.
	*
	*/
	public boolean insert(T elem) {

		//Your code goes here
		// check if elem does not exist 
		if(contains(elem) == true)
		{
			return false;
		}
		else
		{
			// Check if root it's null
			if ( root == null)
			{
				root = new Node<T>(elem);
			}
			else
			{
				// traverse to find position to insert at then leaves or correct order;
				for(Node<T> temp = root; temp != null; )
				{
					if(temp.elem.compareTo(elem) > 0)
					{
						if (temp.left == null)
						{
							temp.left = new Node<T>(elem);
							break;
						}
						else
						{
							temp = temp.left;
						}
					}
					else
					{
						if(temp.right == null)
						{
							temp.right = new Node<T>(elem);
							break;
						}
						else
						{
							temp = temp.right;
						}
					}
				}
			}
			return true;
		}
	}
	
	/**
	* Checks whether a given element is already in the tree.
	* @param elem 
	* 		 	The element being searched for in the tree
	* @return 
	*			Returns true if the element is already in the tree
	*			Returns false if elem is not in the tree
	*
	*/
	public boolean contains(T elem) {

		//Your code goes here
		for(Node<T> temp = root; temp != null ;)
		{
			// if generic member variable elem of object temp equals to generic data type elem  then true it's return
			if (temp.elem.equals(elem))return true;
			// Check if elem is either left or right
			else if (temp.elem.compareTo(elem) > 0)
			{
				// elem it's less temp.elem, then use left link of temp object
				temp = temp.left;
			}
			else
			{
				// elem it's greater than temp.elem, then use right link of temp object
				temp = temp.right;
			}
		}
		// elem does not exist
		return false;

	}
	
	/**
	 * Accesses the node containing elem. 
	 * If no such node exists, the node should be inserted into the tree.
	 * If the element is already in the tree, the tree should either be semi-splayed so that 
	 * the accessed node moves up and the parent of that node becomes the new root or be splayed 
	 * so that the accessed node becomes the new root.
	 * @param elem
	 *			The element being accessed
	 * @param type
	 *			The adjustment type (splay or semi-splay or none)
	 */
	public void access(T elem, SplayType type) {

		//Your code goes here
		if (contains(elem) == true)
		{
			if (SplayType.SEMISPLAY == type)
			{
				semisplay(new Node<T>(elem));
			}
			else
			{
				splay(new Node<T>(elem));
			}
		}
		else
		{
			insert(elem);
		}
	}
	
	/**
	 * Semi-splays the tree using the parent-to-root strategy
	 * @param node
	 *			The node the parent of which will be the new root
	 */
	protected void semisplay(Node<T> node) {
		
		//Your code goes here

		// Check if node it's not null

		if ( node != null )
		{
			node = getCurrent(node.elem);
			if (node != null)
			{
				splayIT(node,true);
			}
		}
	}

	/**
	 * Splays the tree using the node-to-root strategy
	 * @param node
	 *			The node which will be the new root
	 */
	protected void splay(Node<T> node) {
		
		//Your code goes here

		// Check if node it's not null

		if ( node != null )
		{ 
		 	node = getCurrent(node.elem);
		 	if (node != null)
		 	{
				splayIT(node,false);
		 	}
		}
	}

	//Helper functions

	private Node<T> getPredecessor(T elem)
	{
		Node<T> temp = root;

		while(temp != null)
		{
			if ((temp.left != null && temp.left.elem.equals(elem)) || (temp.right != null && temp.right.elem.equals(elem)))
			{
				return temp;
			}
			else if (elem.compareTo(temp.elem) > 0)
			{
				temp = temp.right;
			}
			else
			{
				temp = temp.left;
			}
		}
		return null;
	}



	private void splayIT(Node<T> node,boolean flag)
	{
		
		while (node != root)
		{
			if ( node == root.right || node == root.left)
			{
				
				casesOne(node);
			}
			else
			{
				Node<T> prev = getPredecessor(node.elem);
				Node<T> prevprev = getPredecessor(prev.elem);
				Node<T> prevprevprev = getPredecessor(prevprev.elem);
				if (prevprev.left ==  prev && prev.left == node || prevprev.right ==  prev && prev.right == node)
				{
					
					if(flag == true)
					{
						if (prevprev.left ==  prev && prev.left == node)
						{
							rightRotation(prevprev,prev,prevprevprev);
							node = getPredecessor(node.elem);
						}
						else
						{
							
							leftRotation(prevprev,prev,prevprevprev);
							node = getPredecessor(node.elem);
						}
					}
					else
					{

						if (prevprev.right ==  prev && prev.right == node)
						{
					
							leftRotation(prevprev,prev,prevprevprev);
							leftRotation(prev,node,prevprevprev);
						}
						else
						{

							rightRotation(prevprev,prev,prevprevprev);
							rightRotation(prev,node,prevprevprev);
						}
					}
				}
				else
				{
					if (prevprev.right == prev && prev.left == node)
					{
						rightRotation(prev,node,prevprev);
						leftRotation(prevprev,node,prevprevprev);
					}
					else
					{

						leftRotation(prev,node,prevprev);
						rightRotation(prevprev,node,prevprevprev);
					}
				}
			}
		}
	}

	private void casesOne(Node<T> node)
	{
		if ( node == root.right )
		{
			root.right = node.left;
			node.left = root;
		}	
		else
		{
			root.left = node.right;
			node.right = root;
		}
		root = node;
	}

	private void leftRotation(Node<T> parentNode, Node<T> childNode,Node<T> prevprevious)
	{
		if (parentNode == root)
		{

			root.right = childNode.left;
			childNode.left = root;
			root = childNode;
		}
		else
		{
			parentNode.right = childNode.left;
			childNode.left = parentNode;
			
			if (prevprevious.left == parentNode)
			{
				prevprevious.left = childNode;
			}
			else
			{
				prevprevious.right = childNode;
			}
		}
	}

	private void rightRotation(Node<T> parentNode, Node<T> childNode, Node<T> prevprevious)
	{
		if (parentNode == root)
		{
	
			parentNode.left = childNode.right;
			childNode.right = parentNode;
			root = childNode;
		}
		else
		{
			
			parentNode.left = childNode.right;
			childNode.right = parentNode;
			
			if (prevprevious.left == parentNode)
			{
				prevprevious.left = childNode;
			}
			else
			{
				prevprevious.right = childNode;
			}
		}
	}

	private Node<T> getCurrent(T elem)
	{
		Node<T> temp = root;

		while(temp != null)
		{
			if (elem.equals(temp.elem))
			{
				return temp;
			}
			else if (elem.compareTo(temp.elem) < 0)
			{
				temp = temp.left;
			}
			else
			{
				temp = temp.right;
			}
		}
		return null;
	}
}