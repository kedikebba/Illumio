import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    static List<String[]> unProcessedMappings = new ArrayList<>();
    static List<String[]> unProcessedFlowLogs = new ArrayList<>();
    static Map<String, Map<String, String>> tagMap;
    static Map<String, Integer> tagsCount;
    static Map<String, String> protocolNumberMappings;
    static  Map<String, Map<String, Integer>> destPortProtocolMappingCount;
    public static void main(String[] args) {

        String flowLogDataPath = "/Users/admin/Desktop/flow_logs";
        String tagsDataPath = "/Users/admin/Desktop/tags";
        String protocolsDataPath = "/src/protocol_numbers";

        tagsCount = new HashMap<>();
        destPortProtocolMappingCount = new HashMap<>();
        protocolNumberMappings = new HashMap<>();

        protocolNumberMappings = loadProtocolNumber(protocolsDataPath);
        tagMap = parseTagFile(tagsDataPath);
        parseFlowLog(flowLogDataPath);

        System.out.println("Tag,Count");
        System.out.println(tagsCount);

        System.out.println("dstport,protocol,tag ");
        System.out.println(destPortProtocolMappingCount);

    }

    static void parseFlowLog(String flowLogDataPath){


        try (BufferedReader br = new BufferedReader(new FileReader(flowLogDataPath))) {
            String line;


            while ((line = br.readLine()) != null) {

                String[] tokens = line.trim().split(" ");

                List<String> filteredTokens = Arrays.stream(tokens)
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(token -> !token.isBlank())
                        .filter(token -> !token.equals("null"))
                        .toList();

                if(filteredTokens.size()<14){
                    unProcessedFlowLogs.add(tokens);
                    continue;
                }

                String destPort = filteredTokens.get(6);
                String protocol = protocolNumberMappings.get(filteredTokens.get(7));

                // if we receive a protocol that is not defined by IANA, throw it under this unprocessed list for debugging.
                if(protocol==null){
                    unProcessedFlowLogs.add(tokens);
                    continue;
                }

                if( destPort.equals("-") || protocol.equals("-")) {
                    unProcessedFlowLogs.add(tokens);
                    continue;
                }

                if(tagMap.containsKey(destPort) &&
                        tagMap.get(destPort).get(protocol)!=null){

                    String tag = tagMap.get(destPort).get(protocol);

                    tagsCount.put(tag, tagsCount.getOrDefault(tag, 0) + 1);

                }else{
                    tagsCount.put("Untagged", tagsCount.getOrDefault("Untagged", 0) + 1);
                }

                if(destPortProtocolMappingCount.containsKey(destPort)){

                    Map<String,Integer> protocolNumberMap = destPortProtocolMappingCount.get(destPort);

                    protocolNumberMap.put(protocol, protocolNumberMap.getOrDefault(protocol, 0) + 1);

                    destPortProtocolMappingCount.put(destPort, protocolNumberMap);

                }else{
                    destPortProtocolMappingCount.put(destPort, Map.of(protocol, 1));
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }

    static Map<String, Map<String, String>> parseTagFile(String tagsDataPath){

        Map<String, Map<String, String>> tagMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(tagsDataPath))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] tokens = line.trim().split(",");

                List<String> filteredTokens = Arrays.stream(tokens)
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(token -> !token.isBlank())
                        .filter(token -> !token.equals("null")) // manually removing tokens labelled as "null"
                        .toList();

                if(filteredTokens.size()<3){
                    unProcessedMappings.add(tokens);
                    continue;
                }

                String port = filteredTokens.get(0);
                String protocol = filteredTokens.get(1);
                String tag = filteredTokens.get(2);

                if(tagMap.containsKey(port)){
                    tagMap.get(port).put(protocol, tag);
                }else{
                    tagMap.put(port, Map.of(protocol, tag));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tagMap;
    }


    static Map<String, String> loadProtocolNumber(String protocolDataPath){

        Map<String, String> protocolNumbers = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(protocolDataPath))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] tokens = line.trim().split(":");


                List<String> filteredTokens = Arrays.stream(tokens)
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(token -> !token.isBlank())
                        .filter(token -> !token.equals("null"))
                        .toList();

                String number = filteredTokens.get(0);
                String protocol = filteredTokens.get(1);

                protocolNumbers.put(number, protocol.toLowerCase());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return protocolNumbers;
    }

}