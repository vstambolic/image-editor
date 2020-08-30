package image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import formatters.Formatter;
import formatters.Formatter.FormatNotSupported;
import formatters.XML_Formatter;
import user_interface.MainCard;

public class Image implements Runnable {

	private String name;
	private boolean savedChanges;
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private Layer project = new Layer(this, "", 0, 0, true, true, 100, Layer.DEFAULT_COORDINTES, 0);
	//private static final String executeCPP = "C:\\Users\\Vaso\\Desktop\\ImageEditor.exe C:\\Users\\Vaso\\Desktop\\tmp.xml C:\\Users\\Vaso\\Desktop\\tmp.fun";
	private static final String executeCPP = ".\\ImageEditor.exe .\\tmp.xml .\\tmp.fun";
	private static Image image = new Image();
	public static Image getImage() {
		return image;
	}

	public static enum Status { CREATED, MODIFIED };

	private Status status = Status.CREATED;
	private boolean addButtonSemamphore = true;
	// Layers ------------------------------------------------------------------
	public void addLayer(final Layer layer) {
		layers.add(layer);
		this.status = Status.MODIFIED;
		if (this.addButtonSemamphore)
		this.mainCard.createLayerButton(layer);

	}

	public void removeLayer(int layer_id) {
		if (layer_id < layers.size()) {
			this.mainCard.removeLayer(layers.get(layer_id));

			this.status = Status.MODIFIED;
			for (int i = layer_id + 1; i < layers.size(); ++i)
				this.layers.get(i).setDepth(i - 1);
			layers.remove(layer_id);
		}


	}

	// Files ------------------------------------------------------------------

	public void load(String filePath) throws IOException, FormatNotSupported {
		Formatter.matchFormatter(filePath).read(this);
	}

	public void save(String filePath) throws FormatNotSupported, IOException {
		Formatter formatter = Formatter.matchFormatter(filePath);
		Pattern p = Pattern.compile(".*\\\\(.*)\\..*");
		Matcher m = p.matcher(filePath);
		if (m.matches())
			this.setName(m.group(1));

		if (!(formatter instanceof XML_Formatter))
			this.create();
		formatter.write(this);
		this.savedChanges = true;
	}


	public void create() {
		
		if (this.status == Status.MODIFIED) {
			if (this.layers.stream().anyMatch(layer -> {
				return layer.getVisibleStatus();
			})) {

				Layer lyr;
				lyr = this.layers.stream().max((layer1, layer2) -> {
					return (int) (layer1.getHeight() + layer1.getCoordinates().x - layer2.getHeight() - layer2.getCoordinates().x);
				}).get();//.getHeight();
				long height = lyr.getHeight() + lyr.getCoordinates().x;


				lyr = this.layers.stream().max((layer1, layer2) -> {
					return (int) (layer1.getWidth() + layer1.getCoordinates().y - layer2.getWidth() - layer2.getCoordinates().y);
				}).get();
				long width = lyr.getWidth() + lyr.getCoordinates().y;

			this.project = new Layer(this, "", height, width, true, true, Layer.MAX_OPACITY, Layer.DEFAULT_COORDINTES, 0);

			Stream.of(this.project.getMatrix()).forEach(row -> {
				for (int i = 0; i < row.length; ++i)
					row[i] = new Pixel();
			});

			ListIterator<Layer> it = layers.listIterator(layers.size());
			while (it.hasPrevious()) {
				Layer layer = it.previous();
				if (layer.getVisibleStatus()) {
					int row_offset = layer.getCoordinates().x;
					int col_offset = layer.getCoordinates().y;

					for (int i = 0; i < layer.getHeight(); ++i)
						for (int j = 0; j < layer.getWidth(); ++j) {
							Pixel upperPix = this.project.getMatrix()[row_offset + i][col_offset + j];
							double A_upper = upperPix.scaleAlpha();
							if (A_upper != 1.0) {
								Pixel bottomPix = layer.getMatrix()[i][j];
								double A_bottom = bottomPix.scaleAlpha();
								double A_result = A_upper + (1 - A_upper) * A_bottom;

								upperPix.red = (byte) (Byte.toUnsignedInt(upperPix.red) * A_upper / A_result + Byte.toUnsignedInt(bottomPix.red) * (1.0 - A_upper) * A_bottom / A_result);
								upperPix.green = (byte) (Byte.toUnsignedInt(upperPix.green) * A_upper / A_result + Byte.toUnsignedInt(bottomPix.green) * (1.0 - A_upper) * A_bottom / A_result);
								upperPix.blue = (byte) (Byte.toUnsignedInt(upperPix.blue) * A_upper / A_result + Byte.toUnsignedInt(bottomPix.blue) * (1.0 - A_upper) * A_bottom / A_result);
								upperPix.alpha = (byte) (Byte.toUnsignedInt(Pixel.MAX_BYTE) * A_result);

							}
						}
				}

			}
			}
			else
				this.project = new Layer(this, "", 0, 0, true, true, 100, Layer.DEFAULT_COORDINTES, 0);
			this.status = Status.CREATED;

		}
	}

