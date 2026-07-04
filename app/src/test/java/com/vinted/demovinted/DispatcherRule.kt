package com.vinted.demovinted // package (folder path)

import kotlinx.coroutines.Dispatchers // the coroutine thread pools
import kotlinx.coroutines.ExperimentalCoroutinesApi // opt-in marker for experimental API
import kotlinx.coroutines.test.TestDispatcher // a controllable test dispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher // runs coroutines eagerly
import kotlinx.coroutines.test.resetMain // restore the real Main dispatcher
import kotlinx.coroutines.test.setMain // swap in a test Main dispatcher
import org.junit.rules.TestWatcher // a rule with setup/teardown hooks
import org.junit.runner.Description // info about the running test

@OptIn(ExperimentalCoroutinesApi::class) // accept the experimental test API
class DispatcherRule( // a JUnit rule (like a pytest fixture)
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(), // default eager dispatcher
) : TestWatcher() {

    override fun starting(description: Description) { // before each test
        Dispatchers.setMain(testDispatcher) // make Main use the test dispatcher
    }

    override fun finished(description: Description) { // after each test
        Dispatchers.resetMain() // restore the real Main dispatcher
    }
}