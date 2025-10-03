package MiniLLM_V2.debug;

/***********************************************************************************************************************/

public class Debug
{
	//
	// Gradient Debug
	//
	public static boolean 	gradientDebugClippingFlag		= false;
	//
	// Debug Control Flags
	//
	// For 1D Arrays
	//
	public static boolean 	ArrayPrinterTrainingSampleFlag	= false;
	public static boolean 	ArrayPrinterTokenFlag 			= false;
	public static boolean 	ArrayPrinterFloatFlag 			= true;
	public static boolean 	ArrayPrinterDoubleFlag			= true;
	public static boolean 	ArrayPrinterIntFlag 			= true;
	//
	// For 2D Arrays
	//
	public static boolean 	MatrixPrinterFloatFlag	 		= true;
	public static boolean 	MatrixPrinterDoubleFlag 		= false;
	//
	// For Prediction Debugging
	//
	public static boolean	PredictionPrinterFlag			= false;
	//
	// For some Model's Sections
	//
	public static boolean	ModelCreationSectionFlag		= true;
	public static boolean	ModelForwardSectionFlag			= false;
	//
	// For Model's One Cycle Trace Flag
	//
	public static boolean	ModelOneCycleTraceFlag			= false;

/***********************************************************************************************************************/

}