	public void apply(CompositeOperation operation) {
		try {
			//this.mainCard.getSouth
			this.save(".\\tmp.xml");
			operation.exportToFile(".\\tmp.fun");
			//			this.save("C:\\Users\\Vasilije\\Desktop\\tmp.xml");
			//			operation.exportToFile("C:\\Users\\Vasilije\\Desktop\\tmp.fun");
			System.out.println("START");
			Process process = Runtime.getRuntime().exec(executeCPP);
			System.out.println("WAIT");
			process.waitFor();
			System.out.println("END");

			this.layers.clear();
			this.addButtonSemamphore = false;
			this.load(".\\tmp.xml");
			this.addButtonSemamphore = true;
			this.mainCard.getImagePanel().repaint();

		}
		catch (FormatNotSupported | IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}


	// Selections -----------------------------------------------------------------
	private HashMap<String, Selection> activeSelections = new HashMap<String, Selection>();
	private HashMap<String, Selection> inactiveSelections = new HashMap<String, Selection>();

	private MainCard mainCard;

	public void addSelectionActive(Selection selection) {
		activeSelections.put(selection.getName(), selection);
		this.mainCard.createSelectionButton(selection.getName());
	}

	public void addSelectionInactive(Selection selection) {
		inactiveSelections.put(selection.getName(), selection);
		this.mainCard.createSelectionButton(selection.getName());

	}
	public void setSelectionActive(String selectionName) {
		Selection selection = this.inactiveSelections.get(selectionName);
		this.inactiveSelections.remove(selectionName);
		this.activeSelections.put(selectionName, selection);
	}

	public void setSelectionInactive(String selectionName) {
		Selection selection = this.activeSelections.get(selectionName);
		this.activeSelections.remove(selectionName);
		this.inactiveSelections.put(selectionName, selection);
	}

	// Other -----------------------------------------------------------------
	public void setName(final String name) {
		this.name = name;
	}

	public long getWidth() {
		return this.project.getWidth();
	}

	public long getHeight() {
		return this.project.getHeight();
	}

	public ArrayList<Layer> getLayers() {
		return this.layers;
	}

	public boolean getSavedStatus() {
		return this.savedChanges;
	}

	public Layer getProject() {
		return this.project;
	}


	public int getLayerNum() {
		return this.layers.size();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return this.name;
	}

	public HashMap<String, Selection> getActiveSelections() {
		return this.activeSelections;
	}

	public HashMap<String, Selection> getInactiveSelections() {
		return this.inactiveSelections;
	}

	public void setMainCard(MainCard mainCard) {
		this.mainCard = mainCard;
	}

	public Selection getSelectionByName(String name) {
		if (this.activeSelections.get(name) != null)
			return this.activeSelections.get(name);

		return this.inactiveSelections.get(name);
	}

	public void clear() {
		this.status = Status.MODIFIED;
		this.activeSelections.clear();
		this.inactiveSelections.clear();
		this.layers.clear();
		this.mainCard.clear();
	}

	public void sharpenAnimation() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		System.out.println("Animation started.");
		this.create();
		if (this.getProject().getWidth() != 0) {

			int sharpIndex = 128;

			Image tmp = new Image();

			mainCard.getImagePanel().setImage(tmp);

			do {
				tmp.setProject(this.project.clone());
				tmp.project.sharpen(sharpIndex);
				mainCard.getImagePanel().repaint();
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				sharpIndex >>= 1;

			} while (sharpIndex != 1);
			mainCard.getImagePanel().setImage(this);
			mainCard.getImagePanel().repaint();

		}
		System.out.println("Animation ended.");

	}

	private void setProject(Layer project2) {
		this.project = project2;
	}

}
