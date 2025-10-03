package MiniLLM_V2.utilities.activation_functions;

/**************************************************************/

public class ActivationFunctions
{

/**************************************************************/

    public static float relu(float x)
    {
        return Math.max(0, x);
    }

/**************************************************************/

    public static float reluDerivative(float x)
    {
        return x > 0 ? 1f : 0f;
    }

/**************************************************************/

}
