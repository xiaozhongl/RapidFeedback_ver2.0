package com.RapidFeedback;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName PDFUtil
 * @Description This class deals with all the things about the PDF result
 *              report.
 *
 * @author Dinghao Yong
 */
public class PDFUtil {

	public static final Rectangle PAGE_SIZE = PageSize.A4;
	public static final float MARGIN_LEFT = 50;
	public static final float MARGIN_RIGHT = 50;
	public static final float MARGIN_TOP = 50;
	public static final float MARGIN_BOTTOM = 50;
	public static final float SPACING = 20;

	private Document document = null;

	/**
	 * Create the target file
	 *
	 * @param fileName the path for the temp file
	 * @return
	 */
	public void createDocument(String fileName) {
		File file = new File(fileName);
		FileOutputStream out = null;
		document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT,
				MARGIN_TOP, MARGIN_BOTTOM);
		try {
			out = new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// open the document for writing
		document.open();
	}

	/**
	 * write chapter to doc
	 *
	 * @param chapter
	 * @return
	 */
	public void writeChapterToDoc(Chapter chapter) {
		try {
			if (document != null) {
				if (!document.isOpen())
					document.open();
				document.add(chapter);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * function : create the charpter
	 *
	 * @param title
	 * @param chapterNum
	 * @param alignment   0: align=left，1: align=center
	 * @param numberDepth the sequence number; if 0, no sequence number
	 * @param font
	 * @return Chapter
	 */
	public static Chapter createChapter(String title, int chapterNum,
			int alignment, int numberDepth, Font font) {
		Paragraph chapterTitle = new Paragraph(title, font);
		chapterTitle.setAlignment(alignment);
		Chapter chapter = new Chapter(chapterTitle, chapterNum);
		chapter.setNumberDepth(numberDepth);
		return chapter;
	}

	/**
	 * function: create the section belongs to one chapter
	 *
	 * @param chapter
	 * @param title
	 * @param font
	 * @param numberDepth
	 * @return section
	 */
	public static Section createSection(Chapter chapter, String title,
			Font font, int numberDepth) {
		Section section = null;
		if (chapter != null) {
			Paragraph sectionTitle = new Paragraph(title, font);
			sectionTitle.setSpacingBefore(SPACING);
			section = chapter.addSection(sectionTitle);
			section.setNumberDepth(numberDepth);
		}
		return section;
	}

	/**
	 * add content to the pdf file
	 *
	 * @param text
	 * @param font
	 * @return phrase
	 */
	public static Phrase createPhrase(String text, Font font) {
		Phrase phrase = new Paragraph(text, font);
		return phrase;
	}

	/**
	 * create a list in the pad file
	 *
	 * @param numbered     if True, the list will be a numbered one
	 * @param lettered     True = use letter to number the list，False = use
	 *                     numbers to number the list
	 * @param symbolIndent
	 * @return list
	 */
	public static List createList(boolean numbered, boolean lettered,
			float symbolIndent) {
		List list = new List(numbered, lettered, symbolIndent);
		return list;
	}

	/**
	 * function create list items
	 *
	 * @param content conten of the list item
	 * @param font
	 * @return listItem
	 */
	public static ListItem createListItem(String content, Font font) {
		ListItem listItem = new ListItem(content, font);
		return listItem;
	}

	/**
	 * function: create the font
	 *
	 * @param fontname
	 * @param size     font size
	 * @param style    font stile
	 * @param color    font color
	 * @return Font
	 */
	public static Font createFont(String fontname, float size, int style,
			BaseColor color) {
		Font font = FontFactory.getFont(fontname, size, style, color);
		return font;
	}

	/**
	 * close pdf file
	 */
	public void closeDocument() {
		if (document != null) {
			document.close();
		}
	}

	/**
	 * delete pdf file
	 */
	public boolean deletePdf(String filepath) {
		boolean result = false;
		try {
			File file = new File(filepath);
			if (file.isFile()) {
				if (file.delete()) {
					System.out.println(file.getName()
							+ " Delete the pdf file successfully！");
					result = true;
				} else {
					System.out.println(
							"Wrong file name !! Fail to delete the pdf file !!");
				}
			} else {
				System.out.println(
						"Wrong file path !! Fail to delete the pdf file !!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * read pdf, with the use of pdfBox open source
	 *
	 * @param fileName
	 */
	public static void readPDF(String fileName) {
		File file = new File(fileName);
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			// create a new pdf parser
			PDFParser parser = new PDFParser(new RandomAccessBuffer(in));
			// parse the pdf file
			parser.parse();
			// get the pdf object
			PDDocument pdfdocument = parser.getPDDocument();
			// create a pdf stripper
			PDFTextStripper stripper = new PDFTextStripper();
			// strip from the
			String result = stripper.getText(pdfdocument);
			System.out.println("the content of the pdf file：");
			System.out.println(result);

		} catch (Exception e) {
			System.out.println("reading pdf file" + file.getAbsolutePath()
					+ "failed！" + e);
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * finally...the creation function of the pdf file
	 *
	 * @param marksList
	 * @param pj
	 * @param studentName
	 * @param studentNumber
	 * 
	 */
	// returnonestudentinfo returnprojectdetails
	public void create(ArrayList<Mark> marksList, ProjectInfo pj,
			StudentInfo studentInfo, String filePath, String fileName)
			throws Exception {

		String studentName = studentInfo.getFirstName() + " "
				+ studentInfo.getMiddleName() + " " + studentInfo.getSurname();
		String studentNumber = studentInfo.getNumber();

		String projectName = pj.getProjectName();
		String subjectCode = pj.getSubjectCode();
		String subjectName = pj.getSubjectName();
		double studentMark = marksList.get(0).getTotalMark();
		String mark = Double.toString(studentMark);
		int numOfAssessors = marksList.size();

		// name + e-mail of assessors
		ArrayList<String> assistantList = new ArrayList<String>();

		// name of assessors
		ArrayList<String> assistantNameList = new ArrayList<String>();
		for (int x = 0; x < numOfAssessors; x++) {
			assistantNameList.add(marksList.get(x).getLecturerName());
			assistantList.add(marksList.get(x).getLecturerName() + "   "
					+ pj.getAssistant().get(x));
		}

		// marks from the fisrt assessor as the basic criteria
		ArrayList<Criteria> criteriaList = marksList.get(0).getCriteriaList();
		ArrayList<Criteria> commentList = marksList.get(0).getCommentList();

		// String fileName = "./"+projectName+"_"+studentNumber+".pdf"; //the
		// file name
		PDFUtil pdfUtil = new PDFUtil();

		// the fonts
		Font chapterFont = PDFUtil.createFont("font", 20, Font.BOLD,
				new BaseColor(1, 64, 133));// the font of the title
		Font sectionFont = PDFUtil.createFont("font", 16, Font.BOLD,
				new BaseColor(0, 0, 0));// the font of the section name
		Font textFont = PDFUtil.createFont("font", 10, Font.NORMAL,
				new BaseColor(0, 0, 0));// the font of the text
		Font nameFont = PDFUtil.createFont("font", 10, Font.BOLDITALIC,
				new BaseColor(1, 64, 133));// the font of the lecturer name in
											// feedabcks
		Font markFont = PDFUtil.createFont("font", 14, Font.NORMAL,
				new BaseColor(0, 0, 0));// the font of the lecturer name in
										// feedabcks

		// create
		pdfUtil.createDocument(filePath + fileName);

		// create the fisrt page
		Chapter chapter = PDFUtil.createChapter(
				"Presentation Feedback Report - " + subjectCode, 1, 1, 0,
				chapterFont);

		// student info
		Section section1 = PDFUtil.createSection(chapter, "Student Information",
				sectionFont, 0);
		Phrase text1 = PDFUtil.createPhrase(
				"Student:  " + studentName + "  " + studentNumber, textFont);
		section1.add(text1);

		// subject info
		Section section2 = PDFUtil.createSection(chapter, "Project Infomration",
				sectionFont, 0);
		Phrase text2 = PDFUtil.createPhrase(projectName + "Project for subject "
				+ subjectCode + " " + subjectName, textFont);
		section2.add(text2);

		// mark
		Section section3 = PDFUtil.createSection(chapter, "Final Mark",
				sectionFont, 0);
		Phrase text3 = PDFUtil.createPhrase(mark, textFont);
		section3.add(text3);

		// Assessor Info
		Section section4 = PDFUtil.createSection(chapter, "Assessor(s)",
				sectionFont, 0);
		List assessorInfoList = PDFUtil.createList(false, false, 20);
		for (int i = 0; i < numOfAssessors; i++) {
			ListItem listItem = PDFUtil.createListItem(assistantList.get(i),
					textFont);
			assessorInfoList.add(listItem);
		}
		section4.add(assessorInfoList);

		Section section5 = PDFUtil.createSection(chapter, "Date", sectionFont,
				0);
		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM, yyyy");
		// String str = date.toString();
		Phrase text5 = PDFUtil.createPhrase(
				"This report is generated at " + date.toString(), textFont);
		section5.add(text5);

		pdfUtil.writeChapterToDoc(chapter);

		Chapter chapter2 = PDFUtil.createChapter("Criteria and Marks ", 0, 1, 0,
				chapterFont);

		for (int j = 0; j < criteriaList.size(); j++) {
			String criteriaName = marksList.get(0).getCriteriaList().get(j)
					.getName();
			Section section6 = PDFUtil.createSection(chapter2, criteriaName,
					sectionFont, 0);
			Phrase text = PDFUtil.createPhrase(
					Double.toString(marksList.get(0).getMarkList().get(j)) + "/"
							+ criteriaList.get(j).getMaximunMark(),
					markFont);
			section6.add(text);
			for (int i = 0; i < numOfAssessors; i++) {
				Phrase textName = PDFUtil.createPhrase("Assessor " + (i + 1),
						nameFont);
				List list = PDFUtil.createList(true, false, 20);
				ArrayList<SubSection> subsectionList = marksList.get(i)
						.getCriteriaList().get(j).getSubsectionList();
				for (int m = 0; m < subsectionList.size(); m++) {
					String str = "";
					str = str + "<" + subsectionList.get(m).getName() + "> :  "
							+ subsectionList.get(m).getShortTextList().get(0)
									.getLongtext().get(0)
									.replaceAll("$name$", studentName);
					ListItem listItemSecondLine = PDFUtil.createListItem(str,
							textFont);
					list.add(listItemSecondLine);
				}
				section6.add(textName);
				section6.add(list);
			}
		}

		for (int j = 0; j < commentList.size(); j++) {
			String criteriaName = marksList.get(0).getCommentList().get(j)
					.getName();
			Section section7 = PDFUtil.createSection(chapter2, criteriaName,
					sectionFont, 0);
			for (int i = 0; i < numOfAssessors; i++) {
				Phrase textName = PDFUtil.createPhrase("Assessor " + (i + 1),
						nameFont);
				List list = PDFUtil.createList(true, false, 20);
				ArrayList<SubSection> subsectionList = marksList.get(i)
						.getCommentList().get(j).getSubsectionList();
				for (int m = 0; m < subsectionList.size(); m++) {
					String str = "";
					str = str + "<" + subsectionList.get(m).getName() + "> :  "
							+ subsectionList.get(m).getShortTextList().get(0)
									.getLongtext().get(0)
									.replaceAll("$name$", studentName);
					ListItem listItemSecondLine = PDFUtil.createListItem(str,
							textFont);
					list.add(listItemSecondLine);
				}
				section7.add(textName);
				section7.add(list);
			}
		}

		pdfUtil.writeChapterToDoc(chapter2);
		pdfUtil.closeDocument();
		System.out.println("Create successfully: the report for" + studentName);

	}
}