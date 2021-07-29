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
		if ( root == null )
		{
			root = new  Node<T>(elem);
			return true;
		}
		else
		{
			Node<T> curr = root;

			while(true)
			{
				if(curr.elem.equals(elem))
				{
					return false;
				}
				else if (curr.elem.compareTo(elem) < 0) // if curr.elem < elem
				{
					// < MEANS USE RIGHT OBJECT
					if ( curr.right == null)
					{
						curr.right = new Node<T>(elem);
						return true;
					}
					curr = curr.right;
				}
				else
				{
					if(curr.left == null)
					{
						curr.left = new Node<T>(elem);
						return true;
					}
					curr = curr.left;
				}
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
		Node<T> curr = root;

		while(curr != null)
		{
			if (curr.elem.equals(elem))
			{
				return true;
			}
			else if( curr.elem.compareTo(elem) < 0)
			{
				curr = curr.right;
			}
			else
			{
				curr = curr.left;
			}
		}

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
		Node<T> curr = root;

		while(curr!=null)
		{
			if (curr.elem.equals(elem))
			{
				break;
			}
			else if (curr.elem.compareTo(elem) < 0)
			{
				curr = curr.right;
			}
			else
			{
				curr = curr.left;
			}
		}
		if ( curr != null)
		{
			if (type == SplayType.SPLAY)
			{
				splay(curr);
			}
			else
			{
				semisplay(curr);
			}
		}
		else
		{
			this.insert(elem);
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
			T elem = node.elem;
			Node<T> curr = search(elem);
			Boolean flag = true;
			if(root != null){
				while (curr != root)
				{
					//  R’s parent is the root
					if (curr == root.left || curr == root.right)
					{
						parentRoot(root,curr);
					}
				
					else
					{
						Node<T> parent = getPrevNode(root,elem);
						Node<T> grandParent = getPrevNode(root,parent.elem);
						//R is in a homogeneous configuration with its predecessors perform a homogeneous splay, first rotate Q about P
						if ((grandParent.elem.compareTo(parent.elem) < 0 && parent.elem.compareTo(elem) < 0) || (grandParent.elem.compareTo(parent.elem) > 0 && parent.elem.compareTo(elem) > 0))
						{
							if ((grandParent.elem.compareTo(parent.elem) < 0 && parent.elem.compareTo(elem) < 0))
							{
								Node<T> grandGParent = getPrevNode(root,grandParent.elem);

								if (grandGParent != null)
								{
									zag(grandGParent,grandParent,parent);
									
								}
								else
								{
									parentRoot(root,parent);
								}
								parent = getPrevNode(root,elem);
								curr = parent ;
								elem = curr.elem;

							}
							else
							{
								Node<T> grandGParent = getPrevNode(root,grandParent.elem);

								if (grandGParent != null)
								{
									zig(grandGParent,grandParent,parent);
								}
								else
								{
									parentRoot(root,parent);
								}
								parent = getPrevNode(root,elem);
								curr = parent ;
								elem = curr.elem;
							}
						}
						else
						{

							Node<T> grandGParent = getPrevNode(root,grandParent.elem);
							
							if(grandGParent != null)
							{
								if (parent.elem.compareTo(elem) < 0)
								{
									zag(grandParent,parent,curr);
									zig(grandGParent,grandParent,curr);
								}
								else
								{
									zig(grandParent,parent,curr);
									zag(grandGParent,grandParent,curr);
								}

							}
							else
							{
								
								if (parent.elem.compareTo(elem) < 0)
								{
									zag(grandParent,parent,curr);
									parentRoot(root,curr);
								}
								else
								{
									zig(grandParent,parent,curr);
									parentRoot(root,curr);
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
			T elem = node.elem;
			Node<T> curr = search(elem);
			if(root != null){
				while (curr != root)
				{
					//  R’s parent is the root
					if (curr == root.left || curr == root.right)
					{
						parentRoot(root,curr);
					}
				
					else
					{
						Node<T> parent = getPrevNode(root,elem);
						Node<T> grandParent = getPrevNode(root,parent.elem);
						//R is in a homogeneous configuration with its predecessors perform a homogeneous splay, first rotate Q about P
						if ((grandParent.elem.compareTo(parent.elem) < 0 && parent.elem.compareTo(elem) < 0) || (grandParent.elem.compareTo(parent.elem) > 0 && parent.elem.compareTo(elem) > 0))
						{
							if ((grandParent.elem.compareTo(parent.elem) < 0 && parent.elem.compareTo(elem) < 0))
							{
								Node<T> grandGParent = getPrevNode(root,grandParent.elem);

								if (grandGParent != null)
								{
									zag(grandGParent,grandParent,parent);
									zag(grandGParent,parent,curr);
								}
								else
								{
									parentRoot(root,parent);
									parentRoot(root,curr);
								}

							}
							else
							{
								Node<T> grandGParent = getPrevNode(root,grandParent.elem);

								if (grandGParent != null)
								{
									zig(grandGParent,grandParent,parent);
									zig(grandGParent,parent,curr);
								}
								else
								{
									parentRoot(root,parent);
									parentRoot(root,curr);
								}

							}
						}
						else
						{
							Node<T> grandGParent = getPrevNode(root,grandParent.elem);
							
							if(grandGParent != null)
							{
								if (parent.elem.compareTo(elem) < 0)
								{
									zag(grandParent,parent,curr);
									zig(grandGParent,grandParent,curr);
								}
								else
								{
									zig(grandParent,parent,curr);
									zag(grandGParent,grandParent,curr);
								}

							}
							else
							{
								if (parent.elem.compareTo(elem) < 0)
								{
									zag(grandParent,parent,curr);
									parentRoot(root,curr);
								}
								else
								{
									zig(grandParent,parent,curr);
									parentRoot(root,curr);
								}
							}
						}
					}
				}
			}
		}
	}

	//Helper functions

	private void parentRoot(Node<T> parent, Node<T> curr)
	{
		if (parent.elem.compareTo(curr.elem) < 0)
		{
			root.right = curr.left;
			curr.left = root;
			root = curr;
		}
		else
		{
			root.left = curr.right;
			curr.right = root;
			root = curr;
		}
	}


	private Node<T> getPrevNode(Node<T> node, T elem)
	{
		while(node != null)
		{
			if ( node.elem.compareTo(elem) < 0 )
			{
				if ( node.right != null && node.right.elem.equals(elem))
				{
					return node;
				}
				node = node.right;
			}
			else
			{
				if ( node.left != null && node.left.elem.equals(elem))
				{
					return node;
				}
				node = node.left;				
			}
		}
		return null;
	}


	private void zig(Node<T> grandParent, Node<T> parent, Node<T> node)
	{
		parent.left = node.right;
		node.right = parent;
		linkGrandParent(grandParent,parent,node);
	}


	private void zag(Node<T> grandParent, Node<T> parent, Node<T> node)
	{
		parent.right = node.left;
		node.left = parent;
		linkGrandParent(grandParent,parent,node);
	}


	private void linkGrandParent(Node<T> grandParent, Node<T> parent, Node<T> node)
	{
		if (grandParent.elem.compareTo(parent.elem) < 0)
		{
			grandParent.right = node;
		}
		else
		{
			grandParent.left = node;
		}
	}

	private Node<T> search(T elem)
	{
		Node<T> curr = root;

		while(curr != null)
		{
			if(curr.elem.equals(elem))
			{
				return curr;
			}
			else if (curr.elem.compareTo(elem) < 0)
			{
				curr = curr.right;
			}
			else
			{
				curr = curr.left;
			}
		}
		return null;
	}
}
