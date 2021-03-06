package com.coroptis.jblinktree.performance.locking;

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
 * Class provide new node identification numbers.
 * 
 * @author jajir
 * 
 */
public interface IdGenerator {

    /**
     * When new tree is created than first node have this id.
     */
    final static int FIRST_NODE_ID = 0;

    /**
     * When new node should be created than this method generate new node id.
     * Each method call return different id.
     * <p>
     * Method is thread safe.
     * </p>
     * 
     * @return new node id
     */
    int getNextId();

    /**
     * Get previously assigned id.
     * 
     * @return previously assigned node id
     */
    int getPreviousId();

}
