package armonkeykit.core.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

/**
 * Allows the loading of Obj files to use as AR Content.
 * @author Adam Clarkson
 *
 */
public class ObjectLoader {

	
	/**
	 * Load any obj file from specified location. If a mtl file is being used as a material it must be in the same directory as the model
	 * @param name Custom name to identify the model
	 * @param modelPath The URL location of the model
	 * @return Node 
	 */
	public static Node loadObjectFromFile(String name, URL modelPath){
		URL model = modelPath;
		Node n = new Node(name + " Object Node");
		// Create something to convert .obj format to .jme
		FormatConverter converter=new ObjToJme();
		// Point the converter to where it will find the .mtl file from
		converter.setProperty("mtllib", model);

		// This byte array will hold my .jme file
		ByteArrayOutputStream BO=new ByteArrayOutputStream();
		try {
			// Use the format converter to convert .obj to .jme
			converter.convert(model.openStream(), BO);
			n=(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
		}catch(IOException e) {
			System.exit(1);
		}
		return n;
	}
}
