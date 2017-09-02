package app.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.view.View;
/***
 * 弹框封装类
 * @author Administrator
 *
 */
public class DialogUtil {
	public static class DialogClicker implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int arg1) {
			dialog.cancel();
			dialog.dismiss();
		}

	}

	public static final int loading = 0;

	public static class DialogInfo {
		public String title;
		public int iconId;
		public SpannableStringBuilder message;
		public String positiveButtonText;
		public DialogInterface.OnClickListener positiveButtonClickListener;
		public String negativeButtonText;
		public DialogInterface.OnClickListener negativeButtonClickListener;
		public String neutralButtonText;
		public DialogInterface.OnClickListener neutralButtonClickListener;
		public View view;

		public Context aty;

		public DialogInfo(Context context) {
			super();
			this.aty = context;
		}

	}

	public static DialogInterface.OnClickListener getNewCancelOption(
			Context activity) {
		return getNewCancelOption(activity, false);
	}

	public static DialogInterface.OnClickListener getNewCancelOption(
			final Context activity, final boolean destroyActivity) {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				dialog.dismiss();

			}
		};
	}

	public static AlertDialog showChoiceDialog(DialogInfo abtract) {

		AlertDialog dialog = showChoiceDialog(abtract, true);
		return dialog;
	}

	public static AlertDialog showChoiceDialog(DialogInfo abtract,
			boolean cancelable) {
		if (abtract.aty == null) {
			throw new IllegalStateException("contxet is null");
		}
		Builder dialog = new Builder(abtract.aty)
				.setCancelable(cancelable);
		if (abtract.positiveButtonText != null
				&& abtract.positiveButtonClickListener != null) {
			dialog.setPositiveButton(abtract.positiveButtonText,

			abtract.positiveButtonClickListener);

		}
		if (abtract.negativeButtonText != null
				&& abtract.negativeButtonClickListener != null) {
			dialog.setNegativeButton(abtract.negativeButtonText,
					abtract.negativeButtonClickListener);

		}

		if (abtract.neutralButtonText != null
				&& abtract.neutralButtonClickListener != null) {
			dialog.setNeutralButton(abtract.neutralButtonText,
					abtract.neutralButtonClickListener);

		}

		if (abtract.title != null) {
			dialog.setTitle(abtract.title);
		}
		if (abtract.iconId != 0) {
			dialog.setIcon(abtract.iconId);
		}
		if (abtract.message != null) {
			dialog.setMessage(abtract.message);
		}
		AlertDialog ad = dialog.create();
		
		return ad;
	}

	public static AlertDialog showNeutralDialog(DialogInfo abtract) {
		return showNeutralDialog(abtract, false);
	}

	public static AlertDialog showNeutralDialog(DialogInfo abtract,
			boolean cancable) {
		return new Builder(abtract.aty)
				.setTitle(abtract.title)
				.setIcon(abtract.iconId)
				.setMessage(abtract.message)
				.setNeutralButton(abtract.neutralButtonText,
						abtract.neutralButtonClickListener)
				.setCancelable(cancable).setView(abtract.view).show();
	}

	public void closeHintNBackToLauncher(DialogInterface dialog, Context context) {
		dialog.dismiss();
		dialog.cancel();
		((Activity) context).finish();
	}
}
