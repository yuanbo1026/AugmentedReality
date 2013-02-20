package dk.au.augcard;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import dk.au.augcard.models.Model;
import dk.au.augcard.parser.ObjParser;
import dk.au.augcard.parser.ParseException;
import dk.au.augcard.util.AssetsFileUtil;
import dk.au.augcard.util.BaseFileUtil;

import android.os.Bundle;
import android.os.Handler;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;

public class MainActivity extends AndARActivity {

	public static final int INDEX_ANDSCH = 0;
	public static final int INDEX_TOMNAR = 1;
	public static final int INDEX_MAKLE = 2;
	
	ArrayList<Plane> mPlanes = new ArrayList<Plane>();
	ARToolkit mArtoolkit;
	Handler mHandler = new Handler();
	Runnable mThread;
	int mMaxRotation = -60;
	boolean mRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			//register a object for each marker type
			mArtoolkit = super.getArtoolkit();
			
			//Add the businesscards
			addBusinessCard("makle_card.obj", "AU16.pat");
			addBusinessCard("andsch_card.obj", "APP16.pat");
			addBusinessCard("tomnar_card.obj", "CV16.pat");
			
			//Register arobjects
			for(Plane p : mPlanes) {
				mArtoolkit.registerARObject(p);
			}
			
			StandardRender render = new StandardRender();
			super.setNonARRenderer(render);
			
			mThread = new Runnable() {
				@Override
				public void run() {
					for(Plane p : mPlanes){
						p.update();
					}
					mHandler.postDelayed(this, 1);
				}
			};
			mHandler.postDelayed(mThread, 0);

		} catch (AndARException ex){
			ex.printStackTrace();
		}		
	}
	
	public void addBusinessCard(String objModel, String pattern) {
		Model model = loadModel(objModel);
		Plane plane = new Plane(model, pattern, 15.0);
		mPlanes.add(plane);
	}
	
	@Override
	protected void onDestroy() {
		mHandler.removeCallbacks(mThread);
		super.onDestroy();
	}
	
	public Model loadModel(String modelFileName) {
		try {
			BaseFileUtil fileUtil= null;
			fileUtil = new AssetsFileUtil(getResources().getAssets());
			fileUtil.setBaseFolder("models/");

			//read the model file:						
			if(modelFileName.endsWith(".obj")) {
				ObjParser parser = new ObjParser(fileUtil);
				if(fileUtil != null) {
					BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName);
					if(fileReader != null) {
						return parser.parse("Model", fileReader);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable ex) {
		mHandler.removeCallbacks(mThread);
		ex.printStackTrace();
		finish();
	}

}
