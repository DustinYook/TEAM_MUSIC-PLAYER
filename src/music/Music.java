/** 
 * 12131819 YOOK DONGHYUN, 12345678 LEE JINHO
 * Java Application Programming-002 (Prof. Tamer) // Final Project
 * ============================================================================
 * update log
 * -----------------------------------------------------------------------------
 * - 2019.06.09 : code-refactoring and put some comments on each code (by YOOK)
 */
package music;

public class Music
{
	/** Member Fields */
	private int musicNum; // the number of music
	private String musicName; // the name of music
	private String musicTime; // the running time of music
	private String musicArtist; // the artist of music
	private String musicPath; //the path of music
	
	/** Constructor */
	public Music(int musicNum, String musicName, String musicArtist, String musicTime, String musicPath) 
	{
		super();
		this.musicNum = musicNum;
		this.musicName = musicName;
		this.musicTime = musicTime;
		this.musicArtist = musicArtist;
		this.musicPath = musicPath;
	}

	/** Getters & Setter for Music Number */
	public int getMusicNum() { return musicNum; }
	public void setMusicNum(int musicNum) { this.musicNum = musicNum; }

	/** Getters & Setter for Music Name */
	public String getMusicName() { return musicName; }
	public void setMusicName(String musicName) { this.musicName = musicName; }

	/** Getters & Setter for Music Time */
	public String getMusicTime() { return musicTime; }
	public void setMusicTime(String musicTime) { this.musicTime = musicTime; }

	/** Getters & Setter for Music Artist */
	public String getMusicArtist() { return musicArtist; }
	public void setMusicArtist(String musicArtist) { this.musicArtist = musicArtist; }
	
	/** Getters & Setter for Music Path */
	public String getMusicPath() { return musicPath; }
	public void setMusicPath(String musicPath) { this.musicPath = musicPath; }
}