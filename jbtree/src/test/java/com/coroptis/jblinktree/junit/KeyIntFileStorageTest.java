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

import java.io.File;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.coroptis.jblinktree.FileStorageRule;
import com.coroptis.jblinktree.JbNodeBuilder;
import com.coroptis.jblinktree.JbNodeBuilderImpl;
import com.coroptis.jblinktree.JbTreeData;
import com.coroptis.jblinktree.JbTreeDataImpl;
import com.coroptis.jblinktree.Node;
import com.coroptis.jblinktree.NodeImpl;
import com.coroptis.jblinktree.store.KeyIntFileStorage;
import com.google.common.io.Files;

public class KeyIntFileStorageTest {

    @Rule
    public FileStorageRule fsRule = new FileStorageRule();

    private KeyIntFileStorage<Integer> valueStorage;

    private File tempDirectory;

    private Node<Integer, Integer> node;

    @Test
    public void test_read_and_write() throws Exception {

        valueStorage.store(node);
        
        Node<Integer, Integer> n= valueStorage.load(14);
        
        System.out.println(node);
        System.out.println(n);
        assertEquals(node, n);
    }

    @Before
    public void setup() throws Exception {
        tempDirectory = Files.createTempDir();

        JbTreeData<Integer, Integer> treeData =
                new JbTreeDataImpl<Integer, Integer>(1, 5,
                        fsRule.getIntDescriptor(), fsRule.getIntDescriptor(),
                        fsRule.getIntDescriptor());

        node = new NodeImpl<Integer, Integer>(14, false,
                treeData.getLeafNodeDescriptor());
        node.insertAtPosition(10, -10, 0);

        JbNodeBuilder<Integer, Integer> nodeBuilder =
                new JbNodeBuilderImpl<Integer, Integer>(treeData);

        valueStorage =
                new KeyIntFileStorage<Integer>(
                        new File(tempDirectory.getAbsolutePath()
                                + File.separator + "value.str"),
                        fsRule.getNodeDef(), nodeBuilder);
    }

    @After
    public void tearDown() throws Exception {
        tempDirectory = null;
        valueStorage.close();
        valueStorage = null;
    }
}