package com.coroptis.jblinktree.junit;

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

import org.easymock.EasyMock;

import com.coroptis.jblinktree.JbNodeBuilder;
import com.coroptis.jblinktree.JbNodeService;
import com.coroptis.jblinktree.JbTreeData;
import com.coroptis.jblinktree.JbTreeHelper;
import com.coroptis.jblinktree.JbTreeService;
import com.coroptis.jblinktree.JbTreeTool;
import com.coroptis.jblinktree.JbTreeTraversingService;
import com.coroptis.jblinktree.Node;
import com.coroptis.jblinktree.NodeStore;

/**
 * Class
 *
 * @author jajir
 *
 */
public abstract class AbstractMockingTest {

    protected JbTreeTool<Integer, Integer> treeTool;

    protected JbTreeTraversingService<Integer, Integer> treeTraversingService;

    protected NodeStore<Integer> nodeStore;

    protected JbTreeService<Integer, Integer> jbTreeService;

    protected JbTreeHelper<Integer, Integer> treeHelper;

    protected JbTreeData<Integer, Integer> treeData;

    protected JbNodeBuilder<Integer, Integer> nodeBuilder;

    protected Node<Integer, Integer> n1, n2, n3;
    protected Node<Integer, String> n4;

    protected JbNodeBuilder<Integer, Integer> builder;

    protected JbNodeService<Integer, Integer> nodeService;

    protected Object[] mocks;

    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        nodeStore = EasyMock.createMock(NodeStore.class);
        nodeBuilder = EasyMock.createMock(JbNodeBuilder.class);
        treeTool = EasyMock.createMock(JbTreeTool.class);
        n1 = EasyMock.createMock(Node.class);
        n2 = EasyMock.createMock(Node.class);
        n3 = EasyMock.createMock(Node.class);
        n4 = EasyMock.createMock(Node.class);
        builder = EasyMock.createMock(JbNodeBuilder.class);
        jbTreeService = EasyMock.createMock(JbTreeService.class);
        treeHelper = EasyMock.createMock(JbTreeHelper.class);
        treeData = EasyMock.createMock(JbTreeData.class);
        nodeService = EasyMock.createMock(JbNodeService.class);
        treeTraversingService =
                EasyMock.createMock(JbTreeTraversingService.class);
        mocks = new Object[] { nodeStore, nodeBuilder, treeTool, n1, n2, n3, n4,
                builder, jbTreeService, treeHelper, treeData,
                treeTraversingService, nodeService };
    }

    protected void tearDown() throws Exception {
        n1 = null;
        n2 = null;
        n3 = null;
        n4 = null;
        treeTraversingService = null;
        nodeStore = null;
        builder = null;
        mocks = null;
        nodeService = null;
    }

    protected void replay(final Object... otherMocks) {
        EasyMock.replay(mocks);
        EasyMock.replay(otherMocks);
    }

    protected void verify(final Object... otherMocks) {
        EasyMock.verify(mocks);
        EasyMock.verify(otherMocks);
    }

}
