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
		if (contains(elem,root)==true)
		{
			return false;
		}

		else
		{
			if ( root == null )
			{
				root = new Node<T>(elem);
			}
			else
			{
				Node<T> node = insert(elem,root);
				if (elem.compareTo(node.elem) < 0)
				{
					node.left = new Node<T>(elem);
				}
				else
				{
					node.right = new Node<T>(elem);
				}
			}
			return true;
		}
	}

	public Node<T> insert(T elem, Node<T> node)
	{
		if (elem.compareTo(node.elem) < 0)
		{
			if (node.left == null)
			{
				return node;
			}
			else
			{
				return insert(elem,node.left);
			}
		}
		else
		{
			if ( node.right == null)
			{
				return node;
			}
			else
			{
				return insert(elem,node.right);
			}
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
		return contains(elem,root);
	}

	public boolean contains(T elem,Node<T> node)
	{
		if (node == null)
		{
			return false;
		}
		else
		{
			if (node.elem.equals(elem))
			{
				return true;
			}
			else
			{
				if (elem.compareTo(node.elem) < 0)
				{
					return contains(elem,node.left);
				}
				else
				{
					return contains(elem,node.right);
				}
			}
		}
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
		if (contains(elem,root) == false)
		{
			insert(elem);
		}
		else
		{
			Node<T> node = new Node<T>(elem);
			switch(type)
			{
				case SPLAY:
					splay(node);
					break;
				case SEMISPLAY:
					semisplay(node);
					break;
				default: break;
			}
		}
	}
	
	/**
	 * Semi-splays the tree using the parent-to-root strategy
	 * @param node
	 *			The node the parent of which will be the new root
	 */
	protected void semisplay(Node<T> node) {
		
		//Your code goes here
		if (node != null)
		{
			node = current(root,node.elem);
			if (node != null)
			{
				for (; node != null && root != node;)
				{
					if (root.left == node || root.right== node)
					{
						if (root.left == node)
						{
							root = rotateRight(root,node);
						}
						else
						{
							root = rotateLeft(root,node);
						}
					}
					else
					{
						Node<T> p = parent(root,node.elem);  // Parent of Node you want to access
						Node<T> grandP = parent(root,p.elem); // The grand Parent of node being access
						Node<T> grandGP = parent(root,grandP.elem);

						// Homogeneous configuration
						if (grandP.right == p && p.right == node)
						{
							
							if (grandGP != null){

								if(grandGP.left == grandP){
									grandGP.left = rotateLeft(grandP,p);
								}
								else
								{
									grandGP.right= rotateLeft(grandP,p);
								}
							}
							else
							{
								root = rotateLeft(grandP,p);
							}
							node = p;
						}
						else if (grandP.left == p && p.left == node)
						{
							if (grandGP != null){

								if(grandGP.left == grandP){
									grandGP.left = rotateRight(grandP,p);
								}
								else
								{
									grandGP.right= rotateRight(grandP,p);
								}
							}
							else
							{
								root = rotateRight(grandP,p);
							}
							node = p;
						}
						//  Heterogeneous configuration
						else
						{
							if(grandP.right == p)
							{

								grandP.right = rotateRight(p,node);
								grandGP = parent(root,grandP.elem);
								if(grandGP != null)
								{
									if (grandGP.left == grandP)
									{

										grandGP.left = rotateLeft(grandP,node);
									}
									else
									{
										grandGP.right = rotateLeft(grandP,node);
									}
								}
								else
								{
									root = rotateLeft(grandP,node);
								}
							}
							else
							{
								grandP.left = rotateLeft(p,node);
								grandGP = parent(root,grandP.elem);
								if(grandGP != null)
								{
									if (grandGP.left == grandP)
									{

										grandGP.left = rotateRight(grandP,node);
									}
									else
									{
										grandGP.right = rotateRight(grandP,node);
									}
								}
								else
								{
									root = rotateRight(grandP,node);
								}
							}
						}
					}
				}
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

		if (node != null)
		{

			node = current(root,node.elem);

			if (node != null)
			{

				for (; node != null && root != node;)
				{
					if (root.left == node || root.right== node)
					{

						if (root.left == node)
						{
							root = rotateRight(root,node);
						}
						else
						{
							root = rotateLeft(root,node);
						}
					}
					else
					{
						Node<T> p = parent(root,node.elem);  // Parent of Node you want to access
						Node<T> grandP = parent(root,p.elem); // The grand Parent of node being access
						Node<T> grandGP = parent(root,grandP.elem);

						// Homogeneous configuration
						if (grandP.right == p && p.right == node)
						{
							
							
							if (grandGP != null){

								if(grandGP.left == grandP){
									grandGP.left = rotateLeft(grandP,p);
									grandGP.left = rotateLeft(p,node);
								}
								else
								{
									grandGP.right= rotateLeft(grandP,p);
									grandGP.right= rotateLeft(p,node);
								}
							}
							else
							{
								root = rotateLeft(grandP,p);
								root = rotateLeft(p,node);
							}
					
						}
						else if (grandP.left == p && p.left == node)
						{
							
							if (grandGP != null){

								if(grandGP.left == grandP){
									grandGP.left = rotateRight(grandP,p);
									grandGP.left = rotateRight(p,node);
								}
								else
								{
									grandGP.right= rotateRight(grandP,p);
									grandGP.right = rotateRight(p,node);
								}
							}
							else
							{
								root = rotateRight(grandP,p);
								root = rotateRight(p,node);
							}
							
						}
						//  Heterogeneous configuration
						else
						{
							if(grandP.right == p)
							{

								grandP.right = rotateRight(p,node);
								grandGP = parent(root,grandP.elem);
								if(grandGP != null)
								{
									if (grandGP.left == grandP)
									{

										grandGP.left = rotateLeft(grandP,node);
									}
									else
									{
										grandGP.right = rotateLeft(grandP,node);
									}
								}
								else
								{
									root = rotateLeft(grandP,node);
								}
							}
							else
							{
								grandP.left = rotateLeft(p,node);
								grandGP = parent(root,grandP.elem);
								if(grandGP != null)
								{
									if (grandGP.left == grandP)
									{

										grandGP.left = rotateRight(grandP,node);
									}
									else
									{
										grandGP.right = rotateRight(grandP,node);
									}
								}
								else
								{
									root = rotateRight(grandP,node);
								}
							}
						}
					}
				}
			} 
		}
	}

	//Helper functions
	
	private Node<T> rotateLeft(Node<T> p, Node<T> n)
	{
		p.right = n.left;
		n.left = p;
		return n;
	}

	private Node<T> rotateRight(Node<T> p, Node<T> n)
	{
		p.left = n.right;
		n.right = p;
		return n;
	}

	private Node<T> parent(Node<T> n, T elem)
	{
		if(n != null)
		{
			if (elem.compareTo(n.elem) < 0)
			{
				if (n.left != null && n.left.elem.compareTo(elem)==0)
				{
					return n;
				}
				else
				{
					return parent(n.left,elem);
				}
			}
			else
			{
				if (n.right != null && n.right.elem.compareTo(elem)==0)
				{
					return n;
				}
				else
				{
					return parent(n.right,elem);
				}
			}
		}
		return null;
	}

	private Node<T> current(Node<T> n, T elem)
	{
		if(n != null)
		{
			if (elem.compareTo(n.elem)== 0)
			{
				return n;
			}
			else if (elem.compareTo(n.elem) < 0)
			{
				
				return current(n.left,elem);
			}
			else
			{
				return current(n.right,elem);
			}
		}
		return null;
	}
}