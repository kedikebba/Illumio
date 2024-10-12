import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    static final String protocolNumbersPath = "src/testing_protocols";
    static final String tagsPath = "src/testing_tags";
    static final String flowLogsPath = "src/testing_flow_logs";

    @BeforeEach
    void setUp() throws IOException {

        createMockProtocolNumbersFile(protocolNumbersPath);
        createMockTagFile(tagsPath);
        createMockFlowLogFile(flowLogsPath);
    }

    @Test
    void testParseFlowLog() {

        Main.destPortProtocolMappingCount = new HashMap<>();
        Main.tagsCount = new HashMap<>();

        Main.protocolNumberMappings = Main.loadProtocolNumber(protocolNumbersPath);
        Main.tagMap = Main.parseTagFile(tagsPath);
        Main.parseFlowLog(flowLogsPath);

        System.out.println(Main.tagsCount);

        assertFalse(Main.tagsCount.isEmpty(), "tagsCount should not be empty");
        assertFalse(Main.tagsCount.containsKey("Untagged"), "Should not contain Untagged entries");

        assertFalse(Main.destPortProtocolMappingCount.isEmpty(), "destPortProtocolMappingCount should not be empty");
        assertTrue(Main.destPortProtocolMappingCount.containsKey("80"), "Should contain port 80");
        assertEquals(1, Main.destPortProtocolMappingCount.get("80").get("tcp"), "Port 80 with TCP should have 1 count");
    }

    @Test
    void testParseTagFile() {

        Map<String, Map<String, String>> tagMap = Main.parseTagFile(tagsPath);

        assertFalse(tagMap.isEmpty(), "Tag map should not be empty");
        assertTrue(tagMap.containsKey("80"), "Tag map should contain port 80");
        assertEquals("web", tagMap.get("80").get("tcp"), "Port 80 with TCP should map to 'web'");
    }

    @Test
    void testLoadProtocolNumber() {

        Map<String, String> protocolNumbers = Main.loadProtocolNumber(protocolNumbersPath);


        assertFalse(protocolNumbers.isEmpty(), "Protocol numbers should not be empty");
        assertEquals("tcp", protocolNumbers.get("6"), "Protocol number 6 should map to 'tcp'");
        assertEquals("udp", protocolNumbers.get("17"), "Protocol number 17 should map to 'udp'");
    }

    private void createMockFlowLogFile(String path) throws IOException {

        File file = new File(path);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 80 6 25 20000 1620140761 1620140821 ACCEPT OK \n");
            writer.write("2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 8080 20 25 20000 1620140761 1620140821 ACCEPT OK \n");
        }
    }

    private void createMockTagFile(String path) throws IOException {

        File file = new File(path);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("80,tcp,web\n");
        }
    }

    private void createMockProtocolNumbersFile(String path) throws IOException {

        File file = new File(path);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("6:tcp\n17:udp\n");
        }
    }
}
