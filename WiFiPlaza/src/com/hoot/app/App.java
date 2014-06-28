package com.hoot.app;

import java.lang.reflect.Field;

import com.hoot.manager.SystemEventManager;
import com.hoot.util.WifiAdmin;

import android.app.Application;
import android.view.ViewConfiguration;

public class App extends Application {
	public static Application GlobalContext;

	@Override
	public void onCreate() {
		super.onCreate();
		GlobalContext = this;
		SystemEventManager.getInstance().registSysEvent();
		hasPermanentMenuKey();
	}

	private void hasPermanentMenuKey() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
			ex.printStackTrace();
		}
	}

}
