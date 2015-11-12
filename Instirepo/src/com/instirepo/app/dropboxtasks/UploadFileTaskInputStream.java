package com.instirepo.app.dropboxtasks;

import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;

import com.dropbox.core.v2.Files;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTaskInputStream extends
		AsyncTask<InputStream, Void, Files.FileMetadata> {

	private final Context mContext;
	private final Files mFilesClient;
	private Exception mException;
	private Callback mCallback;
	private String fileName;

	public interface Callback {
		void onUploadComplete(Files.FileMetadata result);

		void onError(Exception e);
	}

	public UploadFileTaskInputStream(Context context, Files filesClient,
			Callback callback, String fileName) {
		mContext = context;
		mFilesClient = filesClient;
		mCallback = callback;
		this.fileName = fileName;
	}

	@Override
	protected void onPostExecute(Files.FileMetadata result) {
		super.onPostExecute(result);
		if (mException != null) {
			mCallback.onError(mException);
		} else if (result == null) {
			mCallback.onError(null);
		} else {
			mCallback.onUploadComplete(result);
		}
	}

	@Override
	protected Files.FileMetadata doInBackground(InputStream... params) {
		try {
			mFilesClient.uploadBuilder("Instirepo Uploads" + "/" + fileName)
					.mode(Files.WriteMode.overwrite).run(params[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}