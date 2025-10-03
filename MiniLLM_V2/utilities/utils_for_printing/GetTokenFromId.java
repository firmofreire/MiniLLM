package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import MiniLLM_V2.create.Tokenizer;

/**************************************************************************************************************/

public class GetTokenFromId
{
    private static Map<String, Integer> stringToIntMap;
    private static Map<Integer, String> intToStringMap;

/**************************************************************************************************************/

    public static String getStringFromInteger(Integer value, Map<String, Integer> originalMap)
    {
        stringToIntMap = new HashMap<>(originalMap);
        intToStringMap = new HashMap<>();
		//
        // Create Reverse Mapping
        //
        for (Entry<String, Integer> entry : originalMap.entrySet())
        {
            intToStringMap.put(entry.getValue(), entry.getKey());
        }

        return intToStringMap.get(value);
    }

/**************************************************************************************************************/


    public static void main(String[] args)
    {
		//
		// Example Usage
		//
        // Create a Sample Map
        //
        // ... add more entries up to 10
        //
        Map<String, Integer> sampleMap = new HashMap<>();
        sampleMap.put("one", 1);
        sampleMap.put("two", 2);
        sampleMap.put("three", 3);

        String resultIntegerToString = GetTokenFromId.getStringFromInteger(2, sampleMap);
        System.out.println("Id: 2 -> Token: " + resultIntegerToString);
    }

/**************************************************************************************************************/

}
