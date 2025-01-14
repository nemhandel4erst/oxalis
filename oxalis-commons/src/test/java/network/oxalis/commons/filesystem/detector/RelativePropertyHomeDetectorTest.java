/*
 * Copyright 2010-2018 Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package network.oxalis.commons.filesystem.detector;

import network.oxalis.api.filesystem.HomeDetector;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author ERST
 */
public class RelativePropertyHomeDetectorTest {

    private HomeDetector homeDetector = new RelativePropertyHomeDetector();

    @BeforeClass
    public void setup(){
        System.setProperty("catalina.base", "/catalina");
    }

    @AfterClass
    public void cleanup(){
        System.clearProperty("catalina.base");
    }

    @Test
    public void testFromJavaSystemProperty() {
        String path = new File("/catalina/some/system/path2").getAbsolutePath();
        String backup = System.getProperty(RelativePropertyHomeDetector.VARIABLE);

        try {
            System.clearProperty(RelativePropertyHomeDetector.VARIABLE);
            File oxalis_home = homeDetector.detect();
            assertNull(oxalis_home);

            System.setProperty(RelativePropertyHomeDetector.VARIABLE, "");
            oxalis_home = homeDetector.detect();
            assertNull(oxalis_home);

            System.setProperty(RelativePropertyHomeDetector.VARIABLE, "some/system/path2");
            oxalis_home = homeDetector.detect();
            assertEquals(oxalis_home.getAbsolutePath(), path);

            System.setProperty(RelativePropertyHomeDetector.VARIABLE, "/some/system/path2");
            oxalis_home = homeDetector.detect();
            assertEquals(oxalis_home.getAbsolutePath(), path);
        } finally {
            if (backup == null) backup = ""; // prevent null pointer exception
            System.setProperty(RelativePropertyHomeDetector.VARIABLE, backup);
        }
    }
}
