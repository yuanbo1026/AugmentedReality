package dk.au.augcard;

import java.io.BufferedReader;
import java.io.IOException;

import android.os.Bundle;
import android.util.Log;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andobjviewer.graphics.Model3D;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.parser.ParseException;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;

public class MainActivity extends AndARActivity {

	Model3D mObject;
	ARToolkit mArtoolkit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		super.setNonARRenderer(renderer);//or might be omited
		try {
			//register a object for each marker type
			mArtoolkit = super.getArtoolkit();
			loadModel("superman.obj");

			mObject = new Model3D(mModel, "patt.hiro");
			mArtoolkit.registerARObject(mObject);
		} catch (AndARException ex){
			//handle the exception, that means: show the user what happened
			System.out.println("");
		}		
		startPreview();
	}
	
	private Model mModel;

	public void loadModel(String modelFileName) {
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
						mModel = parser.parse("Model", fileReader);
						mObject = new Model3D(mModel);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable ex) {
		Log.e("AndAR EXCEPTION", ex.getMessage());
		finish();
	}

}
