/*
 * Copyright (c) 2020, 2021, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */
package com.oracle.coherence.concurrent.atomic;

import com.tangosol.net.Coherence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Tests for {@link AsyncRemoteAtomicMarkableReference}.
 *
 * @author Aleks Seovic  2020.12.09
 * @since 21.12
 */
public class AsyncRemoteAtomicMarkableReferenceTest
        extends AsyncAtomicMarkableReferenceTest
    {
    // ----- test lifecycle -------------------------------------------------

    @BeforeAll
    static void startUp()
        {
        Coherence.clusterMember().start().join();
        }

    @AfterAll
    static void shutDown()
        {
        Coherence.closeAll();
        }

    // ----- AsyncAtomicMarkableReferenceTest methods -----------------------

    protected AsyncAtomicMarkableReference<String> asyncValue()
        {
        RemoteAtomicMarkableReference<String> ref = Atomics.remoteAtomicMarkableReference("value");
        return ref.async();
        }
    }
