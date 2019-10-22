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


public class PDFUtil {

	private static final Rectangle PAGE_SIZE = PageSize.A4;
	private static final float MARGIN_LEFT = 50;
	private static final float MARGIN_RIGHT = 50;
	private static final float MARGIN_TOP = 50;
	private static final float MARGIN_BOTTOM = 50;
	private static final float SPACING = 20;

	private Document document = null;



	private void createDocument(String fileName) {
		File file = new File(fileName);
		FileOutputStream out = null;
		document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT,
				MARGIN_TOP, MARGIN_BOTTOM);
		try {
			out = new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
		// open the document for writing
		document.open();
	}

	private void writeChapterToDoc(Chapter chapter) {
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
	 * function : create the chapter
	 *
	 * @param title
	 * @param chapterNum
	 * @param alignment   0: align=left，1: align=center
	 * @param numberDepth the sequence number; if 0, no sequence number
	 * @param font
	 * @return Chapter
	 */
	private static Chapter createChapter(String title, int chapterNum,
			int alignment, int numberDepth, Font font) {
		Paragraph chapterTitle = new Paragraph(title, font);
		chapterTitle.setAlignment(alignment);
		Chapter chapter = new Chapter(chapterTitle, chapterNum);
		chapter.setNumberDepth(numberDepth);
		return chapter;
	}


	private static Section createSection(Chapter chapter, String title,
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


	private static Phrase createPhrase(String text, Font font) {
		Phrase phrase = new Paragraph(text, font);
		return phrase;
	}


	private static List createList(boolean numbered, boolean lettered,
			float symbolIndent) {
		List list = new List(numbered, lettered, symbolIndent);
		return list;
	}


	private static ListItem createListItem(String content, Font font) {
		ListItem listItem = new ListItem(content, font);
		return listItem;
	}


	private static Font createFont(String fontname, float size, int style,
			BaseColor color) {
		Font font = FontFactory.getFont(fontname, size, style, color);
		return font;
	}


	private void closeDocument() {
		if (document != null) {
			document.close();
		}
	}


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


	public void create(int studentId, int projectId, String filePath, String fileName) {
		MysqlFunction db = new MysqlFunction();
		ProjectStudent student = db.getProjectStudent(studentId, projectId);
		Project project = db.getProject(projectId);

		String studentName = student.getFirstName();
		String studentNumber = String.valueOf(student.getStudentNumber());
		String finalScore = String.valueOf(student.getFinalScore());
		String finalRemark = student.getFinalRemark();
		ArrayList<Remark> remarkList = student.getRemarkList();

		String projectName = project.getName();
		String subjectCode = project.getSubjectCode();
		String subjectName = project.getSubjectName();



		PDFUtil pdfUtil = new PDFUtil();

		// the fonts
		Font chapterFont = PDFUtil.createFont("font", 20, Font.BOLD,
				new BaseColor(1, 64, 133));// the font of the title
		Font sectionFont = PDFUtil.createFont("font", 16, Font.BOLD,
				new BaseColor(0, 0, 0));// the font of the section name
		Font textFont = PDFUtil.createFont("font", 10, Font.NORMAL,
				new BaseColor(0, 0, 0));// the font of the text
		Font nameFont = PDFUtil.createFont("font", 10, Font.BOLDITALIC,
				new BaseColor(1, 64, 133));// the font of the lecturer nam
		Font markFont = PDFUtil.createFont("font", 14, Font.NORMAL,
				new BaseColor(0, 0, 0));// the font of the lecturer name in

		// create
		pdfUtil.createDocument(filePath + fileName);

		// create the first page
		Chapter chapter = PDFUtil.createChapter(
				"Presentation Feedback - " + subjectCode, 1, 1, 0,
				chapterFont);

		// student info
		Section section1 = PDFUtil.createSection(chapter, "Student Information",
				sectionFont, 0);
		Phrase text1 = PDFUtil.createPhrase(
				"Student:  " + studentName + "  " + studentNumber, textFont);
		section1.add(text1);

		// subject info
		Section section2 = PDFUtil.createSection(chapter, "Project Information",
				sectionFont, 0);
		Phrase text2 = PDFUtil.createPhrase(projectName + " for " +  subjectName, textFont);
		section2.add(text2);

		//  final score
		Section section3 = PDFUtil.createSection(chapter, "Final Score",
				sectionFont, 0);
		Phrase text3 = PDFUtil.createPhrase(finalScore, textFont);
		section3.add(text3);

		// final remark
		Section section4 = PDFUtil.createSection(chapter, "Final Remark",
				sectionFont, 0);
		Phrase phrase = PDFUtil.createPhrase(finalRemark, markFont);
		section4.add(phrase);

		Section section5 = PDFUtil.createSection(chapter, "Date", sectionFont,
				0);
		Date date = new Date();

		Phrase text5 = PDFUtil.createPhrase(
				"This report is generated at " + date.toString(), textFont);
		section5.add(text5);

		pdfUtil.writeChapterToDoc(chapter);

		int markerIndex = 1;
		for (Remark remark : remarkList){
			chapter = PDFUtil.createChapter("Assessment from Marker" + String.valueOf(markerIndex),
					0, 1, 0, chapterFont);

			ArrayList<Assessment> assessmentList = remark.getAssessmentList();

			for (Assessment assessment : assessmentList){

				String criterionName = db.getCriteriaName(assessment.getCriterionId());
				ArrayList<SelectedComment> selectedCommentList = assessment.getSelectedCommentList();


				Section section = PDFUtil.createSection(chapter, criterionName,
						sectionFont, 0);

				 phrase = PDFUtil.createPhrase(
						Double.toString(assessment.getScore()) + "/" +
								db.getMaximumMark(assessment.getCriterionId(), projectId), markFont);
				section.add(phrase);

				for (SelectedComment selectedComment: selectedCommentList) {
					String str = "";
					str += "<" + db.getFieldName(selectedComment.getFieldId()) + "> :  "
							+ db.getExpandedCommentText(selectedComment.getExCommentId())
							.replaceAll("$name$", studentName);

					phrase = PDFUtil.createPhrase(str, markFont);
					section.add(phrase);
				}

			}

			Section section = PDFUtil.createSection(chapter, "Remark",
					sectionFont, 0);
			phrase = PDFUtil.createPhrase(remark.getText(), markFont);
			section.add(phrase);
			pdfUtil.writeChapterToDoc(chapter);

			markerIndex += 1;
		}

		pdfUtil.closeDocument();
		System.out.println("Create successfully: the report for " + studentName);
	}
}