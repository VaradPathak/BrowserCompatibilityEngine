/**
 * The Browser Compatibility Engine
 * To convert the existing code into a multi browser compatible code using W3C Standards.
 */
package compatibilityEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Varad Pathak
 * 
 */
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<String> filePathList = new ArrayList<String>();
			List<File> fileList = new ArrayList<File>();
			String inputFilePath = "D:\\JSP\\";
			String lastDirectory = inputFilePath
					.substring(inputFilePath.substring(0,
							inputFilePath.length() - 1).lastIndexOf("\\"));
			File f = new File(inputFilePath.concat("W3C\\"));
			f.mkdir();
			fileList = getFilePath(inputFilePath, filePathList, inputFilePath,
					fileList, lastDirectory);
			for (File newfile : fileList) {
				int tempNo = newfile.getAbsolutePath().indexOf("W3C\\");
				String newFilePath = newfile.getAbsolutePath().substring(0,
						tempNo).concat(
						newfile.getAbsolutePath().substring(tempNo + 4));
				FileInputStream fstream = new FileInputStream(newFilePath);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));

				String strLine;
				// Read File Line By Line
				while ((strLine = br.readLine()) != null) {
					if ((strLine.indexOf("<%@") == -1)
							&& (strLine.indexOf("<html:xhtml") == -1)) {
						String newLine = replaceDocAll(br, strLine);
						newLine = replaceHTMLComments(newLine);
						newLine = replaceInnerText(newLine);
						newLine = replaceNBSP(newLine);
						newLine = correctBrTag(newLine);
						newLine = addAlt(newLine);
						newLine = correctScriptTag(newLine);

						writeToFile(newfile, newLine);
					}
				}
				// Close the input stream
				in.close();
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * @param newfile
	 * @param newLine
	 */
	private static void writeToFile(File newfile, String newLine) {
		BufferedWriter bufferedWriter = null;
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(newfile, true), "UTF-8");
			// Construct the BufferedWriter object
			bufferedWriter = new BufferedWriter(writer);
			// Start writing to the output stream
			bufferedWriter.write(newLine);
			bufferedWriter.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the BufferedWriter
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * @param filePathList
	 * @param mainDirPath
	 * @param fileList
	 * @param lastDirectory
	 * @param string
	 * @return
	 */
	private static List<File> getFilePath(String dirSoursePath,
			List<String> filePathList, String mainDirPath, List<File> fileList,
			String lastDirectory) {

		File dir = new File(dirSoursePath);
		File[] contents = dir.listFiles();
		for (File file : contents) {
			String fileName = null;
			String filePath = file.getAbsolutePath();
			fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
			String newfilePath = mainDirPath.concat("W3C\\")
					.concat(
							filePath.substring(filePath
									.lastIndexOf(lastDirectory) + 5));
			filePath = newfilePath.substring(0,
					(newfilePath.length() - fileName.length()));
			try {
				if (file.getName().endsWith(".jsp")
						&& (file.getAbsolutePath().indexOf("\\W3C\\") == -1)) {
					filePathList.add(file.getAbsolutePath());
					File newfile = new File(newfilePath);
					newfile.createNewFile();
					writeInitToFile(newfile);
					fileList.add(newfile);
				} else if (file.isDirectory()
						&& (file.getAbsolutePath().indexOf("\\W3C\\") == -1)) {
					File f = new File(filePath);
					f.mkdir();
					getFilePath(file.getAbsolutePath().concat("\\"),
							filePathList, mainDirPath, fileList, lastDirectory);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// System.out.println(filePath);
		return fileList;
	}

	/**
	 * @param newfile
	 */
	private static void writeInitToFile(File newfile) {
		String initFilePath = "D:\\INITJSP\\init.jsp";
		if (!newfile.getName().equalsIgnoreCase("init_body.jsp")) {
			try {
				FileInputStream fstream = new FileInputStream(initFilePath);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));

				String strLine;
				// Read File Line By Line
				while ((strLine = br.readLine()) != null) {
					writeToFile(newfile, strLine);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param br
	 * @param strLine
	 * @throws IOException
	 */
	private static String replaceDocAll(BufferedReader br, String strLine)
			throws IOException {
		int index = strLine.indexOf("document.all");
		while (index != -1) {
			/*
			 * if (strLine.indexOf("eval('document.all") != -1) {
			 * System.out.println(strLine + " :" + index); }
			 */
			if (strLine.substring(index + 12).trim().startsWith("[")) {
				int startIndex = strLine.indexOf("[", (index + 12));
				int intermediateIndex = strLine.substring(startIndex + 1)
						.indexOf("[")
						+ startIndex + 1;
				int tempIndex = startIndex;
				int endIndex = strLine.indexOf("]", startIndex);
				while ((intermediateIndex != tempIndex)
						&& (intermediateIndex < endIndex)) {
					intermediateIndex = strLine.substring(endIndex + 1)
							.indexOf("[")
							+ endIndex + 1;
					tempIndex = endIndex;
					endIndex = strLine.indexOf("]", endIndex + 1);
				}
				String inputVariable = strLine.substring((startIndex + 1),
						endIndex);
				if (strLine.substring(endIndex + 1).startsWith(".")) {
					if (inputVariable.startsWith("\"")) {
						inputVariable = inputVariable.substring((inputVariable
								.indexOf("\"") + 1), (inputVariable
								.lastIndexOf("\"")));
						strLine = strLine.substring(0, (index))
								.concat("$get('").concat(inputVariable).concat(
										"', true)").concat(
										strLine.substring(endIndex + 1));
					} else {
						strLine = strLine.substring(0, (index)).concat("$get(")
								.concat(inputVariable).concat(", true)")
								.concat(strLine.substring(endIndex + 1));
					}
					index = strLine.indexOf("document.all");
				} else if (strLine.substring(endIndex + 1).startsWith("[")) {
					if (inputVariable.startsWith("\"")) {
						inputVariable = inputVariable.substring((inputVariable
								.indexOf("\"") + 1), (inputVariable
								.lastIndexOf("\"")));
						strLine = strLine.substring(0, (index))
								.concat("$get('").concat(inputVariable).concat(
										"')").concat(
										strLine.substring(endIndex + 1));
					} else {
						strLine = strLine.substring(0, (index)).concat("$get(")
								.concat(inputVariable).concat(")").concat(
										strLine.substring(endIndex + 1));
					}
					index = strLine.indexOf("document.all");
				}
				index = -1;
			} else {
				index = -1;
			}
		}
		// System.out.println(strLine);
		return strLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String replaceNBSP(String newLine) {
		newLine = newLine.replaceAll("&nbsp;", "&#160;");
		return newLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String replaceInnerText(String newLine) {
		newLine = newLine.replaceAll(".innerText", ".innerHTML");
		return newLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String replaceHTMLComments(String newLine) {
		// if (newLine.indexOf("<!--") != -1) {
		newLine = newLine.replaceAll("<!--", "<%--");
		newLine = newLine.replaceAll("-->", "--%>");
		// }
		return newLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String addAlt(String newLine) {

		int startIndex = newLine.indexOf("<html:img");
		boolean needsAlt = true;
		while (startIndex != -1) {
			int endIndex = newLine.indexOf("/>", startIndex);
			if (endIndex != -1) {
				try {
					Scanner scn = new Scanner(newLine.substring(startIndex + 9)
							.trim()).useDelimiter(" ");

					while (scn.hasNext()) {
						if (scn.next().startsWith("alt")) {
							needsAlt = false;
						}
					}

					if (needsAlt) {
						newLine = newLine.substring(0, newLine.indexOf("/>",
								startIndex))
								+ " alt=\"\""
								+ newLine.substring(endIndex, newLine.length());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				startIndex = newLine.indexOf("<html:img", startIndex + 9);
			} else {
				// TODO: Need to handle multiline statements
				startIndex = -1;
			}
		}
		return newLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String correctBrTag(String newLine) {
		if (newLine.indexOf("<br>") != -1) {
			newLine = newLine.replaceAll("<br>", "<br/>");
		}
		return newLine;
	}

	/**
	 * @param newLine
	 * @return
	 */
	private static String correctScriptTag(String newLine) {
		if (newLine.indexOf("<script>") != -1) {
			newLine = newLine.replaceAll("<script>",
					"<script type=\"text/javascript\">");
		}
		return newLine;
	}
}
