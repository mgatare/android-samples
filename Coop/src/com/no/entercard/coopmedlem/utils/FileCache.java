package com.no.entercard.coopmedlem.utils;

import java.io.File;
import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class FileCache.
 */
public class FileCache {

	/** The cache dir. */
	private File cacheDir;

	/**
	 * Instantiates a new file cache.
	 *
	 * @param context the context
	 */
	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"dump");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * Gets the file.
	 *
	 * @param url the url
	 * @return the file
	 */
	public File getFile(String url) {
		// Identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	/**
	 * Clear.
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}
}