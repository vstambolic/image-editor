package formatters;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import image.CompositeOperation;
import image.Coordinates;
import image.Image;
import image.Layer;
import image.Pixel;
import image.Selection;


public class XML_Formatter extends Formatter {

	public XML_Formatter(String filePath) {
		super(filePath);
	}

	@Override
	protected Pixel getPixel(RandomAccessFile reader) throws IOException {
		byte alpha = reader.readByte();
		byte blue = reader.readByte();
		byte green = reader.readByte();
		byte red = reader.readByte();
		return new Pixel(red, green, blue, alpha);
	}

	@Override
	protected void putPixel(RandomAccessFile writer, Pixel pixel) throws IOException {
		writer.writeByte(pixel.alpha);
		writer.writeByte(pixel.blue);
		writer.writeByte(pixel.green);
		writer.writeByte(pixel.red);

	}

	@Override
	public void read(Image image) throws IOException, FormatNotSupported {
		try {

		File inputFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc;
		doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        
        Element rootElement = doc.getDocumentElement();
        image.setName(rootElement.getAttribute("name"));
        
			// Layers
        Element layersElement = (Element)rootElement.getElementsByTagName("layers").item(0);
			NodeList nList = layersElement.getChildNodes();
			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {

					Element layerElement = (Element) nList.item(i);

					String srcFile = layerElement.getElementsByTagName("source_file").item(0).getTextContent();
					long width = Long.parseLong(layerElement.getElementsByTagName("width").item(0).getTextContent());
					long height = Long.parseLong(layerElement.getElementsByTagName("height").item(0).getTextContent());
					Coordinates coordinates = Coordinates.parse(layerElement.getElementsByTagName("coordinates").item(0).getTextContent());
					int opacity = Integer.parseInt(layerElement.getElementsByTagName("opacity").item(0).getTextContent());
					boolean visible = Boolean.parseBoolean(layerElement.getElementsByTagName("visible").item(0).getTextContent());
					boolean active = Boolean.parseBoolean(layerElement.getElementsByTagName("active").item(0).getTextContent());
					String dataFile = layerElement.getElementsByTagName("data_file").item(0).getTextContent();
					System.out.println(srcFile + " " + dataFile);
					Pattern p = Pattern.compile("(.*\\\\).*");
					Matcher m = p.matcher(filePath);
					String fullDataFilePath = "";
					if (m.matches())
						fullDataFilePath = m.group(1) + dataFile;
					Layer layer = new Layer(image, srcFile, height, width, active, visible, opacity, coordinates, image.getLayerNum());
					try {
						RandomAccessFile pixelReader = new RandomAccessFile(fullDataFilePath, "rw");
						Pixel[][] matrix = layer.getMatrix();
						for (int ii = 0; ii < layer.getHeight(); ++ii)
							for (int jj = 0; jj < layer.getWidth(); ++jj)
								matrix[ii][jj] = this.getPixel(pixelReader);
						pixelReader.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					image.addLayer(layer);
				}
			}
			Element activeSelectionsElement = (Element) rootElement.getElementsByTagName("active_selections").item(0);
			nList = activeSelectionsElement.getChildNodes();

			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element selectionElement = (Element) nList.item(i);
					Selection selection = new Selection(selectionElement.getAttribute("name"));
					NodeList rectNodesList = selectionElement.getChildNodes();
					for (int j = 0; j < rectNodesList.getLength(); ++j) {
						if (rectNodesList.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element rectangleElement = (Element) rectNodesList.item(j);

							Coordinates coordinates = Coordinates.parse(rectangleElement.getElementsByTagName("coordinates").item(0).getTextContent());
							int width = Integer.parseInt(rectangleElement.getElementsByTagName("width").item(0).getTextContent());
							int height = Integer.parseInt(rectangleElement.getElementsByTagName("height").item(0).getTextContent());
							System.out.println(width + " A Y coord" + coordinates.y);

							selection.addRectangle(new Rectangle2D.Double(coordinates.y, coordinates.x, width, height));

						}
					}
					image.addSelectionActive(selection);
				}
			}

