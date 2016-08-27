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

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.coroptis.jblinktree.type.TypeDescriptorInteger;

public class NodeRule implements TestRule {

    private final Integer l;

    private TypeDescriptorInteger intDescriptor;

    private JbTreeData<Integer, Integer> treeData;

    public NodeRule(final Integer l) {
        this.l = l;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                setup();
                base.evaluate();
                tearDown();
            }
        };
    }

    private void setup() {
        intDescriptor = new TypeDescriptorInteger();
        treeData = new JbTreeDataImpl<Integer, Integer>(0, l, intDescriptor,
                intDescriptor, intDescriptor);
    }

    private void tearDown() {
        intDescriptor = null;
        treeData = null;
    }

    public TypeDescriptorInteger getIntDescriptor() {
        return intDescriptor;
    }

    private byte[] convert(final Integer[] fieldInt) {
        byte fieldByte[] = new byte[fieldInt.length * 4 + 1];
        for (int i = 0; i < fieldInt.length; i++) {
            intDescriptor.save(fieldByte, i * 4 + 1, fieldInt[i]);
        }
        return fieldByte;
    }

    /**
     * Construct node and fill byte field.
     *
     * @param idNode
     *            required node id, node will be referred with this id.
     * @param fieldInt
     *            required Integer array representing node content.
     * @return created {@link NodeImpl}
     */
    public NodeImpl<Integer, Integer> makeNodeFromIntegers(final Integer idNode,
            final Integer fieldInt[]) {
        return new NodeImpl<Integer, Integer>(idNode, convert(fieldInt),
                treeData.getLeafNodeDescriptor());
    }

    public NodeImpl<Integer, Integer> makeNodeFromIntegers(final Integer ll,
            final Integer idNode, final Integer fieldInt[]) {
        JbNodeDef<Integer, Integer> td = new JbNodeDefImpl<Integer, Integer>(ll,
                intDescriptor, intDescriptor, intDescriptor);
        NodeImpl<Integer, Integer> n =
                new NodeImpl<Integer, Integer>(idNode, convert(fieldInt), td);
        return n;
    }

    public JbTreeData<Integer, Integer> getTreeData() {
        return treeData;
    }

    public Integer getL() {
        return l;
    }

}
