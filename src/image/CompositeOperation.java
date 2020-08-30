package image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.DefaultListModel;

import formatters.XML_Formatter;

public class CompositeOperation {

	public static final int DEFAULT_ARG = 128;
	private ArrayList<String> operations = new ArrayList<String>();
	private String name;

	public CompositeOperation(String name) {
		this.name = name;
	}

	public void addOperation(String operationCode) {
		this.operations.add(operationCode);
	}

	public static HashMap<String, CompositeOperation> compositeOperationsMap = new HashMap<String, CompositeOperation>();
	public static DefaultListModel<String> listModel = new DefaultListModel<String>();

	public static void addNewCompositeOperation(CompositeOperation compositeOperation) {
		compositeOperationsMap.put(compositeOperation.name, compositeOperation);
		listModel.addElement(compositeOperation.name);

	}
	public static final LinkedHashMap<String, String> NAME_TO_CODE_MAP = new LinkedHashMap<String, String>();
	static {
		NAME_TO_CODE_MAP.put("Add", "add");
		NAME_TO_CODE_MAP.put("Subtract", "sub");
		NAME_TO_CODE_MAP.put("Multiply", "mul");
		NAME_TO_CODE_MAP.put("Divide", "div");
		NAME_TO_CODE_MAP.put("Inverse Subtract", "invsub");
		NAME_TO_CODE_MAP.put("Inverse Divide", "invdiv");
		NAME_TO_CODE_MAP.put("Power", "pow");
		NAME_TO_CODE_MAP.put("Maximum", "max");
		NAME_TO_CODE_MAP.put("Minimum", "min");
		NAME_TO_CODE_MAP.put("Logarithm", "log");
		NAME_TO_CODE_MAP.put("Modulus", "abs");
		NAME_TO_CODE_MAP.put("Invert", "inv");
		NAME_TO_CODE_MAP.put("Black & White", "bnw");
		NAME_TO_CODE_MAP.put("Grayscale", "grs");
		NAME_TO_CODE_MAP.put("Median", "med");
	}
	public static final LinkedHashMap<String, String> CODE_TO_NAME_MAP = new LinkedHashMap<String, String>();

	static {
		CODE_TO_NAME_MAP.put("add", "Add");
		CODE_TO_NAME_MAP.put("sub", "Subtract");
		CODE_TO_NAME_MAP.put("mul", "Multiply");
		CODE_TO_NAME_MAP.put("div", "Divide");
		CODE_TO_NAME_MAP.put("invsub", "Inverse Subtract");
		CODE_TO_NAME_MAP.put("invdiv", "Inverse Divide");
		CODE_TO_NAME_MAP.put("pow", "Power");
		CODE_TO_NAME_MAP.put("max", "Maximum");
		CODE_TO_NAME_MAP.put("min", "Minimum");
		CODE_TO_NAME_MAP.put("log", "Logarithm");
		CODE_TO_NAME_MAP.put("abs", "Modulus");
		CODE_TO_NAME_MAP.put("inv", "Invert");
		CODE_TO_NAME_MAP.put("bnw", "Black & White");
		CODE_TO_NAME_MAP.put("grs", "Grayscale");
		CODE_TO_NAME_MAP.put("med", "Median");
	}

	public static final CompositeOperation BLACK_N_WHITE = new CompositeOperation("BLACK_N_WHITE");
	public static final CompositeOperation BLUR = new CompositeOperation("BLUR");
	public static final CompositeOperation GRAYSCALE = new CompositeOperation("GRAYSCALE");
	public static final CompositeOperation INVERT = new CompositeOperation("INVERT");
	public static final CompositeOperation ENLIGHTEN = new CompositeOperation("ENLIGHTEN");
	static {
		BLACK_N_WHITE.addOperation("bnw");
		BLUR.addOperation("med");
		GRAYSCALE.addOperation("grs");
		INVERT.addOperation("inv");

		ENLIGHTEN.addOperation("mul 1.3");
		ENLIGHTEN.addOperation("add 40");
	}

	public static boolean doesntNeedArg(final String str) {
		return str == "Logarithm" || str == "Modulus" || str == "Invert" || str == "Black & White" || str == "Grayscale" || str == "Median";
	}



	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getOperations() {
		return this.operations;
	}

	public void exportToFile(String filePath) {
		new XML_Formatter(filePath).write(this);

	}
	public static void importFromFile(String filePath) {
		CompositeOperation compositeOperation = new CompositeOperation("");
		new XML_Formatter(filePath).read(compositeOperation);
		addNewCompositeOperation(compositeOperation);
	}

}
