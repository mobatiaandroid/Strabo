package com.vkc.strabo.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class DisplayManagerScale {
	float TAB_COUNT_DIN;
	Context cont;

	public DisplayManagerScale(Context paramContext) {
		this.cont = paramContext;
	}

	@SuppressLint({ "NewApi" })
	public int getDeviceHeight() {
		Point localPoint = new Point();
		WindowManager localWindowManager = ((Activity) this.cont)
				.getWindowManager();

		localWindowManager.getDefaultDisplay().getSize(localPoint);
		return localPoint.y;

	}

	@SuppressLint({ "NewApi" })
	public int getDeviceWidth() {
		Point localPoint = new Point();
		WindowManager localWindowManager = ((Activity) this.cont)
				.getWindowManager();

		localWindowManager.getDefaultDisplay().getSize(localPoint);
		return localPoint.x;

	}
	public float getDisplayDensity() {
		
		
		
		return this.cont.getResources().getDisplayMetrics().density;
		
	}

	public float getDisplayscale() {
		float f = this.cont.getResources().getDisplayMetrics().density;
		if (f <= 0.75D)
			this.TAB_COUNT_DIN = 3.0F;

		if (f <= 1.0)
			this.TAB_COUNT_DIN = 3.0F;
		else if (f <= 1.5)
			this.TAB_COUNT_DIN = 4.0F;
		else if (f <= 2.0)
			this.TAB_COUNT_DIN = 5.0F;
		else if (f <= 3.0D)
			this.TAB_COUNT_DIN = 6.0F;
		return this.TAB_COUNT_DIN;
	}
}
