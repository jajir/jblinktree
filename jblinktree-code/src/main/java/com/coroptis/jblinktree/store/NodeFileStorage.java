package com.coroptis.jblinktree.store;

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

import com.coroptis.jblinktree.Node;

/**
 * Simple storing nodes to file and reading from file.
 *
 * @author jajir
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public interface NodeFileStorage<K, V> extends NodeLoader<K, V> {

    /**
     * Store node.
     *
     * @param node
     *            required node
     */
    void store(Node<K, V> node);

    /**
     * Close file storage.
     */
    void close();

    /**
     * When it's <code>true</code> than tree is newly created node storage.
     *
     * @return Return <code>true</code> when tree is newly created otherwise
     *         return <code>false</code>
     */
    boolean isNewlyCreated();

}
