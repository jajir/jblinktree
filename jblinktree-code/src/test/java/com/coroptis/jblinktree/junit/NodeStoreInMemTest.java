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
import static org.easymock.EasyMock.expectLastCall;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.coroptis.jblinktree.NodeStore;
import com.coroptis.jblinktree.store.NodeStoreInMem;

/**
 * Tests for {@link NodeStoreInMem}.
 *
 * @author jajir
 *
 */
public class NodeStoreInMemTest extends AbstractMockingTest {

    private NodeStore<Integer> tested;

    @Test(expected = IllegalMonitorStateException.class)
    public void test_unlock_already_unlocked_node() throws Exception {
        jbNodeLockProvider.lockNode(3);
        jbNodeLockProvider.unlockNode(3);
        jbNodeLockProvider.unlockNode(3);
        expectLastCall().andThrow(new IllegalMonitorStateException());
        replay();
        tested.lockNode(3);

        tested.unlockNode(3);
        tested.unlockNode(3);
        verify();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        tested = new NodeStoreInMem<Integer, Integer>(nodeBuilder,
                jbNodeLockProvider);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        tested = null;
        super.tearDown();
    }
}
