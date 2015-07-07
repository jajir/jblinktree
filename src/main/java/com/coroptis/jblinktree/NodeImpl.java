package com.coroptis.jblinktree;

/*
 * #%L
 * jblinktree
 * %%
 * Copyright (C) 2015 coroptis
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <p>
 * Generally in tree are inserted keys and values (K,V). There are two kind of
 * nodes:
 * <ul>
 * <li>leaf node - contains keys and values pairs</li>
 * <li>non-leaf node - contains keys and pointers to another nodes</li>
 * </ul>
 * </p>
 * Node contains data stored in a following field:
 * <table border="1" style="border-collapse: collapse">
 * <tr>
 * <td>value</td>
 * <td>P(0)</td>
 * <td>K(1)</td>
 * <td>P(1)</td>
 * <td>K(2)</td>
 * <td>P(2)</td>
 * <td>&nbsp;...&nbsp;</td>
 * <td>K(L*2)</td>
 * <td>P(L*2)</td>
 * <td>K(L*2+1)</td>
 * <td>link</td>
 * </tr>
 * <tr>
 * <td>index</td>
 * <td>0</td>
 * <td>1</td>
 * <td>2</td>
 * <td>3</td>
 * <td>4</td>
 * <td>&nbsp;...&nbsp;</td>
 * <td>L*2-1</td>
 * <td>L*2</td>
 * <td>L*2+1</td>
 * <td>L*2+2</td>
 * </tr>
 * </table>
 * Where:
 * <ul>
 * <li>P - pointer to some another node</li>
 * <li>K - Key inserted into tree, this value represents some user's data. Key
 * could be ordered and compared.</li>
 * <li>V - value inserted into tree</li>
 * <li>L - main parameter of tree, in tree could be maximally L nodes.</li>
 * <li>link - pointer to next sibling node</li>
 * <li>K(L*2+1) - highest key value from node in case of leaf node or highest
 * key value from all referenced nodes.</li>
 * </ul>
 * First value P0 at index 0 have special meaning, when it's {@link NodeImpl#M}
 * than this node is leaf node. In all other cases is non-leaf node.
 * <p>
 * Node is not thread save.
 * </p>
 * 
 * @author jajir
 * 
 */
public class NodeImpl implements Node {

    /**
     * Main node parameter, it's number of nodes.
     */
    private final int l;

    /**
     * When this value in at P(0) position than it's leaf node.
     */
    private final static Integer M = -1;

    private final static Integer EMPTY_NON_LEAF_NODE = -2;

    private final Integer id;

    private Field field;

    /**
     * Create and initialize node.
     * 
     * @param l
     *            required node parameter L
     * @param nodeId
     *            required node id, node will be referred with this id.
     * @param isLeafNode
     *            required value, when it's <code>true</code> than it's leaf
     *            node otherwise it's non-leaf node.
     */
    public NodeImpl(final int l, final Integer nodeId, final boolean isLeafNode) {
	this.l = l;
	this.id = nodeId;
	/**
	 * There is three position even in empty node: P0, max key and link.
	 */
	field = new FieldImpl(3);
	if (isLeafNode) {
	    field.set(0, M);
	}
    }

