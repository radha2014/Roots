package com.roots;

import java.util.HashMap;

public class PaginationModule {

	public PaginationModule() {
		super();
	}

	public HashMap<String, Integer> getPages(int blockSize, int requestedPage,
			int totalSize) {

		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

		if (totalSize <= blockSize) {

			// only one block available
			setPaging(hmap, 0, totalSize);

		} else {

			int startIndex = requestedPage - requestedPage % blockSize;
			if (startIndex + blockSize < totalSize) {

				setPaging(hmap, startIndex, startIndex + blockSize);

			} else {

				setPaging(hmap, startIndex, totalSize);
			}
		}

		return hmap;
	}

	private void setPaging(HashMap<String, Integer> hmap, int startIndex,
			int endIndex) {

		int j = 1;
		for (int i = startIndex; i <= endIndex; i++) {

			hmap.put("pg" + j++, i);
		}
	}

	public HashMap<String, Integer> getPages(int currentImage, int imageListSize) {
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

		System.out.println("currentImage = " + currentImage
				+ ", imageListSize = " + imageListSize);

		switch (imageListSize) {

		case 1:
			hmap.put("currentImage", 0);
			break;

		case 2:
			if (currentImage == 0) {

				hmap.put("currentImage", 0);
				hmap.put("nextImage", 1);
			} else {

				hmap.put("prevImage", 0);
				hmap.put("currentImage", 1);
			}
			break;

		default:

			if (currentImage == 0) {

				hmap.put("currentImage", 0);
				hmap.put("nextImage", 1);
			} else if (currentImage == 1) {

				hmap.put("prevImage", 0);
				hmap.put("currentImage", 1);
				hmap.put("nextImage", 2);
			} else if (currentImage == imageListSize - 1) {

				hmap.put("prevImage", currentImage - 1);
				hmap.put("currentImage", currentImage);
			} else {// currentImage==imageListSize-1

				hmap.put("prevImage", currentImage - 1);
				hmap.put("currentImage", currentImage);
				hmap.put("nextImage", currentImage + 1);
			}
		}

		return hmap;
	}
}
