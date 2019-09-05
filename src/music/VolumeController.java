package music;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class VolumeController extends Thread
{
	public float volume;
	
	/* Constructor */
	public VolumeController(float volume) 
	{
		this.volume = volume;
		volumeControl(this.volume); 
	}
	
	public void volumeControl(float volume)
	{
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		
        for (Mixer.Info mixerInfo : mixers) 
        {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source changes all the volumes
 
            for (Line.Info lineInfo : lineInfos) 
            {
                Line line = null;
                boolean opened = true;
                try 
                {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    
                    if (!opened) 
                        line.open();
                    
                    FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    volCtrl.setValue(volume);
                } 
                catch (LineUnavailableException e) { e.printStackTrace(); } 
                catch (IllegalArgumentException iaEx) { /* Empty */ } 
                finally 
                {
                    if (line != null && !opened) 
                        line.close();
                }
            }
        }
	}
}