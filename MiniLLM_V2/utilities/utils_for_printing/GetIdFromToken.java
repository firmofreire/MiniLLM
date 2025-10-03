package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Map;
import java.util.HashMap;

import MiniLLM_V2.create.Tokenizer;

/**************************************************************************************************************/

public class GetIdFromToken
{
    private static Map<String, Integer> stringToIntMap;

/**************************************************************************************************************/

    public static Integer getIntegerFromString(String key, Map<String, Integer> originalMap)
    {
		stringToIntMap = new HashMap<>(originalMap);

        return stringToIntMap.get(key);
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

        Integer resultStringToInteger = GetIdFromToken.getIntegerFromString("one", sampleMap);

        System.out.println("Token: one -> Id: " + resultStringToInteger);
    }

/**************************************************************************************************************/

}
