package com.coroptis.jblinktree.util;

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
 * Interface encapsulate stack behavior. Implementation shouldn't be thread
 * safe.
 *
 * @author jajir
 *
 */
public interface JbStack {

    /**
     * Get last value from stack and remove it.
     *
     * @return last value
     */
    Integer pop();

    /**
     * Insert value on the top of the stack.
     *
     * @param item
     *            optional item value
     */
    void push(Integer item);

    /**
     * Is stack empty.
     *
     * @return return <code>true</code> stack is empty otherwise
     *         <code>false</code>
     */
    boolean isEmpty();
}
