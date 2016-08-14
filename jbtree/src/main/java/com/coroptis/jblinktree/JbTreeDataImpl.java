package com.coroptis.jblinktree;

import java.util.concurrent.atomic.AtomicInteger;

import com.coroptis.jblinktree.type.TypeDescriptor;
import com.google.common.base.Preconditions;

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

/**
 * Simple {@link JbTreeData} implementations.
 *
 * @author jajir
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public final class JbTreeDataImpl<K, V> implements JbTreeData<K, V> {

    /**
     * Root node id. Points to tree root node.
     */
    private Integer rootNodeId;

    /**
     * Maximal number of keys in node.
     */
    private final int l;

    /**
     * Leaf node description.
     */
    private final JbNodeDef<K, V> leafNodeDescriptor;

    /**
     * Non-leaf node definition.
     */
    private final JbNodeDef<K, Integer> nonLeafNodeDescriptor;

    /**
     * Atomic integer for generating new node ids.
     */
    private final AtomicInteger nextId;

    /**
     * Basic constructor.
     *
     * @param startNodeId
     *            required root node id
     * @param initL
     *            required maximal number of keys in node
     * @param keyTypeDesc
     *            required key type description
     * @param valueTypeDesc
     *            required value type description
     * @param linkTypeDesc
     *            required link type description
     */
    public JbTreeDataImpl(final Integer startNodeId, final int initL,
            final TypeDescriptor<K> keyTypeDesc,
            final TypeDescriptor<V> valueTypeDesc,
            final TypeDescriptor<Integer> linkTypeDesc) {
        this.nextId = new AtomicInteger(NodeStore.FIRST_NODE_ID);
        this.rootNodeId = Preconditions.checkNotNull(startNodeId);
        this.l = initL;
        Preconditions.checkNotNull(keyTypeDesc);
        Preconditions.checkNotNull(valueTypeDesc);
        Preconditions.checkNotNull(linkTypeDesc);
        leafNodeDescriptor = new JbNodeDefImpl<K, V>(initL, keyTypeDesc,
                valueTypeDesc, linkTypeDesc);
        nonLeafNodeDescriptor = new JbNodeDefImpl<K, Integer>(initL,
                keyTypeDesc, linkTypeDesc, linkTypeDesc);
    }

    @Override
    public Integer getRootNodeId() {
        return rootNodeId;
    }

    /**
     * @param newRootNodeId
     *            required new root node id
     */
    @Override
    public void setRootNodeId(final Integer newRootNodeId) {
        this.rootNodeId = Preconditions.checkNotNull(newRootNodeId);
    }

    /**
     * @return the leafNodeDescriptor
     */
    @Override
    public JbNodeDef<K, V> getLeafNodeDescriptor() {
        return leafNodeDescriptor;
    }

    /**
     * @return the nonLeafNodeDescriptor
     */
    @Override
    public JbNodeDef<K, Integer> getNonLeafNodeDescriptor() {
        return nonLeafNodeDescriptor;
    }

    @Override
    public Integer getNextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public Integer getMaxNodeId() {
        return nextId.get();
    }

    @Override
    public void setMaxNodeId(final Integer maxNodeId) {
        nextId.set(maxNodeId);
    }

    /**
     * @return the l
     */
    @Override
    public int getL() {
        return l;
    }

}
