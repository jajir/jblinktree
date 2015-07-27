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

/**
 * Allows to read & write nodes. Main methods are thread safe. Get method
 * returns always new node instance with same data.
 * 
 * @author jajir
 * 
 */
public interface NodeStore<K> {

    void lockNode(Integer nodeId);

    void unlockNode(Integer nodeId);

    /**
     * Return defensive copy of node instance.
     * 
     * @param nodeId
     *            required node id
     * @return
     */
    <S> Node<K, S> get(Integer nodeId);

    <S> Node<K, S> getAndLock(Integer nodeId);

    /**
     * Persist node into node store.
     * <p>
     * Method doens't work with locks.
     * </p>
     * 
     * @param node
     *            required {@link Node}
     */
    <S> void writeNode(Node<K, S> node);

    void deleteNode(Integer idNode);

    /**
     * Get number of nodes that are locked.
     * 
     * @return number of locked nodes.
     */
    int countLockedNodes();

    /**
     * Method provide new node id.
     * <p>
     * Method just delegate to {@link IdGenerator#getNextId()}.
     * </p>
     * 
     * @return new node id
     */
    Integer getNextId();
}