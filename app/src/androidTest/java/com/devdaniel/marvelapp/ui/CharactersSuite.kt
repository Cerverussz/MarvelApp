package com.devdaniel.marvelapp.ui

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    CharactersFragmentTest::class,
    CharactersIntegrationTest::class
)
class CharactersSuite
