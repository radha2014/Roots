package com.roots.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.roots.CommonHelper;
import com.roots.MainDAO;

public class UnusedImages {

	public static void main(String[] args) throws IOException {

		// [ imgColID, <img.jpg_1, img_2.jpg..img.jpg_n> ]
		Map<String, ArrayList<String>> imgCollectionMap = new HashMap<String, ArrayList<String>>();
		MainDAO _mainDAO = new MainDAO();

		_mainDAO.loadCollections(imgCollectionMap,
				"C:\\eclipse3.3.2\\images.txt");
		ArrayList<String> availableImages = _mainDAO.getImgList();

		Set<String> linkedImages = new HashSet<String>();
		for (ArrayList<String> list : imgCollectionMap.values()) {
			linkedImages.addAll(list);
		}

		ArrayList<String> unusedImages = new ArrayList<String>(availableImages);
		unusedImages.removeAll(linkedImages);
		if (CommonHelper.isEmpty(unusedImages)) {
			System.out.println("No unused images found!!");
		} else {
			System.out.println("Following images are not in use :");
			int i = 0;
			for (String img : unusedImages) {
				System.out.println((i++) + ". " + img);
			}
		}

		linkedImages.removeAll(availableImages);
		if (CommonHelper.isEmpty((new ArrayList<String>(linkedImages)))) {
			System.out.println("No image is missing.");
		} else {
			System.out.println("Following images are missing!!!");
			int i = 0;
			for (String img : unusedImages) {
				System.out.println((i++) + ". " + img);
			}
		}

	}
}
