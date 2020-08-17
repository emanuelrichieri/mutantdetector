package emanuelrichieri.mutantdetector.util.suffixtree;

import java.util.Collection;
import java.util.Collections;

/**
 * A Generalized Suffix Tree, based on the Ukkonen's paper "On-line construction of suffix trees"
 * @see <a href="http://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf"/>
 * @see <a href="https://es.wikipedia.org/wiki/Algoritmo_de_Ukkonen" />
 * @see <a href="http://web.ist.utl.pt/joao.carreira/presentation.pdf"/> 

 */
public class GeneralizedSuffixTree {

    /**
     * The index of the last item that was added to the GST
     */
    private int last = 0;
    
    private final Node root = new Node();
    
    // The last leaf that was added during the update operation
    private Node activeLeaf = root;

    /**
     * Searches for the given word within the GST.
     *
     * Returns all the indexes for which the key contains the <tt>word</tt> that was
     * supplied as input.
     *
     * @param word the key to search for
     * @return the collection of indexes associated with the input <tt>word</tt>
     */
    public Collection<Integer> search(String word) {
        return search(word, -1);
    }

    /**
     * Searches for the given word within the GST and returns at most the given number of matches.
     *
     * @param word the key to search for
     * @param results the max number of results to return
     * @return at most <tt>results</tt> values for the given word
     */
    public Collection<Integer> search(String word, int results) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return Collections.emptyList();
        }
        return tmpNode.getData(results);
    }

    /**
     * Searches for the given word within the GST and returns at most the given number of matches.
     *
     * @param word the key to search for
     * @param to the max number of results to return
     * @return at most <tt>results</tt> values for the given word
     * @see GeneralizedSuffixTree#ResultInfo
     */
    public ResultInfo searchWithCount(String word, int to) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return new ResultInfo(Collections.emptyList(), 0);
        }

        return new ResultInfo(tmpNode.getData(to), tmpNode.getResultCount());
    }

    /**
     * Returns the tree node (if present) that corresponds to the given string.
	 * Verifies if exists a path from the root to a node such that the concatenation
	 * of all the labels on the path is a superstring of the given word.
	 * If such a path is found, the last node on it is returned.
     */
    private Node searchNode(String word) {
        Node currentNode = root;
        Edge currentEdge;

        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);
            // follow the edge corresponding to this char
            currentEdge = currentNode.getEdge(ch);
            if (null == currentEdge) {
                // there is no edge starting with this char
                return null;
            } else {
                String label = currentEdge.getLabel();
                int lenToMatch = Math.min(word.length() - i, label.length());
                if (!word.regionMatches(i, label, 0, lenToMatch)) {
                    // the label on the edge does not correspond to the one in the string to search
                    return null;
                }

                if (label.length() >= word.length() - i) {
                    return currentEdge.getDestination();
                } else {
                    // advance to next node
                    currentNode = currentEdge.getDestination();
                    i += lenToMatch - 1;
                }
            }
        }
        return null;
    }

    /**
     * Adds the specified <tt>index</tt> to the GST under the given <tt>key</tt>.
     *
     * Entries must be inserted so that their indexes are in non-decreasing order,
     * otherwise an IllegalStateException will be raised.
     *
     * @param key the string key that will be added to the index
     * @param index the value that will be added to the index
     * @throws IllegalStateException if an invalid index is passed as input
     */
    public void insert(String key, int index) throws IllegalStateException {
        if (index < last) {
            throw new IllegalStateException("The input index must not be less than any of the previously inserted ones. Got " + index + ", expected at least " + last);
        } else {
            last = index;
        }

        // reset activeLeaf
        activeLeaf = root;

        String remainder = key;
        Node node = root;

        // proceed with tree construction (closely related to procedure in Ukkonen's paper)
        String text = "";
        // iterate over the string, one char at a time
        for (int i = 0; i < remainder.length(); i++) {
            text += remainder.charAt(i);
            // use intern to make sure the resulting string is in the pool.
            // comparing interns is faster than string equals
            text = text.intern();

            // line 7: update the tree with the new transitions due to this new char
            Pair<Node, String> active = update(node, text, remainder.substring(i), index);
            // line 8: make sure the active pair is canonical
            active = canonize(active.getFirst(), active.getSecond());
            
            node = active.getFirst();
            text = active.getSecond();
        }

        // add leaf suffix link, is necessary
        if (null == activeLeaf.getSuffix() && activeLeaf != root && activeLeaf != node) {
            activeLeaf.setSuffix(node);
        }
    }

    /**
     * Tests whether the string stringPart + t is contained in the subtree that has inputNode as root.
     * If that's not the case, and there exists a path of edges e1, e2, ... such that
     *     e1.label + e2.label + ... + $end = stringPart
     * and there is an edge g such that
     *     g.label = stringPart + rest
     * Then g will be split in two different edges, one having $end as label, and the other one
     * having rest as label.
     * 
     * @see Extension rules from page 19 on <a href="http://web.ist.utl.pt/joao.carreira/presentation.pdf"/> 
     * 
     * @param inputNode the starting node
     * @param stringPart the string to search
     * @param t the following character
     * @param remainder the remainder of the string to add to the index
     * @param value the value to add to the index
     * @return a pair containing
     *                  true/false depending on whether (stringPart + t) is contained in the subtree starting in inputNode
     *                  the last node that can be reached by following the path denoted by stringPart starting from inputNode
     *         
     */
    private Pair<Boolean, Node> testAndSplit(final Node inputNode, final String stringPart, final char t, final String remainder, final int value) {
        // descend the tree as far as possible
        Pair<Node, String> farthestFound = canonize(inputNode, stringPart);
        
        Node node = farthestFound.getFirst(); // Farthest node reachable from inputNode by stringPart
        String str = farthestFound.getSecond(); // Remaining str not reachable 

        if (!"".equals(str)) {
            Edge g = node.getEdge(str.charAt(0));

            String label = g.getLabel();
            // must see whether "str" is substring of the label of an edge
            if (label.length() > str.length() && label.charAt(str.length()) == t) {
                // the rest of the edge label starts with t
            	return new Pair<Boolean, Node>(true, node);
            } else {
            	// the rest of the edge label is empty (label.length == str.length) or doesn't start with t
            	
                // need to split the edge
                String newlabel = label.substring(str.length());
                assert (label.startsWith(str));

                // build a new node
                Node r = new Node();
                // build a new edge
                Edge newedge = new Edge(str, r);

                g.setLabel(newlabel);

                // link s -> r
                r.addEdge(newlabel.charAt(0), g);
                node.addEdge(str.charAt(0), newedge);

                return new Pair<Boolean, Node>(false, r);
            }

        } else { 
        	// All stringPart is reachable
        	// analyzing remainder  (remainder starts with t) 
        	
            Edge e = node.getEdge(t);
            if (null == e) {
                // if there is no t-transtion from s
                return new Pair<Boolean, Node>(false, node);
            } else {
                if (remainder.equals(e.getLabel())) {
                	// All remainder from stringPart is in the tree in the found edge
                    // update payload of destination node
                    e.getDestination().addRef(value);
                    return new Pair<Boolean, Node>(true, node);
                    
                } else if (remainder.startsWith(e.getLabel())) {
                	// Part of the remainder from stringPart is in the tree
                	// and in the found edge
                    return new Pair<Boolean, Node>(true, node);
                    
                } else if (e.getLabel().startsWith(remainder)) {
                	// edge label contains a part of remainder
                    // need to split as above
                    Node newNode = new Node();
                    newNode.addRef(value);
                    // newNode will be reachable by newEdge
                    Edge newEdge = new Edge(remainder, newNode);
                    // Spliting edge from remainder end 
                    e.setLabel(e.getLabel().substring(remainder.length()));
                    newNode.addEdge(e.getLabel().charAt(0), e); // newNode add edge with remainder first char
                    node.addEdge(t, newEdge); // newEdge connects node with newNode
                    return new Pair<Boolean, Node>(false, node);
                } else {
                    // they are different words. No prefix. but they may still share some common substr
                    return new Pair<Boolean, Node>(true, node);
                }
            }
        }

    }

    /**
     * Return a (Node, String) (n, remainder) pair such that 
     *  <br> - n is a farthest descendant of node (the input node) that can be reached by 
     *  	following a path of edges denoting a prefix of inputstr 
     *  <br> - and remainder will be string that must be appended to the concatenation of labels 
     *  	from s to n to get inpustr. (i.e., part of inputstr not reachable following a path of edges)
     */
    private Pair<Node, String> canonize(final Node node, final String inputstr) {

        if ("".equals(inputstr)) {
            return new Pair<Node, String>(node, inputstr);
        } else {
            Node currentNode = node;
            String str = inputstr;
            Edge g = node.getEdge(str.charAt(0));
            // descend the tree as long as a proper label is found
            
            // while an edge is found from the remaining str (remaining str starts with edge label)
            while (g != null && str.startsWith(g.getLabel())) {
                str = str.substring(g.getLabel().length());
                currentNode = g.getDestination();
                if (str.length() > 0) {
                    g = currentNode.getEdge(str.charAt(0));
                }
            }

            return new Pair<Node, String>(currentNode, str);
        }
    }

    /**
     * Updates the tree starting from inputNode and by adding stringPart.
     * 
     * Returns a reference (Node, String) pair for the string that has been added so far.
     * This means:
     * - the Node will be the Node that can be reached by the longest path string (S1)
     *   that can be obtained by concatenating consecutive edges in the tree and
     *   that is a substring of the string added so far to the tree.
     * - the String will be the remainder that must be added to S1 to get the string
     *   added so far.
     * 
     * @param inputNode the node to start from
     * @param stringPart the string to add to the tree
     * @param rest the rest of the string
     * @param value the value to add to the index
     */
    private Pair<Node, String> update(final Node inputNode, final String stringPart, final String rest, final int value) {
        Node s = inputNode;
        String tempstr = stringPart;
        char newChar = stringPart.charAt(stringPart.length() - 1); // Last char of the string part is the new char to add

        // line 1
        Node oldroot = root;

        // line 1b
        Pair<Boolean, Node> ret = testAndSplit(s, tempstr.substring(0, tempstr.length() - 1), newChar, rest, value);

        Node r = ret.getSecond();
        boolean endpoint = ret.getFirst();

        Node leaf;
        // line 2
        while (!endpoint) {
            // line 3
            Edge tempEdge = r.getEdge(newChar);
            if (null != tempEdge) {
                // such a node is already present. This is one of the main differences from Ukkonen's case:
                // the tree can contain deeper nodes at this stage because different strings were added by previous iterations.
                leaf = tempEdge.getDestination();
            } else {
                // must build a new leaf
                leaf = new Node();
                leaf.addRef(value);
                Edge newedge = new Edge(rest, leaf);
                r.addEdge(newChar, newedge);
            }

            // update suffix link for newly created leaf
            if (activeLeaf != root) {
                activeLeaf.setSuffix(leaf);
            }
            activeLeaf = leaf;

            // line 4
            if (oldroot != root) {
                oldroot.setSuffix(r);
            }

            // line 5
            oldroot = r;

            // line 6
            if (null == s.getSuffix()) { // root node
                assert (root == s);
                // this is a special case to handle what is referred to as node _|_ on the paper
                tempstr = tempstr.substring(1);
            } else {
                Pair<Node, String> canret = canonize(s.getSuffix(), safeCutLastChar(tempstr));
                s = canret.getFirst();
                // use intern to ensure that tempstr is a reference from the string pool
                tempstr = (canret.getSecond() + tempstr.charAt(tempstr.length() - 1)).intern();
            }

            // line 7
            ret = testAndSplit(s, safeCutLastChar(tempstr), newChar, rest, value);
            r = ret.getSecond();
            endpoint = ret.getFirst();

        }

        // line 8
        if (oldroot != root) {
            oldroot.setSuffix(r);
        }
        oldroot = root;

        return new Pair<Node, String>(s, tempstr);
    }

    Node getRoot() {
        return root;
    }

    private String safeCutLastChar(String seq) {
        if (seq.length() == 0) {
            return "";
        }
        return seq.substring(0, seq.length() - 1);
    }

    public int computeCount() {
        return root.computeAndCacheCount();
    }

    /**
     * An utility object, used to store the data returned by the GeneralizedSuffixTree GeneralizedSuffixTree.searchWithCount method.
     * It contains a collection of results and the total number of results present in the GST.
     * @see GeneralizedSuffixTree#searchWithCount(java.lang.String, int) 
     */
    public static class ResultInfo {

        /**
         * The total number of results present in the database
         */
        public int totalResults;
        /**
         * The collection of (some) results present in the GST
         */
        public Collection<Integer> results;

        public ResultInfo(Collection<Integer> results, int totalResults) {
            this.totalResults = totalResults;
            this.results = results;
        }
    }

    /**
     * A private class used to return a tuples of two elements
     */
    private class Pair<A, B> {

        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }
}
