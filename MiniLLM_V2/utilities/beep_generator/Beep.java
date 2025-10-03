package MiniLLM_V2.utilities.beep_generator;

import javax.sound.sampled.*;

//**********************************************************************************************************//

public class Beep
{

//**********************************************************************************************************//

    public static void playBeep(double frequency, int durationMillis, float volume)
    {
        try
        {
            float sampleRate = 44100;
            int sampleSizeInBits = 16;
            int channels = 1;
            boolean signed = true;
            boolean bigEndian = false;

            AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            SourceDataLine line = AudioSystem.getSourceDataLine(af);
            line.open(af);
			//
            // Get the Master Gain control
            //
            if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB); 											// Set volume in decibels
            }

            line.start();

            byte[] buffer = new byte[(int) (sampleRate * durationMillis / 1000 * af.getFrameSize())];
            double twoPiF = 2 * Math.PI * frequency;

            for (int i = 0; i < buffer.length / af.getFrameSize(); i++)
            {
                double angle = i / (sampleRate / frequency) * twoPiF;
                short val = (short) (Math.sin(angle) * Short.MAX_VALUE);

                buffer[i * 2] = (byte) (val & 0xFF);
                buffer[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
            }

            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();

        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

//**********************************************************************************************************//

    public static void main(String[] args)
    {
		double 	frequency		= 48000;

		int 	durationMillis 	= 500;

		float 	volume			= 0.2f;			// volume is a float between 0.0 (silent) and 1.0 (max)
		//
        // Example: play a 1000 Hz beep for 500 milliseconds at 50% volume
        //
        playBeep(48000, 500, 0.2f); // volume is a float between 0.0 (silent) and 1.0 (max)
    }

//**********************************************************************************************************//

}
