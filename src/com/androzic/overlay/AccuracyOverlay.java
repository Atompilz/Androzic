package com.androzic.overlay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.androzic.Androzic;
import com.androzic.MapView;
import com.androzic.R;
import com.androzic.util.Geo;

public class AccuracyOverlay extends MapOverlay
{
	Paint paint;
	int radius = 0;
	float accuracy = 0;

	public AccuracyOverlay(final Activity activity)
	{
		super(activity);

		paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(context.getResources().getColor(R.color.accuracy));
	}

	public void setAccuracy(float accuracy)
	{
		if (accuracy > 0 && this.accuracy != accuracy)
		{
			this.accuracy = accuracy;
			Androzic application = (Androzic) context.getApplication();
			double[] loc = application.getLocation();
			double[] prx = Geo.projection(loc[0], loc[1], accuracy/2, 90);
			int[] cxy = application.getXYbyLatLon(loc[0], loc[1]);
			int[] pxy = application.getXYbyLatLon(prx[0], prx[1]);
			radius = (int) Math.hypot((pxy[0]-cxy[0]), (pxy[1]-cxy[1]));
		}
		enabled = true;
    }

	@Override
	public void onMapChanged()
	{
		float a =  accuracy;
		accuracy = 0;
		setAccuracy(a);
	}

	@Override
	protected void onDraw(Canvas c, MapView mapView)
	{
		if (mapView.getAutoFollow() && radius > 0)
		{
			final int[] cxy = mapView.currentXY;
			c.drawCircle(cxy[0], cxy[1], radius, paint);
		}
	}

	@Override
	protected void onDrawFinished(Canvas c, MapView mapView)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreferencesChanged(SharedPreferences settings)
	{
		// TODO Auto-generated method stub

	}
}
