package io.nuvolo.juice.business.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.mockito.junit.jupiter.MockitoExtension;

@Suite
@IncludeEngines("cucumber")
@ExtendWith(MockitoExtension.class)
@SelectClasspathResource("features/happy-path.feature")
public class HappyPathTest {
}
