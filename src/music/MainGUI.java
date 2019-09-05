/** 
 * 12131819 YOOK DONGHYUN, 12345678 LEE JINHO
 * Java Application Programming-002 (Prof. Tamer) // Final Project
 * ============================================================================
 * update log
 * -----------------------------------------------------------------------------
 * - 2019.06.09 : code-refactoring and put some comments on each code (by YOOK)
 */
package music;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JList;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainGUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	/** GUI Components */
	private JFrame frame;
	private JPanel pnlSongName;
	private JLabel lblSongTitle;
	private JLabel lblArtistTitle;
	private JPanel pnlOutput;
	private JPanel pnlTimeOutput;
	private JPanel pnlMenu;
	private JLabel lblOpenFile;
	private JLabel lblOpenPlaylist;
	private JLabel lblAddSong;
	private JLabel lblNewPlaylist;
	private JLabel lblRemoveSong;
	private JLabel lblSavePlaylist;
	private JLabel lblRemovePlaylist;
	private JButton btnOpenFile;
	private JButton btnAddSong;
	private JButton btnRemoveSong;
	private JButton btnOpenPlaylist;
	private JButton btnNewPlaylist;
	private JButton btnSavePlaylist;
	private JButton btnRemovePlaylist;
	private JPanel pnlList;
	private JPanel pnlShowList;
	private JLabel lblSongName;
	private JLabel lblArtist;
	private JLabel lblDuration;
	private JScrollPane scrollPane;
	private JPanel pnlTime;
	private JPanel pnlController;
	private JLabel lblTime;
	private JProgressBar progressBar;
	private JButton btnBack;
	private JButton btnPlay;
	private JButton btnPause;
	private JButton btnStop;
	private JButton btnNext;
	private JLabel lblVol;
	private JSlider slider;
	private JPanel pnlButton;
	private JPanel pnlVolume;
	private JLabel lblVolSize;
	
	private JList list;
	private DefaultListModel listModel;

	/** User-defined Member Fields */
	private ArrayList<String> musicList; // list for string
	private ArrayList<Music> music; // list for Music object
	
	private int musicNum; // number of music
	private String musicName; // name of music
	private String musicArtist; // artist of music
	private String musicTime; // duration of music 
	private String musicPath; // path of music 
	private String fileName; // file name of music 
	private String filePath; // file path of music
	
	private MusicPlayer player = new MusicPlayer(); // thread for MP3 playing
	private VolumeController volumeController = new VolumeController(0.5F); // thread for volume controlling
	
	private boolean pause_flag = false; // control pause & play mechanism

	/** Launch the application */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} 
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	/** Create the application */
	public MainGUI() { initialize(); }

	/** Initialize the contents of the frame */
	private void initialize() 
	{
		/* JFrame */
		try 
		{
			UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel"); // Apply Look and Feel
			JFrame.setDefaultLookAndFeelDecorated(true);
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) { e.printStackTrace(); }
		frame = new JFrame("Music Player");
		frame.setBounds(100, 100, 565, 642);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/** ====================== Header of music player ========================== */
		/* Panel for song name */
		pnlSongName = new JPanel();
		pnlSongName.setBorder(new EmptyBorder(20, 10, 10, 10));
		frame.getContentPane().add(pnlSongName, BorderLayout.NORTH);
		pnlSongName.setLayout(new GridLayout(0, 1, 0, 0));
		
		/* Label for song title */
		lblSongTitle = new JLabel("Sample Song Name");
		lblSongTitle.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
		pnlSongName.add(lblSongTitle);
		
		/* Label for artist title */
		lblArtistTitle = new JLabel("Sample Artist Name");
		lblArtistTitle.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		pnlSongName.add(lblArtistTitle);
		
		/** ====================== Body of music player =========================== */
		/* Panel for Output */
		pnlOutput = new JPanel();
		pnlOutput.setBorder(new EmptyBorder(0, 10, 10, 10));
		frame.getContentPane().add(pnlOutput, BorderLayout.CENTER);
		GridBagLayout gbl_pnlOutput = new GridBagLayout();
		gbl_pnlOutput.columnWidths = new int[]{400, 0};
		gbl_pnlOutput.rowHeights = new int[]{20, 162, 0, 0};
		gbl_pnlOutput.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlOutput.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		pnlOutput.setLayout(gbl_pnlOutput);
		
		/* ======================== 1. Time output =================================== */
		pnlTimeOutput = new JPanel();
		GridBagConstraints gbc_pnlTimeOutput = new GridBagConstraints();
		gbc_pnlTimeOutput.insets = new Insets(0, 0, 5, 0);
		gbc_pnlTimeOutput.anchor = GridBagConstraints.NORTH;
		gbc_pnlTimeOutput.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlTimeOutput.gridx = 0;
		gbc_pnlTimeOutput.gridy = 0;
		pnlOutput.add(pnlTimeOutput, gbc_pnlTimeOutput);
		pnlTimeOutput.setLayout(new GridLayout(0, 1, 0, 0));
		
		/* Panel for time */
		pnlTime = new JPanel();
		FlowLayout flowLayout = (FlowLayout) this.pnlTime.getLayout();
		flowLayout.setHgap(1);
		pnlTimeOutput.add(pnlTime);
		
		/* Label for time */
		lblTime = new JLabel("Time");
		lblTime.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		pnlTime.add(lblTime);
		
		/* Progress bar */
		progressBar = new JProgressBar();
		pnlTime.add(progressBar);
		
		/* ======================== 2. Controller =================================== */
		pnlController = new JPanel();
		pnlTimeOutput.add(pnlController);
		this.pnlController.setLayout(new GridLayout(2, 0, 0, 0));
		
		/** ====================== a. Panel for MP3 control buttons ================ */
		this.pnlButton = new JPanel();
		this.pnlController.add(this.pnlButton);
		
		/* Back button */
		btnBack = new JButton(new ImageIcon("src/back.png"));
		this.pnlButton.add(this.btnBack);
		btnBack.addActionListener(this);
		
		/* Play button */
		btnPlay = new JButton(new ImageIcon("src/play.png"));
		this.pnlButton.add(this.btnPlay);
		btnPlay.addActionListener(this);
		
		/* Pause button */
		btnPause =  new JButton(new ImageIcon("src/pause.png"));
		this.pnlButton.add(this.btnPause);
		btnPause.addActionListener(this);
		
		/* Stop button */
		btnStop = new JButton(new ImageIcon("src/stop.png"));
		this.pnlButton.add(this.btnStop);
		btnStop.addActionListener(this);
		
		/* Next button */
		btnNext = new JButton(new ImageIcon("src/next.png"));
		this.pnlButton.add(this.btnNext);
		btnNext.addActionListener(this);
	
		/** ================= b. Panel for volume control ========================= */
		this.pnlVolume = new JPanel();
		this.pnlController.add(this.pnlVolume);
		this.pnlVolume.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		/* Label for volume */
		lblVol = new JLabel(new ImageIcon("src/volume.png"));
		this.pnlVolume.add(this.lblVol);
		
		/* Slider for volume */
		volumeController.start();
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() // if detect change on slider
		{
            @Override
            public void stateChanged(ChangeEvent e) 
            {
            	 JSlider source = (JSlider)e.getSource(); // get information
                 if (!source.getValueIsAdjusting())
                 {
                	System.out.println("Set volume to " + (int)source.getValue()); // print out for testing
                	volumeController.volumeControl((float)(source.getValue())/100); // set volume at desired level
                	
                	lblVolSize.setText("" + (int)source.getValue()); // update volume size label
                 }
            }
        });
		this.pnlVolume.add(this.slider);
		
		/* Label for Volume Size */
		this.lblVolSize = new JLabel("50"); // default volume size is 50
		this.lblVolSize.setFont(new Font("Dialog", Font.BOLD, 18));
		this.pnlVolume.add(this.lblVolSize);
		
		/* ======================== 3. Music List ======================================== */
		/* Panel for List */
		pnlList = new JPanel();
		GridBagConstraints gbc_pnlList = new GridBagConstraints();
		gbc_pnlList.insets = new Insets(0, 0, 5, 0);
		gbc_pnlList.fill = GridBagConstraints.BOTH;
		gbc_pnlList.gridx = 0;
		gbc_pnlList.gridy = 1;
		pnlOutput.add(pnlList, gbc_pnlList);
		pnlList.setLayout(new BorderLayout(0, 0));
		
		/* Panel for ShowList */
		pnlShowList = new JPanel();
		pnlList.add(pnlShowList, BorderLayout.NORTH);
		GridBagLayout gbl_pnlShowList = new GridBagLayout();
		gbl_pnlShowList.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlShowList.rowHeights = new int[]{0, 0};
		gbl_pnlShowList.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlShowList.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlShowList.setLayout(gbl_pnlShowList);
		
		/* Label for Song Name */
		lblSongName = new JLabel("Song name");
		lblSongName.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblSongName = new GridBagConstraints();
		gbc_lblSongName.insets = new Insets(0, 0, 0, 5);
		gbc_lblSongName.gridx = 1;
		gbc_lblSongName.gridy = 0;
		pnlShowList.add(lblSongName, gbc_lblSongName);
		
		/* Label for Artist */
		lblArtist = new JLabel("Artist");
		lblArtist.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblArtist = new GridBagConstraints();
		gbc_lblArtist.insets = new Insets(0, 0, 0, 5);
		gbc_lblArtist.gridx = 5;
		gbc_lblArtist.gridy = 0;
		pnlShowList.add(lblArtist, gbc_lblArtist);
		
		/* Label for Duration */
		lblDuration = new JLabel("Duration");
		lblDuration.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblDuration = new GridBagConstraints();
		gbc_lblDuration.gridx = 10;
		gbc_lblDuration.gridy = 0;
		pnlShowList.add(lblDuration, gbc_lblDuration);
		scrollPane = new JScrollPane();
		pnlList.add(scrollPane, BorderLayout.CENTER);
		
		/* JList */
		listModel = new DefaultListModel(); // instantiate DefaultListModel
		musicList = new ArrayList<String>(); // instantiate ArrayList MusicList
		music = new ArrayList<Music>(); // instantiate ArrayList for Music
		list = new JList(listModel); // instantiate JList with DefaultListModel
		list.setVisibleRowCount(1);
		scrollPane.setViewportView(list);

		/* ====================== 4. File control buttons ============================ */
		/* Panel for Control Menu */
		pnlMenu = new JPanel();
		GridBagConstraints gbc_pnlMenu = new GridBagConstraints();
		gbc_pnlMenu.fill = GridBagConstraints.BOTH;
		gbc_pnlMenu.gridx = 0;
		gbc_pnlMenu.gridy = 2;
		pnlOutput.add(pnlMenu, gbc_pnlMenu);
		GridBagLayout gbl_pnlMenu = new GridBagLayout();
		gbl_pnlMenu.columnWidths = new int[]{90, 0, 0, 0, 200, 0};
		gbl_pnlMenu.rowHeights = new int[]{50, 50, 50, 50, 0};
		gbl_pnlMenu.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlMenu.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlMenu.setLayout(gbl_pnlMenu);

		/* Open File Button */
		btnOpenFile = new JButton(new ImageIcon("src/OpenFile.png"));
		btnOpenFile.addActionListener(this);
		// Layout for Open file
		GridBagConstraints gbc_btnOpenFile = new GridBagConstraints();
		gbc_btnOpenFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenFile.gridx = 1;
		gbc_btnOpenFile.gridy = 0;
		pnlMenu.add(btnOpenFile, gbc_btnOpenFile);
		// Label for Open File
		lblOpenFile = new JLabel("Open File");
		lblOpenFile.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblOpenFile = new GridBagConstraints();
		gbc_lblOpenFile.fill = GridBagConstraints.BOTH;
		gbc_lblOpenFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblOpenFile.gridx = 2;
		gbc_lblOpenFile.gridy = 0;
		pnlMenu.add(lblOpenFile, gbc_lblOpenFile);
		
		/* Open Play List Button */
		btnOpenPlaylist = new JButton(new ImageIcon("src/OpenPlayList.png"));
		btnOpenPlaylist.addActionListener(this);
		// Layout for Open Play List
		GridBagConstraints gbc_btnOpenPlaylist = new GridBagConstraints();
		gbc_btnOpenPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenPlaylist.gridx = 3;
		gbc_btnOpenPlaylist.gridy = 0;
		pnlMenu.add(btnOpenPlaylist, gbc_btnOpenPlaylist);
		// Label for Open Play List
		lblOpenPlaylist = new JLabel("Open Playlist");
		lblOpenPlaylist.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblOpenPlaylist = new GridBagConstraints();
		gbc_lblOpenPlaylist.fill = GridBagConstraints.BOTH;
		gbc_lblOpenPlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_lblOpenPlaylist.gridx = 4;
		gbc_lblOpenPlaylist.gridy = 0;
		pnlMenu.add(lblOpenPlaylist, gbc_lblOpenPlaylist);
		
		/* Add Song Button */
		btnAddSong = new JButton(new ImageIcon("src/AddSong.png"));
		btnAddSong.addActionListener(this);
		// Layout for Add Song
		GridBagConstraints gbc_btnAddSong = new GridBagConstraints();
		gbc_btnAddSong.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddSong.gridx = 1;
		gbc_btnAddSong.gridy = 1;
		pnlMenu.add(btnAddSong, gbc_btnAddSong);
		// Label for Add Song
		lblAddSong = new JLabel("Add Song");
		lblAddSong.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblAddSong = new GridBagConstraints();
		gbc_lblAddSong.fill = GridBagConstraints.BOTH;
		gbc_lblAddSong.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddSong.gridx = 2;
		gbc_lblAddSong.gridy = 1;
		pnlMenu.add(lblAddSong, gbc_lblAddSong);
		
		/* New Play List Button */
		btnNewPlaylist = new JButton(new ImageIcon("src/NewPlayList.png"));
		btnNewPlaylist.addActionListener(this);
		// Layout for New Play List
		GridBagConstraints gbc_btnNewPlaylist = new GridBagConstraints();
		gbc_btnNewPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewPlaylist.gridx = 3;
		gbc_btnNewPlaylist.gridy = 1;
		pnlMenu.add(btnNewPlaylist, gbc_btnNewPlaylist);
		// Label for New Play List
		lblNewPlaylist = new JLabel("New Playlist");
		lblNewPlaylist.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewPlaylist = new GridBagConstraints();
		gbc_lblNewPlaylist.fill = GridBagConstraints.BOTH;
		gbc_lblNewPlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewPlaylist.gridx = 4;
		gbc_lblNewPlaylist.gridy = 1;
		pnlMenu.add(lblNewPlaylist, gbc_lblNewPlaylist);
		
		/* Remove Song Button */
		btnRemoveSong = new JButton(new ImageIcon("src/RemoveSong.png"));
		btnRemoveSong.addActionListener(this);
		// Layout for Remove Song
		GridBagConstraints gbc_btnRemoveSong = new GridBagConstraints();
		gbc_btnRemoveSong.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveSong.gridx = 1;
		gbc_btnRemoveSong.gridy = 2;
		pnlMenu.add(btnRemoveSong, gbc_btnRemoveSong);
		// Label for Remove Song
		lblRemoveSong = new JLabel("Remove Song");
		lblRemoveSong.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblRemoveSong = new GridBagConstraints();
		gbc_lblRemoveSong.fill = GridBagConstraints.BOTH;
		gbc_lblRemoveSong.insets = new Insets(0, 0, 5, 5);
		gbc_lblRemoveSong.gridx = 2;
		gbc_lblRemoveSong.gridy = 2;
		pnlMenu.add(lblRemoveSong, gbc_lblRemoveSong);
		
		/* Save Play List Button */
		btnSavePlaylist = new JButton(new ImageIcon("src/SavePlayList.png"));
		btnSavePlaylist.addActionListener(this);
		// Layout for Save Play List
		GridBagConstraints gbc_btnSavePlaylist = new GridBagConstraints();
		gbc_btnSavePlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnSavePlaylist.gridx = 3;
		gbc_btnSavePlaylist.gridy = 2;
		pnlMenu.add(btnSavePlaylist, gbc_btnSavePlaylist);
		// Label for Save Play List
		lblSavePlaylist = new JLabel("Save Playlist");
		lblSavePlaylist.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblSavePlaylist = new GridBagConstraints();
		gbc_lblSavePlaylist.fill = GridBagConstraints.BOTH;
		gbc_lblSavePlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavePlaylist.gridx = 4;
		gbc_lblSavePlaylist.gridy = 2;
		pnlMenu.add(lblSavePlaylist, gbc_lblSavePlaylist);
		
		/* Remove Play List Button */
		btnRemovePlaylist = new JButton(new ImageIcon("src/RemovePlayList.png"));
		btnRemovePlaylist.addActionListener(this);
		// Layout for Remove Play List
		GridBagConstraints gbc_btnRemovePlaylist = new GridBagConstraints();
		gbc_btnRemovePlaylist.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemovePlaylist.gridx = 3;
		gbc_btnRemovePlaylist.gridy = 3;
		pnlMenu.add(btnRemovePlaylist, gbc_btnRemovePlaylist);
		// Label for Remove Play List
		lblRemovePlaylist = new JLabel("Remove Playlist");
		lblRemovePlaylist.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		GridBagConstraints gbc_lblRemovePlaylist = new GridBagConstraints();
		gbc_lblRemovePlaylist.fill = GridBagConstraints.BOTH;
		gbc_lblRemovePlaylist.gridx = 4;
		gbc_lblRemovePlaylist.gridy = 3;
		pnlMenu.add(lblRemovePlaylist, gbc_lblRemovePlaylist);	
	}
	
	public void updateTitleLabel() // update title label according to which music is on play
	{
		lblSongTitle.setText(music.get(list.getSelectedIndex()).getMusicName());
		lblArtistTitle.setText(music.get(list.getSelectedIndex()).getMusicArtist());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		/** ================================ 1. MP3 playing manipulation ============================== */
		/* Back Button */
		if(e.getSource() == btnBack)
		{
			try
			{
				try
				{
					System.out.println("Back"); // for test only
					player.player.close(); // close Player object (terminate MP3 playing)
				}
				catch(Exception ex) { System.err.println("There is no MP3 playing thread"); }
				finally
				{
					if(list.getSelectedIndex() > 0) // if it is not the first song
					{
						list.setSelectedIndex(list.getSelectedIndex() - 1); // go previous
						updateTitleLabel(); // and update label
					}
					else // print error message on console
						throw new Exception("There is no previous song");
				}
			}
			catch (Exception e1) { System.err.println(e1); }
			finally
			{
				String path = music.get(list.getSelectedIndex()).getMusicPath(); // get path from selected list item
				player.setPath(path); // set path
				player.play(-1); // play the song
			}
		}
		
		/* Play Button */
		if(e.getSource() == btnPlay)
		{
			if(pause_flag == false) // never paused before
			{
				System.out.println("Play"); // for test only
				String path = music.get(list.getSelectedIndex()).getMusicPath(); // get path from selected list item
				updateTitleLabel(); // update song title label
				player.setPath(path); // set path
				player.play(-1); // play the song
			}
			else // paused at least once
			{
				System.out.println("Resume"); // for test only
				player.resume(); // resume MP3 playing
			}
		}
		
		/* Pause Button */
		if(e.getSource() == btnPause)
		{
			System.out.println("Pause"); // for test only
			pause_flag = true; // set flag that it is paused at least once
			player.pause(); // pause MP3 playing
		}
		
		/* Stop Button */
		if(e.getSource() == btnStop)
		{
			try 
			{
				System.out.println("Stop"); // for test only
				player.player.close(); // close Player object (terminate MP3 playing)
				pause_flag = false; // stop means it set playing situation as default
			}
			catch(Exception ex) { System.err.println("MP3 Playing thread is terminated"); }
		}
		
		/* Next Button */
		if(e.getSource() == btnNext)
		{
			try
			{
				try 
				{
					System.out.println("Next"); // for test only
					player.player.close(); // close Player object (terminate MP3 playing)
				}
				catch(Exception ex1) { System.err.println("There is no MP3 playing thread"); }
				finally
				{
					if(list.getSelectedIndex() < listModel.getSize() - 1) // if it reaches the end of the list
					{
						list.setSelectedIndex(list.getSelectedIndex() + 1);
						updateTitleLabel();  // update song title label
					}
					else
						throw new Exception("There is no next song!");
				}
			}
			catch(Exception ex2) { System.err.println(ex2); }
			finally
			{
				String path = music.get(list.getSelectedIndex()).getMusicPath(); // get path from selected list item
				player.setPath(path); // set path
				player.play(-1); // play the song
			}
		}
		
		/** ================================ 2. File manipulation =========================================== */
		/* Open File Button */
		if(e.getSource() == btnOpenFile) 
		{
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3","mp3");
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(MainGUI.this);
			
			if(returnVal==JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				fileName=null;
				filePath=null;
				
				try 
				{
					FileReader fis = new FileReader(file);
					BufferedReader reader = new BufferedReader(fis);
					
					String fileName = file.getName(); // get file name
					String[] split = fileName.split(";"); // use semicolon as denominator
					
					musicName = split[0]; // set name
					String temp = split[1]; 
					String[] tempArr = temp.split(".mp3");
					musicArtist = tempArr[0]; // set artist
					/** Need to be handled */
					musicTime = "01:00"; // set playing time
					musicPath = file.getPath(); // set path
					
					lblSongTitle.setText(musicName); // set song title label
					lblArtistTitle.setText(musicArtist); // set 
					
					musicNum = 0;
					
					listModel.clear();
					musicList.clear();
					music.clear();
					
					music.add(new Music(musicNum+1,musicName,musicArtist,musicTime,musicPath)); // add to music
					musicList.add(music.get(musicNum).getMusicNum() + "\t\t\t\t\t" 
								+ music.get(musicNum).getMusicName() + "\t\t\t\t\t"
								+ music.get(musicNum).getMusicArtist() + "\t\t\t\t\t"
								+ music.get(musicNum).getMusicTime()); // add to music list
					
					for(String song : musicList) 
						listModel.addElement(song);
				} 
				catch (FileNotFoundException e1) { e1.printStackTrace(); }
			}
			list.setSelectedIndex(0); // put focus on first element of list
		}
		
		/* Add Song Button */
		if(e.getSource() == btnAddSong) 
		{
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3","mp3");
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(MainGUI.this);
			
			if(returnVal==JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				
				try 
				{
					FileReader fis = new FileReader(file);
					String fileName = file.getName();
					String[] split = fileName.split("; ");
					
					musicName = split[0]; // set name
					String temp = split[1]; 
					String[] tempArr = temp.split(".mp3");
					musicArtist = tempArr[0]; // set artist
					/** Need to be handled */
					musicTime = "02:00";
					musicPath = file.getPath();
					listModel.clear();
					
					musicNum +=1; // increase number
					music.add(new Music(musicNum+1,musicName,musicArtist,musicTime,musicPath)); // add to music
					musicList.add(music.get(musicNum).getMusicNum() + "\t\t\t\t\t"
								+ music.get(musicNum).getMusicName() + "\t\t\t\t\t"
								+ music.get(musicNum).getMusicArtist() + "\t\t\t\t\t"
								+ music.get(musicNum).getMusicTime()); // add to music list
					
					for(String song : musicList) // add to DefaultListModel to show on GUI
						listModel.addElement(song);
				} 
				catch (FileNotFoundException e1) { e1.printStackTrace(); }
			}
			list.setSelectedIndex(0); // put focus on first element of list
		}
		
		/* Remove Song Button */
		if(e.getSource() == btnRemoveSong) 
		{
			 int delCount = 0;
		     for (int pos : list.getSelectedIndices()) 
		     {
		    	 listModel.remove(pos - delCount);
		         delCount++;
		     }
		}
		
		/* Open Play List Button */
		if(e.getSource() == btnOpenPlaylist) 
		{
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter fnef1 = new FileNameExtensionFilter("txt","txt");
			fc.setFileFilter(fnef1);
			int returnVal = fc.showOpenDialog(MainGUI.this);
			
			if(returnVal==JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				
				try 
				{
					// initialize 
					music.clear();
					musicList.clear();
					listModel.clear();
					
					fileName = file.getName(); // get file name 
					filePath = file.getPath(); // get file path
					
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
				
					String sCurrentLine;
					while((sCurrentLine = br.readLine()) != null) 
					{
						String[] split = sCurrentLine.split("\t"); // use tab as denominator
						
						musicNum = Integer.parseInt(split[0]) - 1; // set number
						musicName = split[1]; // set name
						musicArtist = split[2]; // set artist
						musicTime = split[3]; // set playing time
						musicPath = split[4]; // set path
						
						music.add(new Music(musicNum+1,musicName,musicArtist,musicTime,musicPath)); // add to music
						musicList.add(music.get(musicNum).getMusicNum()+"\t\t\t\t\t" 
									+ music.get(musicNum).getMusicName()+"\t\t\t\t\t"
									+ music.get(musicNum).getMusicArtist()+"\t\t\t\t\t"
									+ music.get(musicNum).getMusicTime()); // add to music list
					}
					br.close();
					
					for(String song : musicList) // add to DefaultListModel to show on GUI
						listModel.addElement(song);
				} 
				catch (IOException e1) { e1.printStackTrace(); } 
			}
			list.setSelectedIndex(0); // focus on first element of list
		}
		
		/* New Play List Button */
		if(e.getSource() == btnNewPlaylist) 
		{
			music.clear();
			musicList.clear();
			listModel.clear();
		}
		
		/* Save Play List Button */
		if(e.getSource() == btnSavePlaylist) 
		{
			if(fileName == null) // if play list does not exist, create new one 
			{
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter fnef1 = new FileNameExtensionFilter("txt","txt");
				fc.setFileFilter(fnef1);
				int returnVal = fc.showSaveDialog(MainGUI.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) 
				{
					File file = new File(fc.getSelectedFile()+".txt"); // save file in text format
					
					try 
					{
						fileName = file.getName(); // set file name
						filePath = file.getPath(); // set file path
						
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						
						for(int i = 0; i <= musicNum; i++) // update music list 
						{
							bw.write(musicList.get(i)+"\t"+music.get(i).getMusicPath());
							bw.newLine();
						}
						
						bw.flush();
						bw.close();
						System.out.println("Save success");
					} 
					catch (IOException e1) { e1.printStackTrace(); }
				}
			}
			else // if play list already exists, just update it
			{
				try 
				{
					File file = new File(filePath);
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				
					for(int i=0; i <= musicNum; i++) 
					{
						bw.write(musicList.get(i));
						bw.newLine();
					}
					bw.flush();
					bw.close();
				} 
				catch (IOException e1) { e1.printStackTrace(); }
			}
		}

		/* Remove Play List Button */
		if(e.getSource() == btnRemovePlaylist) 
		{
			try
			{
				File file = new File(filePath);
	    		if(fileName != null) // In case, play list exists
	    		{
	    			file.delete();
	    			System.out.println(fileName + " is deleted!");
	    			fileName = null;
		    		filePath = null;
	    		}
	    		else // In case, play list does not exist
	    			throw new Exception("Delete operation is failed!");
	    	}
			catch(Exception e1) { System.err.println(e1); }
			
			music.clear();
			musicList.clear();
		    listModel.clear();
		}
	}
}