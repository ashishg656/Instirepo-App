package serverApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.instirepo.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageRequestManager {

	static ImageRequestManager mInstance;

	public static ImageRequestManager get(Context context) {
		if (mInstance == null)
			mInstance = new ImageRequestManager();
		return mInstance;
	}

	public void requestImage(final Context context, final ImageView imageView,
			final String imgUrl, int position) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnLoading(R.drawable.symphony)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		if (position == -1) {
			/*
			 * imageView.setBackgroundColor(context.getResources().getColor(
			 * ZUIUtils.getColorValue(getRandomNumber())));
			 */
		} else {
			/*
			 * imageView.setBackgroundColor(context.getResources().getColor(
			 * ZUIUtils.getColorValue(position)));
			 */
		}

		ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap bitmap) {
						imageView.setImageBitmap(bitmap);
						imageView.setBackgroundColor(0);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
					}
				});
	}

	public void requestImage1(final Context context, final ImageView imageView,
			final String imgUrl, final RequestBitmap requestBitmap, int position) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)

				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		if (position == -1) {
			/*
			 * imageView.setBackgroundColor(context.getResources().getColor(
			 * ZUIUtils.getColorValue(getRandomNumber())));
			 */
		} else {
			/*
			 * imageView.setBackgroundColor(context.getResources().getColor(
			 * ZUIUtils.getColorValue(position)));
			 */
		}
		ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap bitmap) {
						imageView.setImageBitmap(bitmap);
						if (requestBitmap != null) {
							requestBitmap.onRequestCompleted(bitmap);
							imageView.setBackgroundColor(0);
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
					}
				});
	}

	public void requestImage2(final Context context, final ImageView imageView,
			final String imgUrl, final RequestBitmap requestBitmap, int position) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnLoading(R.drawable.symphony)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		/*
		 * imageView.setBackgroundColor(context.getResources().getColor(
		 * ZUIUtils.getColorValue(position)));
		 */
		ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap bitmap) {
						requestBitmap.onRequestCompleted(bitmap);
						imageView.setImageBitmap(bitmap);
						// imageView.setBackgroundColor(0);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
					}
				});
	}

	public interface RequestBitmap {
		void onRequestCompleted(Bitmap bitmap);
	}
}
