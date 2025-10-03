package MiniLLM_V2._exec_ui;

import javax.swing.*;

/*************************************************************************************/

public class _MainUI
{

/*************************************************************************************/

	public static void main(String args[])
    {
 		SwingUtilities.invokeLater(() ->
		{
			try
	 		{
				BuildLLMMenu bllm = new BuildLLMMenu();

				bllm.initBuildLLMMenu();
			}
			catch(Exception e)
			{
				e.printStackTrace();

				System.out.println("Treatment of UI exception: e ="  + e);
			}
		});
    }

/*************************************************************************************/
}
