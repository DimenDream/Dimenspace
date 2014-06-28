package com.hoot.util;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

public class DialogUtil {
	public static interface DialogListenerHandler {
		void positive();

		void negative();

		void netural();

		void action1();

		void action2();
	}

	public static class BaseDialogListenerHandler implements
			DialogListenerHandler {

		@Override
		public void positive() {
			// TODO Auto-generated method stub

		}

		@Override
		public void negative() {
			// TODO Auto-generated method stub

		}

		@Override
		public void netural() {
			// TODO Auto-generated method stub

		}

		@Override
		public void action1() {
			// TODO Auto-generated method stub

		}

		@Override
		public void action2() {
			// TODO Auto-generated method stub

		}

	}

	public static void createListDialog(final Context context, int resId,
			final DialogListenerHandler listenerHandler) {
		final ListAdapter adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, context.getResources()
						.getStringArray(resId));

		final AlertDialog.Builder builder = new Builder(context);
//		builder.setTitle(R.string.choose_photo);
		builder.setSingleChoiceItems(adapter, -1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					// doTakePhoto();
					listenerHandler.action1();
					break;
				case 1:
					// doPickPhotoFromGallery();
					listenerHandler.action2();
					break;
				}
			}
		});
//		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		});
		builder.create().show();

	}
}
