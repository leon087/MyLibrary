package com.loopj.android.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;

public abstract class ApkHttpResponseHandler extends AsyncHttpResponseHandler {
	private File mFile;
	private Context context;
	private static final int BUFFER_SIZE = 1024 * 50;

	/**
	 * Obtains new ApkHttpResponseHandler and stores response in passed file
	 * 
	 * @param file
	 *            File to store response within, must not be null
	 */
	public ApkHttpResponseHandler(Context context, String name) {
		super();
		this.mFile = context.getFileStreamPath(name);
		this.context = context;
	}

	/**
	 * Attempts to delete file with stored response
	 * 
	 * @return false if the file does not exist or is null, true if it was
	 *         successfully deleted
	 */
	public boolean deleteTargetFile() {
		return getTargetFile() != null && getTargetFile().delete();
	}

	/**
	 * Retrieves File object in which the response is stored
	 * 
	 * @return File file in which the response is stored
	 */
	protected File getTargetFile() {
		assert (mFile != null);
		return mFile;
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers,
			byte[] responseBytes, Throwable throwable) {
		onFailure(statusCode, headers, throwable, getTargetFile());
	}

	/**
	 * Method to be overriden, receives as much of file as possible Called when
	 * the file is considered failure or if there is error when retrieving file
	 * 
	 * @param statusCode
	 *            http file status line
	 * @param headers
	 *            file http headers if any
	 * @param throwable
	 *            returned throwable
	 * @param file
	 *            file in which the file is stored
	 */
	public abstract void onFailure(int statusCode, Header[] headers,
			Throwable throwable, File file);

	@Override
	public final void onSuccess(int statusCode, Header[] headers,
			byte[] responseBytes) {
		onSuccess(statusCode, headers, getTargetFile());
	}

	/**
	 * Method to be overriden, receives as much of response as possible
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response http headers if any
	 * @param file
	 *            file in which the response is stored
	 */
	public abstract void onSuccess(int statusCode, Header[] headers, File file);

	@Override
	protected byte[] getResponseData(HttpEntity entity) throws IOException {
		if (entity != null) {
			InputStream instream = entity.getContent();
			long contentLength = entity.getContentLength();
			// FileOutputStream buffer = new FileOutputStream(getTargetFile());
			OutputStream buffer = new BufferedOutputStream(
					context.openFileOutput(mFile.getName(),
							Context.MODE_WORLD_READABLE
									| Context.MODE_WORLD_WRITEABLE));
			if (instream != null) {
				try {
					byte[] tmp = new byte[BUFFER_SIZE];
					int l, count = 0;
					// do not send messages if request has been cancelled
					while ((l = instream.read(tmp)) != -1
							&& !Thread.currentThread().isInterrupted()) {
						count += l;
						buffer.write(tmp, 0, l);
						sendProgressMessage(count, (int) contentLength);
					}
				} finally {
					instream.close();
					buffer.flush();
					buffer.close();
				}
			}
		}
		return null;
	}

}