			Element inactiveSelectionsElement = (Element) rootElement.getElementsByTagName("inactive_selections").item(0);
			nList = inactiveSelectionsElement.getChildNodes();

			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element selectionElement = (Element) nList.item(i);
					Selection selection = new Selection(selectionElement.getAttribute("name"));
					NodeList rectNodesList = selectionElement.getChildNodes();
					for (int j = 0; j < rectNodesList.getLength(); ++j) {
						if (rectNodesList.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element rectangleElement = (Element) rectNodesList.item(j);

							Coordinates coordinates = Coordinates.parse(rectangleElement.getElementsByTagName("coordinates").item(0).getTextContent());
							int width = Integer.parseInt(rectangleElement.getElementsByTagName("width").item(0).getTextContent());
							int height = Integer.parseInt(rectangleElement.getElementsByTagName("height").item(0).getTextContent());
							selection.addRectangle(new Rectangle2D.Double(coordinates.y, coordinates.x, coordinates.y + width, coordinates.x + height));
						}
					}
					image.addSelectionInactive(selection);
				}
			}
		}
		catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
	}
	@Override
	public void write(Image image) throws IOException {
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.newDocument();
	         
	         // root element
	         Element rootElement = doc.createElement("image");
	         rootElement.setAttribute("name", image.getName());
	         doc.appendChild(rootElement);

	         // Layers element
	         Element layersElement = doc.createElement("layers");
	         rootElement.appendChild(layersElement);
	         
	         image.getLayers().stream().forEach(layer -> {
	        	 Element layerElement = doc.createElement("layer");
	        	 
	        	 Element srcFile = doc.createElement("source_file");
	        	 srcFile.appendChild(doc.createTextNode(layer.getSourceFile()));
	        	 
	        	 Element width = doc.createElement("width");
				width.appendChild(doc.createTextNode(Long.toString(layer.getWidth())));

				Element height = doc.createElement("height");
				height.appendChild(doc.createTextNode(Long.toString(layer.getHeight())));

				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(layer.getCoordinates().x + " " + layer.getCoordinates().y));

				Element opacity = doc.createElement("opacity");
				opacity.appendChild(doc.createTextNode(Integer.toString(layer.getOpacity())));

				Element visible = doc.createElement("visible");
				visible.appendChild(doc.createTextNode(Boolean.toString(layer.getVisibleStatus())));

				Element active = doc.createElement("active");
				active.appendChild(doc.createTextNode(Boolean.toString(layer.getActiveStatus())));

				String datafileName = image.getName() + "_layer_#" + layer.getDepth() + ".lyr";

				Pattern p = Pattern.compile("(.*\\\\).*");
				Matcher m = p.matcher(filePath);

				String fullDataFilePath = datafileName;
				if (m.matches())
					fullDataFilePath = m.group(1) + datafileName;

				Element data_file = doc.createElement("data_file");
				data_file.appendChild(doc.createTextNode(datafileName));
				try {
					RandomAccessFile pixelWriter = new RandomAccessFile(fullDataFilePath, "rw");
					Pixel[][] matrix = layer.getMatrix();
					for (int i = 0; i < layer.getHeight(); ++i)
						for (int j = 0; j < layer.getWidth(); ++j)
							this.putPixel(pixelWriter, matrix[i][j]);
					pixelWriter.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}


				layerElement.appendChild(srcFile);
				layerElement.appendChild(width);
				layerElement.appendChild(height);
				layerElement.appendChild(coordinates);
				layerElement.appendChild(opacity);
				layerElement.appendChild(visible);
				layerElement.appendChild(active);
				layerElement.appendChild(data_file);
	        	 
				layersElement.appendChild(layerElement);

			});

			// ActiveSelections element
			Element activeSelectionsElement = doc.createElement("active_selections");
			rootElement.appendChild(activeSelectionsElement);

			image.getActiveSelections().keySet().stream().forEach(key -> {
				Element selectionElement = doc.createElement("selection");
				selectionElement.setAttribute("name", key);

				image.getActiveSelections().get(key).getRectangles().stream().forEach(rect -> {
					Element rectangleElement = doc.createElement("rectangle");

					Element coords = doc.createElement("coordinates");
					coords.appendChild(doc.createTextNode((int) rect.getY() + " " + (int) rect.getX()));
					Element width = doc.createElement("width");

					System.out.println(rect.getWidth());
					width.appendChild(doc.createTextNode(Integer.toString((int) rect.getWidth())));
					Element height = doc.createElement("height");
					height.appendChild(doc.createTextNode(Integer.toString((int) rect.getHeight())));

					rectangleElement.appendChild(coords);
					rectangleElement.appendChild(width);
					rectangleElement.appendChild(height);

					selectionElement.appendChild(rectangleElement);
				});
	        	 
				activeSelectionsElement.appendChild(selectionElement);
	         });

			// InactiveSelections element
			Element inactiveSelectionsElement = doc.createElement("inactive_selections");
			rootElement.appendChild(inactiveSelectionsElement);

			image.getInactiveSelections().keySet().stream().forEach(key -> {
				Element selectionElement = doc.createElement("selection");
				selectionElement.setAttribute("name", key);

				image.getInactiveSelections().get(key).getRectangles().stream().forEach(rect -> {
					Element rectangleElement = doc.createElement("rectangle");

					Element coords = doc.createElement("coordinates");
					coords.appendChild(doc.createTextNode((int) rect.getY() + " " + (int) rect.getX()));
					Element width = doc.createElement("width");
					width.appendChild(doc.createTextNode(Integer.toString((int) rect.getWidth())));
					Element height = doc.createElement("height");
					height.appendChild(doc.createTextNode(Integer.toString((int) rect.getHeight())));

					rectangleElement.appendChild(coords);
					rectangleElement.appendChild(width);
					rectangleElement.appendChild(height);

					selectionElement.appendChild(rectangleElement);
				});

				inactiveSelectionsElement.appendChild(selectionElement);
			});

	         // write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	         DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
	         transformer.transform(source, result);
	         
			//	         // Output to console for testing
			//	         StreamResult consoleResult = new StreamResult(System.out);
			//	         transformer.transform(source, consoleResult);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

	public void write(final CompositeOperation compositeOperation) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			// root element
			Element rootElement = doc.createElement("composite_operation");
			rootElement.setAttribute("name", compositeOperation.getName());
			doc.appendChild(rootElement);

			compositeOperation.getOperations().stream().forEach(str -> {
				Element operationElement = doc.createElement("operation");

				operationElement.appendChild(doc.createTextNode(str));

				rootElement.appendChild(operationElement);

			});

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);

			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read(CompositeOperation compositeOperation) {
		try {

			File inputFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			Element rootElement = doc.getDocumentElement();
			compositeOperation.setName(rootElement.getAttribute("name"));

			NodeList nList = rootElement.getChildNodes();
			for (int i = 0; i < nList.getLength(); i++)
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE)
					compositeOperation.addOperation(((Element) nList.item(i)).getTextContent());

			compositeOperation.getOperations().stream().forEach(str -> System.out.println(str));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
