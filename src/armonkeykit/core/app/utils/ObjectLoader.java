/*******************************************************************************
*    This file is part of ARMonkeyKit.
*
*    ARMonkeyKit is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    ARMonkeyKit is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with ARMonkeyKit.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/

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
