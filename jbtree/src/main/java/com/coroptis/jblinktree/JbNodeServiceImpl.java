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
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Implementation of {@link JbNodeService}.
 *
 * @author jan
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public final class JbNodeServiceImpl<K, V> implements JbNodeService<K, V> {

    @Override
    public Integer getCorrespondingNodeId(final Node<K, Integer> node,
            final K key) {
        if (node.isLeafNode()) {
            throw new JblinktreeException("Leaf node '" + node.getId()
                    + "' doesn't have any child nodes.");
        }
        if (node.isEmpty()) {
            return node.getLink();
        }
        for (int i = 0; i < node.getKeyCount(); i++) {
            if (node.getNodeDef().getKeyTypeDescriptor().compareValues(key,
                    node.getKey(i)) <= 0) {
                return node.getValue(i);
            }
        }
        return node.getLink();
    }

    @Override
    public <S> void insert(final Node<K, S> node, final K key, final S value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        for (int i = 0; i < node.getKeyCount(); i++) {
            if (key.equals(node.getKey(i))) {
                /**
                 * Rewrite value.
                 */
                node.setValue(i, value);
                return;
            } else if (node.getNodeDef().getKeyTypeDescriptor()
                    .compareValues(node.getKey(i), key) > 0) {
                // field.get(i) > key
                couldInsertedKey(node);
                /**
                 * given value should be inserted 1 before current index
                 */
                node.insertAtPosition(key, value, i);
                return;
            }
        }
        couldInsertedKey(node);
        /**
         * New key is bigger than all others so should be at the end.
         */
        node.insertAtPosition(key, value, node.getKeyCount());
    }

    /**
     * When new value can't be inserted into this node it throws
     * {@link JblinktreeException}.
     *
     * @param node
     *            required node
     * @param <S>
     *            value type
     */
    private <S> void couldInsertedKey(final Node<K, S> node) {
        if (node.getKeyCount() >= node.getNodeDef().getL()) {
            throw new JblinktreeException("Leaf (" + node.getId()
                    + ") is full another value can't be inserted.");
        }
    }

    @Override
    public boolean updateKeyForValue(final Node<K, Integer> node,
            final Integer valueToUpdate, final K keyToSet) {
        if (node.isLeafNode()) {
            throw new JblinktreeException(
                    "method could by used just on non-leaf nodes");
        }
        for (int i = 0; i < node.getKeyCount(); i++) {
            if (node.getValue(i).equals(valueToUpdate)) {
                if (node.getKey(i).equals(keyToSet)) {
                    return false;
                } else {
                    node.setKey(i, keyToSet);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Integer> getNodeIds(final Node<K, Integer> node) {
        final List<Integer> out = new ArrayList<Integer>();
        for (int i = 0; i < node.getKeyCount(); i++) {
            out.add((Integer) node.getValue(i));
        }
        return out;
    }

    @Override
    public <S> void writeTo(final Node<K, S> node, final StringBuilder buff,
            final String intendation) {
        buff.append(intendation);
        buff.append("node");
        buff.append(node.getId());
        buff.append(" [shape=record,label=\" {");
        if (node.isLeafNode()) {
            buff.append("L");
        } else {
            buff.append("N");
        }
        buff.append("|");
        buff.append(node.getId());
        buff.append("}");
        for (int i = 0; i < node.getKeyCount() - 1; i = i + 2) {
            buff.append(" | {");
            buff.append("");
            buff.append(node.getKey(i + 1));
            buff.append("| <F");
            buff.append(node.getValue(i));
            buff.append("> ");
            buff.append(node.getValue(i));
            buff.append("}");
        }
        buff.append(" | ");
        if (node.getLink() != null) {
            buff.append("<L");
            buff.append(node.getLink());
            buff.append("> ");
        }
        buff.append(node.getLink());
        buff.append("\"];\n");
    }

    @Override
    public <S> S remove(final Node<K, S> node, final K key) {
        Preconditions.checkNotNull(key);
        for (int i = 0; i < node.getKeyCount(); i++) {
            if (key.equals(node.getKey(i))) {
                /**
                 * Remove key and value.
                 */
                final S oldValue = node.getValue(i);
                node.removeAtPosition(i);
                return oldValue;
            } else if (node.getNodeDef().getKeyTypeDescriptor()
                    .compareValues(node.getKey(i), key) > 0) {
                /**
                 * if key in node is bigger than given key than node doesn't
                 * contains key to delete.
                 */
                return null;
            }
        }
        return null;
    }

    @Override
    public V getValueByKey(final Node<K, V> node, final K key) {
        Preconditions.checkNotNull(key);
        for (int i = 0; i < node.getKeyCount(); i++) {
            if (key.equals(node.getKey(i))) {
                return node.getValue(i);
            }
        }
        return null;
    }

}