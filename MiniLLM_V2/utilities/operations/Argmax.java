package MiniLLM_V2.utilities.operations;

public class Argmax
{
    public static int argmax(float[] array)
    {
        if (array == null || array.length == 0) return -1;

        int maxIndex = 0;

        for (int i = 1; i < array.length; i++)
        {
            if (array[i] > array[maxIndex])
            {
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