    /**
     * Construct node and fill byte field.
     * 
     * @param l
     *            required node parameter L
     * @param nodeId
     *            required node id, node will be referred with this id.
     * @param field
     *            required Integer array representing node content.
     * @return created {@link NodeImpl}
     */
    public static NodeImpl makeNode(final int l, final Integer idNode, final Integer field[]) {
	NodeImpl n = new NodeImpl(l, idNode, true);
	n.field = new FieldImpl(field);
	return n;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getLink()
     */
    @Override
    public Integer getLink() {
	return field.get(field.getLength() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#setLink(java.lang.Integer)
     */
    @Override
    public void setLink(final Integer link) {
	field.set(field.getLength() - 1, link);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getP0()
     */
    @Override
    public Integer getP0() {
	return field.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#setP0(java.lang.Integer)
     */
    @Override
    public void setP0(final Integer p0) {
	field.set(0, p0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#isEmpty()
     */
    @Override
    public boolean isEmpty() {
	return getKeysCount() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getKeysCount()
     */
    @Override
    public int getKeysCount() {
	if (isLeafNode()) {
	    return (field.getLength() - 3) / 2;
	} else {
	    if (field.get(1) == null) {
		return 0;
	    }
	    return (field.getLength() - 1) / 2;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#insert(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public void insert(final Integer key, final Integer value) {
	Preconditions.checkNotNull(key);
	Preconditions.checkNotNull(value);
	if (isLeafNode()) {
	    for (int i = 1; i < field.getLength() - 2; i = i + 2) {
		if (field.get(i) == key) {
		    /**
		     * Rewrite value.
		     */
		    field.set(i + 1, value);
		    return;
		} else if (field.get(i) > key) {
		    couldInsertedKey();
		    /**
		     * given value should be inserted 1 before current index
		     */
		    insertToPosition(key, value, i);
		    return;
		}
	    }
	    couldInsertedKey();
	    /**
	     * New key is bigger than all others so should be at the end.
	     */
	    insertToPosition(key, value, field.getLength() - 2);
	    setMaxKeyValue(key);
	} else {
	    for (int i = 1; i < field.getLength() - 1; i = i + 2) {
		if (key.equals(field.get(i))) {
		    /**
		     * Rewrite value.
		     */
		    field.set(i - 1, value);
		    return;
		} else if (field.get(i) > key) {
		    couldInsertedKey();
		    /**
		     * given value should be inserted 1 before current index
		     */
		    insertToPosition(value, key, i - 1);
		    return;
		}
	    }
	    couldInsertedKey();
	    /**
	     * New key is bigger than all others so should be at the end.
	     */
	    insertToPosition(value, key, field.getLength() - 1);
	}
    }

    /**
     * When new value can't be inserted into this node it throws
     * {@link JblinktreeException}.
     */
    private void couldInsertedKey() {
	if (field.getLength() >= l * 2 + 2) {
	    throw new JblinktreeException("Leaf (" + id
		    + ") is full another value can't be inserted.");
	}
    }

    /**
     * Insert key and value to some specific index position in field.
     * 
     * @param key
     *            required key
     * @param value
     *            required value
     * @param targetIndex
     *            required target index in field
     */
    private void insertToPosition(final Integer key, final Integer value, final int targetIndex) {
	Field field2 = new FieldImpl(field.getLength() + 2);
	if (targetIndex > 0) {
	    field2.copy(field, 0, 0, targetIndex);
	}
	field2.set(targetIndex, key);
	field2.set(targetIndex + 1, value);
	field2.copy(field, targetIndex, targetIndex + 2, field.getLength() - targetIndex);
	field = field2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#remove(java.lang.Integer)
     */
    @Override
    public boolean remove(final Integer key) {
	Preconditions.checkNotNull(key);
	if (!isLeafNode() && field.getLength() == 3) {
	    if (key.equals(field.get(1))) {
		/**
		 * When last pointer is removed, null in field[0] means there is
		 * no value, but it's not a leaf.
		 */
		field.set(0, EMPTY_NON_LEAF_NODE);
		field.set(1, null);
		return true;
	    }
	    return false;
	}
	for (int i = 1; i < field.getLength() - 1; i = i + 2) {
	    if (key.equals(field.get(i))) {
		/**
		 * Remove key and value.
		 */
		if (isLeafNode()) {
		    removeFromPosition(i);
		    if (field.getLength() > 3) {
			setMaxKeyValue(field.get(field.getLength() - 4));
		    } else {
			setMaxKeyValue(null);
		    }
		} else {
		    removeFromPosition(i - 1);
		}
		return true;
	    } else if (field.get(i) > key) {
		/**
		 * if key in node is bigger than key than node doesn't contains
		 * key to delete.
		 */
		return false;
	    }
	}
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#updateNodeValue(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public boolean updateNodeValue(final Integer nodeIdToUpdate, final Integer nodeMaxValue) {
	if (isLeafNode()) {
	    throw new JblinktreeException("method could by used just on non-leaf nodes");
	}
	for (int i = 0; i < field.getLength() - 2; i = i + 2) {
	    if (field.get(i).equals(nodeIdToUpdate)) {
		if (field.get(i + 1).equals(nodeMaxValue)) {
		    return false;
		} else {
		    field.set(i + 1, nodeMaxValue);
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Remove two bytes from node field at given position. Method doesn't care
     * about meaning of bites.
     * 
     * @param position
     *            required position
     */
    private void removeFromPosition(final int position) {
	Field tmp = new FieldImpl(field.getLength() - 2);
	if (position > 0) {
	    tmp.copy(field, 0, 0, position);
	}
	tmp.copy(field, position + 2, position, field.getLength() - position - 2);
	field = tmp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.coroptis.jblinktree.Node#moveTopHalfOfDataTo(com.coroptis.jblinktree
     * .NodeImpl)
     */
    @Override
    public void moveTopHalfOfDataTo(final Node nodea) {
	final NodeImpl node = (NodeImpl) nodea;
	Preconditions.checkArgument(node.isEmpty());
	if (getKeysCount() < 1) {
	    throw new JblinktreeException("In node " + id + " are no values to move.");
	}
	if (isLeafNode()) {
	    // copy top half to empty node
	    final int startKeyNo = getKeysCount() / 2;
	    final int startIndex = startKeyNo * 2 + 1;
	    final int length = field.getLength() - startIndex;
	    node.field = new FieldImpl(length + 1);
	    node.field.copy(field, startIndex, 1, length);

	    // remove copied data from this node
	    Field field2 = new FieldImpl(startIndex + 2);
	    field2.copy(field, 0, 0, startIndex);
	    field = field2;
	    setLink(node.getId());
	    setMaxKeyValue(field.get(field.getLength() - 4));
	    node.field.set(0, M);
	} else {
	    // copy top half to empty node
	    final int startKeyNo = (getKeysCount()) / 2 - 1;
	    final int startIndex = startKeyNo * 2 + 2;
	    final int length = field.getLength() - startIndex;
	    node.field = new FieldImpl(length);
	    node.field.copy(field, startIndex, 0, length);

	    // remove copied data from this node
	    Field field2 = new FieldImpl(startIndex + 1);
	    field2.copy(field, 0, 0, startIndex);
	    field = field2;
	    setLink(node.getId());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getMaxKey()
     */
    @Override
    public Integer getMaxKey() {
	return field.get(field.getLength() - 2);
    }

    /**
     * Override {@link System#toString()} method.
     */
    @Override
    public String toString() {
	StringBuilder buff = new StringBuilder();
	buff.append("Node{id=");
	buff.append(id);
	buff.append(", isLeafNode=");
	buff.append(isLeafNode());
	buff.append(", field=[");
	for (int i = 0; i < field.getLength(); i++) {
	    if (i != 0) {
		buff.append(", ");
	    }
	    buff.append(field.get(i));
	}
	buff.append("]}");
	return buff.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getId()
     */
    @Override
    public Integer getId() {
	return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#isLeafNode()
     */
    @Override
    public boolean isLeafNode() {
	return M.equals(field.get(0));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.coroptis.jblinktree.Node#getCorrespondingNodeId(java.lang.Integer)
     */
    @Override
    public Integer getCorrespondingNodeId(final Integer key) {
	if (isLeafNode()) {
	    throw new JblinktreeException("Leaf node '" + id + "' doesn't have any child nodes.");
	}
	if (isEmpty()) {
	    return getLink();
	}
	for (int i = 1; i < field.getLength() - 1; i = i + 2) {
	    if (key <= field.get(i)) {
		return field.get(i - 1);
	    }
	}
	return field.get(field.getLength() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.coroptis.jblinktree.Node#getPreviousCorrespondingNode(java.lang.Integer
     * )
     */
    @Override
    public Integer getPreviousCorrespondingNode(Integer key) {
	if (isLeafNode()) {
	    throw new JblinktreeException("Leaf node doesn't have any child nodes.");
	}
	for (int i = 1; i < field.getLength() - 1; i = i + 2) {
	    if (key <= field.get(i)) {
		if (i > 3) {
		    return field.get(i - 3);
		} else {
		    return null;
		}
	    }
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getValue(java.lang.Integer)
     */
    @Override
    public Integer getValue(final Integer key) {
	Preconditions.checkNotNull(key);
	if (!isLeafNode()) {
	    throw new JblinktreeException("Non-leaf node '" + id + "' doesn't have leaf value.");
	}
	for (int i = 1; i < field.getLength() - 2; i = i + 2) {
	    if (key.equals(field.get(i))) {
		return field.get(i + 1);
	    }
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getNodeIds()
     */
    @Override
    public List<Integer> getNodeIds() {
	final List<Integer> out = new ArrayList<Integer>();
	for (int i = 0; i < field.getLength() - 2; i = i + 2) {
	    out.add(field.get(i));
	}
	return out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getKeys()
     */
    @Override
    public List<Integer> getKeys() {
	final List<Integer> out = new ArrayList<Integer>();
	for (int i = 1; i < field.getLength(); i = i + 2) {
	    out.add(field.get(i));
	}
	return out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#setMaxKeyValue(java.lang.Integer)
     */
    @Override
    public void setMaxKeyValue(final Integer maxKey) {
	field.set(field.getLength() - 2, maxKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getMaxValue()
     */
    @Override
    public Integer getMaxValue() {
	return field.get(field.getLength() - 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#verify()
     */
    @Override
    public boolean verify() {
	if ((field.getLength()) % 2 == 0) {
	    throw new JblinktreeException("node " + id
		    + " have inforrect number of items in field: " + toString() + "");
	}
	// if (field[0] == null) {
	// throw new JblinktreeException("node " + id + " have null P0");
	// }
	if (!isLeafNode()) {
	    for (int i = 0; i < field.getLength() - 2; i = i + 2) {
		if (field.get(i) != null && field.get(i).equals(id)) {
		    throw new JblinktreeException("node contains pointer to itself: " + toString());
		}
	    }
	}
	return true;
    }

    @Override
    public int hashCode() {
	return Arrays.hashCode(new Object[] { l, id, field });
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof NodeImpl)) {
	    return false;
	}
	NodeImpl n = (NodeImpl) obj;
	if (equal(l, n.l) && equal(id, n.id) && equal(field.getLength(), n.field.getLength())) {
	    for (int i = 0; i < field.getLength(); i++) {
		if (!equal(field.get(i), n.field.get(i))) {
		    return false;
		}
	    }
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * It's just copy of com.google.common.base.Objects.equals(Object).
     * 
     * @param a
     * @param b
     * @return
     */
    private boolean equal(Object a, Object b) {
	return a == b || (a != null && a.equals(b));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getL()
     */
    @Override
    public int getL() {
	return l;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#writeTo(java.lang.StringBuilder,
     * java.lang.String)
     */
    @Override
    public void writeTo(final StringBuilder buff, final String intendation) {
	buff.append(intendation);
	buff.append("node");
	buff.append(getId());
	buff.append(" [shape=record,label=\" {");
	if (isLeafNode()) {
	    buff.append("L");
	} else {
	    buff.append("N");
	}
	buff.append("|");
	buff.append(getId());
	buff.append("}");
	if (isLeafNode()) {
	    for (int i = 1; i < field.getLength() - 2; i = i + 2) {
		buff.append(" | {");
		buff.append("");
		buff.append(field.get(i));
		buff.append("| <F");
		buff.append(field.get(i + 1));
		buff.append("> ");
		buff.append(field.get(i + 1));
		buff.append("}");

	    }
	} else {
	    for (int i = 0; i < field.getLength() - 2; i = i + 2) {
		buff.append(" | {");
		buff.append("");
		buff.append(field.get(i + 1));
		buff.append("| <F");
		buff.append(field.get(i));
		buff.append("> ");
		buff.append(field.get(i));
		buff.append("}");

	    }
	}
	buff.append(" | ");
	if (getLink() != null) {
	    buff.append("<L");
	    buff.append(getLink());
	    buff.append("> ");
	}
	buff.append(getLink());
	buff.append("\"];\n");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coroptis.jblinktree.Node#getField()
     */
    @Override
    public Integer[] getField() {
	return field.getField();
    }

}
