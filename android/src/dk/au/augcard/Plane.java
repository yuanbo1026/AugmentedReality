package dk.au.augcard;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import edu.dhbw.andobjviewer.graphics.Model3D;
import edu.dhbw.andobjviewer.models.Model;

public class Plane extends Model3D {

	public static final String TAG = "Plane";
	
	private float mMaxRotation = -60;
	private float mRotationSpeed = 0.2f;
	public boolean mRunning = false;
	
	public Plane(Model model, String pattern, double markerWidth) {
		super(model, pattern, markerWidth);
	}

	public Plane(Model mModel) {
		super(mModel);
	}

	@Override
	public void init(GL10 gl) {
		super.init(gl);
		reset();
	}
	
	public synchronized void update() {
		if(isVisible()) {
			Log.d(TAG, "Is visible");
			mRunning = true;
			if(getModel().zrot < mMaxRotation){
				Log.d(TAG, "Animate rotation");
				setZrot(getModel().zrot+mRotationSpeed);
			}
		}else if(!isVisible() && mRunning){
			reset();
			mRunning = false;
		}
	}
	
	public void reset() {
		Log.d(TAG, "Reset pos and rot");
		getModel().zrot = -180;
		getModel().xpos = -7.5f;
		getModel().ypos = -6;
	}

}
